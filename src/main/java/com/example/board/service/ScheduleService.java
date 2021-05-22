package com.example.board.service;

import com.example.board.entity.economy_video.EconomyVideo;
import com.example.board.entity.economy_video.ScheduleChannel;
import com.example.board.repository.economy_video.EconomyVideoRepository;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private static WebDriver webDriver;
    private static JavascriptExecutor js;
    private static String WEB_DRIVER_ID = "webdriver.chrome.driver";
    private static String WEB_DRIVER_PATH = "chromedriver";
    private final EconomyVideoRepository economyVideoRepository;

    @PostConstruct
    private void init() {
        setUp();
    }

    private void setUp() {
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("headless");

        webDriver = new ChromeDriver(chromeOptions);
        webDriver.manage().window().maximize();

        js = (JavascriptExecutor) webDriver;

    }

    private void setDown() {
        webDriver.quit();
    }

    public void economyVideoScheduling(ScheduleChannel scheduleChannel) {
        calculate(scheduleChannel);
    }

    private void calculate(ScheduleChannel scheduleChannel) {
        System.out.println(scheduleChannel.getChannelName() + "   " + scheduleChannel.getChannelUrl());
        webDriver.get(scheduleChannel.getChannelUrl());

        try {
            long lastHeight = (long) js.executeScript(getScrollHeight());

            while (true) {

                js.executeScript(moveScroll());
                Thread.sleep(2000);

                long newHeight = (long) js.executeScript(getScrollHeight());

                if (newHeight == lastHeight) {
                    break;
                }

                lastHeight = newHeight;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<WebElement> elements = webDriver.findElements(By.cssSelector("#contents #items .ytd-grid-renderer #dismissible"));

        if (scheduleChannel.getEconomyVideoList().size() < 1) {
            saveAllVideo(elements, scheduleChannel);
        } else {
            if (!getTitleElement(elements.get(0)).getText().equals(scheduleChannel.getEconomyVideoList().get(0).getTitle())) {
                int count = elements.size() - scheduleChannel.getEconomyVideoList().size();
                for (int index = 0; index < count; index++) {
                    saveVideo(elements.get(index), scheduleChannel);
                }
            }
        }


    }

    private void saveVideo(WebElement element, ScheduleChannel scheduleChannel) {
        WebElement titleElement = getTitleElement(element);
        String url = titleElement.getAttribute("href");
        String title = titleElement.getText();

        String thumbnailUrl = getImgThumbnailUrl(url);

        System.out.println(title + "   " + url + "  " + thumbnailUrl);

        EconomyVideo video = EconomyVideo
                .builder()
                .title(title)
                .thumbnailUrl(thumbnailUrl)
                .url(url)
                .build();

        video.mappingChannel(scheduleChannel);

        economyVideoRepository.save(video);
    }

    private void saveAllVideo(List<WebElement> elements, ScheduleChannel scheduleChannel) {
        for (WebElement element : elements) {
            saveVideo(element, scheduleChannel);
        }
    }

    private WebElement getTitleElement(WebElement element) {
        return element.findElement(By.cssSelector("#dismissible #meta h3 #video-title"));
    }

    private String getImgThumbnailUrl(String href) {
        String[] split = href.split("=");

        return String.format("https://img.youtube.com/vi/%s/0.jpg", split[1]);
    }

    private String moveScroll() {
        return "window.scrollTo(0, document.documentElement.scrollHeight);";
    }

    private String getScrollHeight() {
        return "return document.documentElement.scrollHeight";
    }
}
