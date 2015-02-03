package org.igu.meteorjs;

import javax.swing.*;

import com.intellij.openapi.module.WebModuleBuilder;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.util.IconLoader;

/**
 * @author iguissouma
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
