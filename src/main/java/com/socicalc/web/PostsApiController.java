package com.socicalc.web;

import com.socicalc.service.posts.PostsService;
import com.socicalc.web.dto.PostsResponseDto;
import com.socicalc.web.dto.PostsSaveRequestDto;
import com.socicalc.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RequiredArgsConstructor
@Controller
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping(value = "/api/v1/recommendation", produces = "application/json")
    public @ResponseBody HashMap<String, String> recommendation(HttpServletRequest request) {

        String contentid = request.getParameter("contentid");
        postsService.updateRecommendation(contentid);
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
