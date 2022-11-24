# Timetable-Clash-Detection
# In this project I implemented a basic timetable system with a clash detection module. 
UI was implemented using javafx library. It is good library for creating a desktop application with a lot 
features and views. The look and feel of JavaFX applications can be customized. Cascading Style 
Sheets (CSS) separate appearance and style from implementation so that developers can concentrate 
on coding. Graphic designers can easily customize the appearance and style of the application through 
the CSS. Despite this, desktop applications are just not as popular as they used to be, to make a 
suggestion to improve the usability as an alternative the web user interface should be implemented 
instead, i.e. react or angular or vue.js. 
As specified in the specification an object-oriented design approach was applied during the 
development of most parts of this system. Additionally, the user interface has models written in java 
that are similar but not equal to entity models stored in the database. As for features used from 
object-oriented design, encapsulation was used to hide internals in the classes designed, inheritance 
was used to reduce code duplication and reuse codebase for similar features. All the data used in this 
application was stored and handled by classes and objects of these classes. So, system architecture 
become flexible and simple.
For storage of data, we decided to use the MySQL database, which is one of the most popular 
relational databases. A relational database organizes data into one or more tables in where data may 
be related to each other are stored in the same table; these relations help structure the data. SQL is a 
language programmers use to create, modify, and extract data from the relational database, as well 
as control user access to the database. In addition to relational databases and SQL, an RDBMS like 
MySQL works with an operating system to implement a relational database in a computer's storage 
system, manages users, allows for network access and facilitates testing database integrity and 
creation of backups. Using a database allows the developer to configure strict structure of data stored 
and use it in an object-oriented approach without difficulties. As a tool to connect and access the 
database from the application code, the spring data framework was used. It is part of the Spring 
Framework, which is the base framework for huge amounts of java applications nowadays. Spring 
data was configured to use Hibernate inside without much configuration necessary, it allows to write a 
few lines of code for our storage functionality as it has many ‘out-of-the-box’ methods for managing 
data in databases. Furthermore, to handle collections of data we used streams in combination with 
lambdas, which makes our code short and precise. Simply put, streams are defined as wrappers 
around a data source, which allows us to operate with that data source and help make bulk data 
processing convenient and fast.
The clash detection algorithm was in our opinion the most difficult component to implement within the 
application. As we used an object-oriented design, we are forced to use these objects to detect any 
collisions in the timetables. The base for the algorithm was the object Term that represents a 
semester of education, as we can’t have any clashes between terms. Each term can have four 
modules, each of module can have a random number of activities (lectures, webinars, etc.). So, we 
need to compare all activities with each other. It shouldn’t be difficult, but we also need to present a 
detailed error output, this is why we cannot simply create a common list and put all activities for 
further comparison here. We must compare activities of module with activities in the same module 
and activities in other modules. Also, we have to eliminate duplication clash reports, i.e. activity 1 and 
activity 5 have a collision in timetable, we have to report it only once, but in naïve implementation it 
will be reported twice, this would happen when checking clashed for activity 1 and activity 5. we must 
eliminate clashes with itself if that makes sense. 
