package com.socicalc.web.dto;

import com.socicalc.domain.posts.JapWordsPosts;
import com.socicalc.domain.posts.KorWordsPosts;
import lombok.Getter;

@Getter
public class WordsResponseDto {

    private Long id;
    private String title;
    private String content;

    public WordsResponseDto(KorWordsPosts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
    }

    public WordsResponseDto(JapWordsPosts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
    }
}
