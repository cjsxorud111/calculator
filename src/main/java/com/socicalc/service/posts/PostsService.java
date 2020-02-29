package com.socicalc.service.posts;

import com.socicalc.domain.posts.JapWordsPostsRepository;
import com.socicalc.domain.posts.KorWordsPostsRepository;
import com.socicalc.domain.posts.Posts;
import com.socicalc.domain.posts.PostsRepository;
import com.socicalc.domain.words.WordCrawler;
import com.socicalc.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;
    private final KorWordsPostsRepository korWordsPostsRepository;
    private final JapWordsPostsRepository japWordsPostsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    //TODO 책 갖고와서 JPA로 db insert하고나서 select만들어 main페이지에 뿌려주기
    @Transactional
    public void saveWikiWord() {
        WordCrawler wordCrawler = new WordCrawler();

        List<PostsWordRequestDto> wikiKorWordList = wordCrawler.getWikiWord();
        List<PostsWordRequestDto> wikiJapWordList = wordCrawler.getWikiJapWord();

        for (PostsWordRequestDto korWord : wikiKorWordList) {
            korWordsPostsRepository.save(korWord.toKorEntity());
        }

        for (PostsWordRequestDto japWord : wikiJapWordList) {
            japWordsPostsRepository.save(japWord.toJapEntity());
        }
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    //TODO db셀렉트 테스트코드 공부후 테스트코드 작성
    @Transactional(readOnly = true)
    public List<WordsResponseDto> findWords() {
        List<WordsResponseDto> korWordList = korWordsPostsRepository.findKorWords().stream()
                .map(WordsResponseDto::new)
                .collect(Collectors.toList());

        List<WordsResponseDto> japWordList = japWordsPostsRepository.findJapWords().stream()
                .map(WordsResponseDto::new)
                .collect(Collectors.toList());

        List<WordsResponseDto> words = new ArrayList<>();
        words.addAll(korWordList);
        words.addAll(japWordList);

        return words;
    }

    @Transactional
    public void delete (Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new
                IllegalArgumentException("해당 게시글이 없습니다.=" + id));
        postsRepository.delete(posts);
    }
}
