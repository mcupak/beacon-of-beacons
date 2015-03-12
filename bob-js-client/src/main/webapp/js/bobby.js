function writeIFrame(chromosome, position, allele, genome, beacons) {
	
	var iFrame = document.getElementById("ga4gh-beacon-bobby");
	var url = 'http:\/\/dnastack.com\/ga4gh\/bob\/query.html?';
	url += "\&ref=" + genome + "\&pos="+ position + "\&alt=" + allele + "\&chrom=" + chromosome + "\&beacon=";
	var beaconIds = [];
	for(var i in beacons) {
		beaconIds.push(beacons[i]);
	}
	url += "[" + beaconIds + "]#results";
	if ( iFrame == null) {
		document.write("<div id=\"beacon_iframe_container\"><iframe scrolling=\"yes\" id=\"ga4gh-beacon-bobby\" src=" + url + "></iframe></div>");
	}
	else {
		iFrame.setAttribute("src", url);
	}
	
	//css to only display the results in the IFrame
	document.getElementById("beacon_iframe_container").setAttribute("style", "border: 1px solid #E2E0E1; margin:15px auto; max-width:650px; overflow:hidden");
	document.getElementById("ga4gh-beacon-bobby").setAttribute("style",  "border:0px none; margin-left:-36px; height:800px; margin-top:-300px; width:650px;");
	
}


