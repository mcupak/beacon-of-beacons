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
Java 1.7 or newer, Maven 3.1 or newer, Java EE runtime (WildFly 8 recommended).

##How to run it
Start the server:

    For Linux:   JBOSS_HOME/bin/standalone.sh -c standalone-full.xml
    For Windows: JBOSS_HOME\bin\standalone.bat -c standalone-full.xml

Build the project:

    mvn clean install

Use bob-rest module to...

...deploy BoB:

    mvn wildfly:deploy

...undeploy BoB:

    mvn wildfly:undeploy

...test the beacons:

    mvn test -Parq-wildfly-managed

After deployment, the application will be running on <http://localhost:8080/>.

##How to use it
Visit the project website for more information: <http://mcupak.github.io/beacon-of-beacons/>

##Technologies
Java EE. CDI, EJB, JAX-RS, JAXB, Bean Validation. Tested with Arquillian/ShrinkWrap.
