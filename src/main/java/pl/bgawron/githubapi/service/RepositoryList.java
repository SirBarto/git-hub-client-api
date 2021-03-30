package pl.bgawron.githubapi.service;

import pl.bgawron.githubapi.model.Repository;

import java.util.ArrayList;
import java.util.List;

public class RepositoryList extends RepositorySort{

    private final List<Repository> repositoryList = new ArrayList<>();

    public void addToList(String fullName, String description, String cloneUrl, int stars, int watchers, String createdAt)
    {
        repositoryList.add(new Repository(
                fullName,description,
                cloneUrl,stars,
                watchers,createdAt)
        );
    }

    public void clearList()
    {
        if(!repositoryList.isEmpty())
            repositoryList.clear();
    }

    List<Repository> displayList(String sortParameter)
    {
        return getListToSort(sortParameter,repositoryList);
    }

}
