package com.rdevelop.hackernews.core.domain;

import lombok.Value;

@Value
public class NewsEntry {
    int rank;
    String title;
    int points;
    int commentCount;
}
