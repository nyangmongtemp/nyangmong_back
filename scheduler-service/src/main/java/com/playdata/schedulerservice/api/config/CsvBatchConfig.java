package com.playdata.schedulerservice.api.config;

import com.playdata.schedulerservice.api.entity.AnimalHospital;
import com.playdata.schedulerservice.api.util.AnimalHospitalFieldSetMapper;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CsvBatchConfig {

    // 배치 관련 메타데이터 저장소 (실행 정보)
    private final JobRepository jobRepository;
    // 배치에서 사용하는 트랜잭션 관리자
    private final PlatformTransactionManager transactionManager;
    // 데이터베이스 연결 정보
    private final DataSource dataSource;

    // 데이터 읽기 (ItemReader)
    @Bean
    public FlatFileItemReader<AnimalHospital> animalHospitalReader() {
        return new FlatFileItemReaderBuilder<AnimalHospital>()
                .name("animalHospitalCsvReader") // 이름 지어주기
                // 어떤 파일을 읽을것인가
                .resource(new FileSystemResource("/Users/ubing/Desktop/nyangmong/csv/animalHospital.csv"))
                .delimited() // 쉼표로 구분된 csv 파일입니다.
                .names(
                        "id",                        // 번호
                        "serviceName",               // 개방서비스명
                        "serviceId",                 // 개방서비스아이디
                        "regionCode",                // 개방자치단체코드
                        "managementNumber",          // 관리번호
                        "approvalDate",              // 인허가일자
                        "permitCancelDate",          // 인허가취소일자
                        "businessStatusCode",        // 영업상태구분코드
                        "businessStatusName",        // 영업상태명
                        "detailedBusinessStatusCode",// 상세영업상태코드
                        "detailedBusinessStatusName",// 상세영업상태명
                        "closureDate",               // 폐업일자
                        "temporaryClosureStartDate", // 휴업시작일자
                        "temporaryClosureEndDate",   // 휴업종료일자
                        "reopeningDate",             // 재개업일자
                        "phoneNumber",               // 소재지전화
                        "siteArea",                  // 소재지면적
                        "postalCode",                // 소재지우편번호
                        "fullAddress",               // 소재지전체주소
                        "roadAddress",               // 도로명전체주소
                        "roadPostalCode",            // 도로명우편번호
                        "businessName",              // 사업장명
                        "lastModified",              // 최종수정시점
                        "dataUpdateType",            // 데이터갱신구분
                        "dataUpdateDate",            // 데이터갱신일자
                        "businessType",              // 업태구분명
                        "coordinateX",               // 좌표정보 x (EPSG:5174)
                        "coordinateY",               // 좌표정보 y (EPSG:5174)
                        "taskType",                  // 업무구분명
                        "detailedTaskType",          // 상세업무구분명
                        "rightsHolderNumber",        // 권리주체일련번호
                        "totalEmployees"             // 총직원수
                )
                .encoding("UTF-8")
                .fieldSetMapper(new AnimalHospitalFieldSetMapper())
                .linesToSkip(1) // 첫 줄 (헤더) 건너뛰기
                .build();
    }

    // 데이터 저장하기 (ItemWriter)
    @Bean
    public JdbcBatchItemWriter<AnimalHospital> animalHospitalWriter() {
        return new JdbcBatchItemWriterBuilder<AnimalHospital>()
                .itemSqlParameterSourceProvider(BeanPropertySqlParameterSource::new)
                .sql("""
                        INSERT INTO animal_hospital (
                            id, service_name, service_id, region_code,
                            management_number, approval_date, permit_cancel_date,
                            business_status_code, business_status_name, detailed_business_status_code,
                            detailed_business_status_name, closure_date, temporary_closure_start_date,
                            temporary_closure_end_date, reopening_date, phone_number,
                            site_area, postal_code, full_address, road_address,
                            road_postal_code, business_name, last_modified,
                            data_update_type, data_update_date, business_type,
                            coordinate_x, coordinate_y, task_type,
                            detailed_task_type, rights_holder_number, total_employees
                        ) VALUES (
                            :id, :serviceName, :serviceId, :regionCode,
                            :managementNumber, :approvalDate, :permitCancelDate,
                            :businessStatusCode, :businessStatusName, :detailedBusinessStatusCode,
                            :detailedBusinessStatusName, :closureDate, :temporaryClosureStartDate,
                            :temporaryClosureEndDate, :reopeningDate, :phoneNumber,
                            :siteArea, :postalCode, :fullAddress, :roadAddress,
                            :roadPostalCode, :businessName, :lastModified,
                            :dataUpdateType, :dataUpdateDate, :businessType,
                            :coordinateX, :coordinateY, :taskType,
                            :detailedTaskType, :rightsHolderNumber, :totalEmployees
                        )
                        ON DUPLICATE KEY UPDATE
                            service_name = VALUES(service_name),
                            service_id = VALUES(service_id),
                            region_code = VALUES(region_code),
                            management_number = VALUES(management_number),
                            approval_date = VALUES(approval_date),
                            permit_cancel_date = VALUES(permit_cancel_date),
                            business_status_code = VALUES(business_status_code),
                            business_status_name = VALUES(business_status_name),
                            detailed_business_status_code = VALUES(detailed_business_status_code),
                            detailed_business_status_name = VALUES(detailed_business_status_name),
                            closure_date = VALUES(closure_date),
                            temporary_closure_start_date = VALUES(temporary_closure_start_date),
                            temporary_closure_end_date = VALUES(temporary_closure_end_date),
                            reopening_date = VALUES(reopening_date),
                            phone_number = VALUES(phone_number),
                            site_area = VALUES(site_area),
                            postal_code = VALUES(postal_code),
                            full_address = VALUES(full_address),
                            road_address = VALUES(road_address),
                            road_postal_code = VALUES(road_postal_code),
                            business_name = VALUES(business_name),
                            last_modified = VALUES(last_modified),
                            data_update_type = VALUES(data_update_type),
                            data_update_date = VALUES(data_update_date),
                            business_type = VALUES(business_type),
                            coordinate_x = VALUES(coordinate_x),
                            coordinate_y = VALUES(coordinate_y),
                            task_type = VALUES(task_type),
                            detailed_task_type = VALUES(detailed_task_type),
                            rights_holder_number = VALUES(rights_holder_number),
                            total_employees = VALUES(total_employees)
                        """)
                .dataSource(dataSource) // 데이터베이스 정보 전달
                .build();

    }

    // 작업단계 만들기 (step)
    @Bean
    public Step csvToDbStep() {
        return new StepBuilder("csvToDbStep", jobRepository)
                // <AnimalHospital, AnimalHospital>: Reader에서 읽어온 타입과 Writer로 전달하는 데이터 타입 명시
                // chunk: step이 작업을 처리할 때 기준에 맞춰 나눠서 작업을 처리.
                // chunk(500): 500개씩 묵어서 처리, 단위별로 작업 후 commit, 문제가 있다면 rollback
                // 단위를 나눠놓지 않으면 전체 데이터가 rollback 되기 때문에, 작은 단위로 나눠 작업을 진행
                .<AnimalHospital, AnimalHospital>chunk(500, transactionManager)
                .reader(animalHospitalReader())
                .writer(animalHospitalWriter())
                .build();
    }

    // 전체 작업 정의하기 (Job)
    @Bean
    public Job csvToDbJob() {
        return new JobBuilder("csvToDbJob", jobRepository)
                .start(csvToDbStep())
                .build();
    }

}