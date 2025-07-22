package com.playdata.schedulerservice.api.service;

import com.playdata.schedulerservice.api.entity.PetCulture;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {

    private final EntityManager entityManager;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private Map<String, String> tableNames = Map.of("동물약국", "drug_store",
            "문예회관", "literary_center", "미술관", "pet_art", "미용", "pet_style",
            "박물관", "pet_museum", "반려동물용품", "pet_shop", "카페", "pet_cafe",
            "호텔", "pet_hotel");

    // petCulture의 데이터를 100행씩 조회해와서 처리하는 메소드
    public void petCultureToDetailTable() {
        // 아이템 개수
        int pageSize = 500;
        // 페이지 수
        int offset = 0;

        while (true) {
            List<PetCulture> batch = entityManager
                    .createQuery("SELECT p FROM PetCulture p", PetCulture.class)
                    .setFirstResult(offset)
                    .setMaxResults(pageSize)
                    .getResultList();
            
            if (batch.isEmpty()) {
                break;
            }

            System.out.println("Fetched batch of size: " + batch.size());
            for (PetCulture item : batch) {
                String tableName = tableNames.get(item.getCategoryThree());
                // 사용하지 않는 카테고리의 데이터인 경우
                if (tableName == null) {
                    // 로직을 그냥 수행하게끔
                    continue;
                }
                // 데이터를 삽입할 수 있게 Map으로 데이터 매핑
                Map<String, Object> paramMap = item.convertToParamMap();
                try {
                    // 매핑된 데이터를 해당하는 테이블로 삽입
                    mappingtoDetail(tableName, paramMap);
                } catch (Exception e) {
                    log.error("Failed to insert into table: {}, error: {}", tableName, e.getMessage(), e);
                    throw new RuntimeException("Failed to insert data into table: " + tableName, e);
                }
            }
            // 100개가 끝났으면, 다음 100개로 되게끔
            offset += pageSize;
        }
    }

    public void mappingtoDetail(String tableName, Map<String, Object> paramMap) {
        String sql = String.format("""
    INSERT INTO %s (
        facility_name, category_one, category_two, category_three,
        sido, sigungu, legal_dong, li_name, lnbr_no,
        road_name, building_no, mapx, mapy, zip_no,
        road_address, full_address, tel_num, url,
        rest_info, oper_time, parking, price, pet_with,
        pet_info, pet_size, pet_restrict, in_place,
        out_place, info_desc, extra_fee, last_update
    ) VALUES (
        :facilityName, :categoryOne, :categoryTwo, :categoryThree,
        :sido, :sigungu, :legalDong, :liName, :lnbrNo,
        :roadName, :buildingNo, :mapx, :mapy, :zipNo,
        :roadAddress, :fullAddress, :telNum, :url,
        :restInfo, :operTime, :parking, :price, :petWith,
        :petInfo, :petSize, :petRestrict, :inPlace,
        :outPlace, :infoDesc, :extraFee, :lastUpdate
    )
    ON DUPLICATE KEY UPDATE
    category_one = VALUES(category_one),
    category_two = VALUES(category_two),
    category_three = VALUES(category_three),
    sido = VALUES(sido),
    sigungu = VALUES(sigungu),
    legal_dong = VALUES(legal_dong),
    li_name = VALUES(li_name),
    lnbr_no = VALUES(lnbr_no),
    road_name = VALUES(road_name),
    building_no = VALUES(building_no),
    mapx = VALUES(mapx),
    mapy = VALUES(mapy),
    zip_no = VALUES(zip_no),
    road_address = VALUES(road_address),
    full_address = VALUES(full_address),
    tel_num = VALUES(tel_num),
    url = VALUES(url),
    rest_info = VALUES(rest_info),
    oper_time = VALUES(oper_time),
    parking = VALUES(parking),
    price = VALUES(price),
    pet_with = VALUES(pet_with),
    pet_info = VALUES(pet_info),
    pet_size = VALUES(pet_size),
    pet_restrict = VALUES(pet_restrict),
    in_place = VALUES(in_place),
    out_place = VALUES(out_place),
    info_desc = VALUES(info_desc),
    extra_fee = VALUES(extra_fee),
    last_update = VALUES(last_update)
    """, tableName);

        // 실행
        namedParameterJdbcTemplate.update(sql, paramMap);
    }

}
