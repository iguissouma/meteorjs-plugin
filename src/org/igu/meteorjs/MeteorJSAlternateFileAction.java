package org.igu.meteorjs;

import com.intellij.codeInsight.hint.HintManager;
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
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;

import java.util.ArrayList;

/**
 * Created by iguissouma on 01/02/2015.
 */
public class MeteorJSAlternateFileAction extends AnAction {
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
            // find these in project
            // open these...
            // iterate thru files
            final String currentFilename = currentFile.getName();
            final String alternateName = getAlternateFileName(currentFilename);

            final ProjectFileIndex projectFileIndex = ProjectRootManager.getInstance(currentProject).getFileIndex();
            ArrayList<PsiFile> psiFiles = new ArrayList<PsiFile>();
            projectFileIndex.iterateContent(new ContentIterator() {
                private PsiManager psiManager = PsiManager.getInstance(currentProject);

                public boolean processFile(VirtualFile fileOrDir) {
                    // if not a directory
                    if (!fileOrDir.isDirectory()) {
                        // and not currentFile...
                        System.out.println("fileOrDir.getName() = " + fileOrDir.getName());
                        if (!currentFilename.equals(fileOrDir.getName()) || !currentFile.getPath().equals(fileOrDir.getPath())) {
                            // iterate thru matchers and test...
                            if (alternateName.equals(fileOrDir.getName())) {

                                PsiFile psiFile = psiManager.findFile(fileOrDir);
                                if (psiFile != null) {
                                    psiFile.navigate(true);
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
            } else {
                PsiFile psiFile = psiFiles.get(0);
                psiFile.navigate(true);
            }
        }

    }

    private String getAlternateFileName(String fileName) {
        String fileNameWithOutExt = fileName.replaceFirst("[.][^.]+$", "");
        return fileNameWithOutExt + getAlternateFileExtension(fileName);
    }

    private String getAlternateFileExtension(String name) {
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        String ext = name.substring(lastIndexOf).toLowerCase();
        if (".js".equals(ext) || ".coffee".equals(ext)) {
            return ".html";
        } else {
            return ".js";
        }
    }


}



