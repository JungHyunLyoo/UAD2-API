UAD's second project
====
![UAD](https://user-images.githubusercontent.com/47667821/64473706-fb272880-d1a4-11e9-88a8-9fbfbb133038.png)

설명
----
- UAD 프로젝트의 두 번째 버전.
 
- Php Laravel로 개발된 첫 번째 버전을 Java SpringBoot로 개편. 
<br>Front는 React로 동일
    
- UAD 첫 번째 버전    
    - 저장소
        https://pjhdev.com:8081/col/app
    - 멤버 구성 
        - pm : 11유정현
        - infra : 11박준호 
        - back(Php Laravel) : 13김시훈
        - front(React) : 13진여준

멤버 구성
----
- pm : 11유정현
- infra : 11김종엽, 11박준호 
- back(Java SpringBoot) : 11김종엽, 11조형근, 11박준호
- front(React) : 13진여준

개발 환경
----
#### ide
- intellij idea <ultimate 2019.1>
- heidiSQL <9.5.0.5196 (64bit)>
- vscode
#### spring
- boot 2.1.5.RELEASE
- spring 5.1
- junit 5
- tomcat 9
- java 8
- restdocs 2.0.3
- jpa 2.1.5
#### DB (only production db)
- host : pjhdev.com
- mysql 10.1.41-MariaDB
- jpa 2.1.5
#### 협업도구
- notion
    - 사용 가이드 : https://www.notion.so/Notion-1ad7ccbc41a44298814a4820d4acb14e
- git
    - source tree 3.0.8
    - git bash
    - 커밋 메세지
        - [무엇을]-어떻게
    - 커밋 전략
        1. 각 멤버들이 local develop에 작업 커밋
        2. 해당 멤버가 origin develop으로 pull request 날림
        3. 모든 멤버들이 해당 request에 코멘트를 남김
        4. 모든 멤버들의 확인이 끝나면 pm이 develop에 풀 땡김
        5. 참고로 master는 개발 완료 후 운영을 시작하면 운영할 예정
                    
#### readme 마크다운 사용법
- https://gist.github.com/ihoneymon/652be052a0727ad59601
