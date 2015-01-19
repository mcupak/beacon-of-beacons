// constants
var timeout = 60000;
var restUrl = "http://beacon-dnastack.rhcloud.com/rest/";
var beaconsUrl = restUrl + "beacons";
var responsesUrl = restUrl + "responses?";

function createRequest() {
    var xhr = new XMLHttpRequest();
    if ("withCredentials" in xhr) {
        // do nothing
    } else if (typeof XDomainRequest != "undefined") {
        // XDomainRequest for IE.
        xhr = new XDomainRequest();
    } else {
        // CORS not supported.
        xhr = null;
    }

    return xhr;
}

function openRequest(xhr, method, url) {
    if (xhr != null) {
        if ("withCredentials" in xhr) {
            // XHR for Chrome/Firefox/Opera/Safari.
            xhr.open(method, url, true);
        } else if (typeof XDomainRequest != "undefined") {
            xhr.open(method, url);
        }
        xhr.setRequestHeader("Accept", "application/json");
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.timeout = timeout;
    }

    return xhr;
}

function sendRequest(xhr) {
    if (xhr != null) {
        xhr.send();
    }
}

function getBeacons() {
    var xhr = openRequest(createRequest(), "GET", beaconsUrl);

    xhr.onreadystatechange = function () {

        if (xhr.readyState == 4 && xhr.status == 200) {
            obj = JSON.parse(xhr.responseText);
            var arrayLength = obj.length;
            var resultTable = '<select class="form-control" name="beacon">';
            resultTable += '<option value="all">All</option>';
            for (var i = 0; i < arrayLength; i++) {
                if (obj[i] !== null) {
                    var tags = (obj[i].aggregator) ? "[aggregator]" : "";
                    resultTable += '<option value="' + obj[i].id + '">' + obj[i].name + ' (' + obj[i].organization + ') ' + tags + '</option>';
                }
            }
            resultTable += "</select>";

            document.getElementById("beaconlist").innerHTML = resultTable;
        }
    };

    xhr.ontimeout = function () {
        document.getElementById("beaconlist").innerHTML = "Request timed out.";
    };

    sendRequest(xhr);
}

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex.exec(location.search);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

function isNull(value) {
    if (value == null || value === "") {
        return true;
    }
    return false;
}

function writeInvalid(name) {
    var div = document.getElementById("params");
    var newHTML = div.innerHTML + "<div class=\"alert alert-danger\" role=\"alert\">Invalid " + name + "</div>";
    div.innerHTML = newHTML;
}

function queryBeacon(beacon, chrom, pos, alt, ref) {
    var xhr = createRequest();

    xhr.onreadystatechange = function () {

        if (xhr.readyState == 4 && xhr.status == 200) {

            obj = JSON.parse(xhr.responseText);
            var arrayLength = obj.length;
            var resultTable = "";
            //resultTable += "<center><table><tr><th>Beacon</th><th>Result</th></tr>";
            resultTable += "<center><table>";

            var rows = "";

            for (var i = 0; i < arrayLength; i++) {
                var response = obj[i].response;
                var responseIndicator = "";

                if (response == null) {
                    responseIndicator = "<span title='There was a problem obtaining the response for this beacon.' class='label label-warning'>&#x2005;&#x2005;&#x2005;&#x2005;</span>";
                } else if (response == true) {
                    responseIndicator = "<span class='label label-success'>Yes</span>";
                } else {
                    responseIndicator = "<span class='label label-danger'>No</span>";
                }

                var aggField = obj[i].beacon.aggregator;
                var aggString = "";

                if (aggField != null && aggField == true) {
                    aggString = " [aggregator]";
                }

                if (obj[i].beacon.name == "Beacon of Beacons") {
                    var line = "<tr style=\"padding-bottom: 10em;\"><td>" + responseIndicator + "</td><td><b>" + obj[i].beacon.name + " (" + obj[i].beacon.organization + ")</b></td></tr>";
                    rows = line + rows; // prepend
                } else {
                    var line = "<tr><td>" + responseIndicator + "</td><td>" + obj[i].beacon.name + " (" + obj[i].beacon.organization + ")" + aggString + "</td></tr>";
                    rows += line; // append
                }
            }

            resultTable += rows;
            resultTable += "</table></center>";

            document.getElementById("results").innerHTML = resultTable;
        }
    };

    xhr.ontimeout = function () {
        document.getElementById("results").innerHTML = "Request timed out.";
    };

    var urlArr = [];
    urlArr.push(responsesUrl, "chrom=" + chrom, "&pos=" + pos, "&allele=" + alt);

    if (beacon != "all") {
        urlArr.push("&beacon=" + beacon);
    }
    if (ref != "all") {
        urlArr.push("&ref=" + ref);
    }
    var url = urlArr.join("");

    sendRequest(openRequest(xhr, "GET", url));
}

function printResponses() {
    var beacon = getParameterByName("beacon");
    var chrom = getParameterByName("chrom");
    var pos = getParameterByName("pos");
    var alt = getParameterByName("alt");
    var ref = getParameterByName("ref");

    var failed = false;

    if (isNull(beacon)) {
        writeInvalid("beacon");
        failed = true;
    }

    if (isNull(chrom)) {
        writeInvalid("chromosome");
        failed = true;
    }

    if (isNull(pos)) {
        writeInvalid("position");
        failed = true;
    }

    if (isNull(alt)) {
        writeInvalid("allele");
        failed = true;
    }

    if (isNull(ref)) {
        writeInvalid("genome");
        failed = true;
    }

    if (!failed) {
        var paramArr = [];
        paramArr.push(
                "<center><div>",
                "<span class='label label-primary'>Beacon: " + beacon + "</span> ",
                "<span class='label label-success'>Chromosome: " + chrom + "</span> ",
                "<span class='label label-warning'>Position: " + pos + "</span> ",
                "<span class='label label-danger'>Allele: " + alt + "</span> ",
                "<span class='label label-info'>Genome: " + ref + "</span>",
                "</div></center>");
        var paramString = paramArr.join("");

        document.getElementById("params").innerHTML = paramString;
        queryBeacon(beacon, chrom, pos, alt, ref);
        document.getElementById("results").innerHTML = "<center><img height='30' src='img/wait.gif'</center>";
    }
}