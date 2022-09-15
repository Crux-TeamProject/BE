

# :ferris_wheel: CRUX! :ferris_wheel:

클라이밍 유저들을 위한 모임, 정보 제공 커뮤니티 서비스입니다.

<br>

##  :rocket: 프로젝트 사용기술

- [Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle)
- [Java 11](https://docs.oracle.com/javase/11/docs/api/)
- [Gradle](https://docs.gradle.org/current/userguide/userguide.html)
- [AWS EC2](https://aws.amazon.com/ko/ec2/)
- [MySQL](https://dev.mysql.com/doc/refman/8.0/en/)

- intellij IDEA

<br>
<br>

##  :rocket: WIKI

- click! :arrow_forward: [WIKI Home]
- click! :arrow_forward: [Usecase]
- click! :arrow_forward: [API 상세스펙]
- click! :arrow_forward: [PR규칙]
- click! :arrow_forward: [이슈해결을 정리한 테크블로그]

<br>
<br>

##  :rocket: 프로젝트 주요 관심사



<br>
<br>

### :diamond_shape_with_a_dot_inside: Git-Flow 브랜치 전략

**Git-Flow 브랜치 전략**에 따라 기능별로 브랜치를 나누어 작업하고 있고
모든 브랜치에 대해 pull request를 통한 리뷰 완료 후 Merge를 하고 있습니다.


:white_check_mark: master : 제품으로 출시될 수 있는 브랜치를 의미합니다.     
:white_check_mark: develop : 다음 출시 버전을 개발하는 브랜치입니다. feature에서 리뷰완료한 브랜치를 Merge하고 있습니다.    
:white_check_mark: feature : 기능을 개발하는 브랜치    
:white_check_mark: release : 이번 출시 버전을 준비하는 브랜치    
:white_check_mark: hotfix : 출시 버전에서 발생한 버그를 수정하는 브랜치

<br>

#### 참고문헌
- 우아한 형제들 기술블로그 "우린 Git-flow를 사용하고 있어요"   
  <https://woowabros.github.io/experience/2017/10/30/baemin-mobile-git-branch-strategy.html>


<br>
<br>

### :diamond_shape_with_a_dot_inside: PR 규칙

- 신규개발 건은 `develop` 을 base로 `feature/#이슈번호` 의 브랜치명으로 생성 후 작업한 다음 PR을 날립니다.
- 아직 개발 진행 중이라면 `In Progress` 라벨을 달고, 코드리뷰가 필요한 경우 `Asking for Review` 라벨을 답니다. 리뷰 후 리팩토링이 필요하다면 추가로 `refactoring` 라벨을 달아 진행합니다.
- 모든 PR은 반드시 지정한 리뷰어에게 코드리뷰를 받아야만 합니다.
- 리뷰어 중 1명 이상의 `Approve` 를 받아야 `Merge pull request` 를 할 수 있습니다.
- `commit` 을 할 때마다 Github-Actions CI가 자동으로 실행되며 단위테스트, 통합테스트에 모두 통과되어야 `Merge pull request`가 가능합니다.

<br>
<br>


##  :rocket: DB ERD 구조
