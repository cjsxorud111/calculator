package com.socicalc.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JapWordsPostsRepository extends JpaRepository<JapWordsPosts, Long> {
    @Query(value = "SELECT ID, TITLE, CONTENT FROM JAP_WORDS_POSTS ORDER BY RAND() LIMIT 5", nativeQuery = true)
    List<JapWordsPosts> findJapWords();
}
