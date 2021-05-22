package com.example.board.schedule;

import com.example.board.entity.economy_video.ScheduleChannel;
import com.example.board.repository.economy_video.EconomyVideoRepository;
import com.example.board.repository.economy_video.ScheduleChannelRepository;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private static WebDriver webDriver;
    private static JavascriptExecutor js;
    private static String WEB_DRIVER_ID = "webdriver.chrome.driver";
    private static String WEB_DRIVER_PATH = "chromedriver";
    private static List<ScheduleChannel> URL_LIST = new ArrayList<>();
    private static String URL = "https://www.youtube.com/channel/UCD9mxN8o9qLei1MbzroeP_A/videos";
    private final EconomyVideoRepository economyVideoRepository;
    private final ScheduleChannelRepository scheduleChannelRepository;

    public void setUp() {
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("headless");

        webDriver = new ChromeDriver(chromeOptions);
        webDriver.manage().window().maximize();

        js = (JavascriptExecutor) webDriver;

        URL_LIST = getChannelList();

    }

    private List<ScheduleChannel> getChannelList() {
        return scheduleChannelRepository.findAll();
    }

    public void setDown() {
        webDriver.quit();
    }

    @Scheduled(cron = "* * * * * *")
    public void cronJobSch() {
        setUp();

        economyVideoScheduling();

        setDown();

    }

    private void economyVideoScheduling() {
        for (ScheduleChannel scheduleChannel : URL_LIST) {
            calculate(scheduleChannel);
        }
    }

    @Async("executor")
    public void calculate(ScheduleChannel scheduleChannel) {
        webDriver.get(scheduleChannel.getChannelUrl());
        try {
            long lastHeight = (long) js.executeScript(getScrollHeight());
            System.out.println("lastHeight : " + lastHeight);
            while (true) {
                js.executeScript(moveScroll());
                Thread.sleep(2000);

                long newHeight = (long) js.executeScript(getScrollHeight());

                if (newHeight == lastHeight) {
                    break;
                }

                lastHeight = newHeight;

                System.out.println("lastHeight : " + lastHeight);
                System.out.println("newHeight : " + newHeight);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<WebElement> elements = webDriver.findElements(By.cssSelector("#contents #items .ytd-grid-renderer #dismissible"));

        saveVideo(elements);
    }

    private void saveVideo(List<WebElement> elements) {
        for (WebElement element : elements) {
            String src = getImgThumbnailUrl(element);

            WebElement titleElement = getTitleElement(element);

            String text = titleElement.getText();
            String href = titleElement.getAttribute("href");
            System.out.println(text + "   " + href + "  " + src);
        }
    }

    private WebElement getTitleElement(WebElement element) {
        return element.findElement(By.cssSelector("#dismissible #meta h3 #video-title"));
    }

    private String getImgThumbnailUrl(WebElement element) {
        WebElement thumbnailElement = element.findElement(By.cssSelector("#thumbnail #img"));
        return thumbnailElement.getAttribute("src");
    }

    private String moveScroll() {
        return "window.scrollTo(0, document.documentElement.scrollHeight);";
    }

    private String getScrollHeight() {
        return "return document.documentElement.scrollHeight";
    }
}
