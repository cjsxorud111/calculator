package com.socicalc.domain.posts;

import com.socicalc.domain.BaseTimeEntity;
import com.socicalc.domain.words.PlatformEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class KorWordsPosts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlatformEnum platform;

    @Builder
    public KorWordsPosts(String title, String content, PlatformEnum platform) {
        this.title = title;
        this.content = content;
        this.platform = platform;
    }
}
