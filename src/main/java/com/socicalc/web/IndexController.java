package com.socicalc.web;

import com.socicalc.service.posts.PostsService;
import com.socicalc.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("posts", postsService.findAllDesc());
        return "index";
    }

    @GetMapping("/main")
    public String main(Model model) {
        System.out.println("테스트얌ㅎㅎㅎ");
        //TODO 회원가입기능 추가할 때 오류 수정 후 사용
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
        //model.addAttribute("words",postsService.findWords());
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
