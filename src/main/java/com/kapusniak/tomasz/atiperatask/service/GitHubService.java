package com.kapusniak.tomasz.atiperatask.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kapusniak.tomasz.atiperatask.exception.*;
import com.kapusniak.tomasz.atiperatask.model.RepositoryDetails;
import com.kapusniak.tomasz.atiperatask.model.github.Branch;
import com.kapusniak.tomasz.atiperatask.model.github.Commit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.kapusniak.tomasz.atiperatask.enums.GitHubMapKeys.*;
import static com.kapusniak.tomasz.atiperatask.enums.GitHubUrl.BRANCH_URL;
import static com.kapusniak.tomasz.atiperatask.enums.GitHubUrl.REPO_URL;
import static com.kapusniak.tomasz.atiperatask.exception.ExceptionMessages.*;

@Service
@RequiredArgsConstructor
public class GitHubService {

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    public void validateUsername(String username) {
        if (username.trim().isEmpty()) {
            throw new UsernameEmptyNameException(USERNAME_EMPTY.toString());
        }
    }

    private String findUserRepoUrl(String username) {
        validateNullUsername(username);

        return REPO_URL.getUrl(username);
    }

    public void validateNullUsername(String username) {
        if (username == null) {
            throw new UsernameNotFoundException(USERNAME_NULL.toString());
        }
    }

    public List<Map<String, Object>> findUserRepos(String username) {
        validateNullUsername(username);
        String repoUrl = findUserRepoUrl(username);
        String reposResponse = getJSONResponseFromUrl(repoUrl);

        return convertJSONresponseToMapList(reposResponse);
    }

    public List<Map<String, Object>> findUserReposWithoutForks(String username) {

        return findUserRepos(username).stream()
                .filter(map -> map.containsKey(FORK.toString())
                        && Boolean.FALSE.equals(map.get(FORK.toString())))
                .toList();
    }

    public List<Map<String, Object>> convertJSONresponseToMapList(String response) {
        if (response == null) {
            throw new ResponseNotFoundException(RESPONSE_NULL.toString());
        }
        List<Map<String, Object>> listOfMapsFromResponse;
        try {
            listOfMapsFromResponse = objectMapper.readValue(
                    response,
                    new TypeReference<List<Map<String, Object>>>() {
                    }
            );

        } catch (JsonProcessingException e) {
            throw new JsonParsingException(JSON_PARSE + e.getMessage());
        }
        return listOfMapsFromResponse;
    }

    public String getJSONResponseFromUrl(String url) {
        if (url == null) {
            throw new UrlNotFoundException(URL_NULL.toString());
        }

        return restTemplate.getForObject(url, String.class);
    }

    public String extractRepoName(Map<String, Object> repoMap) {
        validateMap(repoMap);
        for (Map.Entry<String, Object> entry : repoMap.entrySet()) {
            if (entry.getKey().equals(NAME.toString())) {
                return (String) entry.getValue();
            }
        }
        throw new MapNoKeyException(MAP_KEY + NAME.toString());
    }

    public List<Branch> findRepoBranches(Map<String, Object> repoMap) {
        validateMap(repoMap);
        List<Branch> branches = new ArrayList<>();

        String ownerName = extractOwnerName(repoMap);
        String repoName = extractRepoName(repoMap);

        String branchesUrl = findBranchesUrl(ownerName, repoName);

        for (Map.Entry<String, Object> entry : repoMap.entrySet()) {
            String k = entry.getKey();
            if (k.equals(BRANCHES_URL.toString())) {
                List<Map<String, Object>> branchMap = fetchAndConvertBranchData(branchesUrl);
                branches.addAll(convertBranchMapToBranchList(branchMap));
                return branches;
            }

        }
        throw new MapNoKeyException(MAP_KEY + BRANCHES_URL.toString());

    }

    private List<Branch> convertBranchMapToBranchList(List<Map<String, Object>> branchMap) {
        return branchMap
                .stream()
                .map(this::extractBranch)
                .toList();
    }

    private List<Map<String, Object>> fetchAndConvertBranchData(String branchesUrl) {
        String branchesResponse = getJSONResponseFromUrl(branchesUrl);
        return convertJSONresponseToMapList(branchesResponse);
    }

    private String findBranchesUrl(String username, String repositoryName) {
        validateNullUsername(username);
        if (repositoryName == null) {
            throw new RepositoryNotFoundException(REPOSITORY_NULL.toString());
        }
        return BRANCH_URL.getUrl(username, repositoryName);

    }

    private String extractOwnerName(Map<String, Object> repoMap) {
        validateMap(repoMap);
        StringBuilder ownerName = new StringBuilder();

        for (Map.Entry<String, Object> entry : repoMap.entrySet()) {
            if (entry.getKey().equals(FULL_NAME.toString())) {
                ownerName.append((String) entry.getValue());
                return ownerName.toString().split("/")[0];
            }
        }

        throw new MapNoKeyException(MAP_KEY.toString() + FULL_NAME);
    }

    private Branch extractBranch(Map<String, Object> branchMap) {
        validateMap(branchMap);
        Branch branch = new Branch();

        branchMap.forEach((k, v) -> {
            if (k.equals(NAME.toString())) {
                branch.setName((String) v);
            }
            if (k.equals(PROTECTED.toString())) {
                branch.setProtectedBranch((Boolean) v);
            }
            if (k.equals(COMMIT.toString())) {
                final Commit commit = extractCommit((Map<String, String>) v);
                branch.setCommit(commit);
            }

        });

        return branch;
    }

    public <T> void validateMap(Map<String, T> map) {
        if (map == null) {
            throw new MapNotFoundException(MAP_NULL.toString());
        }
    }

    private Commit extractCommit(Map<String, String> commitMap) {
        validateMap(commitMap);
        Commit commit = new Commit();

        commitMap.forEach((k, v) -> {
            if (k.equals(SHA.toString())) {
                commit.setLastSha(v);
            }
            if (k.equals(URL.toString())) {
                commit.setUrl(v);
            }
        });

        return commit;
    }

    public List<RepositoryDetails> showUserRepositoriesWithoutForks(String username) {
        validateNullUsername(username);

        List<RepositoryDetails> repositoryDetailsList = new ArrayList<>();

        List<Map<String, Object>> userReposWithoutForks = findUserReposWithoutForks(username);
        userReposWithoutForks.forEach(repo -> {
            RepositoryDetails repositoryDetails = buildRepositoryDetails(username, repo);
            repositoryDetailsList.add(repositoryDetails);

        });
        return repositoryDetailsList;
    }

    private RepositoryDetails buildRepositoryDetails(String username, Map<String, Object> repo) {
        RepositoryDetails repositoryDetails = new RepositoryDetails();
        repositoryDetails.setOwner(username);
        repositoryDetails.setRepositoryName(extractRepoName(repo));
        repositoryDetails.setBranches(findRepoBranches(repo));
        return repositoryDetails;
    }
}