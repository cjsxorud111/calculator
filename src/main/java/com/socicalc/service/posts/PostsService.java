package com.socicalc.service.posts;

import com.socicalc.domain.posts.KorWordsPostsRepository;
import com.socicalc.domain.posts.Posts;
import com.socicalc.domain.posts.PostsRepository;
import com.socicalc.domain.words.KoreanWordDivide;
import com.socicalc.domain.words.WordCrawler;
import com.socicalc.mybatisdao.Mapper;
import com.socicalc.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private static final String[] CURRENCY_LIST_ROUND_HALF_UP_ONE = {"USD", "EUR", "SGD", "AUD"};
    private static final Logger LOGGER = LoggerFactory.getLogger(PostsService.class);
    protected static final String[] test11 = {"test1", "test2", "test3"};
    private static final int test111 = 1;
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
        String selectedPassword = mapper.getPassword(id);
        return selectedPassword.equals(sendedPassword);
    }

    public List<DefinedWordsResponseDto> searchAutoComplete(String word) {

        System.out.println(KoreanWordDivide.toKoJaso(word));
        return mapper.searchAutoComplete(KoreanWordDivide.toKoJaso(word));
    }

    // 저장된 단어 잘라서 저장
    public void updateDividedWord() {
        List<DefinedWordsResponseDto> dividedByColumnWords = mapper.getForDivideWord();
        for (DefinedWordsResponseDto i : dividedByColumnWords) {
            String dividedWord = KoreanWordDivide.toKoJaso(i.getTitle());
            mapper.updateDividedWord(dividedWord, i.getId());
        }
    }

    public static double roundUpPrice(String currency, BigDecimal decimal) {

        for (String c : CURRENCY_LIST_ROUND_HALF_UP_ONE) {
            if (c.equalsIgnoreCase(currency)) {
                return decimal.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        }

        return decimal.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public DefinedWordsResponseDto getContentById(int wordId) {
        return mapper.getContentById(wordId);
    }

    public void modifyWord(DefineNewWordsRequestDto defineNewWordsRequestDto) {
        mapper.modifyWord(defineNewWordsRequestDto);
    }

    public boolean test() {
        String url = null;
        String test = "테스트임";

        try {
            int a = 1;
            test.getBytes("euc-kr");
            url = java.net.URLEncoder.encode(test, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println(" XXXXXXXXXXXXXXXXXXXX");
            LOGGER.error("지원하지 않는 인코딩 방식입니다.", e);
        }

        System.out.println("this.s.test!!!!라고"+url);
        return false;
    }
}
