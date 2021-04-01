package pl.bgawron.githubapi.service.RepositoryServiceLogic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;
import pl.bgawron.githubapi.model.Repository;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class GetRepositoryFromGitHubApi {

    @Autowired
    private AccessToGitHubApiEndPoint accessToGitHubApiEndPoint;

    private final ObjectMapper mapper = new ObjectMapper();

    private final RepositoryList repositoryList = new RepositoryList();

    int pageNumber=1, page=1;

    protected void getFetchedDataFromGitHubAPi(String owner) throws JsonProcessingException
    {
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
    }

    protected List<Repository> displayList(String sort){
        return repositoryList.displayList(sort);
    }

    @Nullable
    private String gitHubEndPoint(String owner, int page)
    {
        return accessToGitHubApiEndPoint.resultGitHubEndPoint(owner,page);
    }

    @Nullable
    public int countRepo(String owner) throws JsonProcessingException
    {
        JsonNode node = mapper.readTree(accessToGitHubApiEndPoint.resultCountRepo(owner));
        return node.get("public_repos").intValue();
    }

    private int pagePagination(String owner)
    {
        int lastPage=0;

        ResponseEntity<JsonNode> responseEntity = accessToGitHubApiEndPoint.resultPagePagination(owner);

        HttpHeaders headers = responseEntity.getHeaders();
        String rel = headers.getFirst("Link");
        assert rel != null;
        String[] path1 = rel.split(",");

        for (String s : path1) {
            if (s.contains("last")) {
                Pattern pattern = Pattern.compile("[^\\d]*[\\d]+[^\\d]*[\\d]+[^\\d]+([\\d])");
                Matcher matcher = pattern.matcher(s);

                while (matcher.find()) {
                    lastPage = Integer.parseInt(matcher.group(1));
                }
            }
        }

        return lastPage;
    }



}
