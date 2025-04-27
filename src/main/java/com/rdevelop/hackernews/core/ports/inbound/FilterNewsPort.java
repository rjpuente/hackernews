package com.rdevelop.hackernews.core.ports.inbound;

import com.rdevelop.hackernews.core.domain.NewsEntry;

import java.util.List;

public interface FilterNewsPort {
    List<NewsEntry> filterByTitleWordCountGreaterThan(List<NewsEntry> entries, int wordCount);

    List<NewsEntry> filterByTitleWordCountLessOrEqual(List<NewsEntry> entries, int wordCount);
}
