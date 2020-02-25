package com.socicalc.domain.words;

import com.socicalc.web.dto.PostsWordRequestDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class WordCrawlerTest {

    @Test
    public void getWikiWord() {
        //given
        String successWord = " 11 채팅을 뜻하는 말이다. DM(Direct Messege)와 비슷하다. 반대말로 '단톡'이 있다.";
        WordCrawler wordCrawler = new WordCrawler();

        //when
        List<PostsWordRequestDto> list = wordCrawler.getWikiWord();

        //then
        assertEquals(successWord, list.get(8).getContent());

    }

    @Test
    public void getWikiJapWord() {
        //given
        String successWord = null;
        WordCrawler wordCrawler = new WordCrawler();

        //when
        List<PostsWordRequestDto> list = wordCrawler.getWikiJapWord();

        //then
        assertEquals(null, list);

    }
}
