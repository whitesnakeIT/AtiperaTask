package com.kapusniak.tomasz.atiperatask.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kapusniak.tomasz.atiperatask.exception.*;
import com.kapusniak.tomasz.atiperatask.model.RepositoryDetails;
import com.kapusniak.tomasz.atiperatask.model.github.Branch;
import com.kapusniak.tomasz.atiperatask.testdata.TestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kapusniak.tomasz.atiperatask.enums.GitHubMapKeys.BRANCHES_URL;
import static com.kapusniak.tomasz.atiperatask.enums.GitHubMapKeys.NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@SpringBootTest
class GitHubServiceTest {

    @InjectMocks
    private GitHubService gitHubService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RestTemplate restTemplate;

    private List<Map<String, Object>> getRepoMaps() {
        List<Map<String, Object>> maps = new ArrayList<>();
        for (Map<String, Object> originalMap : TestData.repoMap) {
            Map<String, Object> clonedMap = new HashMap<>(originalMap);
            maps.add(clonedMap);
        }

        return maps;
    }

    private List<Map<String, Object>> getRepoMapsOnlyForks() {
        List<Map<String, Object>> maps = new ArrayList<>();
        for (Map<String, Object> originalMap : TestData.repoMapOnlyForks) {
            Map<String, Object> clonedMap = new HashMap<>(originalMap);
            maps.add(clonedMap);
        }

        return maps;
    }

    private String getRepoResponse() {
        return new String(TestData.repoResponse);
    }

    @Test
    @DisplayName("should not throw an exception when username is not empty.")
    void validateUsername() {
        // given
        String username = "validUsername";

        // when
        // then
        assertDoesNotThrow(() -> gitHubService.validateUsername(username));
    }

    @Test
    @DisplayName("should throw an exception when username contains only white chars.")
    void validateEmptyUsername() {
        // given
        String username = "    ";

        // when
        // then
        assertThrows(UsernameEmptyNameException.class,
                () -> gitHubService.validateUsername(username));
    }

    @Test
    @DisplayName("should throw an exception when username is null.")
    void validateNullUsername() {
        // given
        String username = null;

        // when
        // then
        assertThrows(UsernameNotFoundException.class,
                () -> gitHubService.validateNullUsername(username));
    }

    @Test
    @DisplayName("should throw an exception when response is null.")
    void validateNullResponse() {
        // given
        String response = null;

        // when
        // then
        assertThrows(ResponseNotFoundException.class,
                () -> gitHubService.convertJSONresponseToMapList(response));
    }

    @Test
    @DisplayName("should correctly map JSON response to List<Map<String,Object>>")
    void convertJSONresponseToMapList() throws JsonProcessingException {
        //given
        String repoResponse = getRepoResponse();

        // and
        given(objectMapper.readValue(anyString(),
                any(TypeReference.class)))
                .willReturn(getRepoMaps());

        // when
        List<Map<String, Object>> repositoryMapList = gitHubService
                .convertJSONresponseToMapList(repoResponse);

        // then
        assertThat(repositoryMapList).isNotEmpty();

        // verify
        verify(objectMapper, times(1))
                .readValue(anyString(), any(TypeReference.class));
    }

    @Test
    @DisplayName("should throw an exception when response is null")
    void convertJSONresponseToMapListNull() {
        // given
        String nullResponse = null;

        // when
        // then
        assertThrows(ResponseNotFoundException.class,
                () -> gitHubService.convertJSONresponseToMapList(nullResponse));
    }

    @Test
    @DisplayName("should find user repositories with forks")
    void findUserRepos() throws JsonProcessingException {
        // given
        String username = "exampleUsername";

        // and
        given(objectMapper.readValue(anyString(),
                any(TypeReference.class)))
                .willReturn(getRepoMaps());
        given(restTemplate.getForObject(anyString(),
                eq(String.class)))
                .willReturn(getRepoResponse());

        // when
        List<Map<String, Object>> userReposMapList = gitHubService.findUserRepos(username);

        // then
        assertThat(userReposMapList).isNotEmpty();

        // verify
        verify(objectMapper, times(1))
                .readValue(anyString(), any(TypeReference.class));
        verify(restTemplate, times(1))
                .getForObject(anyString(), eq(String.class));

    }

    @Test
    @DisplayName("should return empty map when user have only forked repos")
    void findUserReposWithoutForks() throws JsonProcessingException {
        // given
        String username = "exampleUsername";

        // and
        given(objectMapper.readValue(anyString(),
                any(TypeReference.class)))
                .willReturn(getRepoMapsOnlyForks());
        given(restTemplate.getForObject(anyString(),
                eq(String.class)))
                .willReturn(getRepoResponse());

        // when
        List<Map<String, Object>> userReposMapList = gitHubService.findUserReposWithoutForks(username);

        // then
        assertThat(userReposMapList).isEmpty();

        // verify
        verify(objectMapper, times(1))
                .readValue(anyString(), any(TypeReference.class));
        verify(restTemplate, times(1))
                .getForObject(anyString(), eq(String.class));

    }

    @Test
    @DisplayName("should throw an exception when map is null")
    void validMapNull() {
        // given
        Map<String, Object> nullMap = null;

        // when
        // then
        assertThrows(MapNotFoundException.class,
                () -> gitHubService.validateMap(nullMap));
    }

    @Test
    @DisplayName("should correctly extract repository name from repository map")
    void extractRepoName() {
        // given
        String correctName = "AtiperaTask";
        Map<String, Object> repoMap = getRepoMaps().get(0);

        // when
        String result = gitHubService.extractRepoName(repoMap);

        // then
        assertThat(result).isEqualTo(correctName);
    }

    @Test
    @DisplayName("should throw an exception when map don't have key 'name'")
    void extractRepoNameWithNoKey() {
        // given
        Map<String, Object> repoMap = getRepoMaps().get(0);
        repoMap.remove(NAME.toString());

        // when
        // then
        assertThrows(MapNoKeyException.class,
                () -> gitHubService.extractRepoName(repoMap));
    }

    @Test
    @DisplayName("should throw an exception when map don't have key 'branches_url'")
    void findRepoBranchesWithNoKey() {
        // given
        Map<String, Object> repoMap = getRepoMaps().get(0);
        repoMap.remove(BRANCHES_URL.toString());

        // when
        // then
        assertThrows(MapNoKeyException.class,
                () -> gitHubService.findRepoBranches(repoMap));

    }

    @Test
    @DisplayName("should find all branches in repository")
    void findRepoBranches() throws JsonProcessingException {
        // given
        Map<String, Object> repoMap = getRepoMaps().get(0);

        // and
        given(objectMapper.readValue(anyString(),
                any(TypeReference.class)))
                .willReturn(getRepoMapsOnlyForks());
        given(restTemplate.getForObject(anyString(),
                eq(String.class)))
                .willReturn(getRepoResponse());

        // when
        List<Branch> branchList = gitHubService.findRepoBranches(repoMap);

        // then
        assertThat(branchList).isNotEmpty();

        // verify
        verify(objectMapper, times(1))
                .readValue(anyString(), any(TypeReference.class));
        verify(restTemplate, times(1))
                .getForObject(anyString(), eq(String.class));
    }

    @Test
    @DisplayName("should return repository list with details, repo count is added because" +
            "function needs to call github api for branches for all repos")
    void showUserRepositoriesWithoutForks() throws JsonProcessingException {
        // given
        String username = "exampleUsername";
        int repoCount = getRepoMaps().size();

        // and
        given(objectMapper.readValue(anyString(),
                any(TypeReference.class)))
                .willReturn(getRepoMaps());
        given(restTemplate.getForObject(anyString(),
                eq(String.class)))
                .willReturn(getRepoResponse());

        // when
        List<RepositoryDetails> repositoryDetails =
                gitHubService.showUserRepositoriesWithoutForks(username);

        // then
        assertThat(repositoryDetails).isNotEmpty();

        // verify
        verify(objectMapper, times(1 + repoCount))
                .readValue(anyString(), any(TypeReference.class));
        verify(restTemplate, times(1 + repoCount))
                .getForObject(anyString(), eq(String.class));
    }
}