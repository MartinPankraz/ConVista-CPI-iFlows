/**
 * Class creates jar files from base42 encoded file content retrieved from CPI logging
 */
package com.convista.groovydemo

class Base64Decoder {

	static main(args) {
		String name = "com.sap.gw.rt.camel.components.custom-development_1.39.5";
		File fileBase64 = new File("./libs/"+name+".txt");
		File fileBinary = new File("./libs/"+name+".jar");
		fileBinary.bytes = fileBase64.text.decodeBase64();
	}

}