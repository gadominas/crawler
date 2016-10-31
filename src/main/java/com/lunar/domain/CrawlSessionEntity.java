package com.lunar.domain;

import java.util.ArrayList;

/**
 * Created by gadominas on 10/31/16.
 */
public class CrawlSessionEntity {
    private String repoKey;

    private Long txDate;

    private ArrayList<Person> persons = new ArrayList<>();

    public String getRepoKey() {
        return repoKey;
    }

    public void setRepoKey(String repoKey) {
        this.repoKey = repoKey;
    }

    public Long getTxDate() {
        return txDate;
    }

    public void setTxDate(Long txDate) {
        this.txDate = txDate;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }
}
