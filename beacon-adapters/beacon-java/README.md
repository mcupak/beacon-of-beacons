#Java Beacon

##Contents

* [What it is](#what-it-is)
* [System requirements](#system-requirements)
* [How to run it](#how-to-run-it)
* [How it works](#how-it-works)
* [Technologies](#technologies)

##What it is
This project contains BDK (beacon development kit) for Java (EE) developers. It provides a skeleton of a simple beacon allowing the developers to plug in their own data/functionality. The API makes sure the response produced is compatible with what the Beacon of Beacons can consume.

##System requirements
All you need to build this project is Java 7.0 (Java SDK 1.7) or later, Maven 3.0 or later. Since the project is Java EE based, an application server with support for Java EE 6 is needed to deploy the application (e.g. JBoss EAP or WildFly).

##How to run it
Start the JBoss server:

    For Linux:   JBOSS_HOME/bin/standalone.sh
    For Windows: JBOSS_HOME\bin\standalone.bat

Build and deploy the archive:

    mvn clean install jboss-as:deploy

The application should now be running on <http://localhost:8080/beacon-java>.

In order to run the tests in a managed (remote) container, use the test-managed (test-remote) Maven profile. Example:

    mvn clean test -Ptest-managed

##How it works
The project provides the following:
- API for beacons
- sample beacon implementation
- conversion of parameters to a normalized form (the same way Beacon of Beacons does)
- sample navigation webpage
- sample testsuite

In order to create your own beacon, we suggest you do the following:
- implement BeaconService interface (replace the sample implementation provided in the SampleBeaconService class, you shouldn't need to touch any other classes in this project)
- modify index.jsp to provide a landing page for your beacon (optional)
- provide query with a YES and NO response in BeaconResourceTest class to make sure your beacon works (optional)

The API takes care of the rest and provides the following endpoints upon deployment of your beacon:

    http://localhost:8080/beacon-java/rest/info - information about your beacon
    http://localhost:8080/beacon-java/rest/query - access to query service

##Technologies
Java EE. CDI, JAX-RS, JAXB. Tested with Arquillian/ShrinkWrap.
