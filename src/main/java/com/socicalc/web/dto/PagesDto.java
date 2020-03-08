package com.socicalc.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagesDto {
    int allNum;
    List<PageNumDto> num;
}
