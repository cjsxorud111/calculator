package com.socicalc.web;

import com.socicalc.domain.words.KoreanWordDivide;
import com.socicalc.domain.words.PlatformEnum;
import com.socicalc.service.posts.PostsService;
import com.socicalc.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class PostsApiController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PostsService postsService;

    private boolean passwordCheck(int id, String password) {
        return postsService.passwordCheck(id, password);
    }

    @PostMapping(value = "/api/v1/search_auto_complete", produces = "application/json")
    public @ResponseBody List<DefinedWordsResponseDto> searchAutoComplete(Model model, HttpServletRequest request) {
        String word = request.getParameter("word");
        logger.debug("검색할 단어: {}", word);
        List<DefinedWordsResponseDto> searchedAutoCompleteWord = postsService.searchAutoComplete(word);
        model.addAttribute("searchedAutoCompleteWord", searchedAutoCompleteWord);
        return searchedAutoCompleteWord;
    }

    @PostMapping(value = "/api/v1/delete_word", produces = "application/json")
    public @ResponseBody HashMap<String, String> deleteWord(HttpServletRequest request) {
        int wordId = Integer.parseInt(request.getParameter("mean"));
        String deletePassword = request.getParameter("delete_password");
        System.out.println("단어삭제메서드");
        boolean passwordCheckResult = passwordCheck(wordId, deletePassword);
        if (passwordCheckResult) {
            System.out.println("해당 단어를 삭제합니다.");
            postsService.deleteWord(wordId);
        }
        System.out.println("비번 불일치 단어삭제 불가");
        HashMap<String, String> hashMap = new HashMap<>();
        return hashMap;
    }

    @PostMapping(value = "/api/v1/recommendation", produces = "application/json")
    public @ResponseBody HashMap<String, String> recommendation(HttpServletRequest request) {

        String contentid = request.getParameter("contentid");
        postsService.updateRecommendation(contentid);
        HashMap<String, String> hashMap = new HashMap<>();
        return hashMap;
    }

    @PostMapping(value = "/api/v1/define_word", produces = "application/json")
    public @ResponseBody HashMap<String, String> defineNewWord(HttpServletRequest request) {
        DefineNewWordsRequestDto defineNewWordsRequestDto = new DefineNewWordsRequestDto();
        String word = request.getParameter("word");
        KoreanWordDivide koreanWordDivide = new KoreanWordDivide();
        String dividedWord = koreanWordDivide.toKoJaso(word);
        String password = request.getParameter("password");
        String tarea = request.getParameter("tarea");
        defineNewWordsRequestDto.setTitle(word);
        defineNewWordsRequestDto.setPassword(password);
        defineNewWordsRequestDto.setTarea(tarea);
        defineNewWordsRequestDto.setPlatform(PlatformEnum.USER);
        defineNewWordsRequestDto.setDividedWord(dividedWord);

        System.out.println("어디잘됫냐" + tarea + "word" +password);
        postsService.defineNewDefineWord(defineNewWordsRequestDto);
        HashMap<String, String> hashMap = new HashMap<>();
        return hashMap;
    }

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto) {
        return postsService.save(requestDto);
    }

    @GetMapping("/api/v1/wikiwords")
    public void saveWikiWord() {
        postsService.saveWikiWord();
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto) {
        return postsService.update(id, requestDto);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById(@PathVariable Long id) {
        return postsService.findById(id);
    }

    @GetMapping("/api/v1/main-paging")
    public PostsResponseDto getMainPages(@PathVariable Long id) {
        return postsService.findById(id);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id) {
        postsService.delete(id);
        return id;
    }

}
