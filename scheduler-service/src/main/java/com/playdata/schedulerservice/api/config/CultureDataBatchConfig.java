package com.playdata.schedulerservice.api.config;

import com.playdata.schedulerservice.api.entity.AnimalHospital;
import com.playdata.schedulerservice.api.entity.PetCulture;
import com.playdata.schedulerservice.api.util.AnimalHospitalFieldSetMapper;
import com.playdata.schedulerservice.api.util.PetCultureFielSetMapper;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class CultureDataBatchConfig {

    // 배치 관련 메타데이터 저장소 (실행 정보)
    private final JobRepository jobRepository;
    // 배치에서 사용하는 트랜잭션 관리자
    private final PlatformTransactionManager transactionManager;
    // 데이터베이스 연결 정보
    private final DataSource dataSource;

    // 데이터 읽기 (ItemReader)
    @Bean
    public FlatFileItemReader<PetCulture> petCultureReader() {
        return new FlatFileItemReaderBuilder<PetCulture>()
                .name("petCultureCsvReader") // 이름 지어주기
                // 어떤 파일을 읽을것인가
                // 현재 사용할 디렉토리의 위치는 본인에 맞게 수정해야함.
                .resource(new FileSystemResource("C:\\nyangmong_image\\culture\\pet_culture.csv"))
                .delimited() // 쉼표로 구분된 csv 파일입니다.
                .names(
                        "facilityName",        // 시설명
                        "categoryOne",         // 카테고리 1명
                        "categoryTwo",         // 카테고리 2명
                        "categoryThree",       // 카테고리 3명
                        "sido",                // 시도명
                        "sigungu",             // 시군구명
                        "legalDong",           // 법정동명
                        "liName",              // 리명
                        "lnbrNo",              // 번지번호
                        "roadName",            // 도로명
                        "buildingNo",          // 건물번호
                        "mapx",                // 위치위도
                        "mapy",                // 위치경도
                        "zipNo",               // 우편번호
                        "roadAddress",         // 도로명주소명
                        "fullAddress",         // 지번주소
                        "telNum",              // 전화번호
                        "url",                 // 홈페이지 URL
                        "restInfo",            // 휴무일안내내용
                        "operTime",            // 운영시간
                        "parking",             // 주차가능여부
                        "price",               // 이용가격내용
                        "petWith",             // 반려동물가능여부
                        "petInfo",             // 반려동물정보내용
                        "petSize",             // 입장가능반려동물크기값
                        "petRestrict",         // 반려동물제한사항내용
                        "inPlace",             // 내부장소동반가능여부
                        "outPlace",            // 외부장소동반가능여부
                        "desc",                // 시설정보설명
                        "extraFee",            // 반려동물동반추가요금값
                        "lastUpdate"          // 최종수정일자

                )
                .encoding("UTF-8")
                .fieldSetMapper(new PetCultureFielSetMapper())
                .linesToSkip(1) // 첫 줄 (헤더) 건너뛰기
                .build();
    }

    // 데이터 저장하기 (ItemWriter)
    @Bean
    public JdbcBatchItemWriter<PetCulture> petCultureWriter() {
        return new JdbcBatchItemWriterBuilder<PetCulture>()
                .itemSqlParameterSourceProvider(BeanPropertySqlParameterSource::new)
                .sql("""
                    INSERT INTO pet_culture (
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
                    """)
                .dataSource(dataSource)
                .build();

    }

    // 작업단계 만들기 (step)
    @Bean
    public Step csvToDbStepCulture() {
        return new StepBuilder("csvToDbStepCulture", jobRepository)
                // <AnimalHospital, AnimalHospital>: Reader에서 읽어온 타입과 Writer로 전달하는 데이터 타입 명시
                // chunk: step이 작업을 처리할 때 기준에 맞춰 나눠서 작업을 처리.
                // chunk(500): 500개씩 묵어서 처리, 단위별로 작업 후 commit, 문제가 있다면 rollback
                // 단위를 나눠놓지 않으면 전체 데이터가 rollback 되기 때문에, 작은 단위로 나눠 작업을 진행
                .<PetCulture, PetCulture>chunk(500, transactionManager)
                .reader(petCultureReader())
                .writer(petCultureWriter())
                .build();
    }

    // 전체 작업 정의하기 (Job)
    @Bean
    public Job csvToDbJobCulture() {
        return new JobBuilder("csvToDbJobCulture", jobRepository)
                .start(csvToDbStepCulture())
                .build();
    }

}
