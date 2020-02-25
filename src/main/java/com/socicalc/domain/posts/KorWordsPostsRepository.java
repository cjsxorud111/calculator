package com.socicalc.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KorWordsPostsRepository extends JpaRepository<KorWordsPosts, Long> {
    @Query(value = "SELECT ID, TITLE, CONTENT FROM KOR_WORDS_POSTS ORDER BY RAND() LIMIT 5", nativeQuery = true)
    List<KorWordsPosts> findKorWords();
}
