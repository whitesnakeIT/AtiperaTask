package com.kapusniak.tomasz.atiperatask.model.github;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Branch {

    private String name;

    private Commit commit;

    @JsonIgnore
    private Boolean protectedBranch;

    @Override
    public String toString() {
        return "Branch{" +
                "name='" + name + '\'' +
                ", commit=" + commit +
                '}';
    }
}
