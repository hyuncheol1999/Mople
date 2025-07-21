package com.mopl.crawler;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.mopl.model.GameDTO;

import java.util.ArrayList;
import java.util.Arrays;
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
		options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
				+ "AppleWebKit/537.36 (KHTML, like Gecko) " + "Chrome/114.0.0.0 Safari/537.36"); // 데스크탑 환경 User-Agent

		WebDriver driver = new ChromeDriver(options);

		try {
			driver.get(url);
			Thread.sleep(3000); // 렌더링 대기

			List<WebElement> games = driver
					.findElements(By.cssSelector("#content > div > div:nth-child(4) > div:nth-child(1) > ul > li"));

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

				List<WebElement> teamElements = game
						.findElements(By.cssSelector("strong[class^='MatchBoxHeadToHeadArea_team__']"));
				List<WebElement> logoElements = game
						.findElements(By.cssSelector("div[class^='MatchBoxHeadToHeadArea_emblem__'] img"));

				awayTeam = teamElements.get(0).getText();
				homeTeam = teamElements.get(1).getText();
				awayLogo = logoElements.get(0).getAttribute("src");
				homeLogo = logoElements.get(1).getAttribute("src");

				if ("종료".equals(state)) {
					List<WebElement> scoreElements = game
							.findElements(By.cssSelector("strong[class^='MatchBoxHeadToHeadArea_score__']"));

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

	public Map<String, List<GameDTO>> getSchduleMonth(String url) {
	    Map<String, List<GameDTO>> map = new HashMap<>();
	    System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");

	    ChromeOptions options = new ChromeOptions();
	    options.addArguments("--headless=new");
	    options.addArguments("--window-size=1280,1024");
	    options.addArguments("--disable-gpu");
	    options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
	            + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");

	    WebDriver driver = new ChromeDriver(options);
	    List<String[]> teamList = Arrays.asList(
	    	    new String[]{"한화", "https://sports-phinf.pstatic.net/team/kbo/default/HH.png?type=f108_108"},
	    	    new String[]{"삼성", "https://sports-phinf.pstatic.net/team/kbo/default/SS.png?type=f108_108"},
	    	    new String[]{"KIA", "https://sports-phinf.pstatic.net/team/kbo/default/HT.png?type=f108_108"},
	    	    new String[]{"두산", "https://sports-phinf.pstatic.net/team/kbo/default/OB.png?type=f108_108"},
	    	    new String[]{"LG", "https://sports-phinf.pstatic.net/team/kbo/default/LG.png?type=f108_108"},
	    	    new String[]{"롯데", "https://sports-phinf.pstatic.net/team/kbo/default/LT.png?type=f108_108"},
	    	    new String[]{"SSG", "https://sports-phinf.pstatic.net/team/kbo/default/SK.png?type=f108_108"},
	    	    new String[]{"NC", "https://sports-phinf.pstatic.net/team/kbo/default/NC.png?type=f108_108"},
	    	    new String[]{"KT", "https://sports-phinf.pstatic.net/team/kbo/default/KT.png?type=f108_108"},
	    	    new String[]{"키움", "https://sports-phinf.pstatic.net/team/kbo/default/WO.png?type=f108_108"}
	    	);
	   
	    
	    try {
	        driver.get(url);
	        Thread.sleep(3000);

	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        long lastHeight = (long) js.executeScript("return document.body.scrollHeight");

	        while (true) {
	            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
	            Thread.sleep(2500);
	            long newHeight = (long) js.executeScript("return document.body.scrollHeight");
	            if (newHeight == lastHeight) break;
	            lastHeight = newHeight;
	        }

	        WebElement container = driver.findElement(By.cssSelector("div[class^='ScheduleLeagueType_match_list_container__']"));
	        List<WebElement> dateGroups = container.findElements(By.cssSelector("div[class^='ScheduleLeagueType_match_list_group__']"));

	        for (WebElement group : dateGroups) {
	            // 날짜
	            String date = group.findElement(By.cssSelector("em[class^='ScheduleLeagueType_title__']")).getText();
	            List<GameDTO> list = new ArrayList<>();

	            // 경기 리스트
	            WebElement matchList = group.findElement(By.cssSelector("ul[class^='ScheduleLeagueType_match_list__']"));
	            List<WebElement> games = matchList.findElements(By.cssSelector("li[class^='MatchBox_match_item__']"));

	            for (WebElement game : games) {
	                try {
	                    GameDTO dto = new GameDTO();

	                    dto.setTime(game.findElement(By.cssSelector("div[class^='MatchBox_time__']")).getText());
	                    dto.setPlace(game.findElement(By.cssSelector("div[class^='MatchBox_stadium__']")).getText());

	                    String state = game.findElement(By.cssSelector("em[class^='MatchBox_status__']")).getText();
	                    dto.setState(state);

	                    List<WebElement> teamElements = game.findElements(By.cssSelector("strong[class^='MatchBoxHeadToHeadArea_team__']"));
	                    String away = teamElements.get(0).getText();
	                    String home = teamElements.get(1).getText();
	                    dto.setAway(away);
	                    dto.setHome(home);
	                    
	                    for (String[] team : teamList) {
	                        if (team[0].equals(away)) {
	                            dto.setLogo1(team[1]);
	                            break;
	                        }
	                    }

	                    // home 팀 로고 매칭
	                    for (String[] team : teamList) {
	                        if (team[0].equals(home)) {
	                            dto.setLogo2(team[1]);
	                            break;
	                        }
	                    }
	             

	                    if ("종료".equals(state)) {
	                        List<WebElement> scoreElements = game.findElements(By.cssSelector("strong[class^='MatchBoxHeadToHeadArea_score__']"));
	                        if (scoreElements.size() >= 2) {
	                            dto.setAwayScore(scoreElements.get(0).getText());
	                            dto.setHomeScore(scoreElements.get(1).getText());
	                        }
	                    } else {
	                        dto.setAwayScore("");
	                        dto.setHomeScore("");
	                    }

	                    list.add(dto);
	                } catch (Exception innerEx) {
	                    System.out.println("⚠️ 경기 파싱 중 오류: " + innerEx.getMessage());
	                }
	            }

	            map.put(date, list);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        driver.quit();
	    }

	    return map;
	}

}
