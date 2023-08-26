package com.kapusniak.tomasz.atiperatask.testdata;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@Getter
public class TestData {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String repoResponse;
    public static String repoResponseOnlyForks;
    public static String branchesResponse;

    public static List<Map<String, Object>> repoMap;
    public static List<Map<String, Object>> repoMapOnlyForks;
    public static List<Map<String, Object>> branchesMap;
    public static Map<String,String> commitMap;

    public static String username;
    public static String repoName;

    static {
        username = "whitesnakeit";
        repoName = "AtiperaTask";
        try {
            ClassLoader classLoader = TestData.class.getClassLoader();
            repoResponse = new String(Objects.requireNonNull(
                    classLoader.getResourceAsStream("test-data/repoResponse.json"))
                    .readAllBytes(), StandardCharsets.UTF_8);
            repoResponseOnlyForks = new String(Objects.requireNonNull(
                    classLoader.getResourceAsStream("test-data/repoResponseOnlyForks.json"))
                    .readAllBytes(), StandardCharsets.UTF_8);
            branchesResponse = new String(Objects.requireNonNull(
                    classLoader.getResourceAsStream("test-data/branchesResponse.json"))
                    .readAllBytes(), StandardCharsets.UTF_8);

            repoMap = objectMapper
                    .readValue(repoResponse, new TypeReference<>() {});
           repoMapOnlyForks = objectMapper
                    .readValue(repoResponseOnlyForks, new TypeReference<>() {});
            branchesMap = objectMapper
                    .readValue(branchesResponse, new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
