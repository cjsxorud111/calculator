package com.socicalc.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefinedWordsResponseDto {
    private int id;
    private String title;
    private String content;
    private String recommendation_count;

}
