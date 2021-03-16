# git-hub-client-api

Api result searched all public repositories by owner name and sort by stars with asc or desc parametr.
The default parameter is always asc.
Default official GitHubApi give 1000 request result per 1 hour with no auth  GitHub account in my custom api code.
 
Release version 1.0 //2021-03-16

Deployed on Heroku

GET https://git-hub-client-api.herokuapp.com/repositories/{owner}?sort=stars,asc   --> A - Z

GET https://git-hub-client-api.herokuapp.com/repositories/{owner}?sort=stars,desc  --> Z - A

Example:

```json
[
    {
        "fullName": "octocat/test-repo1",
        "description": "null",
        "cloneUrl": "https://github.com/octocat/test-repo1.git",
        "stars": 6,
        "watchers": 6,
        "createdAt": "2016-04-14T21:29:25Z"
    },
    {
        "fullName": "octocat/boysenberry-repo-1",
        "description": "Testing",
        "cloneUrl": "https://github.com/octocat/boysenberry-repo-1.git",
        "stars": 9,
        "watchers": 9,
        "createdAt": "2018-05-10T17:51:29Z"
    }
]
```
-------------
Futher issue list

- add aut account to upper limit request result
- perform next test for searchnig parameter and sorter
- change page_count method

