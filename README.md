# Beacon Network

## Contents

* [What it is](#what-it-is)
* [System requirements](#system-requirements)
* [How to set up the runtime](#how-to-set-up-the-runtime)
* [How to run it](#how-to-run-it)
* [How to test it](#how-to-test-it)
* [How to use it](#how-to-use-it)
* [License and terms of use](#license-and-terms-of-use)
* [Technologies](#technologies)

## What it is
Beacon Network (formerly known as the Beacon of Beacons, or BoB, for short) provides a unified REST API to publicly available GA4GH Beacons (see <http://ga4gh.org/#/beacon> for more details about the Beacon project itself). Beacon Network standardizes the way beacons are accessed and aggregates their results, thus addressing one of the missing parts of the Beacon project itself.

## System requirements
Java 8, Maven 3.1 or newer, Java EE runtime (WildFly 8 recommended).

## How to set up the runtime
Beacon Network needs access to a database through a JTA datasource (`java:/jboss/datasources/bob`). Make sure you have the required datasource on your application server or change the configuration in `persistence.xml`.

As Beacon Network can execute many queries in parallel, it's advisable to increase the pool sizes for EJB subsystem in Wildfly, particularly `slsb-strict-max-pool` (`bean-instance-pools`) and `default` (`thread-pools`).

## How to run it
Start the server:

    For Unix:       JBOSS_HOME/bin/standalone.sh -c standalone-full.xml
    For Windows:    JBOSS_HOME\bin\standalone.bat -c standalone-full.xml

Build the project:

    mvn clean install

Deploy (from `bob-rest` module):

    mvn wildfly:deploy

After deployment, the application will be running on <http://localhost:8080/>.

To undeploy when you're done, run:

    mvn wildfly:undeploy


## How to test it

To run tests for the persistence layer, execute the following in `bob-data-impl` module:

    mvn test -Pall-tests-managed

To test the supported beacons, execute the same command in `bob-rest` module. Note that the tests need access to a Wildfly instance, the path to which can be set in `$JBOSS_HOME` environment variable.

## How to Docker it

The [ci](ci/) directory contains scripts for running the Beacon Network, and running end-to-end tests
against a deployed endpoint.

#### Building the App Image

You can build the docker image with this command:
```bash
./ci/build-docker-image bob:$(git describe --tags) bob $(git describe --tags)
```

The arguments, respectively, are:
1. The docker image tag including version.
2. The name of the app (currently unused but part of the contract with the Jenkins build job).
3. The version of the app, used to change the version of the maven artifacts installed via
   the maven versions plugin.

It's not necessary to use `git describe` for the version but it is recommended.

#### Running the App Image
When running the docker image you should expose port 8080 and pass in the following environment
variables:

* JAVA_OPTS
  * Recommended settings: "-Xms2048m -Xmx9216m -XX:MetaspaceSize=96M -XX:MaxMetaspaceSize=256m -Djava.net.preferIPv4Stack=true -Djboss.modules.system.pkgs=org.jboss.byteman -Djava.awt.headless=true"
* MYSQL_HOST
  * IP Address or domain name of MySQL server
* MYSQL_PORT
  * Port to use for MySQL server connection
* MYSQL_USERNAME
  * User name for MySQL authentication
* MYSQL_PASSWORD
  * Password for MySQL authentication

## How to use it
Visit the project website for more information: <http://mcupak.github.io/beacon-of-beacons/>

## Technologies
Java EE. CDI, EJB, JAX-RS, JAXB, Bean Validation, JPA. Tested with Arquillian/ShrinkWrap/AssertJ.
