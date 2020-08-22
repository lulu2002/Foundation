package org.mineacademy.fo.settings;

public abstract class AutoLoadStaticConfig extends YamlStaticConfig {
    @Override
    protected void preLoad() {
        super.preLoad();
        YamlConfigLoader.load(this.getClass());
    }
}
