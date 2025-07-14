package com.playdata.schedulerservice.api.batch.reader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playdata.schedulerservice.api.entity.AddressCode;
import com.playdata.schedulerservice.api.entity.ContentType;
import com.playdata.schedulerservice.api.entity.MapEntity;
import com.playdata.schedulerservice.api.util.SafeEnumParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

/**
 * Spring Batch의 ItemReader 구현체.
 * 외부 공공 API(지도 데이터)를 호출해 데이터를 읽어옴.

 * 특징:
 * - API가 페이징 방식이므로 모든 페이지를 순회하며 데이터를 수집.
 * - 수집한 데이터는 메모리에 저장해 Iterator로 순차 제공.
 * - StepScope를 사용해 Job 파라미터 주입 또는 스텝 실행 시 인스턴스 재생성 가능.
 */
@Component
@StepScope
public class MapApiItemReader implements ItemReader<MapEntity> {

    private final Iterator<MapEntity> dataIterator;

    public MapApiItemReader() {
        List<MapEntity> results = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            int pageNo = 1;
            int numOfRows = 500;
            int totalCount;
            String serviceKey = "JSn0E7LvFMcdl%2Bt%2FuNmxvKAfkGfvNVUlemWjY4O5%2BRNFksB7TRlw%2BXuaMe6Zz7Yt5QCYPl3G6Tc2t8jx6FUePg%3D%3D";

            do {
                String url = String.format(
                        "https://apis.data.go.kr/B551011/KorPetTourService/areaBasedList?serviceKey=%s&numOfRows=%d&pageNo=%d&MobileOS=ETC&MobileApp=nyangmong&_type=json",
                        serviceKey, numOfRows, pageNo);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

                HttpResponse<String> response = HttpClient.newHttpClient()
                        .send(request, HttpResponse.BodyHandlers.ofString());

                JsonNode body = mapper.readTree(response.body())
                        .path("response")
                        .path("body");

                JsonNode items = body.path("items").path("item");

                totalCount = body.path("totalCount").asInt();

                if (items.isArray()) {
                    for (JsonNode item : items) {
                        results.add(parseToEntity(item));
                    }
                } else if (items.isObject()) {
                    results.add(parseToEntity(items));
                }

                pageNo++;

            } while ((pageNo - 1) * numOfRows < totalCount);

        } catch (Exception e) {
            throw new RuntimeException("API 호출 실패", e);
        }

        this.dataIterator = results.iterator();
    }

    /**
     * 배치 Step이 한 건씩 데이터 요청 시 호출됨.
     * Iterator에서 다음 요소를 반환하고 없으면 null 반환하여 Step 종료 신호.
     */
    @Override
    public MapEntity read() {
        return dataIterator.hasNext() ? dataIterator.next() : null;
    }

    /**
     * JsonNode를 MapEntity 객체로 변환.
     * API 응답 필드와 엔티티 필드 매핑 처리 및 enum 안전 변환 적용.
     */
    private String getRequiredField(JsonNode item, String fieldName) {
        if (!item.hasNonNull(fieldName)) {
            throw new IllegalArgumentException(fieldName + " 필수값 누락");
        }
        return item.get(fieldName).asText();
    }
    private MapEntity parseToEntity(JsonNode item) {
        String addressCodeStr = getRequiredField(item, "areacode");
        AddressCode addressCode = AddressCode.from(addressCodeStr);

        String contentTypeStr = getRequiredField(item, "contenttypeid");
        ContentType contentType = ContentType.from(contentTypeStr);

        String title = getRequiredField(item, "title");
        String addr1 = getRequiredField(item, "addr1");
        String mapx = getRequiredField(item, "mapx");
        String mapy = getRequiredField(item, "mapy");
        String contentId = getRequiredField(item, "contentid");

        // 나머지 필드들은 옵션 처리
        String image1 = item.hasNonNull("firstimage") ? item.get("firstimage").asText() : null;
        String image2 = item.hasNonNull("firstimage2") ? item.get("firstimage2").asText() : null;
        String addr2 = item.hasNonNull("addr2") ? item.get("addr2").asText() : null;
        String tel = item.hasNonNull("tel") ? item.get("tel").asText() : null;
        String zipCode = item.hasNonNull("zipcode") ? item.get("zipcode").asText() : null;

        return MapEntity.builder()
                .addressCode(addressCode)
                .contentType(contentType)
                .title(title)
                .addr1(addr1)
                .mapx(mapx)
                .mapy(mapy)
                .image1(image1)
                .image2(image2)
                .addr2(addr2)
                .tel(tel)
                .zipCode(zipCode)
                .contentId(contentId)
                .build();
    }
}