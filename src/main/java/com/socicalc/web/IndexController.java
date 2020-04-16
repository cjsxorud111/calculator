package com.socicalc.web;

import com.socicalc.domain.words.KoreanWordDivide;
import com.socicalc.service.posts.PostsService;
import com.socicalc.web.dto.DefinedWordsResponseDto;
import com.socicalc.web.dto.KorWordsResponseDto;
import com.socicalc.web.dto.PagesDto;
import com.socicalc.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PostsService postsService;

    @GetMapping("/test")
    public String index() { return "index"; }

    @GetMapping("/define")
    public String define() {
        return "define";
    }

    @GetMapping("/word")
    public String word(Model model, HttpServletRequest request) {
        String definedWord = request.getParameter("mean");
        logger.debug("검색할 단어: {}", definedWord);
        String divided = KoreanWordDivide.toKoJaso(definedWord);
        model.addAttribute("word", postsService.getDefinedWord(divided));
        return "word";
    }

    @PostMapping("/modify")
    public String modify(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        int wordId = Integer.parseInt(request.getParameter("modify_hidden_id"));
        String modifyPassword = request.getParameter("modify_hidden_password");

        boolean passwordCheckResult = postsService.passwordCheck(wordId, modifyPassword);
        DefinedWordsResponseDto definedWordsResponseDto = postsService.getContentById(wordId);
        if (passwordCheckResult) {
            model.addAttribute("content",definedWordsResponseDto.getContent());
            model.addAttribute("wordId", wordId);
            model.addAttribute("word", definedWordsResponseDto.getTitle());
            model.addAttribute("modify_password", modifyPassword);
            return "modify";
        } else {
            String divided = KoreanWordDivide.toKoJaso(definedWordsResponseDto.getTitle());
            model.addAttribute("word", postsService.getDefinedWord(divided));
            String encodeResult = URLEncoder.encode(definedWordsResponseDto.getTitle(), "UTF-8");

            return "redirect:/word?mean="+encodeResult;
        }
    }

    @GetMapping("/")
    public String main(Model model, HttpServletRequest request) {

        //TODO 페이징 로직 도메인으로 옮기기
        String thisPage = request.getParameter("page");
        int thisPageInt = 0;
        if (thisPage == null) {
            thisPageInt = 1;
        } else {
            thisPageInt = Integer.parseInt(thisPage);
        }

        if (!postsService.test()){

        }
        List<List<KorWordsResponseDto>> words = postsService.findWords(thisPageInt);
        PagesDto num = postsService.getPageNum(thisPageInt);

        model.addAttribute("firstColumn", words.get(0));
        model.addAttribute("secondColumn", words.get(1));
        model.addAttribute("thirdColumn", words.get(2));

        model.addAttribute("previousPages", num.getNum().get(0).getNumm() - 1);
        model.addAttribute("afterPages", num.getNum().get(num.getNum().size() - 1).getNumm() + 1);

        String numToString = Integer.toString(num.getAllNum());

        String lastPageNum = numToString.substring(0, numToString.length() - 1);
        lastPageNum += 0;
        int lastIntPageNum = Integer.parseInt(lastPageNum);
        model.addAttribute("pageNum", num);

        if (thisPageInt <= 10) {
            model.addAttribute("firstArrowActive", "disabled");
            model.addAttribute("lastArrowActive", "");
        } else if (thisPageInt > lastIntPageNum) {
            model.addAttribute("firstArrowActive", "");
            model.addAttribute("lastArrowActive", "disabled");
        } else {
            model.addAttribute("firstArrowActive", "");
            model.addAttribute("lastArrowActive", "");
        }

        return "main";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }

}
