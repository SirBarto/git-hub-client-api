package pl.bgawron.githubapi.model;

import java.util.List;

public class PublicRepositoriesList {

    private List<Repository> publicRepositoriesList;

    public List<Repository> getPublicRepositoriesList() {
        return publicRepositoriesList;
    }

    public void setPublicRepositoriesList(List<Repository> publicRepositoriesList) {
        this.publicRepositoriesList = publicRepositoriesList;
    }
}
