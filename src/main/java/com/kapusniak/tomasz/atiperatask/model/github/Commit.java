package com.kapusniak.tomasz.atiperatask.model.github;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Commit {

    @JsonProperty("last_sha")
    private String lastSha;

    @JsonIgnore
    private String url;

    @Override
    public String toString() {
        return "Commit{" +
                "sha='" + lastSha + '\'' +
                '}';
    }
}
