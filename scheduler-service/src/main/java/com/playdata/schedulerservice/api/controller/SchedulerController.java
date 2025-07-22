package com.playdata.schedulerservice.api.controller;

import com.playdata.schedulerservice.api.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 동물 배치 Job 실행용 REST API Controller.
 *
 * POST /api/animals/sync-api 호출 시 수동으로 배치 Job 실행.
 * 배치 중복 실행 방지를 위해 현재시간 기반 JobParameters 전달.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class SchedulerController {

    private final JobLauncher jobLauncher;
    private final Job syncAnimalJob;
    private final Job syncMapJob;

    private final Job csvToDbJob;

    // made By 이은혁
    private final Job csvToDbJobCulture;
    private final SchedulerService schedulerService;


    @GetMapping("/scheduler/api/animal")
    public String animalRunApiSyncJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(syncAnimalJob, jobParameters);
            return "배치 실행 완료 - 상태: " + execution.getStatus();

        } catch (Exception e) {
            return "배치 실행 실패: " + e.getMessage();
        }
    }

    @GetMapping("/scheduler/api/map")
    public String mapRunApiSyncJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(syncMapJob, jobParameters);
            return "배치 실행 완료 - 상태: " + execution.getStatus();

        } catch (Exception e) {
            return "배치 실행 실패: " + e.getMessage();
        }
    }

    @PostMapping("/scheduler/csv-to-db")
    public String runCsvToDbJob() {
        try {
            // Spring Batch는 같은 파라미터로는 한 번만 실행되는 규칙이 있음.
            // 매번 다른 파라미터를 만들면 같은 배치를 여러 번 실행할 수 있습니다.
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis()) // 현재 시간 추가
                    .toJobParameters();

            log.info(" ========== CSV To Database 배치 작업 시작! =========");
            JobExecution jobExecution = jobLauncher.run(csvToDbJob, jobParameters);
            log.info(" ========== 배치 완료! 상태: {} =========", jobExecution.getStatus());

            return String.format("배치 실행 완료! 상태: %s, 처리된 아이템 수: %d",
                    jobExecution.getStatus(),
                    jobExecution.getStepExecutions().iterator().next().getWriteCount());

        } catch (Exception e) {
            log.error("배치 실행 중 오류 발생!", e);
            return "배치 실행 실패!: " + e.getMessage();
        }
    }

    @PostMapping("/scheduler/pet")
    public String runPetCultureJob() {
        try {
            // Spring Batch는 같은 파라미터로는 한 번만 실행되는 규칙이 있음.
            // 매번 다른 파라미터를 만들면 같은 배치를 여러 번 실행할 수 있습니다.
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis()) // 현재 시간 추가
                    .toJobParameters();

            log.info(" ========== CSV To Database 배치 작업 시작! =========");
            JobExecution jobExecution = jobLauncher.run(csvToDbJobCulture, jobParameters);
            log.info(" ========== 배치 완료! 상태: {} =========", jobExecution.getStatus());

            return String.format("배치 실행 완료! 상태: %s, 처리된 아이템 수: %d",
                    jobExecution.getStatus(),
                    jobExecution.getStepExecutions().iterator().next().getWriteCount());

        } catch (Exception e) {
            log.error("배치 실행 중 오류 발생!", e);
            return "배치 실행 실패!: " + e.getMessage();
        }
    }

    @PostMapping("/scheduler/detail")
    public String runPetCultureJobDetail() {

        schedulerService.petCultureToDetailTable();
        return "성공적으로 테이블로 매핑됨!";

    }
}