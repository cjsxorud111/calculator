package com.socicalc.web;

import com.socicalc.service.posts.PostsService;
import com.socicalc.web.dto.KorWordsResponseDto;
import com.socicalc.web.dto.PagesDto;
import com.socicalc.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model) {
        //model.addAttribute("posts", postsService.findAllDesc());
        return "index";
    }

    @GetMapping("/word")
    public String word(Model model, HttpServletRequest request) {
        String definedWord = request.getParameter("mean");
        model.addAttribute("word", postsService.getDefinedWord(definedWord));
        System.out.println("검색할 단어: " + definedWord);
        return "word";
    }

    @GetMapping("/main")
    public String main(Model model, HttpServletRequest request) {
        //TODO 회원가입기능 추가할 때 오류 수정 후 사용
        System.out.println("서버오류 확인로그");
        /*SessionUser user = (User) httpSession.getAttribute("user");

        if (user != null) {
            model.addAttribute("userName", user.getName());
        }*/

        /*String URL = "https://weather.naver.com/rgn/cityWetrMain.nhn";
        Document doc = null;
        try {
            doc = Jsoup.connect(URL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements elem = doc.select(".tbl_weather tbody>tr:nth-child(1)");
        String[] str = elem.text().split(" ");
        Elements elem2=doc.select(".tbl_weather tbody>tr:nth-child(1) img");

        model.addAttribute("elem", elem);*/
        // 단어크롤링
        //postsService.saveWikiWord();
        // 마이바티스로 select
        String thisPage = request.getParameter("page");
        int thisPageInt = 0;
        if (thisPage == null) {
            thisPageInt = 1;
        } else {
            thisPageInt = Integer.parseInt(thisPage);
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
