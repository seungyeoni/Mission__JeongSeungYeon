package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeablePersonService {
    private final LikeablePersonRepository likeablePersonRepository;
    private final InstaMemberService instaMemberService;

    @Transactional
    public RsData<LikeablePerson> like(Member member, String username, int attractiveTypeCode) {
        if ( member.hasConnectedInstaMember() == false ) {
            return RsData.of("F-2", "먼저 본인의 인스타그램 아이디를 입력해야 합니다.");
        }

        if (member.getInstaMember().getUsername().equals(username)) {
            return RsData.of("F-1", "본인을 호감상대로 등록할 수 없습니다.");
        }

        InstaMember fromInstaMember = member.getInstaMember();
        InstaMember toInstaMember = instaMemberService.findByUsernameOrCreate(username).getData();

        /*
        // version1. 혼자 생각하고풀어본 방법.(for문 사용)

        // 해당 인스타회원이 좋아하는 사람들 목록
        List<LikeablePerson> likeablePeople = fromInstaMember.getFromLikeablePeople();

        if (likeablePeople.size() >= 10){
            return RsData.of("F-3", "호감 상대 등록은 최대 10명까지 가능합니다.");
        }

        for(LikeablePerson likeablePerson: likeablePeople){
            if (likeablePerson.getToInstaMember().getId().equals(toInstaMember.getId())){
                if(likeablePerson.getAttractiveTypeCode() == attractiveTypeCode){
                    return RsData.of("F-4", "인스타유저(%s)는 이미 호감상대로 등록되어 있습니다.".formatted(username));
                }
                String beforeAttractiveTypeDisplayName = likeablePerson.getAttractiveTypeDisplayName();

                likeablePerson.modifyAttractiveTypeCode(attractiveTypeCode);

                return RsData.of("S-2", "인스타 유저(%s)에 대한 호감사유를 %s에서 %s(으)로 변경합니다.".formatted(username, beforeAttractiveTypeDisplayName, likeablePerson.getAttractiveTypeDisplayName()), likeablePerson);
            }
        }
         */

        // version2. 강사님 힌트듣고 풀어본 방법.(JPA 쿼리 메서드 사용)
        Long countLikeablePeople = this.likeablePersonRepository.countByFromInstaMember(fromInstaMember);
        LikeablePerson findlikeablePerson = this.likeablePersonRepository.findByFromInstaMemberAndToInstaMember(fromInstaMember, toInstaMember);

        if(countLikeablePeople >= 10){
            return RsData.of("F-3", "호감 상대 등록은 최대 10명까지 가능합니다.");
        }

        if(findlikeablePerson!=null){
            if(findlikeablePerson.getAttractiveTypeCode()==attractiveTypeCode){
                return RsData.of("F-4", "인스타유저(%s)는 이미 호감상대로 등록되어 있습니다.".formatted(username));
            }
            String beforeAttractiveTypeDisplayName = findlikeablePerson.getAttractiveTypeDisplayName();

            findlikeablePerson.modifyAttractiveTypeCode(attractiveTypeCode);

            return RsData.of("S-2", "인스타 유저(%s)에 대한 호감사유를 %s에서 %s(으)로 변경합니다.".formatted(username, beforeAttractiveTypeDisplayName, findlikeablePerson.getAttractiveTypeDisplayName()), findlikeablePerson);
        }

        LikeablePerson likeablePerson = LikeablePerson
                .builder()
                .fromInstaMember(member.getInstaMember()) // 호감을 표시하는 사람의 인스타 멤버
                .fromInstaMemberUsername(member.getInstaMember().getUsername()) // 중요하지 않음
                .toInstaMember(toInstaMember) // 호감을 받는 사람의 인스타 멤버
                .toInstaMemberUsername(toInstaMember.getUsername()) // 중요하지 않음
                .attractiveTypeCode(attractiveTypeCode) // 1=외모, 2=능력, 3=성격
                .build();

        likeablePersonRepository.save(likeablePerson); // 저장

        // 너가 좋아하는 호감표시 생겼어.
        fromInstaMember.addFromLikeablePerson(likeablePerson);

        // 너를 좋아하는 호감표시 생겼어.
        toInstaMember.addToLikeablePerson(likeablePerson);

        return RsData.of("S-1", "입력하신 인스타유저(%s)를 호감상대로 등록되었습니다.".formatted(username), likeablePerson);
    }

    @Transactional
    public RsData delete(LikeablePerson likeablePerson) {

        this.likeablePersonRepository.delete(likeablePerson);

        String likeCanceledUsername = likeablePerson.getToInstaMember().getUsername();
        return RsData.of("S-1", "인스타유저(%s)는 호감상대에서 삭제되었습니다.".formatted(likeCanceledUsername));
    }

    public Optional<LikeablePerson> findById(Long id) {
        return likeablePersonRepository.findById(id);
    }
}
