

# 🧗‍♂️ CRUX! 🧗‍♂️

### 클라이밍 유저들을 위한 모임, 정보 제공 커뮤니티 서비스입니다.

<br>

## [CRUX로 클라이밍 시작하기](https://youmadeit.shop/)

<img src= "https://user-images.githubusercontent.com/67731994/194262200-818f0c6f-dd12-4e8d-8423-dbf0e6c4c40e.png" width="700" height="530"/>

<br>

-----
## :rocket: 백엔드 팀원소개
|팀원|깃허브|역할분담|
|-----|---|---|
|채태림🔰|[Github](https://github.com/ctr0812)|클라이밍짐 데이터 크롤링 및 스케줄러 기능, 크루모임 SSE 알림전송 기능, 클라이밍짐 리뷰기능 API, CI/CD|
|이민규|[Github](https://github.com/01192mg)|DB 테이블설계,  크루 채팅 기능,  크루 CRUD 및 좋아요 API|
|조현우|[Github](https://github.com/jhw927)|JWT 로그인, 회원가입, 마이페이지, Oauth2로그인, 이메일 인증|

<br>
<br>

-----
##  :rocket: 프로젝트 사용기술 및 선택배경
<br>

> Language, Framework, Build Tool, IDE
- [Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle)
스프링 부트 사용시 제공되는 내장 웹서버를 사용을 위해서 선택했습니다, 추후 사용하게될 Redis의 설정을 자동으로 되게끔 제공한다 하여 선택했습니다.
- [Java 8](https://docs.oracle.com/javase/8/docs/api/)
 LTS 버전 중 자바 8에 도입된 람다, 스트림 문법을 사용하기 위해서 선택했습니다.
- [Gradle](https://docs.gradle.org/current/userguide/userguide.html)
XML 파일로 설정해야 하는 Maven에 비해 가독성이 좋고, 빌드 속도가 빠르다고 해서 선택했습니다.
- [intellij IDEA](https://www.jetbrains.com/help/idea/getting-started.html)
대부분의 IT 서비스 회사에서 Intellij를 공식 IDE로 사용 중이기에 선택했습니다.
<br>

> Server

- [AWS EC2](https://aws.amazon.com/ko/ec2/)
이번 프로젝트는 수익성을 목표로한 프로젝트가 아니다보니 초기 투자비용이 발생가능한 부분을 배제하는 과정에서 프리티어를 제공하는 AWS의 EC2가 최적이라 판단되었고, RDS, S3등 AWS 제공하는 서비스를 사용하기에 함께 관리하기에도 효율적이라 판단하여 사용했습니다.
<br>

> DB
- [MySQL](https://dev.mysql.com/doc/refman/8.0/en/)
오픈 소스 라이센스라 무료로 사용할 수 있고, 빠른 처리 속도, 표준 SQL 형식을 사용, 안정적이며 기능 개발 및 오류 발생시 많은 기업들이 사용하여 자료가 풍부해서 선택했습니다.
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
Spring Data JPA를 사용하면 JPA 기반 저장소를 쉽게 구현할 수 있습니다.  JPA 기반 데이터 액세스 계층에 대한 향상된 지원을 다룹니다. 데이터 액세스 기술을 사용하는 Spring 기반 애플리케이션을 더 쉽게 구축할 수 있습니다
- [Querydsl](http://querydsl.com/static/querydsl/latest/reference/html/)
쿼리를 가시적으로 정확하게 작성할 수 있고 컴파일 단계에서 오류를 확인 할 수 있습니다. 동적쿼리 생성이 가능하며 JPA와 연계하여 사용할 수 있어서 사용했습니다.  
<br>

> CI/CD
- [GitHub Actions](https://docs.github.com/en/actions/learn-github-actions/understanding-github-actions)
클라우드가 있어서 별도 설치가 필요 없으며 사용이 간편하므로 작은 프로젝트에 적합 합니다.
- [AWS S3](https://docs.aws.amazon.com/ko_kr/AmazonS3/latest/userguide/Welcome.html)
배포 파일 저장을 위해 사용하였습니다.
<br>

> 채팅 / 알림
- [Stomp](https://docs.spring.io/spring-integration/reference/html/stomp.html)
웹 소켓 서브 프로토콜로서 메시지 포맷이 자유롭고, 여러 브로커 사용 가능, Controller 처럼 사용하여 관리가 용이하여 사용, 게임이 실시간으로 진행 되며 상대의 코드를 확인하고, 양방향 통신의 이점을 이용해 메시지에 대한 내용을 처리 하기 위해 사용하였습니다.
- [Redis](https://redis.io/docs/stack/get-started/tutorials/stack-spring/)
인메모리 데이터베이스여서 데이터가 유실될 가능성이 있지만 속도가 매우 빠르기 때문에 민감하지 않은 데이터를 저장하기에 적합하다고 생각되어 사용했습니다.
- [SSE](https://www.baeldung.com/spring-server-sent-events)
알림 기능 구현을 위해 단방향 통신이 가능하며 구현이 간편한 SSE를 선택하였습니다.
<br>

> 로그인 / 회원가입
- [Oauth2](https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api) 
이용하려는 서비스마다 회원가입을 일일이 다 할 필요없이 기존의 사용하던 타사의 정보를 이용하여 로그인을 진행 할 수 있습니다.
- [Spring Security](https://docs.spring.io/spring-security/reference/index.html)
많은 보안 기능을 제공과 설정의 관리가 상대적으로 용이하며 관련 자료가 많아 사용하였습니다.
- [JWT](https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/oauth2/jwt/Jwt.html) 
세션에 비해 서버에 부담이 덜한 보안 방식인 토큰 방식을 채택하였습니다.


<br>
<br>

-----
## :rocket: 아키텍쳐

<img src= "https://user-images.githubusercontent.com/67731994/194245095-b81171fa-2bab-4c7a-9b58-16f3fbd11dac.png" width="800" height="530"/>

<br>
<br>

-----

##  :rocket: DB ERD 구조

### :diamond_shape_with_a_dot_inside: 이미지 클릭하시면 확대해서 볼 수 있습니다

<img src="https://user-images.githubusercontent.com/67731994/194267822-5405d522-ae03-4548-b35a-033a8e3c42ac.png" width="800" height="450"/>


<br>
<br>

-----

##  :rocket: 트러블 슈팅

<br>

<details>
<summary> 💥 무한스크롤 페이지네이션 방법 변경 문제 💥 </summary>

#### 문제점
오프셋 기반 무한스크롤 문제점<br>
offset 값이 클때 이전의 모든 데이터를 읽어야 하기 때문에 성능이 저하됩니다.<br>
데이터 중복 문제를 해결할 수 없습니다.

<img src= "https://user-images.githubusercontent.com/67731994/194271231-876adc83-2817-493a-a603-027b7182fa62.png" width="700" height="180"/>


따라서 오프셋 기반 페이지네이션에서 커서 기반 페이지네이션으로 전환했습니다<br>
하지만 커서기반 페이지네이션에서는 커서 데이터가 unique한 값이 아니어서 누락되는 데이터가 발생했습니다. 

#### 해결방법
커서를 커스텀해서 유니크한 커서를 생성했습니다.
Querydsl로 복잡한 동적쿼리를 작성하여 누락되는 데이터 및 중복되는 데이터가 발생하지 않게 해결하였습니다.<br>

<img src= "https://user-images.githubusercontent.com/67731994/194267443-5487c025-0b94-45e5-8916-e186d247f8be.png" width="600" height="400"/>

</details>


<details>
<summary> 💥 클라이밍짐 카카오api 데이터크롤링 문제 💥 </summary>

#### 문제점
클라이밍짐 정보제공을 위해 전국 클라이밍짐 정보를 크롤링해서 DB에 저장하려했습니다.<br>
하지만 카카오 키워드 검색 API 데이터 최대 45개까지만 조회 가능한 문제가 발생했습니다.

#### 해결방법
조회된 데이터가 15개가 넘을 경우 전국좌표를 4등분 하여서 15개 이하의 데이터가 조회될 때 까지 재귀적으로 api를 호출 했습니다.<br>
이를 통해 전국의 클라이밍짐 정보를 빠짐없이 DB에 저장할 수 있었습니다.

<img src= "https://user-images.githubusercontent.com/67731994/194274103-bccf63d9-f28d-4ec9-ad2a-54a838506085.png" width="850" height="400"/>

</details>

<br>

-----

### :diamond_shape_with_a_dot_inside: Git-Flow 브랜치 전략

**Git-Flow 브랜치 전략**에 따라 기능별로 브랜치를 나누어 작업하고 있고
모든 브랜치에 대해 pull request를 통한 리뷰 완료 후 Merge를 하고 있습니다.


:white_check_mark: master : 제품으로 출시될 수 있는 브랜치를 의미합니다.     
:white_check_mark: develop : 다음 출시 버전을 개발하는 브랜치입니다. feature에서 리뷰완료한 브랜치를 Merge하고 있습니다.    
:white_check_mark: feature : 기능을 개발하는 브랜치    
:white_check_mark: release : 이번 출시 버전을 준비하는 브랜치    
:white_check_mark: hotfix : 출시 버전에서 발생한 버그를 수정하는 브랜치

#### 참고문헌
- 우아한 형제들 기술블로그 "우린 Git-flow를 사용하고 있어요"   
  <https://woowabros.github.io/experience/2017/10/30/baemin-mobile-git-branch-strategy.html>

<br>

-----

### :diamond_shape_with_a_dot_inside: PR 규칙

- 신규개발 건은 `develop` 을 base로 `feature/#이슈번호` 의 브랜치명으로 생성 후 작업한 다음 PR을 날립니다.
- 리뷰어 중 1명 이상의 `Approve` 를 받아야 `Merge pull request` 를 할 수 있습니다.
- `Main branch pull request`를 할 때마다 Github-Actions CI가 자동으로 실행되며 단위테스트, 통합테스트에 모두 통과되어야 `Merge pull request`가 가능합니다.


