package com.rdevelop.hackernews.infraestructre.adapters.inbound;

import com.rdevelop.hackernews.application.dto.NewsEntryDto;
import com.rdevelop.hackernews.application.mapper.NewsEntryMapper;
import com.rdevelop.hackernews.core.ports.inbound.FilterNewsPort;
import com.rdevelop.hackernews.core.ports.outbound.FetchNewsPort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final FetchNewsPort fetchNewsPort;
    private final FilterNewsPort filterNewsPort;
    private final NewsEntryMapper newsEntryMapper;

    public NewsController(FetchNewsPort fetchNewsPort, FilterNewsPort filterNewsPort, NewsEntryMapper newsEntryMapper) {
        this.fetchNewsPort = fetchNewsPort;
        this.filterNewsPort = filterNewsPort;
        this.newsEntryMapper = newsEntryMapper;
    }

    @GetMapping("/top")
    public List<NewsEntryDto> top(@RequestParam(defaultValue = "30") int limit) {
        return fetchNewsPort.getTopStories(limit).stream()
                .map(newsEntryMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/long")
    public List<NewsEntryDto> longTitles(@RequestParam(defaultValue = "30") int limit) {
        return filterNewsPort
                .filterByTitleWordCountGreaterThan(fetchNewsPort.getTopStories(limit), 5)
                .stream()
                .map(newsEntryMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/short")
    public List<NewsEntryDto> shortTitles(@RequestParam(defaultValue = "30") int limit) {
        return filterNewsPort
                .filterByTitleWordCountLessOrEqual(fetchNewsPort.getTopStories(limit), 5)
                .stream()
                .map(newsEntryMapper::toDto)
                .collect(Collectors.toList());
    }
}
