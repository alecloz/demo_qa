package qa.demo.tests.files;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import qa.demo.tests.pageobject.BaseTest;
import qa.demo.tests.files.model.GlossaryModel;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class DownloadAndUploadFilesTest extends BaseTest {
    /*
        использовать для скачивания файла если нет href
         static {
                Configuration.proxyEnabled = true;
                Configuration.fileDownload = PROXY;
            }
        */
    ClassLoader cl = WorkWithFilesTest.class.getClassLoader();
    Gson gson = new Gson();
    @Disabled
    @Test
    void downloadTxtFileTest() throws Exception {
        open("https://github.com/junit-team/junit5/blob/main/README.md");
        File downloaded = $("#raw-url").download();
        try (InputStream is = new FileInputStream(downloaded)) {
            byte[] bytes = is.readAllBytes();
            String content = new String(bytes, StandardCharsets.UTF_8);
            Assertions.assertTrue(content.contains("This repository is the home of _JUnit 5_."));
        }
    }

    /* никогда не использовать uploadFile() из-за проблем с локальностью пути, либо из-за того, что будет падать
            при загрузке из .jar. Необходимо использовать uploadFromClasspath()
            File file = $("input[type='file']").uploadFile();
            Корневая папка classpath - resources
            */
    @Disabled
    @Test
    void uploadFileTest() throws Exception {
        open("https://fineuploader.com/demos.html");
        $("input[type='file']").uploadFromClasspath("cat.png");
        $(".qq-file-name").shouldHave(text("cat.png"));
    }
    @Disabled
    @Test
    void downloadPdfFileTest() throws Exception {
        open("https://junit.org/junit5/docs/current/user-guide/");
        File downloaded = $("a[href*='junit-user-guide-5.9.3.pdf']").download();
        PDF pdf = new PDF(downloaded);
        Assertions.assertEquals("JUnit 5 User Guide", pdf.title);
    }

    @Disabled
    @Test
    void downloadXlsFileTest() throws Exception {
        open("https://excelvba.ru/programmes/Teachers?ysclid=lfcu77j9j9951587711");
        File downloaded = $("a[href*='teachers.xls']").download();
        XLS xls = new XLS(downloaded);
        Assertions.assertEquals("1. Общие положения",
                xls.excel.getSheetAt(0).
                        getRow(1)
                        .getCell(4)
                        .getStringCellValue());
    }

    @Disabled
    @Test
    void improvedJsonTest() throws Exception {
        try (InputStream stream = cl.getResourceAsStream("glossary.json");
             Reader reader = new InputStreamReader(stream)) {
            GlossaryModel glossary = gson.fromJson(reader, GlossaryModel.class);

            Assertions.assertEquals("example glossary", glossary.getTitle());
            Assertions.assertEquals("S", glossary.getGlossDiv().getTitle());
            Assertions.assertTrue(glossary.getGlossDiv().isFlag());
        }
    }
}