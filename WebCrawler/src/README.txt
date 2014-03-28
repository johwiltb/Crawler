Johnathan Wiltberger

*All programming done on Mac OSX Mavericks*

Project - Web Crawler
Application is used to crawl a specific, user-defined domain for a user-
defined text query.  The application allows the user to choose both the
searching algorithm as well as the string matching algorithm.  The 
application will then print the results of the search back out to the 
user.

Current information about the project as follows:
For policy following, the crawler obeys Disallow tags for all crawlers listed.
This is because the crawler is not known as of yet, and I would rather be safe 
than sorry.  

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
