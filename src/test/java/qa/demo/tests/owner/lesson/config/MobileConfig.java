package qa.demo.tests.owner.lesson.config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:${device}.properties",
        "classpath:mobile.properties",
})
public interface MobileConfig extends Config {

    @Key("device.name")
    String getDeviceName();

    @Key("platform.name")
    String getPlatformName();

    @Key("platform.version")
    String getPlatformVersion();

}
