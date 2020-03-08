package com.socicalc.mybatisdao;

import com.socicalc.web.dto.KorWordsResponseDto;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface Mapper {
    @Select("select * from kor_words_posts where id >= #{firstContentId} and id <= #{lastContentId}")
    List<KorWordsResponseDto> getCon(int firstContentId, int lastContentId);

    @Select("select count(*) from kor_words_posts")
    int getPageNum();
}
