package com.rdevelop.hackernews.infrastructre.adapters.inbound;

import com.rdevelop.hackernews.application.mapper.NewsEntryMapper;
import com.rdevelop.hackernews.application.services.FilterNewsService;
import com.rdevelop.hackernews.core.domain.NewsEntry;
import com.rdevelop.hackernews.core.ports.outbound.FetchNewsPort;
import com.rdevelop.hackernews.infraestructre.adapters.inbound.NewsController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers =  NewsController.class)
@Import({NewsEntryMapper.class, FilterNewsService.class})
public class NewsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FetchNewsPort fetchNewsPort;

    @BeforeEach
    public void setup() {
        List<NewsEntry> sample = Arrays.asList(
                new NewsEntry(1, "One two three four five six", 10, 1),
                new NewsEntry(2, "Short title", 50, 5),
                new NewsEntry(3, "Another example here", 30, 10)
        );
        when(fetchNewsPort.getTopStories(3)).thenReturn(sample);
    }


    @Test
    void topEndpointReturnsAll() throws Exception {
        mockMvc.perform(get("/api/news/top").param("limit", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].rank").value(1))
                .andExpect(jsonPath("$[1].rank").value(2))
                .andExpect(jsonPath("$[2].rank").value(3));
    }

    @Test
    void longTitlesEndpointFiltersByWordsAndOrdersByComments() throws Exception {
        mockMvc.perform(get("/api/news/long").param("limit", "3"))
                .andExpect(status().isOk())
                // only rank=1 has >5 words
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].rank").value(1));
    }

    @Test
    void shortTitlesEndpointFiltersByWordsAndOrdersByPoints() throws Exception {
        mockMvc.perform(get("/api/news/short").param("limit", "3"))
                .andExpect(status().isOk())
                // ranks 2 & 3 have ≤5 words, ordered by points: 2 then 3
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].rank").value(2))
                .andExpect(jsonPath("$[1].rank").value(3));
    }
}
