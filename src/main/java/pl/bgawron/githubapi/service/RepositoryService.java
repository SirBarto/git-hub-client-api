package pl.bgawron.githubapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jndi.toolkit.url.Uri;
import org.apache.commons.logging.Log;
import org.apache.coyote.Response;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.bgawron.githubapi.model.Repository;
import sun.awt.image.ImageWatched;

import java.lang.Object;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.spi.http.HttpContext;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class RepositoryService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final List<Repository> repositoryList = new ArrayList<>();

    private final ObjectMapper mapper = new ObjectMapper();

    public List<Repository> getAllRepoByUser(String owner, String sort) throws JsonProcessingException
    {
        int pageNumber=1, page=10;
        repositoryList.clear();

        //methond get header

        while(pageNumber<=page)
        {
            JsonNode node = mapper.readTree(gitHubEndPoint(owner,pageNumber));

            for (JsonNode json : node)
            {
                if (json.get("private").asText().equals("false"))
                {
                    repositoryList.add(
                            new Repository(
                                    json.get("full_name").asText(),
                                    json.get("description").asText(),
                                    json.get("clone_url").asText(),
                                    json.get("stargazers_count").asInt(),
                                    json.get("watchers_count").asInt(),
                                    json.get("created_at").asText()
                            )
                    );
                }
            }

            pageNumber++;
        }

        try {
            String sortParameter;
            String typeSort;

            if (sort.contains(","))
            {
                if (sort.contains("asc") || sort.contains("desc"))
                {
                    String[] path = sort.split(",");
                    sortParameter = path[0];
                    typeSort = path[1];
                } else {
                    sortParameter = "";
                    typeSort = "";
                }
            }else {
                sortParameter = "";
                typeSort = "";
            }

            switch (sortParameter)
            {
                case "stars":
                    if (typeSort.equals("asc"))
                    {
                        List<Repository> listSort = repositoryList.stream()
                                .sorted(Comparator.comparingInt(Repository::getStars))
                                .collect(Collectors.toList());
                        return listSort;
                    } else if (typeSort.equals("desc"))
                    {
                        List<Repository> listSort2 = repositoryList.stream()
                                .sorted(Comparator.comparingInt(Repository::getStars).reversed())
                                .collect(Collectors.toList());
                        return listSort2;
                    }
                    break;
                case "createdAt":
                case "full_name":
                    return null;
            }
        } catch (HttpClientErrorException e)
        {
            ResponseEntity.badRequest().body(e.getStatusCode());
            System.out.println(e.getStatusCode());
        }

        return repositoryList;
    }

    @Nullable
    private String gitHubEndPoint(String owner, int page)
    {
        final String GITHUB_API_URL = "https://api.github.com/users/{owner}/repos?per_page=2&page={page}";
        ResponseEntity<JsonNode> responseEntity = restTemplate.getForEntity(GITHUB_API_URL, JsonNode.class, owner, page);
        HttpHeaders headers = responseEntity.getHeaders();
        String rel = headers.getFirst("Link");
        System.out.println(headers.getFirst("Link"));
        String[] path1 = rel.split(",");
        for(int k=0; k<path1.length; k++){
            if(path1[k].contains("last")){
                System.out.println(path1[k]);
                String ref[] = path1[k].split("page");
                System.out.println(ref[0]);
                System.out.println(ref[1]);
                int lastPage = new Integer(ref[1]);

            }
        }

        return restTemplate.getForObject(GITHUB_API_URL, String.class, owner, page);
    }

    @Nullable
    public int countRepo(String owner) throws JsonProcessingException {
        final String GITHUB_API_URL_ACCOUNT = "https://api.github.com/users/{owner}";
        JsonNode node = mapper.readTree(restTemplate.getForObject(GITHUB_API_URL_ACCOUNT,String.class, owner));
        return node.get("public_repos").intValue();
    }

    public int pageCount(String pageFlag){
        int pageNumber=1, page=10, lastPage=0;

        while (!pageFlag.contains("last")){
            System.out.println(pageFlag);
            pageNumber++;
            System.out.println(pageNumber);
        }

        return pageNumber;
    }


}
