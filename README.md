# General Comments

I found "https://geocode.maps.co/" website that alow API calls to get an address coordinates for free. 
There is a limit of calls per second, se i added a sleep() to not exceed that limit. this made the API call slower.

I didn't make a command line tool, but instead a web REST api so it's easier to call.
In addition, due to time constraints i would have added unit testing.

# Questions
● What would you change in the code if the sample data set was very large ? How would you
speed up the most time consuming parts ?

The most time consuming parts are calling the API to get the coordinates for the addresses, and comparing all DB entires to the address.
The way to solve it in my opinion is to save the coordinates in the DB to reduce the API calls. 
Also implement concurrency calls to the external API to reduce the time, and in addition implement caching regarding the callas to the external API *and* the web API. 

● What would you change if we wanted to run the tool multiple different times and wanted the wait
time to get the results to be low ?

A few solutions are to create more instances of the service (if possible).
Another one is to implement concurrency by threads, and lastly is to create a job that does this as a daily job, so some calls can be done over night.
