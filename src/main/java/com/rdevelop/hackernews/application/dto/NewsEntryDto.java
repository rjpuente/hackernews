package com.rdevelop.hackernews.application.dto;

import lombok.Value;

@Value
public class NewsEntryDto {
    int rank;
    String title;
    int points;
    int commentCount;
}
