package org.igu.meteorjs;

import com.intellij.openapi.util.IconLoader;
import javax.swing.Icon;

public class MeteorJSIcons {
    public static final Icon Meteor = load("/icons/meteor.png");

    public MeteorJSIcons() {
    }

    private static Icon load(String path) {
        return IconLoader.getIcon(path, MeteorJSIcons.class);
    }
}