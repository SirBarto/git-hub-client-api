# git-hub-client-api

Api result searched all public repositories by owner name and sort by stars with asc or desc parametr.
The default parameter is always asc.
Default official GitHubApi give 1000 request result per 1 hour with no auth  GitHub account in my custom api code.
 
Release version 1.0 //2021-03-16

Deployed on Heroku

GET https://git-hub-client-api.herokuapp.com/repositories/{owner}?sort=stars,asc   --> A - Z

GET https://git-hub-client-api.herokuapp.com/repositories/{owner}?sort=stars,desc  --> Z - A

-------------
Futher issue list

- add aut account to upper limit request result
- perform next test for searchnig parameter and sorter
- change page_count method

