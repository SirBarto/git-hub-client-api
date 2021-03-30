package pl.bgawron.githubapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import pl.bgawron.githubapi.model.Repository;
import pl.bgawron.githubapi.service.RepositoryServiceLogic.GetRepositoryFromGitHubApi;

import java.util.*;

@Service
public class RepositoryService extends GetRepositoryFromGitHubApi {

    public List<Repository> getAllRepoByUser(String owner, String sort)
    {
        try {
           getFetchedDataFromGitHubAPi(owner);
        } catch (HttpClientErrorException e)
        {
            ResponseEntity.badRequest().body(e.getStatusCode());
            System.out.println(e.getStatusCode());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return displayList(sort);
    }

}
