package com.mopl.crawler;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.mopl.model.GameDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    public Map<String, List<GameDTO>> getSchduleMonth(String url){
    	Map<String, List<GameDTO>> map = new HashMap<String, List<GameDTO>>(); 
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
            Thread.sleep(3000);
            
            List<WebElement> dateGroups = driver.findElements(By.cssSelector("div[class^='ScheduleLeagueType_match_list_group__']"));
            
            for (WebElement group : dateGroups) {
                // 날짜 텍스트 추출
                String date = group.findElement(
                    By.cssSelector("em[class^='ScheduleLeagueType_title__']")
                ).getText();

                // 날짜별 경기 리스트
                List<GameDTO> list = new ArrayList<>();

                // 경기 ul 안의 li 요소들(= 경기 하나하나)
                WebElement matchList = group.findElement(
                    By.cssSelector("ul[class^='ScheduleLeagueType_match_list__']")
                );

                List<WebElement> games = matchList.findElements(
                    By.cssSelector("li[class^='MatchBox_match_box__']")
                );

                for (WebElement game : games) {
                    GameDTO dto = new GameDTO();

                    // 경기 시간
                    dto.setTime(game.findElement(By.cssSelector("div[class^='MatchBox_time__']")).getText());

                    // 경기장
                    dto.setPlace(game.findElement(By.cssSelector("div[class^='MatchBox_stadium__']")).getText());

                    // 상태
                    String state = game.findElement(By.cssSelector("em[class^='MatchBox_status__']")).getText();
                    dto.setState(state);

                    // 팀명과 로고
                    List<WebElement> teamElements = game.findElements(By.cssSelector("strong[class^='MatchBoxHeadToHeadArea_team__']"));
                    List<WebElement> logoElements = game.findElements(By.cssSelector("div[class^='MatchBoxHeadToHeadArea_emblem__'] img"));

                    dto.setAway(teamElements.get(0).getText());
                    dto.setHome(teamElements.get(1).getText());
                    dto.setLogo1(logoElements.get(0).getAttribute("src"));
                    dto.setLogo2(logoElements.get(1).getAttribute("src"));

                    // 스코어
                    if ("종료".equals(state)) {
                        List<WebElement> scoreElements = game.findElements(By.cssSelector("strong[class^='MatchBoxHeadToHeadArea_score__']"));
                        dto.setAwayScore(scoreElements.get(0).getText());
                        dto.setHomeScore(scoreElements.get(1).getText());
                    } else {
                        dto.setAwayScore("");
                        dto.setHomeScore("");
                    }

                    list.add(dto);
                }

                // 날짜별로 map에 넣기
                map.put(date, list);
            }
            
            
            
        }catch (Exception e) {
        	e.printStackTrace();
		}
        
        
        return map;
    }
}
