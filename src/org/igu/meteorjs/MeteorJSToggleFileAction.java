package org.igu.meteorjs;

import java.awt.*;
import java.io.IOException;
import java.util.*;

import javax.swing.*;

import com.intellij.codeInsight.hint.HintManager;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.util.gotoByName.GotoFileCellRenderer;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ex.WindowManagerEx;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.igu.meteorjs.settings.MeteorJSTemplateSettings;

/**
 * @author iguissouma
 */
public class MeteorJSToggleFileAction extends AnAction {

    private static final String[] JS_FILES_EXT = {".js", ".coffee"};
    private static final String[] HTML_FILES_EXT = {".html"};
    private static final String[] EMPTY_FILES_EXT = {""};
    
    private static VirtualFile getCurrentFile(AnActionEvent e) {
        return PlatformDataKeys.VIRTUAL_FILE.getData(e.getDataContext());
    }

    private static PsiFile getCurrentPsiFile(AnActionEvent e) {
        return PlatformDataKeys.PSI_FILE.getData(e.getDataContext());
    }


    private static Project getProject(AnActionEvent e) {
        return PlatformDataKeys.PROJECT.getData(e.getDataContext());
    }

    private static Module getModule(AnActionEvent e) {
        return LangDataKeys.MODULE.getData(e.getDataContext());
    }

    private static Editor getEditor(AnActionEvent e) {
        return PlatformDataKeys.EDITOR.getData(e.getDataContext());
    }


    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        final PsiFile currentFile = getCurrentPsiFile(anActionEvent);
        final Project currentProject = getProject(anActionEvent);
        if (currentFile != null) {
            // iterate through files
            final String currentFilename = currentFile.getName();
            final ArrayList<String> alternateNames = getAlternateFileName(currentFilename);
            final ProjectFileIndex projectFileIndex = ProjectRootManager.getInstance(currentProject).getFileIndex();
            final ArrayList<PsiFile> psiFiles = new ArrayList<PsiFile>();
            projectFileIndex.iterateContent(new ContentIterator() {
                private PsiManager psiManager = PsiManager.getInstance(currentProject);
                public boolean processFile(VirtualFile fileOrDir) {
                    // if not a directory
                    if (!fileOrDir.isDirectory()) {
                        // and not currentFile...
                        if (!currentFilename.equals(fileOrDir.getName()) || !currentFile.getVirtualFile().getPath().equals(fileOrDir.getPath())) {
                            // test if it's equals to the alternate name
                            if (alternateNames.contains(fileOrDir.getName())) {
                                PsiFile psiFile = psiManager.findFile(fileOrDir);
                                if (psiFile != null) {
                                    psiFiles.add(psiFile);
                                }
                            }
                        }
                    }
                    return true;
                }
            });

            if (psiFiles.isEmpty()) {
                final MeteorJSTemplateSettings service = ServiceManager.getService(MeteorJSTemplateSettings.class);
                if (service.ASK_FOR_FILE_CREATION) {
                    if (Messages.showOkCancelDialog("No corresponding file(s) found, " + "\n Do you want to create it?.", "Information", Messages.getInformationIcon()) == DialogWrapper.OK_EXIT_CODE) {
                        PsiDirectory currentDir = currentFile.getContainingDirectory();
                        //PsiDirectory currentDir = getCurrentPsiFile(anActionEvent).getContainingDirectory();
                        for (String alternateName : alternateNames) {
                            String extension = alternateName.toLowerCase().indexOf(".js") > 0 ? "JS" : "HTML";
                            createFromTemplate("Meteor " + extension + " File", currentDir, alternateName, currentProject);
                            break;
                        }
                    }
                } else {
                    Editor editor = getEditor(anActionEvent);
                    if (editor != null) { // fix issue 9: can only display hint if there is a editor instance
                        HintManager.getInstance().showInformationHint(editor, "No corresponding file(s) found");
                    }
                }
            } else if (psiFiles.size() == 1) {
                PsiFile psiFile = psiFiles.get(0);
                psiFile.navigate(true);
            } else {
                final JList valueList = new JList(psiFiles.toArray());
                valueList.setCellRenderer(new AlternateCellRenderer(currentProject));
                valueList.setSelectionModel(new DefaultListSelectionModel());
                final PopupChooserBuilder listPopupBuilder = JBPopupFactory.getInstance().createListPopupBuilder(valueList);
                listPopupBuilder.setTitle("Select the file(s) to open");
                listPopupBuilder.setItemChoosenCallback(new Runnable() {
                    public void run() {
                        for (Object item : valueList.getSelectedValues()) {
                            if (item instanceof PsiFile) {
                                ((PsiFile) item).navigate(true);
                            }
                        }
                    }
                });
                listPopupBuilder.createPopup().showCenteredInCurrentWindow(currentProject);
            }
        }

    }

    private ArrayList<String> getAlternateFileName(String fileName) {
        String fileNameWithOutExt = fileName.replaceFirst("[.][^.]+$", "");
        final ArrayList<String> list =  new ArrayList<String>();
        final String[] alternateFileExtensions = getAlternateFileExtensions(fileName);
        for (String alternateFileExtension : alternateFileExtensions) {
            list.add(fileNameWithOutExt + alternateFileExtension);
        }
        return list;
    }

    private String[] getAlternateFileExtensions(String name) {
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return EMPTY_FILES_EXT; // empty extension
        }
        String ext = name.substring(lastIndexOf).toLowerCase();
        if (".js".equals(ext) || ".coffee".equals(ext)) {
            return HTML_FILES_EXT;
        } else {
            return JS_FILES_EXT;
        }
    }

    private void createFromTemplate(String templateName, final PsiDirectory dir, final String name, Project project) {
        final FileTemplate template = FileTemplateManager.getInstance().getInternalTemplate(templateName);
        Properties properties = new Properties(FileTemplateManager.getInstance().getDefaultProperties(project));
        properties.setProperty("NAME", name);
        final String fileContents;
        try {
            fileContents = template.getText(properties);
        } catch (IOException e) {
            throw new RuntimeException("Unable to parse template '" + templateName + "':" + e.getMessage());
        }
        final Application application = ApplicationManager.getApplication();
        application.runWriteAction(new Runnable() {
            @Override
            public void run() {
                try {
                    final PsiFile file = dir.createFile(name);
                    file.getVirtualFile().setBinaryContent(fileContents.getBytes());
                    file.navigate(true);
                } catch (IOException e) {
                    throw new RuntimeException("Unable to write file '" + name + ":" + e.getMessage());
                }
            }
        });

    }


    /**
     * CellRenderer, renders PsiFile with ideas GotoFileCellRenderer and all other with DefaultListCellRenderer like a title (bgcolor: control)
     */
    static class AlternateCellRenderer extends DefaultListCellRenderer {
        private ListCellRenderer gotoFileCellRenderer;

        AlternateCellRenderer(Project project) {
            this.gotoFileCellRenderer = new GotoFileCellRenderer(WindowManagerEx.getInstanceEx().getFrame(project).getSize().width);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof PsiFile) {
                return gotoFileCellRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            } else {
                if (value.toString().length() == 0) {
                    value = " "; // we need a character to have correct height
                }
                Component c = super.getListCellRendererComponent(list, value, index, false, cellHasFocus);
                c.setBackground(UIManager.getColor("control"));
                return c;
            }
        }
    }



}
