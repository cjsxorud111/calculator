package com.socicalc.domain.words;

import com.socicalc.web.dto.PostsWordRequestDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WordCrawler {

    public List<PostsWordRequestDto> getWikiWord() {
        String URL = "https://ko.wikipedia.org/wiki/%EB%8C%80%ED%95%9C%EB%AF%BC%EA%B5%AD%EC%9D%98_%EC%9D%B8%ED%84%B0%EB%84%B7_%EC%8B%A0%EC%A1%B0%EC%96%B4_%EB%AA%A9%EB%A1%9D";
        Document doc = null;
        try {
            doc = Jsoup.connect(URL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements contests = doc.select(".mw-parser-output>ul>li");

        int a = 0;

        List<PostsWordRequestDto> words = new ArrayList<>();
        for (Element content : contests) {
            if (content.text().contains(":")) {
                PostsWordRequestDto word = new PostsWordRequestDto();
                String[] splitedContent = content.text().split(":");
                word.setTitle(splitedContent[0]);

                StringBuilder combinedWord;
                combinedWord = new StringBuilder();
                for (int i = 1 ; i < splitedContent.length ; i++) {
                    combinedWord.append(splitedContent[i]);
                }
                word.setContent(combinedWord.toString());
                word.setPlatform(PlatformEnum.KORWIKI);
                words.add(word);
                a++;
            }
        }

        //위키피디아 신조어 정의 마지막li태그 삭제
        words.remove(words.size() - 1);

        return words;
    }

    // 위키 일본신조어 크롤링
    public List<PostsWordRequestDto> getWikiJapWord() {
        String URL = "https://ko.wikipedia.org/wiki/%EB%B6%84%EB%A5%98:%EC%9D%BC%EB%B3%B8%EC%9D%98_%EC%8B%A0%EC%A1%B0%EC%96%B4";
        Document doc = null;
        try {
            doc = Jsoup.connect(URL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements contents = doc.select(".mw-category-group>ul>li>a");

        List <PostsWordRequestDto> words = new ArrayList<>();
        for (Element content : contents) {
            StringBuilder wordUrl = new StringBuilder();

            wordUrl.append("https://ko.wikipedia.org/wiki/");
            wordUrl.append(content.text());

            Document wordDoc = null;
            try {
                wordDoc = Jsoup.connect(wordUrl.toString()).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements wordTitle = wordDoc.select("#firstHeading");
            Elements wordContents = wordDoc.select(".mw-parser-output>p");
            PostsWordRequestDto word = new PostsWordRequestDto();
            word.setTitle(wordTitle.text());
            word.setContent(wordContents.text());
            word.setPlatform(PlatformEnum.JAPWIKI);
            words.add(word);
        }
        return words;
    }
}
