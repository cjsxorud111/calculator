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

    @Select("select * from kor_words_posts where dividedword like concat(#{dividedDefinedWord, jdbcType=VARCHAR}, '%')")
    List<DefinedWordsResponseDto> getDefinedWord(String dividedDefinedWord);

    @Select("select password from kor_words_posts where id = #{id}")
    String getPassword(int id);

    @Select("select * from kor_words_posts where dividedword like concat(#{word, jdbcType=VARCHAR}, '%')")
    List<DefinedWordsResponseDto> searchAutoComplete(String word);

    @Select("select * from kor_words_posts where id = #{wordId, jdbcType=VARCHAR}")
    DefinedWordsResponseDto getContentById(int wordId);

    @Insert("insert into kor_words_posts (content, platform, title, password, dividedWord) values (#{tarea}, #{platform}, #{title}, #{password}, #{dividedWord})")
    void defineNewDefineWord(DefineNewWordsRequestDto defineNewWordsRequestDto);

    @Update("update kor_words_posts set recommendation_count = recommendation_count + 1 where id = #{contentid}")
    void updateRecommendation(String contentid);

    @Select("select * from kor_words_posts")
    List<DefinedWordsResponseDto> getForDivideWord();

    @Update("update kor_words_posts set dividedword = #{dividedWord} where id = #{id}")
    void updateDividedWord(String dividedWord, int id);

    @Delete("delete from kor_words_posts where id = #{wordId}")
    void deleteWord(int wordId);

    @Update("update kor_words_posts set title = #{title}, content = #{tarea}, dividedword = #{dividedWord} where id = #{id}")
    void modifyWord(DefineNewWordsRequestDto defineNewWordsRequestDto);
}
