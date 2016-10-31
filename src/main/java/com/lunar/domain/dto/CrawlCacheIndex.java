package com.lunar.domain.dto;

import com.lunar.domain.Person;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Created by gadominas on 10/30/16.
 */
public class CrawlCacheIndex {
    private URL url;
    private List<Person> persons;
    private String repoKey;

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public List<Person> getPersons() {
        Optional<List<Person>> optional = Optional.ofNullable(persons);

        if( !optional.isPresent() ){
            persons = new LinkedList<>();
        }

        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public String getRepoKey() {
        return repoKey;
    }

    public void setRepoKey(String repoKey) {
        this.repoKey = repoKey;
    }
}
