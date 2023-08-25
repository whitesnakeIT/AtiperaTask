package com.kapusniak.tomasz.atiperatask.model;

import com.kapusniak.tomasz.atiperatask.model.github.Branch;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RepositoryDetails {

    private String owner;

    private String repositoryName;

    private List<Branch> branches;
}
