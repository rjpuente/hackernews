package com.rdevelop.hackernews.application.services;

import com.rdevelop.hackernews.core.domain.NewsEntry;
import com.rdevelop.hackernews.core.ports.inbound.FilterNewsPort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.rdevelop.hackernews.application.util.TextUtils.countWords;

@Service
public class FilterNewsService implements FilterNewsPort {

    @Override
    public List<NewsEntry> filterByTitleWordCountGreaterThan(List<NewsEntry> entries, int wordCount) {
        return entries.stream()
                .filter(e -> countWords(e.getTitle()) > wordCount)
                .sorted(Comparator.comparingInt(NewsEntry::getCommentCount).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<NewsEntry> filterByTitleWordCountLessOrEqual(List<NewsEntry> entries, int wordCount) {
        return entries.stream()
                .filter(e -> countWords(e.getTitle()) <= wordCount)
                .sorted(Comparator.comparingInt(NewsEntry::getPoints).reversed())
                .collect(Collectors.toList());
    }
}
