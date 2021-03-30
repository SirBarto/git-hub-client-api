package pl.bgawron.githubapi.service;

import pl.bgawron.githubapi.model.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class RepositorySort {

    String sortParameter="";
    String typeSort="";

     public List<Repository> getListToSort(String sort, List<Repository> list)
     {
        if (sort.contains(",")) {
            if (sort.contains("asc") || sort.contains("desc")) {
                String[] path = sort.split(",");
                sortParameter = path[0];
                typeSort = path[1];
            } else {
                sortParameter = "";
                typeSort = "";
            }
        } else {
            sortParameter = "";
            typeSort = "";
        }

        if(sortParameter.equals("stars"))
         {
             if(typeSort.equals("asc"))
                return starsASC(list);

             if(typeSort.equals("desc"))
                return starsDESC(list);

         }
         return list;
     }

     private List<Repository> starsASC(List<Repository> list)
     {
         return list.stream()
                 .sorted(Comparator.comparingInt(Repository::getStars))
                 .collect(Collectors.toList());
     }

     private List<Repository> starsDESC(List<Repository> list)
     {
         return list.stream()
                 .sorted(Comparator.comparingInt(Repository::getStars).reversed())
                 .collect(Collectors.toList());
     }


}
