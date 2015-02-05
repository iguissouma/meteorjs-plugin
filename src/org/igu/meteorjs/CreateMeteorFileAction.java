package org.igu.meteorjs;

import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;

/**
 * @author : iguissouma
 */
public class CreateMeteorFileAction extends CreateFileFromTemplateAction {
    private static final String NEW_METEOR_HTML_FILE = "New Meteor file";

    public CreateMeteorFileAction() {
        super("Create new Meteor File", "Meteor files",
                MeteorFileTemplateUtil.ICON);
        
    }



    @Override
    protected void buildDialog(Project project, PsiDirectory psiDirectory, CreateFileFromTemplateDialog.Builder builder) {
        builder.setTitle("Add Meteor File");
        for (FileTemplate fileTemplate : MeteorFileTemplateUtil.getMeteorTemplates()) {
            final String templateName = fileTemplate.getName();
            final String shortName = MeteorFileTemplateUtil.getTemplateShortName(templateName);
            builder.addKind(shortName, MeteorFileTemplateUtil.getTemplateIcon(shortName), templateName);
        }
    }

    @Override
    protected String getActionName(PsiDirectory psiDirectory, String s, String s1) {
        return NEW_METEOR_HTML_FILE;
    }
}
