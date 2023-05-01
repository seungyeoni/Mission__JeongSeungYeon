package com.ll.gramgram.boundedContext.likeablePerson.repository;

import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeablePersonRepository extends JpaRepository<LikeablePerson, Long> {
    LikeablePerson findByFromInstaMemberAndToInstaMember(InstaMember fromInstaMember, InstaMember toInstaMember);

    Long countByFromInstaMember(InstaMember fromInstaMember);
}
