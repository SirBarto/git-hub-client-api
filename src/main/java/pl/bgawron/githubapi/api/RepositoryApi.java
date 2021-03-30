package pl.bgawron.githubapi.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import pl.bgawron.githubapi.service.RepositoryService;
import pl.bgawron.githubapi.model.Repository;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import static jdk.nashorn.internal.objects.NativeMath.log;

@RestController
@RequestMapping("/repositories")
public class RepositoryApi {

    private final RepositoryService repositoryService;

    @Autowired
    public RepositoryApi(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @GetMapping("/{owner}")
    public @ResponseBody
    ResponseEntity<List<Repository>> getAllRepoByUser(@PathVariable String owner,
                                                      @RequestParam(value = "sort", required = false, defaultValue = "stars,asc") String sort
                                                      ) throws IOException {
       try {
           List<Repository> list = repositoryService.getAllRepoByUser(owner,sort);
           if(list.isEmpty()){
               return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
           }else {
               return ResponseEntity.status(HttpStatus.OK).body(list);
           }
       }catch (HttpClientErrorException e){
           log(Level.INFO,e.getStatusCode());
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
       }
    }

}
