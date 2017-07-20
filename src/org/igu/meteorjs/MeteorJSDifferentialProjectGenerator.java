package org.igu.meteorjs;

import com.intellij.lang.javascript.boilerplate.AbstractGithubTagDownloadedProjectGenerator;
import com.intellij.openapi.util.IconLoader;
import com.intellij.platform.templates.github.GithubTagInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author iguissouma
 */
public class MeteorJSDifferentialProjectGenerator extends AbstractGithubTagDownloadedProjectGenerator {

    @NotNull
    @Override
    protected String getDisplayName() {
        return "Meteor JS BoilerPlate By Differential";
    }

    @NotNull
    @Override
    public String getGithubUserName() {
        return "Differential";
    }

    @NotNull
    @Override
    public String getGithubRepositoryName() {
        return "meteor-boilerplate";
    }

    @Nullable
    @Override
    public String getDescription() {
        return "<html>A starting point for MeteorJS applications. Includes iron-router, Bootstrap 3, Font Awesome, LESS and more.</html>";
    }

    @Override
    public Icon getIcon() {
        return IconLoader.getIcon("/icons/Di.png");
    }

    @Nullable
    @Override
    public String getPrimaryZipArchiveUrlForDownload(@NotNull GithubTagInfo githubTagInfo) {
        return null;
    }
}
