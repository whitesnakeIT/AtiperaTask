package com.kapusniak.tomasz.atiperatask.controller;

import com.kapusniak.tomasz.atiperatask.model.RepositoryDetails;
import com.kapusniak.tomasz.atiperatask.service.GitHubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GitHubController {

    private final GitHubService gitHubService;

    @GetMapping("/")
    public String showHomePage() {

        return "index";
    }

    @PostMapping(value = "/",
            consumes = {
                    MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            },
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<RepositoryDetails>> getUserDetails(
            @RequestParam(name = "input") String username) {
        gitHubService.validateUsername(username);

        List<RepositoryDetails> repositoryDetails =
                gitHubService.showUserRepositoriesWithoutForks(username);

        return new ResponseEntity<>(repositoryDetails, HttpStatus.OK);

    }
}
