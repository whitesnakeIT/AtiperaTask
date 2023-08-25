package com.kapusniak.tomasz.atiperatask.testdata;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

public class TestData {

    public static String repositoryResponse;
    public static String branchesResponse;

    public static Map<String,Object> repoMap;

    public static Map<String,Object> branchMap;
    public static Map<String,String> commitMap;

    public static String username;
    public static String repoName;

    static {
        username = "whitesnakeit";
        repoName = "AtiperaTask";
        try {
            ClassLoader classLoader = TestData.class.getClassLoader();
            repositoryResponse = new String(Objects.requireNonNull(classLoader.getResourceAsStream("test-data/repoResponse.json")).readAllBytes(), StandardCharsets.UTF_8);
            branchesResponse = new String(Objects.requireNonNull(classLoader.getResourceAsStream("test-data/branchesResponse.json")).readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
