package qa.demo.tests.simple;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

import static com.codeborne.selenide.Selenide.open;

public class BaseRemoteTest {

    @BeforeAll
    static void preconditionConfiguration() {
        Configuration.pageLoadStrategy = "eager";
        Configuration.baseUrl = "https://demoqa.com";
        Configuration.browserSize = "1920x1080";
        Configuration.holdBrowserOpen = false;
        Configuration.remote = "https://user1:1234@selenoid.autotests.cloud/wd/hub";

        /* enableVNC нужно для того чтобы мы видели, что происходит на экране
                удаленного браузера во время его работы. Без нее будет просто черный экран*/
        /* enableVideo позволяет нам записывать видео прогона тестов*/
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("selenoid:options", Map.of(
                "enableVNC", true,
                "enableVideo", true
        ));
        //подключаем capabilities
        Configuration.browserCapabilities = capabilities;
    }

    @BeforeEach
    void addListener(){
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    public void openPage(String pageURL) {
        open(pageURL);
    }
}
