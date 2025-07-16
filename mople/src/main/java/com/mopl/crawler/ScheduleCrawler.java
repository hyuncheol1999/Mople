package com.mopl.crawler;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.mopl.model.GameDTO;

import java.util.ArrayList;
import java.util.List;

public class ScheduleCrawler {

    public static List<GameDTO> getSchedule(String url) {
        List<GameDTO> list = new ArrayList<>();
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // 창 안 띄우고 백그라운드 실행
        options.addArguments("--window-size=1280,1024"); // 데스크탑 화면 크기
        options.addArguments("--disable-gpu"); // (선택) 안정성 위해
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                             "AppleWebKit/537.36 (KHTML, like Gecko) " +
                             "Chrome/114.0.0.0 Safari/537.36"); // 데스크탑 환경 User-Agent

        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get(url);
            Thread.sleep(3000); // 렌더링 대기

            List<WebElement> games = driver.findElements(By.cssSelector(
                "#content > div > div:nth-child(4) > div:nth-child(1) > ul > li"
            ));

            for (WebElement game : games) {
                GameDTO dto = new GameDTO();
                // 경기 시간
                dto.setTime(game.findElement(By.cssSelector("div[class^='MatchBox_time__']")).getText());
                System.out.println(dto.getTime());

                // 경기장
                dto.setPlace(game.findElement(By.cssSelector("div[class^='MatchBox_stadium__']")).getText());
                System.out.println(dto.getPlace());

                // 경기 상태
                String state = game.findElement(By.cssSelector("em[class^='MatchBox_status__']")).getText();
                dto.setState(state);
                System.out.println(dto.getState());

                String awayTeam = "";
                String homeTeam = "";
                String awayLogo = "";
                String homeLogo = "";
                String awayScore = "";
                String homeScore = "";

                List<WebElement> teamElements = game.findElements(By.cssSelector("strong[class^='MatchBoxHeadToHeadArea_team__']"));
                List<WebElement> logoElements = game.findElements(By.cssSelector("div[class^='MatchBoxHeadToHeadArea_emblem__'] img"));

                awayTeam = teamElements.get(0).getText();
                homeTeam = teamElements.get(1).getText();
                awayLogo = logoElements.get(0).getAttribute("src");
                homeLogo = logoElements.get(1).getAttribute("src");
                
                if ("종료".equals(state)) {
                    List<WebElement> scoreElements = game.findElements(By.cssSelector("strong[class^='MatchBoxHeadToHeadArea_score__']"));

                    awayScore = scoreElements.get(0).getText();
                    homeScore = scoreElements.get(1).getText();
                } else {
                    awayScore = homeScore = "";
                }

                dto.setAway(awayTeam);
                dto.setHome(homeTeam);
                dto.setLogo1(awayLogo);
                dto.setLogo2(homeLogo);
                dto.setAwayScore(awayScore);
                dto.setHomeScore(homeScore);

                list.add(dto);
            }

        } catch (Exception e) {
            System.out.println("크롤링 실패: " + e.getMessage());
        } finally {
            driver.quit();
        }

        return list;
    }
}
