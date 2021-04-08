package pl.bgawron.githubapi;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.bgawron.githubapi.model.Repository;
import pl.bgawron.githubapi.service.RepositoryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {})
class RepositoryServiceTest {

    @Mock
    private RepositoryService repositoryService;

    @Before
    public void prepareMockData() throws Exception {
        MockitoAnnotations.openMocks(this);

        List<Repository> list = new ArrayList<>();
        list.add(new Repository("user/repo1","test","",10,0,"20210311"));
        list.add(new Repository("user/repo2","test","",20,0,"20210311"));
        list.add(new Repository("superuser/repo3","test","",25,0,"20210311"));
        when(repositoryService.getAllPublicRepositoriesByUser("user","")).thenReturn(list);
    }

    @Test
    void checkConnectionWithGitHubEndPoint() throws IOException
    {
        //given
        String jsonMimeType = "application/json";
        HttpUriRequest request = new HttpGet("https://api.github.com/users/");

        //when
        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        //then
        String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
        assertEquals(jsonMimeType,mimeType);
    }

    @Test
    void checkIsOwnerHavePublicRepoWithEmptySortParam() throws IOException
    {
        //given
        String[] userName = {"octocat","hasherezade","SirBarto","dszmaj7"};

        //when
        List<Repository> repositoryList0 = new ArrayList<>(repositoryService.getAllPublicRepositoriesByUser(userName[0],""));
        List<Repository> repositoryList1 = new ArrayList<>(repositoryService.getAllPublicRepositoriesByUser(userName[1],""));
        List<Repository> repositoryList2 = new ArrayList<>(repositoryService.getAllPublicRepositoriesByUser(userName[2],""));
        List<Repository> repositoryList3 = new ArrayList<>(repositoryService.getAllPublicRepositoriesByUser(userName[3],""));

        int a = repositoryService.
                countRepo(userName[0]);
        int b = repositoryService.countRepo(userName[1]);
        int c = repositoryService.countRepo(userName[2]);
        int d = repositoryService.countRepo(userName[3]);

        //then
        assertNotNull(repositoryList1);
        assertAll("Number of public repositories",
              ()-> assertEquals(a,repositoryList0.size()),
              ()-> assertEquals(b,repositoryList1.size()),
              ()-> assertEquals(c,repositoryList2.size()),
                ()-> assertEquals(d,repositoryList3.size())
        );

        System.out.println(repositoryList0.size());
        System.out.println(repositoryList1.size());
        System.out.println(repositoryList2.size());
        System.out.println(repositoryList3.size());

        repositoryList0.clear();
        repositoryList1.clear();
        repositoryList2.clear();
        repositoryList3.clear();
    }

    @Test
    void ifSorterParameterIsCorect() {
        System.out.println("nie wlasciwy parametr sortowania, inny niz gwiazdka");
    }

    @Test
    void ifSorterTypeIsCorrect() {
        System.out.println("nie wlasciwy typ sortowania inny niz asc lub desc. Brak podanego sposobu sortowania");
    }

}