package pl.bgawron.githubapi.service.RepositoryServiceLogic;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.bgawron.githubapi.service.config.RestTemplateConfiguration;

@Service
public final class AccessToGitHubApiEndPoint {

    private final RestTemplateConfiguration restTemplate;

    private final String baseUrl;

    @Autowired
    public AccessToGitHubApiEndPoint(RestTemplateConfiguration restTemplate, @Value("${service.url}") String baseUrl)
    {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public final String resultGitHubEndPoint(String owner, int page)
    {
        return restTemplate.restTemplate().getForObject(baseUrl+"/{owner}/repos?per_page=10&page={page}",String.class,owner,page);
    }

    public final String resultCountRepo(String owner)
    {
        return restTemplate.restTemplate().getForObject(baseUrl+"/{owner}",String.class,owner);
    }

    public final ResponseEntity<JsonNode> resultPagePagination(String owner)
    {
        return restTemplate.restTemplate().getForEntity(baseUrl+"/{owner}/repos?per_page=10",JsonNode.class,owner);
    }

}