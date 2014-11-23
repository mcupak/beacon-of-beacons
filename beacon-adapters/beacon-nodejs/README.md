#Node.js Beacon

##Contents

* [What it is](#what-it-is)
* [System requirements](#system-requirements)
* [How to run it](#how-to-run-it)
* [How it works](#how-it-works)
* [Technologies](#technologies)

##What it is
This project contains BDK (beacon development kit) for Node.js developers. It provides a skeleton of a simple beacon allowing the developers to plug in their own data/functionality. The API makes sure the response produced is compatible with what the Beacon of Beacons can consume.

##System requirements
All you need to build this project restify, which you can install via npm:

    $ npm install restify

##How to run it
Launch beacon.js:

    $ node beacon.js

This starts an embedded server. By default, the application will be available at <http://localhost:8080>

##How it works
In order to implement a beacon, simply override beacon details and query function in beacon.js (marked with TODO in the source code).

The API takes care of the rest and provides the following endpoints when you start your beacon:

    http://localhost:8080/beacon-nodejs/rest/info - information about your beacon
    http://localhost:8080/beacon-nodejs/rest/query - access to query service

Query example:

    GET http://localhost:8080/beacon-nodejs/rest/query?chrom=15&pos=41087870&allele=A&ref=hg19

##Technologies
Node.js, Restify.
