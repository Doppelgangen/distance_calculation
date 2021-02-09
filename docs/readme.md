Database name for project is : distance_calculator
(not distance-calculator)

Liquibase updates by "mvn liquibase:update"
This migration also adding 10 cities to database.

Please rearrange upload path at package com.vikmak.distance.dao.CityDAO if needed
private static final String uploadPath = "C:\\calculation\\"; 

localhost:8080

Index page of project provides necessary fields:
Cities list:
reference for cities in the database (just id and name)
reference for cities in the database (full city info in XML)

Measurements: 
Select box for type of measure
Text box to input "cities from"
Text box to input "cities to"
Distance will be displayed only for the cities that are in the database.
If the city from equals the city to - it won't be displayed in crowflight. 
Please mind that if many distances between cities are absent in the table "distances",
one's might need to refresh page.
 
Add new cities:
File box for multipart xml file.
List should look like:
<cities>
    <city>
        <name></name>
        <latitude></latitude>
        <longitude></longitude>
    </city>
</cities>
Id's are arranged automatically by the database.  

Mostly application is ready:
For start please run following commands:
mvn liquibase:update
mvn wildfly:run 