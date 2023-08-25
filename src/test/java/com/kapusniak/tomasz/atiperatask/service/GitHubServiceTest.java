package com.kapusniak.tomasz.atiperatask.service;

import com.kapusniak.tomasz.atiperatask.exception.UsernameEmptyNameException;
import com.kapusniak.tomasz.atiperatask.exception.UsernameNotFoundException;
import com.kapusniak.tomasz.atiperatask.testdata.TestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class GitHubServiceTest {

    @Autowired
    private GitHubService gitHubService;

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
        assertThrows(UsernameEmptyNameException.class,() -> gitHubService.validateUsername(username));
    }

    @Test
    @DisplayName("should throw an exception when username is null.")
    void validateNullUsername() {
        // given
        String username = null;

        // when
        // then
        assertThrows(UsernameNotFoundException.class,() -> gitHubService.validateNullUsername(username));
    }

    @Test
    void convertJSONresponseToMapList() {
        //given
        String repoResponse = TestData.repositoryResponse;
        String branchesResponse = TestData.branchesResponse;
        // when
        List<Map<String, Object>> repositoryMapList = gitHubService.convertJSONresponseToMapList(repoResponse);
        List<Map<String, Object>> branchesMapList = gitHubService.convertJSONresponseToMapList(branchesResponse);

        // then
        assertThat(repositoryMapList).isNotEmpty();
        assertThat(branchesMapList).isNotEmpty();
    }
    @Test
    void convertJSONresponseToM|apListBadFormat() {
        // given
        String badResponse;
    }

    @Test
    @DisplayName("should find user repositories with forks")
    void findUserRepos() {

    }
    @Test
    @DisplayName("should throw an exception when ")
    void findUserReposNull() {

    }
    @Test
    @DisplayName("should find user repositories without forks")
    void findUserReposWithoutForks() {
    }

    @Test
    void extractRepoName() {
    }

    @Test
    void findRepoBranches() {
    }

    @Test
    void showUserRepositoriesWithoutForks() {
    }


}