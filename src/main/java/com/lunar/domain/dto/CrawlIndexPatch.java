package com.lunar.domain.dto;

import com.lunar.domain.Person;

import java.util.ArrayList;

/**
 * Created by gadominas on 10/30/16.
 */
public class CrawlIndexPatch {
    private String url;
    private ArrayList<Person> persons = new ArrayList<>();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }
}
