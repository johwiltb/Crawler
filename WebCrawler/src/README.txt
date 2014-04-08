Johnathan Wiltberger

*All programming done on Mac OSX Mavericks*

Project - EpiCrawl
Application is used to crawl a specific, user-defined domain for a user-
defined text query.  The application allows the user to choose both the
searching algorithm as well as the string matching algorithm.  The 
application will then print the results of the search back out to the 
user.  The URL links that are visited are added to a file called "links.txt" 
in the current directory.

Current information about the project as follows:
- For policy following, the crawler obeys Disallow tags for "*" crawlers listed.
This is because the crawler is not known as of yet, so no name is used.  
- The crawler prints out the links visited to a file in the local directory 
called links.  

*** FOR PROJECT 1, ONLY DEPTH LIMITED SEARCHING AND NAIVE STRING MATCHING WORKS ***
Known Issues/Limitations:
- Does not correctly crawl URLs with changing subdomains.  Strictly crawls URLs
after the domain.
- Does not understand non-standard robots.txt extensions.
- Probably more!

JRE -> JavaSE-1.7
IDE -> Eclipse Kepler
File list:
./
    src/
        README.txt
        Crawler.java
        UserMenu.java
        BFS.java
        DLS.java
