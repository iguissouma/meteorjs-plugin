package org.igu.meteorjs.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.Configurable.NoScroll;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;


import javax.swing.*;
import java.awt.*;

/**
 * @author iguissouma
 */
public class MeteorJSTemplateConfigurable implements Configurable, NoScroll {


    public static final String ID = "settings.javascript.meteor.template";
    private JBCheckBox myAskForFileCreation;
    private MeteorJSTemplateSettings mySettings;
    private Project myProject;

    public MeteorJSTemplateConfigurable(Project project) {
        this.myProject = project;
    }

    @Nls
    public String getDisplayName() {
        return "Meteor JS Template";
    }

    @Nullable
    public String getHelpTopic() {
        return "settings.javascript.meteor";
    }

    @Nullable
    public JComponent createComponent() {
        this.mySettings = MeteorJSTemplateSettings.getInstance();
         this.myAskForFileCreation = new JBCheckBox("Ask for file creation if it's not found when toggling between JS|HTML");
        FormBuilder builder = FormBuilder.createFormBuilder();
        //builder.addLabeledComponent(MeteorBundle.message("settings.meteor.configurable.executable", new Object[0]), this.myExecutablePathField);
        builder.addComponent(this.myAskForFileCreation);


        JPanel panel1 = builder.getPanel();
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(panel1, "North");
        return wrapper;
    }

    public boolean isModified() {
        return this.myAskForFileCreation.isSelected() != this.mySettings.ASK_FOR_FILE_CREATION;
    }

    public void apply() throws ConfigurationException {
        if(this.mySettings != null) {

            this.mySettings.ASK_FOR_FILE_CREATION = this.myAskForFileCreation.isSelected();
        }

    }

    public void reset() {
        if(this.mySettings != null) {
           this.myAskForFileCreation.setSelected(this.mySettings.ASK_FOR_FILE_CREATION);
        }

    }

    public void disposeUIResources() {
    }
}