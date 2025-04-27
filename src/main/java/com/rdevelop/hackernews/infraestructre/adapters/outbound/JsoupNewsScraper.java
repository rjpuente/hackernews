package com.rdevelop.hackernews.infraestructre.adapters.outbound;

import com.rdevelop.hackernews.core.domain.NewsEntry;
import com.rdevelop.hackernews.core.ports.outbound.FetchNewsPort;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JsoupNewsScraper implements FetchNewsPort {

    private final String hnUrl;

    public JsoupNewsScraper(@Value("${hackernews.url}") String hnUrl) {
        this.hnUrl = hnUrl;
    }


    @Override
    public List<NewsEntry> getTopStories(int limit) {
        try {
            Document doc = fetchDocument();
            return selectRows(doc).stream()
                    .limit(limit)
                    .map(this::parseEntry)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch Hacker News", e);
        }
    }

    private Document fetchDocument() throws IOException {
        return Jsoup.connect(hnUrl)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .referrer("http://www.google.com")
                .timeout(10_000)
                .get();
    }

    private Elements selectRows(Document doc) {
        return doc.select("tr.athing");
    }

    private Optional<NewsEntry> parseEntry(Element row) {
        String title = parseTitle(row);
        if (title == null) return Optional.empty();

        int rank = parseRank(row);
        Element subtext = row.nextElementSibling() != null
                ? row.nextElementSibling().selectFirst("td.subtext")
                : null;
        int points = parsePoints(subtext);
        int commentCount = parseComments(subtext);

        return Optional.of(new NewsEntry(rank, title, points, commentCount));
    }

    private int parseRank(Element row) {
        Element e = row.selectFirst("span.rank");
        if (e == null) return -1;
        return Integer.parseInt(e.text().replace(".", "").trim());
    }

    private String parseTitle(Element row) {
        Element e = row.selectFirst("span.titleline > a");
        return (e != null) ? e.text() : null;
    }

    private int parsePoints(Element subtext) {
        if (subtext == null) return 0;
        Element scoreEl = subtext.selectFirst("span.score");
        if (scoreEl == null) return 0;
        return Integer.parseInt(scoreEl.text()
                .replace(" points", "")
                .trim());
    }

    private int parseComments(Element subtext) {
        if (subtext == null) return 0;
        Elements links = subtext.select("a");
        if (links.isEmpty()) return 0;
        String txt = links.get(links.size() - 1).text();
        if (!txt.endsWith("comments")) return 0;
        return Integer.parseInt(txt.replace(" comments", "").trim());
    }

}
