package com.socicalc.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KorWordsPostsRepository extends JpaRepository<KorWordsPosts, Long> {
    @Query("SELECT KWP FROM KorWordsPosts KWP")
    List<KorWordsPosts> findKorWords();
}
