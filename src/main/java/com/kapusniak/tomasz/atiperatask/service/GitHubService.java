package com.kapusniak.tomasz.atiperatask.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
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

import static com.kapusniak.tomasz.atiperatask.enums.Url.BRANCHES_URL;
import static com.kapusniak.tomasz.atiperatask.enums.Url.REPO_URL;
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
                .filter(map -> map.containsKey("fork") && Boolean.FALSE.equals(map.get("fork")))
                .toList();
    }

    public List<Map<String, Object>> convertJSONresponseToMapList(String response) {
        if (response == null) {
            throw new ResponseNotFoundException(RESPONSE_NULL.toString());
        }
        List<Map<String, Object>> listOfMaps;
        try {
            listOfMaps = objectMapper.readValue(
                    response,
                    new TypeReference<List<Map<String, Object>>>() {
                    }
            );

        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return listOfMaps;
    }

    private String getJSONResponseFromUrl(String url) {
        if (url == null) {
            throw new UrlNotFoundException(URL_NULL.toString());
        }

        return restTemplate.getForObject(url, String.class);
    }

    public String extractRepoName(Map<String, Object> repoMap) {
        if (repoMap == null) {
            throw new MapNotFoundException(MAP_NULL.toString());
        }
        for (Map.Entry<String, Object> entry : repoMap.entrySet()) {
            if (entry.getKey().equals("name")) {
                return (String) entry.getValue();
            }
        }
        throw new RepositoryNotFoundException(REPOSITORY_NOT_FOUND.toString());
    }

    public List<Branch> findRepoBranches(Map<String, Object> repoMap) {
        if (repoMap == null) {
            throw new MapNotFoundException(MAP_NULL.toString());
        }
        List<Branch> branches = new ArrayList<>();

        String ownerName = extractOwnerName(repoMap);
        String repoName = extractRepoName(repoMap);
        repoMap.forEach((k, v) -> {

            if (k.equals("branches_url")) {
                String branchesUrl = findBranchesUrl(ownerName, repoName);
                String branchesResponse = getJSONResponseFromUrl(branchesUrl);
                List<Map<String, Object>> branchMap = convertJSONresponseToMapList(branchesResponse);

                branches.addAll(
                        branchMap
                                .stream()
                                .map(this::extractBranch)
                                .toList());
            }
        });
        return branches;
    }

    private String findBranchesUrl(String username, String repositoryName) {
        validateNullUsername(username);
        if (repositoryName == null) {
            throw new RepositoryNotFoundException(REPOSITORY_NULL.toString());
        }
        return BRANCHES_URL.getUrl(username, repositoryName);

    }

    private String extractOwnerName(Map<String, Object> repoMap) {
        if (repoMap == null) {
            throw new MapNotFoundException(MAP_NULL.toString());
        }
        StringBuilder ownerName = new StringBuilder();

        for (Map.Entry<String, Object> entry : repoMap.entrySet()) {
            if (entry.getKey().equals("full_name")) {
                ownerName.append((String) entry.getValue());
                return ownerName.toString().split("/")[0];
            }
        }

        throw new RepositoryNotFoundException(REPOSITORY_NOT_FOUND.toString());
    }

    private Branch extractBranch(Map<String, Object> branchMap) {
        if (branchMap == null) {
            throw new MapNotFoundException(MAP_NULL.toString());
        }
        Branch branch = new Branch();

        branchMap.forEach((k, v) -> {
            if (k.equals("name")) {
                branch.setName((String) v);
            }
            if (k.equals("protected")) {
                branch.setProtectedBranch((Boolean) v);
            }
            if (k.equals("commit")) {
                final Commit commit = extractCommit((Map<String, String>) v);
                branch.setCommit(commit);
            }

        });

        return branch;
    }

    private Commit extractCommit(Map<String, String> commitMap) {
        if (commitMap == null) {
            throw new MapNotFoundException(MAP_NULL.toString());
        }
        Commit commit = new Commit();

        commitMap.forEach((k, v) -> {
            if (k.equals("sha")) {
                commit.setLastSha(v);
            }
            if (k.equals("url")) {
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
            RepositoryDetails repositoryDetails = new RepositoryDetails();
            repositoryDetails.setOwner(username);
            repositoryDetails.setRepositoryName(extractRepoName(repo));
            repositoryDetails.setBranches(findRepoBranches(repo));
            repositoryDetailsList.add(repositoryDetails);

        });
        return repositoryDetailsList;
    }
}