var beaconsHash;

function activateSelectPicker(resultTable, beaconsInfo) {
	
	document.getElementById("beaconlist").innerHTML = resultTable; 
	$('.selectpicker').selectpicker({ width: 'auto'});
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
	
	//modify script src to js address
	snippet += "<script src=\"http://localhost:8080/bob-js-client/js/bobby.js\"></script>" + "<script>(function() { var beacons =["; 
	
	var arrayLength = beaconIds.length
	for(var i = 0 ; i< arrayLength-1; i ++) {
		snippet += "\"" + beaconIds[i] + "\",";
	}
	
	snippet += "\"" + beaconIds[arrayLength-1] + "\"" + "]; /* writeIframe(chromosome, position, allele, genome, beacons);*/ })(); </script>";
	jQuery("#code-to-copy").text(snippet);
}



