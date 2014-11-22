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
        <title>Sample Beacon</title>
    </head>
    <body>
        <h1>Sample Beacon</h1>
        <p>This is a sample Java beacon. It was designed with ease of programmatic access in mind. It provides XML, JSON and plaintext responses to accommodate needs of all the clients across all the programming languages. The API to use is determined using the header supplied by the client in its GET request, e.g.: "<i>Accept: application/json</i>".</p>
        <p>Try it out:</p>
        <ul>
            <li><a href="<%= request.getRequestURL()%>rest/query?chrom=15&pos=41087870&allele=A&ref=hg19"><%= request.getRequestURL()%>rest/query?chrom=15&pos=41087870&allele=A&ref=hg19</a></li>
            <li><a href="<%= request.getRequestURL()%>rest/query?chrom=15&pos=41087871&allele=A&ref=hg19"><%= request.getRequestURL()%>rest/query?chrom=15&pos=41087871&allele=A&ref=hg19</a></li>
            <li><a href="<%= request.getRequestURL()%>rest/info"><%= request.getRequestURL()%>rest/info</a></li>
        </ul>
    </body>
</html>
