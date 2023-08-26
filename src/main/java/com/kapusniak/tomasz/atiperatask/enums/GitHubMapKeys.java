package com.kapusniak.tomasz.atiperatask.enums;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public enum GitHubMapKeys {

    BRANCHES_URL,
    NAME,
    FULL_NAME,
    PROTECTED,
    COMMIT,
    SHA,
    FORK,
    URL;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

}
