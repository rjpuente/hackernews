package com.rdevelop.hackernews.core.ports.outbound;

import com.rdevelop.hackernews.core.domain.NewsEntry;

import java.util.List;

public interface FetchNewsPort {
    List<NewsEntry> getTopStories(int limit);
}
