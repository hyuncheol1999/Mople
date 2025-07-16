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
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");

        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get(url);
            Thread.sleep(3000); // 렌더링 대기

            List<WebElement> games = driver.findElements(By.cssSelector(
                "#content > div > div:nth-child(4) > div:nth-child(1) > ul > li"
            ));

            for (WebElement game : games) {
                GameDTO dto = new GameDTO();

                // 경기 시간과 장소
                dto.setTime(game.findElement(By.cssSelector("div[class^='MatchBox_time__']")).getText());
                System.out.println(dto.getTime());
                dto.setPlace(game.findElement(By.cssSelector("div[class^='MatchBox_stadium__']")).getText());

                // 경기 상태
                String state = game.findElement(By.cssSelector("div[class^='MatchBox_match_area__'] > em")).getText();
                dto.setState(state);

                String awayTeam = "";
                String homeTeam = "";
                String awayLogo = "";
                String homeLogo = "";
                String awayScore = "";
                String homeScore = "";

                if ("예정".equals(state)) {
                    // 예정 경기 - 스코어 없음, 팀명 위치 기반 선택자 사용
                    awayTeam = game.findElement(By.cssSelector(
                        "div.MatchBox_match_area__39dEr > div:nth-child(2) > div > div.MatchBoxHeadToHeadArea_team_name__3GuBO > div > strong"
                    )).getText();

                    homeTeam = game.findElement(By.cssSelector(
                        "div.MatchBox_match_area__39dEr > div:nth-child(3) > div > div.MatchBoxHeadToHeadArea_team_name__3GuBO > div > strong"
                    )).getText();

                    awayLogo = game.findElement(By.cssSelector(
                        "div.MatchBox_match_area__39dEr > div:nth-child(2) img"
                    )).getAttribute("src");

                    homeLogo = game.findElement(By.cssSelector(
                        "div.MatchBox_match_area__39dEr > div:nth-child(3) img"
                    )).getAttribute("src");

                } else if ("종료".equals(state)) {
                    // 종료 경기 - 클래스 기반 선택자 사용, 스코어 포함
                    awayTeam = game.findElement(By.cssSelector("div[class^='MatchBoxHeadToHeadArea_type_loser__'] strong")).getText();
                    homeTeam = game.findElement(By.cssSelector("div[class^='MatchBoxHeadToHeadArea_type_winner__'] strong")).getText();

                    awayLogo = game.findElement(By.cssSelector("div[class^='MatchBoxHeadToHeadArea_type_loser__'] img")).getAttribute("src");
                    homeLogo = game.findElement(By.cssSelector("div[class^='MatchBoxHeadToHeadArea_type_winner__'] img")).getAttribute("src");

                    awayScore = game.findElement(By.cssSelector("div[class^='MatchBoxHeadToHeadArea_type_loser__'] strong")).getText();
                    homeScore = game.findElement(By.cssSelector("div[class^='MatchBoxHeadToHeadArea_type_winner__'] strong")).getText();

                } else {
                    // 기타 상태 (취소, 연기 등) 필요하면 처리 추가
                    awayTeam = homeTeam = awayLogo = homeLogo = awayScore = homeScore = "";
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
