package com.playdata.schedulerservice.crawling.crawler;

import com.playdata.schedulerservice.crawling.entity.FestivalEntity;
import com.playdata.schedulerservice.crawling.repository.PetRepository;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * 네이버 검색 결과에서 "펫 박람회" 관련 행사를 크롤링하는 컴포넌트 클래스입니다.
 *
 * 주요 기능:
 * - 네이버 검색 결과 페이지를 순회하며 각 행사 상세 페이지 URL을 수집
 * - 상세 페이지 방문 후 행사 정보를 추출
 * - 이미지 다운로드 및 로컬 저장 처리
 * - 중복 데이터는 해시로 체크, 기존 DB 데이터는 업데이트 처리
 * - 신규 데이터는 저장
 *
 * @author
 */
@Component
@RequiredArgsConstructor
public class NaverPetEventCrawler {

    // JPA Repository : DB CRUD 작업용
    private final PetRepository petEventRepository;

    // 날짜 범위 정규식 패턴: "yyyy.MM.dd ~ yyyy.MM.dd" 형식에서 시작일/종료일 추출용
    private static final Pattern DATE_RANGE_PATTERN = Pattern.compile("(\\d{4}\\.\\d{2}\\.\\d{2})[^~]*~[^~]*(\\d{4}\\.\\d{2}\\.\\d{2})");
    // 날짜 파싱 포맷터: "yyyy.MM.dd"
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    // 이미지 저장 경로 (application.properties에서 주입)
    @Value("${imagePath.url}")
    private String saveDir;

    // 크롤링 시작 네이버 검색 URL (인코딩된 '펫 박람회' 검색어)
    private static final String BASE_URL = "https://search.naver.com/search.naver?query=%ED%8E%AB+%EB%B0%95%EB%9E%8C%ED%9A%8C";

    /**
     * 크롤링을 시작하는 메인 메서드.
     * 1) 네이버 검색 결과 페이지 순회하며 상세 페이지 URL 수집
     * 2) 상세 페이지 방문 후 정보 추출 및 DB 저장/업데이트 처리
     */
    public void crawl() {
        // Selenium ChromeDriver 생성 (드라이버 경로 등 환경설정 필요)
        WebDriver driver = new ChromeDriver();

        // 최대 10초간 요소 로딩 대기 설정
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // 상세 페이지 URL을 중복 제거하며 순서 유지하는 LinkedHashSet에 수집
        Set<String> detailUrls = collectDetailUrls(driver, wait);

        // 수집된 각 상세 페이지 URL 방문하여 데이터 추출 및 저장
        for (String detailUrl : detailUrls) {
            try {
                // 상세 페이지 접속
                driver.get(detailUrl);

                // 상세 페이지 내 박람회 주요 영역 로딩 대기
                wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector(".sc_new.cs_common_module.case_normal.color_9._cs_festival")));

                // 상세 페이지 정보 추출 (FestivalEntity 객체 생성)
                FestivalEntity petEvent = extractDetails(driver);

                // DB 저장 또는 기존 데이터 업데이트 처리
                saveOrUpdateEvent(petEventRepository, petEvent);

            } catch (Exception e) {
                // 상세 페이지 처리 중 오류 발생 시 로그 출력
                System.out.println("상세 페이지 크롤링 오류: " + detailUrl);
                e.printStackTrace();
            }
        }

        // 크롤링 종료 후 드라이버 리소스 해제
        driver.quit();
    }

    /**
     * 네이버 검색 결과 페이지를 순회하며 각 카드(행사) 상세 페이지 URL을 수집하는 메서드.
     * 페이징을 자동으로 클릭하여 마지막 페이지까지 모든 URL을 Set에 저장.
     *
     * @param driver Selenium WebDriver 인스턴스
     * @param wait 요소 로딩 대기용 WebDriverWait 인스턴스
     * @return 중복 없이 순서 유지된 상세 페이지 URL Set
     */
    private Set<String> collectDetailUrls(WebDriver driver, WebDriverWait wait) {
        Set<String> detailUrls = new LinkedHashSet<>();

        try {
            // 검색 결과 첫 페이지 접속
            driver.get(BASE_URL);

            while (true) {
                // 박람회 카드 리스트가 완전히 로딩될 때까지 대기
                wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector(".sc_new.cs_common_module.case_list.color_5._cs_festival_list")));

                // 각 카드의 이미지 링크(a 태그) 요소들을 가져옴
                List<WebElement> cards = driver.findElements(By.cssSelector(".card_item a.img_box"));
                for (WebElement card : cards) {
                    // 링크 URL(href) 속성 추출
                    String href = card.getAttribute("href");
                    if (!href.startsWith("http")) {
                        // 상대 경로일 경우 절대 경로로 변환
                        href = "https://search.naver.com/search.naver" + href;
                    }
                    // 중복 없이 Set에 저장
                    detailUrls.add(href);
                }

                try {
                    // 다음 페이지 버튼 찾기 (활성화된 상태)
                    WebElement nextPage = driver.findElement(By.cssSelector(".pgs .pg_next.on"));
                    nextPage.click();  // 다음 페이지 클릭
                    Thread.sleep(2000); // 페이지 전환 안정성 위해 2초 대기
                } catch (NoSuchElementException e) {
                    // 다음 페이지 버튼이 없으면 루프 종료 (마지막 페이지)
                    break;
                }
            }

        } catch (Exception e) {
            // 페이징 또는 네트워크 등 예외 발생 시 로그 출력
            e.printStackTrace();
        }

        return detailUrls;
    }

    /**
     * 상세 페이지에서 박람회 정보를 추출하는 메서드.
     * - 제목, 이미지, 기간, 장소, 시간, 요금 등 기본 정보
     * - 지도 주소 (addr) 추출 포함
     *
     * @param driver Selenium WebDriver 인스턴스 (상세 페이지 위치)
     * @return FestivalEntity 빌더로 생성한 행사 정보 객체
     */
    private FestivalEntity extractDetails(WebDriver driver) {
        // 제목 (예: 2025 PET&MORE 박람회)
        String eventTitle = safeFindText(driver, By.cssSelector(".title strong._text"));

        // 상태 정보 (예: 종료됨, 진행중 등)
        String source = safeFindText(driver, By.cssSelector(".title span.state_end"));

        // 공식 웹사이트 링크 (있을 수도, 없을 수도 있음)
        String eventUrl = safeFindAttribute(driver, By.cssSelector(".title._title_ellipsis a.area_text_title"), "href");

        // 행사 이미지 다운로드 및 로컬 저장
        String imagePath = "";
        try {
            WebElement imageEl = driver.findElement(By.cssSelector(".detail_info img"));
            imagePath = downloadImage(imageEl.getAttribute("src"));
        } catch (NoSuchElementException ignored) {
            // 이미지가 없는 경우는 무시
        }

        // 행사 상세 정보 초기화
        String eventDate = "";        // "2025.12.01 ~ 2025.12.05"
        String reservationDate = "";  // "2025.03.01 ~ 2025.11.30"
        String eventTime = "";        // "10:00 ~ 18:00"
        String location = "";         // "수원컨벤션센터"
        String eventMoney = "";       // "사전등록 무료"
        String addr = "";             // 지도 페이지에서 추출할 실제 주소

        // 시작일과 종료일은 LocalDate로 별도 저장
        LocalDate startDate = null;
        LocalDate endDate = null;

        // 상세정보 섹션(.info_group)을 모두 가져와 반복 처리
        List<WebElement> infoGroups = driver.findElements(By.cssSelector(".info_group"));

        for (WebElement info : infoGroups) {
            String label = info.getText(); // 구분 텍스트 추출

            // 기간(행사일정) 추출
            if (label.contains("기간") && label.contains("~")) {
                try {
                    // 날짜 범위 텍스트 2개 추출
                    List<WebElement> dates = info.findElements(By.cssSelector(".text"));
                    if (dates.size() >= 2) {
                        eventDate = dates.get(0).getText() + " " + dates.get(1).getText();

                        // 정규식으로 yyyy.MM.dd 형식 날짜 추출
                        Matcher matcher = DATE_RANGE_PATTERN.matcher(eventDate);
                        if (matcher.find()) {
                            startDate = LocalDate.parse(matcher.group(1), FORMATTER);
                            endDate = LocalDate.parse(matcher.group(2), FORMATTER);
                        }
                    }

                    // 숨겨진 예약 기간 정보 추출 (자바스크립트 실행)
                    WebElement hiddenEl = info.findElement(By.cssSelector(".more_list .text"));
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    reservationDate = (String) js.executeScript("return arguments[0].textContent;", hiddenEl);
                } catch (NoSuchElementException ignored) {
                    reservationDate = "";
                }
            }

            // 행사 시간 정보
            else if (label.contains("시간")) {
                eventTime = safeFindText(info, By.cssSelector("dd"));
            }

            // 행사 장소 정보
            else if (label.contains("장소")) {
                location = safeFindText(info, By.cssSelector("dd a"));
            }

            // 행사 요금 정보
            else if (label.contains("요금")) {
                eventMoney = safeFindText(info, By.cssSelector(".desc._text"));
            }
        }

        // 지도 버튼 클릭 → iframe 내부에서 실제 주소 추출
        try {
            // .cm_info_box 내 .button_area 안의 .place 클래스를 가진 모든 요소(지도 버튼)를 찾음
            List<WebElement> mapButtons = driver.findElements(By.cssSelector(".cm_info_box .button_area .place"));

            // 버튼이 존재하는 경우에만 처리
            if (!mapButtons.isEmpty()) {
                // 첫 번째 지도 버튼 요소를 선택
                WebElement mapButton = mapButtons.get(0);

                // 버튼의 href 속성(네이버 지도 URL)을 가져옴
                String mapHref = mapButton.getAttribute("href");

                // href가 존재하고 네이버 지도 URL일 경우에만 이동
                if (mapHref != null && mapHref.startsWith("https://map.naver.com")) {
                    // 해당 지도 페이지로 이동
                    driver.get(mapHref);

                    // 최대 10초 대기 설정
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

                    // id가 entryIframe인 iframe이 로드되고 접근 가능해질 때까지 대기 후 iframe으로 전환
                    wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("entryIframe")));

                    // iframe 내부에서 주소가 담긴 요소가 DOM에 나타날 때까지 대기
                    WebElement addressEl = wait.until(
                            ExpectedConditions.presenceOfElementLocated(
                                    By.cssSelector(".place_section_content .LDgIH")  // 주소 요소 CSS 셀렉터
                            )
                    );

                    // 주소 텍스트를 추출하여 변수에 저장
                    addr = addressEl.getText();

                    // iframe에서 빠져나와 기본 컨텐츠로 전환
                    driver.switchTo().defaultContent();
                }
            }
        } catch (Exception e) {
            System.out.println("주소 추출 중 오류 발생: " + e.getMessage());
        }

        // 중복 체크용 고유 해시 생성 (제목 + URL + 장소 기준)
        String hash = generateHash(eventTitle, eventUrl, location);

        // 추출된 데이터를 기반으로 FestivalEntity 생성 및 반환
        return FestivalEntity.builder()
                .title(eventTitle)
                .source(source)
                .url(eventUrl)
                .festivalDate(eventDate)
                .reservationDate(reservationDate)
                .startDate(startDate)
                .endDate(endDate)
                .festivalTime(eventTime)
                .money(eventMoney)
                .location(location)
                .imagePath(imagePath)
                .hash(hash)
                .addr(addr) // 실제 주소
                .build();
    }

    /**
     * DB에 이미 저장된 데이터와 해시값 비교 후
     * 변경사항이 있으면 업데이트, 없으면 새로 저장하는 메서드.
     *
     * @param repository JPA Repository 인스턴스
     * @param petEvent 저장 또는 업데이트할 FestivalEntity 객체
     */
    private void saveOrUpdateEvent(PetRepository repository, FestivalEntity petEvent) {
        Optional<FestivalEntity> optionalEvent = repository.findByHash(petEvent.getHash());

        if (optionalEvent.isPresent()) {
            FestivalEntity existing = optionalEvent.get();

            // equals() 메서드를 오버라이드했다고 가정하고 기존과 비교
            if (!existing.equals(petEvent)) {
                // 변경된 필드를 업데이트하는 메서드 호출 (엔티티 내 구현)
                existing.updateFrom(petEvent);

                repository.save(existing);
            }
        } else {
            // 신규 데이터 저장
            repository.save(petEvent);
        }
    }

    /**
     * 이미지 URL에서 실제 이미지 파일을 다운로드하고,
     * 지정된 로컬 디렉터리에 저장한 후 경로를 반환함.
     *
     * @param imageUrl 이미지 원본 URL
     * @return 저장된 파일의 절대 경로 문자열, 실패 시 null 반환
     */
    private String downloadImage(String imageUrl) {
        try {
            // 이미지 파일명 추출 (마지막 '/' 이후)
            String filename = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);

            // 저장할 디렉터리 및 전체 경로 생성
            Path dirPath = Paths.get(saveDir);
            Path targetPath = dirPath.resolve(filename);

            // 저장 디렉터리가 없으면 생성
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            // URL 연결 스트림 열어 로컬 파일로 복사 (덮어쓰기)
            try (InputStream in = new URL(imageUrl).openStream()) {
                Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }

            // 저장 경로 반환
            return targetPath.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 여러 문자열을 연결하여 MD5 해시를 생성하는 유틸리티 메서드.
     * null 값은 빈 문자열로 처리.
     *
     * @param values 해시 생성에 사용할 문자열 배열
     * @return 16진수 형식 MD5 해시 문자열
     */
    private String generateHash(String... values) {
        StringBuilder sb = new StringBuilder();
        for (String value : values) {
            sb.append(Optional.ofNullable(value).orElse(""));
        }
        return DigestUtils.md5DigestAsHex(sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 안전하게 특정 요소의 텍스트를 추출하는 헬퍼 메서드.
     * 요소가 없으면 빈 문자열 반환.
     *
     * @param parent 요소 탐색 시작 위치 (WebDriver 또는 WebElement)
     * @param by 찾을 요소의 By 셀렉터
     * @return 요소의 텍스트, 없으면 빈 문자열
     */
    private String safeFindText(SearchContext parent, By by) {
        try {
            return parent.findElement(by).getText();
        } catch (NoSuchElementException e) {
            return "";
        }
    }

    /**
     * 안전하게 특정 요소의 속성값을 추출하는 헬퍼 메서드.
     * 요소가 없으면 빈 문자열 반환.
     *
     * @param parent 요소 탐색 시작 위치 (WebDriver 또는 WebElement)
     * @param by 찾을 요소의 By 셀렉터
     * @param attr 추출할 속성명 (예: "href", "src")
     * @return 속성값, 없으면 빈 문자열
     */
    private String safeFindAttribute(SearchContext parent, By by, String attr) {
        try {
            return parent.findElement(by).getAttribute(attr);
        } catch (NoSuchElementException e) {
            return "";
        }
    }
}