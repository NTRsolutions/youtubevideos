package com.youtubevideos.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shruitygovil on 26/07/18.
 */

public class YoutubeResponse {
    private String kind, etag, nextPageToken;
    private PageInfo pageInfo;
    private List<Item> items = new ArrayList<>();

    public String getKind() {
        return kind;
    }

    public String getEtag() {
        return etag;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public List<Item> getItems() {
        return items;
    }

    public static class PageInfo {
        private int totalResults, resultsPerPage;

        public int getTotalResults() {
            return totalResults;
        }

        public int getResultsPerPage() {
            return resultsPerPage;
        }
    }
}
