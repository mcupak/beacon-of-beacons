var beaconsHash;

function activateSelectPicker(resultTable, beaconsInfo) {
	
	document.getElementById("beaconlist").innerHTML = resultTable; 
	$('.selectpicker').selectpicker({ width: '100%'});
	beaconsHash = beaconsInfo;
}

function getBeaconsToQueryIds() {

	var beaconsToQuery = jQuery(".selectpicker").val();
	if (beaconsToQuery == null) {
		beaconsToQuery = [];
		jQuery("option").each( function (i,e) {
			beaconsToQuery.push(e.text);
		})
	}
	var beaconIds = [];
	for (var i in beaconsToQuery) {	
		beaconIds.push( beaconsHash[String(beaconsToQuery[i])] );
	}
	
	return beaconIds; 
}

function getCodeSnippet() {	
	
	var beaconIds = getBeaconsToQueryIds();
	var snippet = "";
	var url = "http://dnastack.com/ga4gh/bob/";
	
	// TODO: change the path accordingly
	var filePath = "js/bobby.js";
	var src = url + filePath;
	snippet += "<script src=\"" + src + "\"></script>" + "<script>(function() { var beacons =[";
	
	var arrayLength = beaconIds.length
	for(var i = 0 ; i< arrayLength-1; i ++) {
		snippet += "\"" + beaconIds[i] + "\",";
	}
	
	snippet += "\"" + beaconIds[arrayLength-1] + "\"" + "]; /*TODO: set these variables as per: https://github.com/ga4gh/schemas/blob/master/src/main/resources/avro/beacon.avdl */ var chromosome=1;var allele=\"A\";var position=1;var genome=\"hg19\"; writeIFrame(chromosome, position, allele, genome, beacons);})(); <\/script>";        

	jQuery("#code-to-copy").text(snippet);
}



