package org.igu.meteorjs;

import java.util.List;

import javax.swing.*;

import com.intellij.icons.AllIcons;
import org.jetbrains.annotations.NotNull;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.IconLoader;
import com.intellij.util.SmartList;
import com.intellij.util.containers.ContainerUtil;

/**
 * @author: iguissouma
 */
public class MeteorFileTemplateUtil {

    private final static String METEOR_TEMPLATE_PREFIX = "Meteor ";

    public static final Icon    ICON                   = IconLoader.getIcon("/icons/meteor.png");

    public static List<FileTemplate> getMeteorTemplates() {
        return getApplicableTemplates(new Condition<FileTemplate>() {

            @Override
            public boolean value(FileTemplate fileTemplate) {
                return fileTemplate.getName().startsWith(METEOR_TEMPLATE_PREFIX);
            }
        });
    }

    public static List<FileTemplate> getApplicableTemplates(Condition<FileTemplate> filter) {
        List<FileTemplate> applicableTemplates = new SmartList<FileTemplate>();
        applicableTemplates.addAll(ContainerUtil.findAll(FileTemplateManager.getInstance().getInternalTemplates(),
                filter));
        applicableTemplates.addAll(ContainerUtil.findAll(FileTemplateManager.getInstance().getAllTemplates(), filter));
        return applicableTemplates;
    }

    public static String getTemplateShortName(String templateName) {
        if (templateName.startsWith(METEOR_TEMPLATE_PREFIX)) {
            return templateName.substring(METEOR_TEMPLATE_PREFIX.length());
        }
        return templateName;
    }

    @NotNull
    public static Icon getTemplateIcon(String name) {
        if (name.startsWith("JS")) {
            return AllIcons.FileTypes.JavaScript;
        } else if (name.startsWith("HTML")) {
            return AllIcons.FileTypes.Html;
        }
        return ICON;
    }

}
