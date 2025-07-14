package com.playdata.schedulerservice.api.batch.writer;

import com.playdata.schedulerservice.api.entity.MapEntity;
import com.playdata.schedulerservice.api.entity.StrayAnimalEntity;
import com.playdata.schedulerservice.api.repository.MapRepository;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Batch에서 데이터를 DB에 기록하는 역할을 하는 ItemWriter 구현체.

 * - MapApiItemReader로부터 넘어온 AnimalsEntity들을 DB에 저장 또는 업데이트.
 * - 이미 존재하는 타이틀(title)의 경우, 변경된 값만 업데이트.
 * - 처음 보는 지도데이터는 신규 데이터로 저장.
 * - 타이틀을 Set에 저장해서 이후 삭제용 필터링에도 활용 가능.
 */
@Component
@RequiredArgsConstructor
public class MapCustomItemWriter implements ItemWriter<MapEntity> {

    private final MapRepository mapRepository;

    // 이후 "DB에는 있는데, API에는 없는 데이터"를 삭제할 때 사용할 수 있음
    @Getter
    private final Set<String> contentIdFromApi = new HashSet<>();

    /**
     * Chunk 단위로 데이터가 넘어오며, 각 지도 정보를 DB에 저장하거나 업데이트한다.
     * @param items 이번 배치 사이클에서 처리할 MapEntity 리스트
     */
    @Override
    @Transactional
    public void write(Chunk<? extends MapEntity> items) {
        for (MapEntity incoming : items) {

            // 유기번호를 Set에 저장 (삭제 대상 판별용)
            contentIdFromApi.add(incoming.getContentId());

            // DB에서 같은 contentId가 이미 존재하는지 확인
            Optional<MapEntity> optional = mapRepository.findByContentId(incoming.getContentId());

            // 이미 존재하는 경우 → 데이터가 바뀌었는지 확인 후 업데이트
            if (optional.isPresent()) {
                MapEntity existing = optional.get();

                // 비교해서 기존 데이터와 다른 경우만 업데이트
                if (isChanged(existing, incoming)) {
                    existing.updateIfChanged(incoming); // 변경된 필드만 업데이트
                    mapRepository.save(existing); // DB 저장
                }
            } else {
                // DB에 존재하지 않는 contentId → 신규 데이터로 저장
                mapRepository.save(incoming);
            }
        }
    }

    /**
     * 기존 DB의 지도 정보와 새로 들어온 정보 간에 변경 여부를 판단하는 메서드.
     * (비교 대상은 중요 필드만 선택적으로 지정)
     *
     * @param db 기존 DB에 저장된 MapEntity
     * @param incoming 새로 들어온 MapEntity
     * @return 변경된 필드가 하나라도 있으면 true
     */
    private boolean isChanged(MapEntity db, MapEntity incoming) {
        return !Objects.equals(db.getAddr2(), incoming.getAddr2()) ||
                !Objects.equals(db.getImage1(), incoming.getImage1()) ||
                !Objects.equals(db.getImage2(), incoming.getImage2()) ||
                !Objects.equals(db.getMapx(), incoming.getMapx()) ||
                !Objects.equals(db.getMapy(), incoming.getMapy()) ||
                !Objects.equals(db.getTel(), incoming.getTel()) ||
                !Objects.equals(db.getZipCode(), incoming.getZipCode()) ||
                !Objects.equals(db.getAddressCode(), incoming.getAddressCode()) ||
                !Objects.equals(db.getContentType(), incoming.getContentType());
    }
}