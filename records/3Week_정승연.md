## Title: [3Week] 정승연

### 미션 요구사항 분석 & 체크리스트

---
**[필수미션]**
- [x] 네이버클라우드플랫폼을 통한 배포.
- [x] 호감표시/호감사유변경 후, 개별 호감표시건에 대해서, 3시간 동안은 호감취소와 호감사유변경을 할 수 없도록 작업.

### 3주차 미션 요약

---

**[접근 방법]**

- `LikeablePersonService`의 `canCancel`, `canLike`, `canModifyLike` 메서드에 if문 이용하여 쿨타임 체크.
  - `canCancel`
    ```java
    if (!likeablePerson.isModifyUnlocked()) {
            return RsData.of("F-5", "%s님에 대해 3시간 동안은 호감취소를 할 수 없습니다.".formatted(likeablePerson.getToInstaMember().getUsername()));
    }
      ```
  - `canLike`
    ```java
    if (fromLikeablePerson != null && !fromLikeablePerson.isModifyUnlocked()) {
            return RsData.of("F-6", "%s님에 대해 3시간 동안은 호감사유변경을 할 수 없습니다.".formatted(username));
    }
      ```
  - `canModifyLike`
     ```java
    if (likeablePerson != null && !likeablePerson.isModifyUnlocked()) {
            return RsData.of("F-6", "%s님에 대해 3시간 동안은 호감사유변경을 할 수 없습니다.".formatted(likeablePerson.getToInstaMember().getUsername()));
    }
      ```
- `LikeablePerson` entity의 `getModifyUnlockDateRemainStrHuman()`에 수정가능한 시간(H시 M+1분) return하도록 함.


-> 목표: 필수 목표 완료.

-> 결과: 호감표시/호감사유변경 3시간 쿨타임 적용 완료. 배포 미완료.

-> 참고자료: 구글링

**[특이사항]**

배포 과정 현재 진행중임.