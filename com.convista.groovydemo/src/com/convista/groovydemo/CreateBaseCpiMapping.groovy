package com.convista.groovydemo

import groovy.xml.MarkupBuilder

class CreateBaseCpiMapping {
	
	public static void main(String[] args) {
		def fileContents = new File('src/com/convista/groovydemo/filialen.csv');//.getText('UTF-8');
		def encoding = new InputStreamReader(new FileInputStream(fileContents)).encoding;
		
		println 'file encoding:' + encoding;
		
		def stringPerCode = [:];
		/***********************
			Cheat sheet for pre-processing of CSV:
			([\s]{2,})(?=[aA-zZ])
			([_]{2,})
			
		*/
		fileContents.splitEachLine(";") {fields ->
			def listOfItems = [];
			
			fields[1] = fields[1].toString().trim();
			if(fields[1].equals("")){
				fields[1] = "Unbekannt";
			}
			if(fields[1].toString().length() > 60){
				fields[1] = fields[1].substring(0,60);
			}
			fields[1] = fields[1].toString().replaceAll(/[\s]{2,}/,' ');
			
			if(stringPerCode.containsKey(fields[1])){
				def alreadyListed = stringPerCode.get(fields[1]).contains(fields[0]);
				if(!alreadyListed){
					stringPerCode.get(fields[1]) << fields[0];
				} 
			}else{
				listOfItems << fields[0];
				stringPerCode[fields[1]] = listOfItems;
			}
		}
			
		def stringWriter = new StringWriter();
		def mappingBuilder = new MarkupBuilder(stringWriter);
		
		mappingBuilder.mkp.xmlDeclaration(version: "1.0", encoding: encoding)
		
		mappingBuilder.root{
			vm(version:'2.0'){
				stringPerCode.each{code, strings ->
					strings.eachWithIndex { item, index ->
						group(id:UUID.randomUUID().toString()) {
							if(index == 0){
								entry(isDefault:'true') {
									agency('id')
									schema('country')
									value(item)
								}
								entry() {
									agency('sap')
									schema('code')
									value(code)
								}
							}else{
								entry{
									agency('id')
									schema('country')
									value(item)
								}
								entry{
									agency('sap')
									schema('code')
									value(code)
								}
							}
						}
					}
				}
			}
		};
		
		File file= new File('test_output.xml');		
		PrintWriter pw = new PrintWriter(file)
		pw.write(stringWriter.toString())
		pw.close()
		
		println "Don't forget to cut the root xml element. Only <vm> is needed!";
	}

}
