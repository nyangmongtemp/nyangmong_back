# nyangmong 커뮤니티 플랫폼

## 1. 프로젝트 개요

- **프로젝트 명**

냥몽 - 반려동물을 위한 sns 기반 커뮤니티 플랫폼

- **목표**

반려동물을 키우는 사용자들이 서비스를 통해 정보를 전달받고, 서로 소통하고 정보를 교류하며, 커뮤니티 활동, 알람/서비스 정보 이용까지 가능한 올인원 웹 플랫폼 구축

반려동물 보호자들이 정보 공유, 커뮤니티 활동, 물품 거래, 질문과 상담 등 다양한 활동을 한 플랫폼 안에서 해결할 수 있도록 하는 종합 커뮤니티 플랫폼을 구축한다.

- **타깃 사용자**

반려동물을 키우는 보호자

반려동물 입양을 고민하는 예비 보호자

보호자 간 교류를 원하는 사람들

반려동물 관계 업계 및 업체

- **플랫폼 유형**

커뮤니티 + 정보 제공 + 부가 서비스

## 2. 주요 기능 정의

**사용자 기능**

- 분양 게시판
    - 사용자 간의 동물 입양 게시판
        - 사용자의 반려동물 분양 전용
        - 사용자 인증 및 관리자 승인 등 보안 요소 포함 예정
    - 유기 동물 입양 게시판
        - api를 통해 받아온 유기 동물의 정보를 게시물 형태로 생성
        - 사용자가 유기 동물의 보호소 정보를 통해 입양

- 정보 게시판
    - 질문 게시판
        - 사용자간의 질문 및 답변
    - 우리 아이 소개 게시판
        - 사용자의 반려동물을 소개
    - 후기 게시판
        - 물품(사료, 장난감 등) 후기 정보

- 자유 게시판
    - 회원들의 자유로운 소통 기능
    - 사용자가 자유로운 주제로 게시물을 생성

- 행사 게시판
    - 반려동물 동반 가능한 행사 정보를 외부 데이터 크롤링 진행 (네이버 ‘펫 박람회’ 진행 예정)

- 지도 기능
    - 반려동물과 관련된 정보들의 장소를 사용자가 손쉽게 확인할 수 있게 지도에 맵핑하여 노출 (공공 api와 지도 api를 이용하여 사용자가 선택한 카테고리에 맞는 정보를 노출)

- 배너 기능
    - 현재 열리고 있는 반려동물 동반 가능 행사 정보 혹은 광고를 메인화면에 슬라이드 형식으로 노출

**관리자 기능**

- 사용자 관리
    - 회원 목록 조회
    - 회원 상세 조회
    - 회원 신고 이력 조회
    - 회원 정지 처리

- 관리자 관리
    - 관리자 등록

- 고객센터 관리
    - 고객센터 1:1 문의 리스트

- 컨텐츠 관리
    - 게시물 삭제

- 관리자 마이페이지
    - 관리자 개인정보 수정
    - 이메일 및 비밀번호 수정
        - 이메일 2차 인증을 통한 사용자 인증 수행

- 로그인/로그아웃
    - 로그인 1차 인증
    - 로그인 2차 인증번호 전송
    - 관리자 2차 인증 확인

- 관리자 로그 관리
    - 사용자의 정보 열람 시, 열람 로그 기록 생성
    - 관리자 간의 정보 열람 로그 확인

## 3. 부가 기능 정의

- 알림 기능
    - 회원이 만들어 놓은 설정에 맞는 푸시 알림 기능

## 4. 정보 제공 기능

- 한국 관광 공사 반려동물 동반 여행 서비스
    
    https://www.data.go.kr/data/15135102/openapi.do
    
- 전국 반려동물 동반 가능 문화 시설 위치 데이터
    
    https://www.data.go.kr/data/42090998/linkedData.do
    
- 행정 안전부_동물병원
    
    https://www.data.go.kr/data/15045050/fileData.do
    
- 농림축산식품부 농림축산검역본부_국가동물보호정보시스템 구조동물 조회 서비스
    
    [https://www.data.go.kr/data/15098931/openapi.do#/API 목록/shelter_v2](https://www.data.go.kr/data/15098931/openapi.do#/API%20%EB%AA%A9%EB%A1%9D/shelter_v2)
    
- 서울특별시청_반려동물을 위한 지역 생활정보
    
    [https://www.culture.go.kr/data/openapi/openapiView.do?id=519&category=I&keyword=동물&searchField=all&gubun=B](https://www.culture.go.kr/data/openapi/openapiView.do?id=519&category=I&keyword=%EB%8F%99%EB%AC%BC&searchField=all&gubun=B)
    
- Tour API
    - ‘반려동물’, ‘동물’ 등의 키워드로 관련된 전국의 행사 정보를 받아올 수 있는 api
    
    https://api.visitkorea.or.kr/#/
    
- 카카오맵 지도 API

---
## 요구사항 정의서
https://www.notion.so/21ea57f66b36808caee5c0a3cb3b9e71

---
## 시스템 아키텍처
https://drive.google.com/file/d/1MiyME-JhoVMHSYvkBzjGB4h7Pk215oPj/view

---
## ERD
https://www.notion.so/ERD-21ba57f66b368050a175fcbfd9dafa00

---
## WBS
https://docs.google.com/spreadsheets/d/1w18JaClePrCvDao6CgbzNxxJOVKuJbb6s-7KWtcPPTw/edit?gid=1419834988#gid=1419834988

---
## 화면설계서(와이어프레임)
https://www.notion.so/21ba57f66b368066a4c3ee305ee44add

---
## API 명세서
https://docs.google.com/spreadsheets/d/1X_YTlp7CTOgj-Iuppggapn36RxCNpFQR/edit?gid=1797139636#gid=1797139636

---
## 수집데이터 (API 데이터)
https://www.notion.so/API-21fa57f66b368034b53fe755b6f93f90
