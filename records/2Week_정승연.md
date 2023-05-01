## Title: [2Week] 정승연

### 미션 요구사항 분석 & 체크리스트

---
**[필수미션]**
- [x] 중복 호감표시 불가능 하도록.
  - [x] 같은 사유일 경우 처리하지 않음.
    - [x] rq.historyBack 사용.
  - [x] 다른 사유일 경우 기존 호감표시 사유 수정.
    - [x] resultCode=S-2
    - [x] msg= 인스타 유저(해당 유저 인스타 ID)에 대한 호감사유를 ( )에서 ( )으로 변경합니다.
- [x] 한 인스타 회원 당 호감상대 등록은 최대 10명까지.
  - [x] rq.historyBack 사용.

### 2주차 미션 요약

---

**[접근 방법]**

**version1. 혼자 생각하고풀어본 방법.(for문 사용)**
- 중복 호감 표시 불가능.
  - LikeablePersonService의 add메서드 수정.
    1. InstaMember 엔티티의 fromLikeablePeople메서드를 통해 해당 인스타회원의 호감상대목록을 가져옴.
    2. for문을 이용하여 호감상대목록에 이미 존재하는지 체크.
    3. 존재하는 경우 attractiveTypeCode가 같은지 체크.
       - attractiveTypeCode가 같은 경우.
         1. LikeablePersonController로 RsData객체 리턴. (resultCode=F-4, msg="인스타유저(%s)는 이미 호감상대로 등록되어 있습니다.")
         2. LikeablePersonController에서 resultCode=F-4이므로 createRsData.isFail()실행되어 rq.historyBack() 리턴.
       - attractiveTypeCode가 다른 경우.
         1. LikeablePerson에 attractiveTypeCode를 수정할 수 있는 modifyAttractiveTypeCode메서드 생성.
         2. LikeablePersonService에서 modifyAttractiveTypeCode메서드를 이용하여 해당하는 likeablePerson객체의 attractiveTypeCode를 수정함.
         3. LikeablePersonController로 RsData객체 리턴. (resultCode=S-2, msg="인스타 유저(%s)에 대한 호감사유를 %s에서 %s(으)로 변경합니다.", data=likeablePerson)

- 호감 상대 등록 최대 10명 제한.
  - LikeablePersonService의 add메서드 수정.
    1. if문을 이용하여 해당 인스타회원이 좋아하는 사람들 목록의 크기가 10 이상인지 체크.
    2. 10 이상이라면 LikeablePersonController로 RsData객체 리턴. (resultCode=F-3, msg="호감 상대 등록은 최대 10명까지 가능합니다.")
  - LikeablePersonController
    1. resultCode=F-3이므로 createRsData.isFail()실행되어 rq.historyBack() 리턴.

**version2. 강사님 힌트듣고 풀어본 방법.(JPA 쿼리 메서드 사용)**
- 중복 호감 표시 불가능.
  - LikeablePersonService의 add메서드 수정, likeablePersonRepository 수정.
    1. 입력한 호감 상대와 같은 사람이 호감 상대 목록에 존재하는지 확인하기 위해 likeablePersonRepository의 findByFromInstaMemberAndToInstaMember메서드 호출.
    2. likeablePersonRepository에서 findByFromInstaMemberAndToInstaMember메서드 이용하여 같은 사람이 존재하지 않으면 null, 존재하면 해당하는 LikeablePerson 객체를 반환하여 findlikeablePerson에 입력.
    3. findlikeablePerson이 NUll인지 아닌지 체크.(Null이 아니면 이미 존재하는 것.)
    4. 이후부터는 version1의 과정과 동일.

- 호감 상대 등록 최대 10명 제한.
  - LikeablePersonService의 add메서드 수정, likeablePersonRepository 수정.
    1. 호감 상대 목록의 개수를 가져오기 위해 likeablePersonRepository의 countByFromInstaMember메서드 호출.
    2. likeablePersonRepository에서 countByFromInstaMember메서드 이용하여 호감 상대 목록의 개수 반환하여 countLikeablePeople에 입력.
    3. 이후부터는 version1의 과정과 동일.

-> 목표: 필수 목표 완료.

-> 결과: 필수 목표 구현 성공.

-> 참고자료: 구글링, 강사님의 힌트강의

**[특이사항]**

1. 테스트코드를 작성하는 법이 아직 어려워서 작성하지 않고 코드를 작성하다보니 효율성이 떨어짐.
2. 깔끔하게 코드를 작성하는 법이 어려움.(for문, if문만 쓰지말고 JPA문법을 이용하자.)
3. 다음부터는 커밋할 때 개발 유형 분류를 해줘야겠음.
4. 메서드나 변수 이름을 명확하게 알 수 있게 정하고 싶은데 잘 안됨...