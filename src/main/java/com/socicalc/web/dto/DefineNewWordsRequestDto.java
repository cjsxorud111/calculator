package com.socicalc.web.dto;

import com.socicalc.domain.words.PlatformEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefineNewWordsRequestDto {
    private String title;
    private String tarea;
    private String password;
    private PlatformEnum platform;
    private String dividedWord;
}
