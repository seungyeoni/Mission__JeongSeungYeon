## Title: [NWeek] 정승연

### 미션 요구사항 분석 & 체크리스트

---
본인이 호감 표시한 상대방 목록의 옆에 있는 삭제버튼을 누르면 그 상대방이 호감 목록에서 삭제되어야 한다.

- [x] 삭제 버튼을 누르면 해당 항목이 호감 목록에서 삭제.
  - [ ] 삭제 처리 전 해당 항목에 대한 소유권이 본인(로그인한 사람)에게 있는지 체크.
  - [x] 삭제되었을 때 안내 메세지 출력.
  - [x] 호감목록 페이지로 돌아오기
    - [x] rq.redirectWithMsg 함수 사용


### 1주차 미션 요약

---

**[접근 방법]**

1. LikeablePersonController에 delete메서드 추가.
2. @GetMapping("/delete/{id}")을 통해 삭제 데이터의 id를 가져옴.
3. likeablePeople의 전체 데이터를 가져오기 위해 likeablePersonService의 findAll()메서드를 호출함.
   1. likeablePersonService의 findAll()메서드에서 likeablePersonRepository의 findAll()메서드를 호출함.
   2. likeablePersonRepository의 findAll()메서드는 결과값을 likeablePersonService에게 전달함.
   3. likeablePersonService는 전달받은 결과값을 LikeablePersonController에 전달함.
4. likeablePeople.get(id-1)객체를 likeablePersonService의 delete()메서드의 인자로 전달함.
5. likeablePersonService의 delete()메서드에서 likeablePersonRepository의 delete()메서드를 호출하고 전달받은 likeablePerson 객체를 전달함.
6. likeablePersonRepository의 delete()메서드가 DB에서 전달받은 likeablePerson 객체를 삭제함.

- 위의 접근 방법으로 구현했지만 6(목록에서 삭제)가 실현되지 않았음. 딱히 오류 메시지도 뜨지 않아서 어떤 문제인지 모르겠음.
  -> 리팩토링 후 해결, @Transactional을 넣어줌.
- 위 문제를 해결하기 위해 1) 구글링(jpa 리포지토리 delete 안됨)해봄.
   - 구글링에 나온 해결방법) delete() 대신 deleteById() 사용. -> 해결 안됨. LikeablePersonRepository extends JpaRepository<LikeablePerson, Integer>오류를 LikeablePersonRepository extends JpaRepository<LikeablePerson, Long>으로 바꿈.(id가 Long임.)
-  위 문제를 해결하기 위해 2) 생각 해봄.
   -  LikeablePerson 엔티티의 @ManytoOne 속성들 때문에 삭제가 안되는 건가? -> 정확한 해결책 찾지 못함.

-> 결론: 6(목록에서 삭제 == DB에서 삭제) 구현하지 못했음.
  -> 리팩토링 결과: 구현 완료.
-> 목표: 필수 목표 완료.(실패...)
  -> 리팩토링 결과: 필수 목표 완료함.
-> 참고 자료: 점프투스프링부트 3-10.수정과 삭제, 구글링

**[특이사항]**

1. 월요일 수업과 화요일 수업 일부를 빠지게 되어 Mission에 대한 감을 잡기가 힘들었음. + 시간이 부족함.
2. 필수 미션을 완벽히 구현하지 못해서 아쉬움. 오류가 안떠서 왜 안되는지 원인을 모르겠음.
3. 정신없이 구현하느라 (+ 될 것 같아서 시간잡아먹음) 질문을 하지 못한 게 아쉬움.
4. 커밋을 중간중간에 했어야 했는데 생각을 못함.

@Transactional에 대한 공부 필요...!!