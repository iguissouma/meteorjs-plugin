package org.igu.meteorjs;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import com.intellij.codeInsight.hint.HintManager;
import com.intellij.ide.util.gotoByName.GotoFileCellRenderer;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ex.WindowManagerEx;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;

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
        final VirtualFile currentFile = getCurrentFile(anActionEvent);
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
                        if (!currentFilename.equals(fileOrDir.getName()) || !currentFile.getPath().equals(fileOrDir.getPath())) {
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
                Editor editor = getEditor(anActionEvent);
                if (editor != null) { // fix issue 9: can only display hint if there is a editor instance
                    HintManager.getInstance().showInformationHint(editor, "No corresponding file(s) found");
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
