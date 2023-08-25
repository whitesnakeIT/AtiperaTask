package com.kapusniak.tomasz.atiperatask.controller;

import com.kapusniak.tomasz.atiperatask.exception.ExceptionMessages;
import com.kapusniak.tomasz.atiperatask.model.ApiError;
import com.kapusniak.tomasz.atiperatask.model.RepositoryDetails;
import com.kapusniak.tomasz.atiperatask.service.ApiErrorService;
import com.kapusniak.tomasz.atiperatask.service.GitHubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GitHubController {

    private final GitHubService gitHubService;

    private final ApiErrorService apiErrorService;

    @GetMapping("/")
    public String showHomePage() {

        return "index";
    }

    @PostMapping(value = "/" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<RepositoryDetails>> getUserDetails(
            @RequestHeader(name = "Accept") String header,
            @RequestParam(name = "input") String username) {
        gitHubService.validateUsername(username);
//        apiErrorService.validateHeader(header);
        List<RepositoryDetails> repositoryDetails = gitHubService.showUserRepositoriesWithoutForks(username);
        return new ResponseEntity<>(repositoryDetails, HttpStatus.OK);
//        return new ResponseEntity<>(List.of(), HttpStatus.OK);

    }
}
