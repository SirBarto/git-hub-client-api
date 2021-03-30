package pl.bgawron.githubapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.bgawron.githubapi.model.Repository;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RepositoryService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper mapper = new ObjectMapper();

    private final RepositoryList repositoryList = new RepositoryList();

    int pageNumber=1, page=1;

    public List<Repository> getAllRepoByUser(String owner, String sort) throws JsonProcessingException
    {
        try {

            page= pagePagination(owner);
            repositoryList.clearList();

            while(pageNumber<=page)
            {
                JsonNode node = mapper.readTree(gitHubEndPoint(owner,pageNumber));

                for (JsonNode json : node)
                {
                    if (json.get("private").asText().equals("false"))
                    {
                        repositoryList.addToList(
                                json.get("full_name").asText(),
                                json.get("description").asText(),
                                json.get("clone_url").asText(),
                                json.get("stargazers_count").asInt(),
                                json.get("watchers_count").asInt(),
                                json.get("created_at").asText()
                        );
                    }
                }
                pageNumber++;
            }

        } catch (HttpClientErrorException e)
        {
            ResponseEntity.badRequest().body(e.getStatusCode());
            System.out.println(e.getStatusCode());
        }

        return repositoryList.displayList(sort);
    }
    
    @Nullable
    private String gitHubEndPoint(String owner, int page)
    {
        final String GITHUB_API_URL = "https://api.github.com/users/{owner}/repos?per_page=10&page={page}";
        return restTemplate.getForObject(GITHUB_API_URL, String.class, owner, page);
    }

    @Nullable
    public int countRepo(String owner) throws JsonProcessingException
    {
        final String GITHUB_API_URL_ACCOUNT = "https://api.github.com/users/{owner}";
        JsonNode node = mapper.readTree(restTemplate.getForObject(GITHUB_API_URL_ACCOUNT,String.class, owner));
        return node.get("public_repos").intValue();
    }

    public int pagePagination(String owner)
    {
        int lastPage=0;
        final String GITHUB_API_URL_PAGE = "https://api.github.com/users/{owner}/repos?per_page=10";

        ResponseEntity<JsonNode> responseEntity = restTemplate.getForEntity(GITHUB_API_URL_PAGE, JsonNode.class, owner);
        HttpHeaders headers = responseEntity.getHeaders();
        String rel = headers.getFirst("Link");
        String[] path1 = rel.split(",");

        for(int k=0; k<path1.length; k++){
            if(path1[k].contains("last")){
                Pattern pattern = Pattern.compile("[^\\d]*[\\d]+[^\\d]*[\\d]+[^\\d]+([\\d])");
                Matcher matcher = pattern.matcher(path1[k]);

                while (matcher.find()){
                    lastPage = Integer.parseInt(matcher.group(1));
                }
            }
        }

        return lastPage;
    }


}
