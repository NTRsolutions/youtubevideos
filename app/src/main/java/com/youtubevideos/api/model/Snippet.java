package com.youtubevideos.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shruitygovil on 26/07/18.
 */

public class Snippet {
    private String publishedAt, channelId, title, description, channelTitle, categoryId, liveBroadcastContent;
    private Thumbnails thumbnails;
    private List<String> tags = new ArrayList<>();

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getLiveBroadcastContent() {
        return liveBroadcastContent;
    }

    public Thumbnails getThumbnails() {
        return thumbnails;
    }

    public List<String> getTags() {
        return tags;
    }


    public static class Thumbnails {
        @SerializedName("default")
        private Default _default;

        public Default getDefault() {
            return _default;
        }

        public static final class Default {
            private String url;
            private int width, height;

            public String getUrl() {
                return url;
            }

            public int getWidth() {
                return width;
            }

            public int getHeight() {
                return height;
            }
        }
    }
}
