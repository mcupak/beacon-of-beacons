#Beacon of Beacons

##Contents

* [What it is](#what-it-is)
* [System requirements](#system-requirements)
* [How to run it](#how-to-run-it)
* [How to use it](#how-to-use-it)
* [Technologies](#technologies)

##What it is
Beacon of Beacons Project (BoB) provides a unified REST API to publicly available GA4GH Beacons (see <http://ga4gh.org/#/beacon> for more details about the Beacon project itself). BoB standardizes the way beacons are accessed and aggregates their results, thus addressing one of the missing parts of the Beacon project itself.

##System requirements
All you need to build this project is Java 7.0 (Java SDK 1.7) or later, Maven 3.0 or later. Since the project is Java EE based and utilizes some of the advanced features of Java EE 6, an application server with support for Java EE 6 Full profile is needed to deploy the application (e.g. JBoss EAP or WildFly).

##How to run it
Start the JBoss server:

    For Linux:   JBOSS_HOME/bin/standalone.sh
    For Windows: JBOSS_HOME\bin\standalone.bat

Build and deploy the archive:

    mvn clean install jboss-as:deploy

The application should now be running on <http://localhost:8080/beacon-of-beacons>.

##How to use it
Visit the project website for more information: <http://mcupak.github.io/beacon-of-beacons/>

##Technologies
Java EE. CDI, EJB, JAX-RS, JAXB, Bean Validation. Tested with Arquillian/ShrinkWrap.
