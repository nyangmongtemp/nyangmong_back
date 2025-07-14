package com.playdata.schedulerservice.api.config;

import com.playdata.schedulerservice.api.batch.writer.MapCustomItemWriter;
import com.playdata.schedulerservice.api.entity.MapEntity;
import com.playdata.schedulerservice.api.repository.MapRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

/**
 * [Spring Batch] StepExecutionListener 구현 클래스.
 * - 한 Step의 실행 전/후에 추가 작업을 삽입하고 싶을 때 사용하는 리스너이다.

 * 본 클래스에서는 Step 실행 "후"(`afterStep`)에 아래의 동작을 수행:
 */
@Component
@RequiredArgsConstructor
public class MapStepListener implements StepExecutionListener {

    private final MapRepository mapRepository;

    private final MapCustomItemWriter itemWriter;

    /**
     * Step 실행 이후에 호출되는 메서드.
     * Step 내부에서 처리된 결과를 바탕으로 후처리 작업을 수행할 수 있다.
     */
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        // Step 실행 중에 수집된 모든 유기번호 (API 응답 기준)
        Set<String> desertionNoFromApi = itemWriter.getContentIdFromApi();

        // 현재 DB에 저장되어 있는 모든 유기동물 데이터를 조회
        List<MapEntity> allInDb = mapRepository.findAll();

        // 전체 DB 데이터를 순회하면서 삭제 조건에 해당하는지 검사
        for (MapEntity entity : allInDb) {

            // API 결과에 포함되지 않은 contentId 번호
            boolean notInApi = !desertionNoFromApi.contains(entity.getContentId());

            if (notInApi) {
                mapRepository.delete(entity);
            }
        }

        // 특별한 종료 상태는 없음 (null 반환 → 기본 처리)
        return null;
    }
}