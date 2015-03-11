function writeIframe(chromosome, position, allele, genome, beacons) {
	
	var iFrame = document.getElementById("ga4gh-beacon-bobby");
	var url = 'http:\/\/dnastack.com\/ga4gh\/bob\/query.html?';
	url += "\&ref=" + genome + "\&pos="+ position + "\&alt=" + allele + "\&chrom=" + chromosome + "\&beacon=";
	var beaconIds = [];
	for(var i in beacons) {
		beaconIds.push(beacons[i]);
	}
	url += "[" + beaconIds + "]#results";
	if ( iFrame == null) {
		document.write("<div id=\"beacon_iframe_container\"><iframe id=\"ga4gh-beacon-bobby\" src=" + url + "></iframe></div>");
	}
	else {
		iFrame.setAttribute("src", url);
	}
	
	//css to make IFrame fluidic
	document.getElementById("beacon_iframe_container").setAttribute("style", "position: relative; padding-bottom: 56.25%; padding-top: 30px;height:0;overflow:hidden");
	document.getElementById("ga4gh-beacon-bobby").setAttribute("style",  "position: absolute; top: 0; left: 0;width: 100%; height: 100%");
	
}


