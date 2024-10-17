# :family: TOSI-User

**TOSI 서비스**에서 회원과 관련된 기능을 **회원 서비스**로 분리한 프로젝트 입니다.  
[TOSI(The Only Story In the World, 토씨) 프로젝트](https://github.com/dykimHub/TOSI)에서 전체 구조를 확인하실 수 있습니다.

## 📅 마이그레이션 & 리팩토링 개요

- **기간**: 2024.09.14 ~

회원 서비스를 분리하면서 전반적인 코드 재사용성과 로직을 개선하였습니다.  
QueryDSL 및 DTO Projection, 캐싱을 추가하여 조회 성능을 개선하였습니다.  
쿠버네티스를 도입하여 오케스트레이션을 개선하였습니다.

## :computer: 기술 스택

![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![Visual Studio Code](https://img.shields.io/badge/Visual%20Studio%20Code-007ACC?style=for-the-badge&logo=Visual%20Studio%20Code&logoColor=white)

![Spring Boot](https://img.shields.io/badge/spring%20boot-%236DB33F.svg?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![QueryDSL](https://img.shields.io/badge/QueryDSL-4C7ED6?style=for-the-badge&logo=spring&logoColor=white)

![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-%23DC382D.svg?style=for-the-badge&logo=redis&logoColor=white)

![Docker](https://img.shields.io/badge/Docker-%232496ED.svg?style=for-the-badge&logo=docker&logoColor=white)
![Kubernetes](https://img.shields.io/badge/kubernetes-%23326CE5.svg?style=for-the-badge&logo=kubernetes&logoColor=white)
![EKS](https://img.shields.io/badge/AWS%20EKS-%23FF9900.svg?style=for-the-badge&logo=amazoneks&logoColor=white)
![ROUTE53](https://img.shields.io/badge/AWS%20route53-%23FF9900.svg?style=for-the-badge&logo=amazonroute53&logoColor=white)

## 📖 API 문서

프로젝트의 API는 **Swagger UI**를 통해 쉽게 확인하고 테스트할 수 있습니다.

- **회원 API 문서 주소**: 🔗 [https://www.tosi.world/swagger-ui/index.html?urls.primaryName=%ED%9A%8C%EC%9B%90](https://www.tosi.world/swagger-ui/index.html?urls.primaryName=%ED%9A%8C%EC%9B%90)

#### 🔐 API 사용 방법

모든 API는 **Access Token**을 획득하신 후 사용할 수 있습니다.

1. **회원 API 문서**에 접속하여 **로그인 API**를 실행합니다:

2. 로그인 API 요청 본문에 아래 정보를 입력하세요:
   ```json
   {
     "email": "test@test.com",
     "password": "test"
   }
   ```
3. 우측 상단 Authorize 버튼을 클릭하고 발급받은 토큰을 입력한 후 Authorize 버튼을 클릭합니다.
4. Try it out 버튼을 클릭하고 요청 변수를 입력한 후 Execute 버튼을 클릭합니다.  
   4-1. Authorization Header를 필요로 하는 API에는 `Bearer {발급받은 토큰}`을 한번 더 입력합니다.
