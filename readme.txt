Basic structure

The backend module contains entities in no.westerdals.news.entities and ejb's in
no.westerdals.news.ejb

The frontend consists of 5 facets and 3 different controllers, there is a facet for frontend
related task but the show_post.xhtml is not fully implemented due to time restrictions, I
was not able to get the page to keep its post id. The controllers are located in
no.westerdals.news.frontend. The logic for authenticating and creating users are located in
AuthenticationController. All logic related to the post is in PostController, currently the only
job UserController has is to get information about the user for user_details.xhtml

I started implementing testSorting and post details but didnt manage to finish it in time(comments
are not working).

Due to time restrictions I don't have a lot of tests for the enitty validation. For some reason
upvoting posts after sorting is bugged(one integration test has a workaround and another one is
disabled). The selenium tests should work fine with firefox and chrome/chromium. I have no development
environment for os x or window but the test should run fine on both.
