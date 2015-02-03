package org.igu.meteorjs;

import com.intellij.ide.util.projectWizard.WizardContext;
import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.module.WebModuleBuilder;
import com.intellij.platform.ProjectTemplate;
import com.intellij.platform.ProjectTemplatesFactory;

/**
 * @author iguissouma
 */
public class MeteorJSTemplateFactory extends ProjectTemplatesFactory {

    @NotNull
    @Override
    public String[] getGroups() {
        return new String[]{WebModuleBuilder.GROUP_NAME};
    }

    @NotNull
    @Override
    public ProjectTemplate[] createTemplates(String s, WizardContext wizardContext) {
        return new ProjectTemplate[]{new MeteorJSDifferentialProjectGenerator()};
    }
}
