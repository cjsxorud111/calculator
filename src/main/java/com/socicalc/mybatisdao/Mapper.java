package com.socicalc.mybatisdao;

import com.socicalc.web.dto.DefineNewWordsRequestDto;
import com.socicalc.web.dto.DefinedWordsResponseDto;
import com.socicalc.web.dto.KorWordsResponseDto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface Mapper {
    @Select("select * from kor_words_posts order by recommendation_count desc limit 29 offset #{firstContentId}")
    List<KorWordsResponseDto> getCon(int firstContentId, int lastContentId);

    @Select("select count(*) from kor_words_posts")
    int getPageNum();

    @Select("select * from kor_words_posts where title = #{definedWord}")
    List<DefinedWordsResponseDto> getDefinedWord(String definedWord);

    @Select("select password from kor_words_posts where id = #{id}")
    String getPassword(int id);

    @Insert("insert into kor_words_posts (content, platform, title, password) values (#{tarea}, #{platform}, #{title}, #{password})")
    void defineNewDefineWord(DefineNewWordsRequestDto defineNewWordsRequestDto);

    @Update("update kor_words_posts set recommendation_count = recommendation_count + 1 where id = #{contentid}")
    void updateRecommendation(String contentid);

    @Delete("delete from kor_words_posts where id = #{wordId}")
    void deleteWord(int wordId);
}
