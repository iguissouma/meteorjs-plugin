package org.igu.meteorjs;

import com.intellij.icons.AllIcons;
import com.intellij.ide.util.frameworkSupport.FrameworkRole;
import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.openapi.module.EmptyModuleType;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.WebModuleBuilder;
import com.intellij.openapi.module.WebModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by iguissouma on 31/01/2015.
 */
public class MeteorJSBoilerplateModuleBuilder extends WebModuleBuilder {

    public static final String GROUP_NAME = "Meteor JS Boilerplate";

    @Override
    public void setupRootModel(ModifiableRootModel modifiableRootModel) throws ConfigurationException {
        this.doAddContentEntry(modifiableRootModel);
    }

    @Override
    public Icon getBigIcon() {
        return IconLoader.getIcon("/icons/meteor@2x.png");
    }

    @Override
    public Icon getNodeIcon() {
        return IconLoader.getIcon("/icons/meteor.png");
    }

    @Override
    public String getDescription() {
        return "Meteor JS Boilerplate";
    }

    @Override
    public String getPresentableName() {
        return GROUP_NAME;
    }

    @Override
    public String getGroupName() {
        return GROUP_NAME;
    }

    @Override
    public String getParentGroup() {
        return "Static Web";
    }


}
