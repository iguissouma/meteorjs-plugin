package org.igu.meteorjs.settings;

import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

/**
 * @author iguissouma
 */
@State(
  name = "MeteorJSTemplateSettings",
  storages = {
    @Storage(
      file = StoragePathMacros.APP_CONFIG + "/other.xml"
    )}
)
public class MeteorJSTemplateSettings implements PersistentStateComponent<MeteorJSTemplateSettings> {
  public boolean ASK_FOR_FILE_CREATION = true;

  public static MeteorJSTemplateSettings getInstance() {
    return ServiceManager.getService(MeteorJSTemplateSettings.class);
  }

  @Nullable
  @Override
  public MeteorJSTemplateSettings getState() {
    return this;
  }

  @Override
  public void loadState(MeteorJSTemplateSettings state) {
    XmlSerializerUtil.copyBean(state, this);
  }
}