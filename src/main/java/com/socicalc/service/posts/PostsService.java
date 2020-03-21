package com.socicalc.service.posts;

import com.socicalc.domain.posts.KorWordsPostsRepository;
import com.socicalc.domain.posts.Posts;
import com.socicalc.domain.posts.PostsRepository;
import com.socicalc.domain.words.KoreanWordDivide;
import com.socicalc.domain.words.WordCrawler;
import com.socicalc.mybatisdao.Mapper;
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
    private final Mapper mapper;

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
    }

    @Transactional
    public PagesDto getPageNum(int thisPageInt) {
        PagesDto pagesDto = new PagesDto();
        int allNum = mapper.getPageNum() / 30;
        pagesDto.setAllNum(allNum);

        String temp = Integer.toString(thisPageInt);
        int numericValue = Character.getNumericValue(temp.charAt(temp.length() - 1));
        if (thisPageInt > 9 && numericValue > 0) {
            String splitTemp = temp.substring(0, temp.length() - 1);
            splitTemp += '1';
            temp = splitTemp;
        } else if (thisPageInt > 9) {
            temp = Integer.toString(thisPageInt - 10);
            String splitTemp = temp.substring(0, temp.length() - 1);
            splitTemp += '1';
            temp = splitTemp;
        } else {
            temp = "1";
        }

        List<PageNumDto> pNum = new ArrayList<>();

        for (int i = Integer.parseInt(temp); i < Integer.parseInt(temp) + 10; i++) {
            PageNumDto pageNumDto = new PageNumDto();
            pageNumDto.setNumm(i);
            pNum.add(pageNumDto);
            if (thisPageInt != i) {
                pageNumDto.setActive("");
            } else {
                pageNumDto.setActive("active");
            }
            if (i > allNum) {
                break;
            }
        }

        pagesDto.setNum(pNum);


        return pagesDto;
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
    public List<List<KorWordsResponseDto>> findWords(int thisPage) {
        List<KorWordsResponseDto> words = new ArrayList<>();
        List<List<KorWordsResponseDto>> dividedByColumnWords = new ArrayList<>();

        List<KorWordsResponseDto> korWordList = mapper.getCon(thisPage * 30 - 30, thisPage * 30);

        words.addAll(korWordList);

        int wordsColumnSize = words.size() / 3;

        dividedByColumnWords.add(words.subList(0, wordsColumnSize));
        dividedByColumnWords.add(words.subList(wordsColumnSize, wordsColumnSize * 2));
        dividedByColumnWords.add(words.subList(wordsColumnSize * 2, words.size()));

        return dividedByColumnWords;
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new
                IllegalArgumentException("해당 게시글이 없습니다.=" + id));
        postsRepository.delete(posts);
    }

    public List<DefinedWordsResponseDto> getDefinedWord(String definedWord) {
        return mapper.getDefinedWord(definedWord);
    }

    public void updateRecommendation(String contentid) {
        mapper.updateRecommendation(contentid);
    }

    public void defineNewDefineWord(DefineNewWordsRequestDto defineNewWordsRequestDto) {
        mapper.defineNewDefineWord(defineNewWordsRequestDto);
    }

    public void deleteWord(int wordId) {
        mapper.deleteWord(wordId);
    }

    public boolean passwordCheck(int id, String sendedPassword) {
        System.out.println("비번체크컨트롤러");
        String selectedPassword = mapper.getPassword(id);
        System.out.println(id + " " + selectedPassword + " " + sendedPassword);
        return selectedPassword.equals(sendedPassword);
    }

    public List<DefinedWordsResponseDto> searchAutoComplete(String word) {

        System.out.println(KoreanWordDivide.toKoJaso(word));
        return mapper.searchAutoComplete(KoreanWordDivide.toKoJaso(word));
    }

    public void updateDividedWord() {
        List<DefinedWordsResponseDto> dividedByColumnWords = mapper.getForDivideWord();
        KoreanWordDivide koreanWordDivide = new KoreanWordDivide();
        for (DefinedWordsResponseDto i : dividedByColumnWords) {
            String dividedWord = koreanWordDivide.toKoJaso(i.getTitle());
            mapper.updateDividedWord(dividedWord, i.getId());
        }
    }
}
