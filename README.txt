Pre-requisites
1. Apache Ant 1.7.1
2. Apache Tomcat 7.0.11 or greater
3. Java 6
4. MySQL 5.1
5. mySQL JDBC driver under ${TOMCAT_HOME}/lib

Important points
1. In case you are using local mySQL5, ensure that you create a new database "jaxrs" on the mySQL5 database. The command is
	create database jaxrs;
	use jaxrs;
2. To enable security on the Tomcat side; open the "${tomcat_home}/conf/tomcat-users.xml" file and at a minimum, add these 2 lines.
	<role rolename="tomcat"/>
	<user username="tomcat" password="tomcat" roles="tomcat"/>
3. If you are building the application using "ant"; please ensure that you have the tomcat root folder defined in the "ant.properties" file as below.
	tomcat.dir=F:\\Program Files\\apache-tomcat-7.0.11
4. The command to create the war and deploy to tomcat is "ant deploy"
5. You can access the running application at (assuming tomcat running at port 8080)
	1. http://localhost:8080/JAXRS_REST_WebServices/hello
	2. http://localhost:8080/JAXRS_REST_WebServices/rest/employee
6. The client examples can be run as JUnit tests    