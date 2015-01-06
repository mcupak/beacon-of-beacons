<%-- 
    Document   : index
    Created on : Aug 24, 2014, 11:31:05 PM
    Author     : Miroslav Cupak (mirocupak@gmail.com)
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Beacon of Beacons</title>
    </head>
    <body>
        <c:set var="rest" scope="session" value="/rest/"/>
        <c:set var="app" scope="session" value="<%= request.getContextPath()%>"/>
        <c:set var="full" scope="session" value="<%= request.getRequestURL()%>"/>

        <c:set var="context" scope="session" value="${app}${rest}"/>
        <c:set var="url" scope="session" value="${fn:substringBefore(full, fn:substringAfter(full, app))}${rest}"/>

        <h1>Beacon of Beacons</h1>
        <p>Beacon of Beacons Project (BoB) provides a unified REST API to publicly available <a href="http://ga4gh.org/#/beacon">GA4GH Beacons</a>. BoB standardizes the way beacons are accessed and aggregates their results, thus addressing one of the missing parts of the Beacon project itself.</p>
        <p>BoB was designed with ease of programmatic access in mind. It provides XML, JSON and plaintext responses to accommodate needs of all the clients across all the programming languages. The API to use is determined using the header supplied by the client in its GET request, e.g.: "<i>Accept: application/json</i>".</p>
        <p>To view the list of available endpoints, go to:</p>
        <ul>
            <li><a href='<c:out value="${context}"/>'><c:out value="${url}"/></a></li>
        </ul>
        <p>For more information, check out our documentation:</p>
        <ul>
            <li><a href="https://github.com/mcupak/beacon-of-beacons">https://github.com/mcupak/beacon-of-beacons</a></li>
        </ul>
        <p>Or learn the API by example:</p>
        <ul>
            <li><a href='<c:out value="${context}"/>references'><c:out value="${url}"/>references</a></li>
            <li><a href='<c:out value="${context}"/>chromosomes'><c:out value="${url}"/>chromosomes</a></li>
            <li><a href='<c:out value="${context}"/>alleles'><c:out value="${url}"/>alleles</a></li>
            <li><a href='<c:out value="${context}"/>beacons'><c:out value="${url}"/>beacons</a></li>
            <li><a href='<c:out value="${context}"/>beacons/bob'><c:out value="${url}"/>beacons/bob</a></li>
            <li><a href='<c:out value="${context}"/>beacons?beacon=wtsi'><c:out value="${url}"/>beacons?beacon=wtsi</a></li>
            <li><a href='<c:out value="${context}"/>responses?chrom=14&amp;pos=106833421&amp;allele=A'><c:out value="${url}"/>responses?chrom=14&amp;pos=106833421&amp;allele=A</a></li>
            <li><a href='<c:out value="${context}"/>responses/amplab?chrom=15&amp;pos=41087870&amp;allele=A&amp;ref=hg19'><c:out value="${url}"/>responses/amplab?chrom=15&amp;pos=41087870&amp;allele=A&amp;ref=hg19</a></li>
            <li><a href='<c:out value="${context}"/>responses/bob?chrom=14&amp;pos=106833421&amp;allele=D'><c:out value="${url}"/>responses/bob?chrom=14&amp;pos=106833421&amp;allele=D</a></li>
            <li><a href='<c:out value="${context}"/>responses?chrom=14&amp;pos=106833421&amp;allele=A&amp;beacon=amplab'><c:out value="${url}"/>responses?chrom=14&amp;pos=106833421&amp;allele=A&amp;beacon=amplab</a></li>
        </ul>
    </body>
</html>
