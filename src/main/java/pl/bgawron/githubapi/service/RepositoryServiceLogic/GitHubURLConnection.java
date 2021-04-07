package pl.bgawron.githubapi.service.RepositoryServiceLogic;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.client.WebClient;
import pl.bgawron.githubapi.model.Repository;
import pl.bgawron.githubapi.service.RepositoryService;
import pl.bgawron.githubapi.service.config.WebClientConfiguration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.xml.ws.WebServiceClient;
import java.util.List;

@Service
public class GitHubURLConnection {

    private final WebClientConfiguration webClient;

    final static String GITHUB_API_URL = "https://api.github.com/users/{owner}/repos?per_page=10&page={page}";
    final static String GITHUB_API_URL_PAGE = "https://api.github.com/users/{owner}/repos?per_page=10";

    @Autowired
    public GitHubURLConnection(WebClientConfiguration webClient) {
        this.webClient = webClient;
    }

    public List<String> webClientResultGitHubEndPoint(String owner, int page)
    {
         Mono<List> response = webClient.getWebClientBuilder().get()
                .uri("/{owner}/repos?per_page=10&page={page}",owner,page)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List>() {});
                //.subscribe(System.out::println);
        List list = response.block();
        
        return list;
        //return restTemplate.restTemplate().getForObject(GITHUB_API_URL,String.class,owner,page);
    }

    public final String webClientResultCountRepo(String owner)
    {

        return null;
        //return restTemplate.restTemplate().getForObject(baseUrl,String.class,owner);
    }

    public final ResponseEntity<JsonNode> webClientResultPagePagination(String owner)
    {

        return null;
        //return restTemplate.restTemplate().getForEntity(GITHUB_API_URL_PAGE, JsonNode.class,owner);
    }



}
