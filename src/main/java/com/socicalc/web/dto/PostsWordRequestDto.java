package com.socicalc.web.dto;

import com.socicalc.domain.posts.KorWordsPosts;
import com.socicalc.domain.words.PlatformEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostsWordRequestDto {
    String title;
    String content;
    PlatformEnum platform;

    @Builder
    public PostsWordRequestDto(String title, String content, PlatformEnum platform) {
        this.title = title;
        this.content = content;
        this.platform = platform;
    }

    public KorWordsPosts toKorEntity() {
        return KorWordsPosts.builder()
                .title(title)
                .content(content)
                .platform(platform)
                .build();
    }

}
