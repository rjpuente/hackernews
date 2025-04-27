package com.rdevelop.hackernews.application.services;

import com.rdevelop.hackernews.core.domain.NewsEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Arrays;
import java.util.List;

public class FilterNewsServiceTest {

    private FilterNewsService filterNewsService;
    private List<NewsEntry> newsEntries;

    @BeforeEach
    public void setUp() {
        filterNewsService = new FilterNewsService();
        newsEntries = Arrays.asList(
                new NewsEntry(1, "Short title", 50, 10),
                new NewsEntry(2, "This is an example", 20, 5),
                new NewsEntry(3, "One two three four five six", 30, 15),
                new NewsEntry(4, "Symbols #$%^ only", 40, 8)
        );
    }

    @Test
    void greaterThanFiveWordsOrdersByCommentsDesc() {
        List<NewsEntry> result = filterNewsService.filterByTitleWordCountGreaterThan(newsEntries, 5);

        assertThat(result)
                .extracting(NewsEntry::getRank)
                .containsExactly(3);
    }

    @Test
    void lessOrEqualFiveWords_ordersByPointsDesc() {
        List<NewsEntry> result = filterNewsService.filterByTitleWordCountLessOrEqual(newsEntries, 5);

        assertThat(result)
                .extracting(NewsEntry::getRank)
                .containsExactly(1, 4, 2);
    }
}
