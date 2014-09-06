<%-- 
    Document   : index
    Created on : Aug 24, 2014, 11:31:05 PM
    Author     : Miroslav Cupak (mirocupak@gmail.com)
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Beacon of Beacons</title>
    </head>
    <body>
        <h1>Beacon of Beacons</h1>
        <p>Beacon of Beacons Project (BoB) provides a unified REST API to publicly available <a href="http://ga4gh.org/#/beacon">GA4GH Beacons</a>. BoB standardizes the way beacons are accessed and aggregates their results, thus addressing one of the missing parts of the Beacon project itself.</p>
        <p>BoB was designed with ease of programmatic access in mind. It provides XML, JSON and plaintext responses to accommodate needs of all the clients across all the programming languages. The API to use is determined using the header supplied by the client in its GET request, e.g.: "<i>Accept: application/json</i>".</p>
        <p>To view the list of available endpoints, go to:</p>
        <ul>
            <li><a href="<%= request.getRequestURL()%>rest/"><%= request.getRequestURL()%>rest/</a></li>
        </ul>
        <p>For more information, check out our documentation:</p>
        <ul>
            <li><a href="https://github.com/mcupak/beacon-of-beacons">https://github.com/mcupak/beacon-of-beacons</a></li>
        </ul>
        <p>Or learn the API by example:</p>
        <ul>
            <li><a href="<%= request.getRequestURL()%>rest/beacons"><%= request.getRequestURL()%>rest/beacons</a></li>
            <li><a href="<%= request.getRequestURL()%>rest/beacons/bob"><%= request.getRequestURL()%>rest/beacons/bob</a></li>
            <li><a href="<%= request.getRequestURL()%>rest/beacons?beacon=wtsi"><%= request.getRequestURL()%>rest/beacons?beacon=wtsi</a></li>
            <li><a href="<%= request.getRequestURL()%>rest/responses?chrom=14&pos=106833421&allele=A"><%= request.getRequestURL()%>rest/responses?chrom=14&pos=106833421&allele=A</a></li>
            <li><a href="<%= request.getRequestURL()%>rest/responses/amplab?chrom=15&pos=41087870&allele=A&ref=hg19"><%= request.getRequestURL()%>rest/responses/amplab?chrom=15&pos=41087870&allele=A&ref=hg19</a></li>
            <li><a href="<%= request.getRequestURL()%>rest/responses/bob?chrom=14&pos=106833421&allele=D"><%= request.getRequestURL()%>rest/responses/bob?chrom=14&pos=106833421&allele=D</a></li>
            <li><a href="<%= request.getRequestURL()%>rest/responses?chrom=14&pos=106833421&allele=A&beacon=amplab"><%= request.getRequestURL()%>rest/responses?chrom=14&pos=106833421&allele=A&beacon=amplab</a></li>
        </ul>
    </body>
</html>
