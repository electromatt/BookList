package com.electromatt.booklisting;

import org.json.JSONArray;

public class Book {

    private String mTitle;
    private String mUrl;
    private String mAuthors;


    public Book(String title, String url, String authors){
        mTitle=title;
        mUrl=url;
        mAuthors=authors;
    }
    public String getTitle(){ return mTitle; }
    public String getUrl(){ return mUrl; }
    public String getAuthors(){ return mAuthors; }
}
