package com.rdevelop.hackernews.application.mapper;

import com.rdevelop.hackernews.application.dto.NewsEntryDto;
import com.rdevelop.hackernews.core.domain.NewsEntry;
import org.springframework.stereotype.Component;

@Component
public class NewsEntryMapper {
    public NewsEntryDto toDto(NewsEntry newsEntry) {
        return new NewsEntryDto(newsEntry.getRank(), newsEntry.getTitle(), newsEntry.getPoints(), newsEntry.getCommentCount());
    }
}
