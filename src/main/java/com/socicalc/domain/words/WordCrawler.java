package com.socicalc.domain.words;

import com.socicalc.web.dto.PostsWordResponseDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WordCrawler {

    public List<PostsWordResponseDto> getWikiWord() {
        String URL = "https://ko.wikipedia.org/wiki/%EB%8C%80%ED%95%9C%EB%AF%BC%EA%B5%AD%EC%9D%98_%EC%9D%B8%ED%84%B0%EB%84%B7_%EC%8B%A0%EC%A1%B0%EC%96%B4_%EB%AA%A9%EB%A1%9D";
        Document doc = null;
        try {
            doc = Jsoup.connect(URL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements content = doc.select(".mw-parser-output>ul>li");

        int a = 0;

        List<PostsWordResponseDto> words = new ArrayList<>();
        for (Element con : content) {
            if (con.text().contains(":")) {
                PostsWordResponseDto word = new PostsWordResponseDto();
                String[] concon = con.text().split(":");
                word.setTitlee(concon[0]);
                word.setConc(concon[1]);

                words.add(word);
                a++;
            }
        }

        //위키피디아 신조어 정의 마지막li태그 삭제
        words.remove(words.size()-1);

        return words;
    }

}
