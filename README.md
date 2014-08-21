Beacon of Beacons
=================


What is it?
-----------
Beacon of Beacons Project (BoB) provides a unified REST API to publicly available GA4GH Beacons (see <http://ga4gh.org/#/beacon> for more details about the Beacon project itself). BoB standardizes the way beacons are accessed and aggregates their results, thus addressing one of the missing parts of the Beacon project itself.

System Requirements
-------------------
All you need to build this project is Java 7.0 (Java SDK 1.7) or later, Maven 3.0 or later. Since the project is Java EE based and utilizes some of the advanced features of Java EE 6, an application server with support for Java EE 6 Full profile is needed to deploy the application (e.g. JBoss EAP or WildFly).

How to run it?
--------------
Start the JBoss server:

    For Linux:   JBOSS_HOME/bin/standalone.sh
    For Windows: JBOSS_HOME\bin\standalone.bat

Build and deploy the archive:

    mvn clean install jboss-as:deploy

The application should now be running on <http://localhost:8080/beacon-aggregator>.

API
---
BoB was designed with ease of programmatic access in mind. It provides both XML and JSON API to accommodate needs of all the clients across all the programming languages. The API to use is determined using the header supplied by the client in its GET request, e.g.:

    Accept: application/json

A beacon "Accepts a query of the form Do you have any genomes with an 'A' at position 100,735 on chromosome 3?" and responds with one of 'Yes' or 'No' (<http://ga4gh.org/#/beacon>). This is provided by the following API endpoint:

    http://localhost:8080/beacon-aggregator/rest/responses?chrom={chromosome}&pos={position}&allele={allele}&beacon={beacon}

where the (chrom, pos, allele) parameters tuple describes the query you want to ask with the following syntax/semantics:

- {chromosome}: Chromosome ID, one of the 1-22, X, Y, MT. For compatibility with conventions set by some of the existing beacons, an arbitrary prefix is accepted as well (e.g. chr1 is equivalent to chrom1 and 1).
- {position}: Coordinate within a chromosome. Position is a number and is 0-based.
- {allele}: Any string of nucleotides A,C,T,G or D, I for deletion and insertion. For compatibility with conventions set by some of the existing beacons, DEL and INS identifiers are also accepted.
- {beacon}: Beacon ID. Optional parameter. Filters the responses to specific beacons. Unless it is specified, the responses from all the supported beacons are obtained.

Bob currently supports all the 5 publicly available beacons and converts the input information into the queries they can understand. This may include a change of chromosome identifier, using a 1-based position instead of a 0-based one as well as using a different allele representation. Some beacons support multiple projects or tracks, in which case BoB queries all of them and look for a positive response from almost one of them. Beacon IDs can be obtained by querying the following URL:

    http://localhost:8080/beacon-aggregator/rest/beacons

Response:

    [
        {
            "id": "ebi",
            "name": "EMBL-EBI"
        },
        {
            "id": "wtsi",
            "name": "Wellcome Trust Sanger Institute"
        },
        {
            "id": "uniprot",
            "name": "UniProt"
        },
        {
            "id": "clinvar",
            "name": "NCBI ClinVar"
        },
        {
            "id": "lovd",
            "name": "Leiden Open Variation"
        },
        {
            "id": "ncbi",
            "name": "NCBI"
        },
        {
            "id": "amplab",
            "name": "AMPLab"
        }
    ]

To display the information about a particular beacon, query its ID:. Example:

    http://localhost:8080/beacon-aggregator/rest/beacons/wtsi

Response:

    {
        "id": "wtsi",
        "name": "Wellcome Trust Sanger Institute"
    }

Alternatively, just filter the list using the option "beacon" parameter. Example:

    http://localhost:8080/beacon-aggregator/rest/beacons?beacon=wtsi

Response:

    [
        {
            "id": "wtsi",
            "name": "Wellcome Trust Sanger Institute"
        }
    ]

Using the beacon information obtained this way, you can execute a query against a specific beacon. Example:

    http://localhost:8080/beacon-aggregator/rest/responses/lovd?chrom=1&pos=808921&allele=T

Response:

    {
        "beacon": {
            "id": "lovd",
            "name": "Leiden Open Variation"
        },
        "query": {
            "allele": "T",
            "chromosome": "1",
            "position": 808921
        },
        "response": true
    }

As you can see, the beacon responded with its own info, the query details and a response field denoting whether a match was found (true) or not (false). Null value is used to describe a problem, e.g. an invalid query. Some beacons, for example, do not support allele strings of length>1 or X/Y chromosomes.

You can also query all the beacons at once. Example:

    http://localhost:8080/beacon-aggregator/rest/responses?chrom=14&pos=106833420&allele=A

Response:

    [
        {
            "beacon": {
                "id": "ebi",
                "name": "EMBL-EBI"
            },
            "query": {
                "allele": "A",
                "chromosome": "14",
                "position": 106833420
            },
            "response": true
        },
        {
            "beacon": {
                "id": "wtsi",
                "name": "Wellcome Trust Sanger Institute"
            },
            "query": {
                "allele": "A",
                "chromosome": "14",
                "position": 106833420
            },
            "response": true
        },
        {
            "beacon": {
                "id": "uniprot",
                "name": "UniProt"
            },
            "query": {
                "allele": "A",
                "chromosome": "14",
                "position": 106833420
            },
            "response": false
        },
        {
            "beacon": {
                "id": "clinvar",
                "name": "NCBI ClinVar"
            },
            "query": {
                "allele": "A",
                "chromosome": "14",
                "position": 106833420
            },
            "response": false
        },
        {
            "beacon": {
                "id": "lovd",
                "name": "Leiden Open Variation"
            },
            "query": {
                "allele": "A",
                "chromosome": "14",
                "position": 106833420
            },
            "response": false
        },
        {
            "beacon": {
                "id": "ncbi",
                "name": "NCBI"
            },
            "query": {
                "allele": "A",
                "chromosome": "14",
                "position": 106833420
            },
            "response": true
        },
        {
            "beacon": {
                "id": "amplab",
                "name": "AMPLab"
            },
            "query": {
                "allele": "A",
                "chromosome": "14",
                "position": 106833420
            },
            "response": true
        }
    ]

Or just filter for one of them. Example:

    http://localhost:8080/beacon-aggregator/rest/responses?chrom=14&pos=106833420&allele=A&beacon=amplab

Response:

    [
        {
            "beacon": {
                "id": "amplab",
                "name": "AMPLab"
            },
            "query": {
                "allele": "A",
                "chromosome": "14",
                "position": 106833420
            },
            "response": true
        }
    ]

If you want to utilize the "beacon of beacons" feature and are only interested in whether any of the existing beacons have a particular variant, treat BoB as a beacon itself and let it perform the aggregation. Example:

    http://localhost:8080/beacon-aggregator/rest/beacons/bob

Response:

    {
        "id": "bob",
        "name": "beacon of beacons"
    }

Query example:

    http://localhost:8080/beacon-aggregator/rest/responses/bob?chrom=14&pos=106833420&allele=D

Response:

    {
        "beacon": {
            "id": "bob",
            "name": "beacon of beacons"
        },
        "query": {
            "allele": "D",
            "chromosome": "14",
            "position": 106833420
        },
        "response": true
    }


Technologies
------------
Java EE. CDI, EJB, JAX-RS, JAXB, Bean Validation. Tested with Arquillian/ShrinkWrap.
