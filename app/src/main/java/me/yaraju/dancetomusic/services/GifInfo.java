package me.yaraju.dancetomusic.services;

import java.util.List;

public class GifInfo {
    private String url;
    private List<String> tags;
    private String id;

    private GifInfo(Builder builder) {
        url = builder.url;
        tags = builder.tags;
    }

    public String getUrl() {
        return url;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static final class Builder {
        private String id;
        private String url;
        private List<String> tags;

        public Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }

        public GifInfo build() {
            return new GifInfo(this);
        }
    }
}
