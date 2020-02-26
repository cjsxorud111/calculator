package com.socicalc.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JapWordsPostsRepository extends JpaRepository<JapWordsPosts, Long> {
    @Query("SELECT JWP FROM JapWordsPosts JWP")
    List<JapWordsPosts> findJapWords();
}
