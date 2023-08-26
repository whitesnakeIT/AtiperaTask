## Description  📝

This is a recruitment task for the Junior Java Developer position at Atipera Company.

**Task Status**: 🟢  100% Completed

## Docker  🐳

- Docker files: The `Dockerfile` and `docker-compose.yml` files are located in the `src/docker` directory.

### Downloading the Docker Image  📦

- **Link**: [Download the Docker image from Docker Hub](https://hub.docker.com/r/whitesnakenl/atipera)
  
- **Command to Pull the Image**:
  ```bash
  docker pull whitesnakenl/atipera:1.0.0
  ```

## Testing  🧪

The application has been rigorously tested in multiple environments:

- **Browser**:  🌐 Fully functional when accessed via a web browser.
- **Postman**:  📬 API calls and responses have been verified using Postman.

### Test Coverage

`GithubService` and `GitHubController` have more than 90% test coverage.

> **Note**:  🛑 Additional tests were omitted due to project deadlines.

### HTML Form and API :memo:

Support for `application/x-www-form-urlencoded` has been added to the POST method in `GitHubController`. This allows for sending requests in both `application/json` format and using traditional HTML forms.


# Usage  🛠️

When the application is running, it can be accessed at [localhost:8080](http://localhost:8080/).

There are two ways to interact with the application:

1. **Form Input**:  🖊️ Use the form on the index page to input the data.
2. **URL Path**:  🔗 Access the service directly via the URL `http://localhost:8080/{username}` where `{username}` is the GitHub username you're interested in.

### GitHub API Rate Limiting :warning:

Without authorization, GitHub allows only **60 requests per hour**. However, upon creating and using your own personal access token, this limit is upgraded to **5000 requests per hour**.

## Future Development Suggestions :bulb:

Here are some features and improvements that could enhance the application:

1. **Pagination**: 📄  Implement pagination to manage large numbers of repositories or other data more effectively.
  
2. **Extended Endpoints**:  🌐 Expand the API to include additional endpoints for fetching other types of data, such as issues, pull requests, or user profiles.

3. **Dashboard**:  📊 Add a user-friendly dashboard to display repository details and other GitHub metrics in a more visual format.

4. **Caching**:  🚀 Introduce caching mechanisms to minimize redundant API calls and speed up data retrieval.

5. **Authentication**:  🔐 Add OAuth2 for GitHub to allow more than 60 requests per hour per user and access to more personalized data.

6. **Search and Filter**:  🔍 Add a search and filter feature to easily find specific repositories or users.
