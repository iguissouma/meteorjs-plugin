package org.igu.meteorjs;

import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.platform.ProjectTemplate;
import com.intellij.platform.ProjectTemplatesFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Created by iguissouma on 31/01/2015.
 */
public class MeteorJSTemplateFactory extends ProjectTemplatesFactory {
    @NotNull
    @Override
    public String[] getGroups() {
        return new String[]{MeteorJSBoilerplateModuleBuilder.GROUP_NAME};
    }

    @NotNull
    @Override
    public ProjectTemplate[] createTemplates(String s, WizardContext wizardContext) {
        return new ProjectTemplate[]{new MeteorJSDifferentialProjectGenerator()};
    }
}
