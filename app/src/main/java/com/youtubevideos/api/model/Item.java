package com.youtubevideos.api.model;

/**
 * Created by shruitygovil on 26/07/18.
 */

public class Item {

    private String kind, etag;
    private Snippet snippet;
    private ID id;

    public String getKind() {
        return kind;
    }

    public String getEtag() {
        return etag;
    }

    public Snippet getSnippet() {
        return snippet;
    }
    public ID getId() {
        return id;
    }

}
