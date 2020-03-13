package com.socicalc.mybatisdao;

import com.socicalc.web.dto.DefinedWordsResponseDto;
import com.socicalc.web.dto.KorWordsResponseDto;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface Mapper {
    @Select("select * from kor_words_posts where id >= #{firstContentId} and id <= #{lastContentId}")
    List<KorWordsResponseDto> getCon(int firstContentId, int lastContentId);

    @Select("select count(*) from kor_words_posts")
    int getPageNum();

    @Select("select * from kor_words_posts where title = #{definedWord}")
    List<DefinedWordsResponseDto> getDefinedWord(String definedWord);

    @Update("update kor_words_posts set recommendation_count = recommendation_count + 1 where id = #{contentid}")
    void updateRecommendation(String contentid);
}
