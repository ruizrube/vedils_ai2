package com.google.appinventor.components.runtime.ld4ai.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Properties;

import com.google.appinventor.components.runtime.ld4ai.ConceptForGenericRDF;
import com.google.appinventor.components.runtime.ld4ai.ConceptForWikiData;
import com.google.appinventor.components.runtime.ld4ai.SPARQLClientForGenericRDF;
import com.google.appinventor.components.runtime.ld4ai.SPARQLClientForWikiData;
import com.google.appinventor.components.runtime.ld4ai.utils.GenericRDFClient;


public class Main {

    public static void main(String[] args) throws IOException {
        // SPARQL Query
        //String szQuery = "select * where {?Subject ?Predicate ?Object} LIMIT 10";
    	
    	/*String szQuery = "prefix dbpo: <http://dbpedia.org/ontology/>" + "\n" + 
    			"prefix dbpr: <http://dbpedia.org/resource/>" + "\n" +
    			"select distinct ?Predicate ?Object where {" + "\n" +
    			"?Subject ?Predicate ?Object" + "\n" +
    			"filter(?Subject = dbpr:Taylor_Swift)}";*/
    	
    	/*String szQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" + "\n" +
    			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "\n" +
    			"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" + "\n" +
    			"PREFIX owl: <http://www.w3.org/2002/07/owl#>" + "\n" +
    			"PREFIX meshv: <http://id.nlm.nih.gov/mesh/vocab#>" + "\n" +
    			"PREFIX mesh: <http://id.nlm.nih.gov/mesh/>" + "\n" +
    			"PREFIX mesh2015: <http://id.nlm.nih.gov/mesh/2015/>" + "\n" +
    			"PREFIX mesh2016: <http://id.nlm.nih.gov/mesh/2016/>" + "\n" +
    			"PREFIX mesh2017: <http://id.nlm.nih.gov/mesh/2017/>" + "\n" +
    			"PREFIX mesh2018: <http://id.nlm.nih.gov/mesh/2018/>" + "\n" +

				"SELECT DISTINCT ?class" + "\n" +
				"FROM <http://id.nlm.nih.gov/mesh>" + "\n" +
				"WHERE { [] a ?class . }" + "\n" +
				"ORDER BY ?class";*/
    	
    	
    	/*String szQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" + "\n" +
    		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "\n" +
    		"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" + "\n" +
    		"PREFIX owl: <http://www.w3.org/2002/07/owl#>" + "\n" +
    		"PREFIX meshv: <http://id.nlm.nih.gov/mesh/vocab#>" + "\n" +
    		"PREFIX mesh: <http://id.nlm.nih.gov/mesh/>" + "\n" +
    		"PREFIX mesh2015: <http://id.nlm.nih.gov/mesh/2015/>" + "\n" +
    		"PREFIX mesh2016: <http://id.nlm.nih.gov/mesh/2016/>" + "\n" +
    		"PREFIX mesh2017: <http://id.nlm.nih.gov/mesh/2017/>" + "\n" +
    			  
			"select ?type ?label ?s {" + "\n" +
    			   "?type a owl:Class . " + "\n" +
    			   "?type rdfs:label ?label . " + "\n" +
    			"}";	*/
    	
    	
    	/*String szQuery = "prefix dbpo: <http://dbpedia.org/ontology/>" + "\n" + 
    			"PREFIX owl: <http://www.w3.org/2002/07/owl#>" + "\n" +
    			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "\n" +
    			"prefix dbpr: <http://dbpedia.org/resource/>" + "\n" +
    			"select ?type ?label ?s {" + "\n" +
 			   "?type a owl:Class . " + "\n" +
 			   "?type rdfs:label ?label . " + "\n" +
 			"}";*/
    	
    	//Q1) SELECT PROPERTIES (USED)
    		
    		//Wikidata:
    	
    	
    		//Mesh:
    	
    		/*String szQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" + "\n" + 
        		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "\n" + 
        		"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" + "\n" + 
        		"PREFIX owl: <http://www.w3.org/2002/07/owl#>" + "\n" + 
        		"PREFIX meshv: <http://id.nlm.nih.gov/mesh/vocab#>" + "\n" + 
        		"PREFIX mesh: <http://id.nlm.nih.gov/mesh/>" + "\n" + 
        		"PREFIX mesh2015: <http://id.nlm.nih.gov/mesh/2015/>" + "\n" + 
        		"PREFIX mesh2016: <http://id.nlm.nih.gov/mesh/2016/>" + "\n" + 
        		"PREFIX mesh2017: <http://id.nlm.nih.gov/mesh/2017/>" + "\n" + 

				"SELECT ?p ?label WHERE {" + "\n" +
        			  "?p rdfs:domain meshv:Descriptor . " + "\n" +
        			  "?p rdfs:label ?label . " + "\n" +
        		"}";*/
    		
    	
    	//Q2) SELECT SUPERCLASSES
    		
    		//Wikidata:
    	
    		//Mesh:
    	
    		/*String szQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" + "\n" + 
        		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "\n" + 
        		"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" + "\n" + 
        		"PREFIX owl: <http://www.w3.org/2002/07/owl#>" + "\n" + 
        		"PREFIX meshv: <http://id.nlm.nih.gov/mesh/vocab#>" + "\n" + 
        		"PREFIX mesh: <http://id.nlm.nih.gov/mesh/>" + "\n" + 
        		"PREFIX mesh2015: <http://id.nlm.nih.gov/mesh/2015/>" + "\n" + 
        		"PREFIX mesh2016: <http://id.nlm.nih.gov/mesh/2016/>" + "\n" + 
        		"PREFIX mesh2017: <http://id.nlm.nih.gov/mesh/2017/>" + "\n" + 

    			"SELECT ?type ?label ?s {" + "\n" +
    				"?type a owl:Class . " + "\n" +
    				"?type rdfs:label ?label . " + "\n" +
    				"FILTER ( regex (?label,\"MeSH\", \"i\") )" + "\n" +
        		"}";*/
    	
    	//Q3) SELECT SUBCLASSES (USED)
    		
    		//Wikidata:
    	
    		//Mesh:
    		
    		/*String szQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" + "\n" + 
            		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "\n" + 
            		"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" + "\n" + 
            		"PREFIX owl: <http://www.w3.org/2002/07/owl#>" + "\n" + 
            		"PREFIX meshv: <http://id.nlm.nih.gov/mesh/vocab#>" + "\n" + 
            		"PREFIX mesh: <http://id.nlm.nih.gov/mesh/>" + "\n" + 
            		"PREFIX mesh2015: <http://id.nlm.nih.gov/mesh/2015/>" + "\n" + 
            		"PREFIX mesh2016: <http://id.nlm.nih.gov/mesh/2016/>" + "\n" + 
            		"PREFIX mesh2017: <http://id.nlm.nih.gov/mesh/2017/>" + "\n" + 

					"SELECT ?subClass ?label WHERE {" + "\n" +
            	        "?subClass rdfs:subClassOf meshv:Descriptor . " + "\n" +
            	        "?subClass rdfs:label ?label . " + "\n" +
            		"}";*/
    	
    	//Q4) SELECT INSTANCES (con limit)
    		
    		//Wikidata:
    	
    		//Mesh:
    	
			/*String szQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" + "\n" + 
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "\n" + 
			"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" + "\n" + 
			"PREFIX owl: <http://www.w3.org/2002/07/owl#>" + "\n" + 
			"PREFIX meshv: <http://id.nlm.nih.gov/mesh/vocab#>" + "\n" + 
			"PREFIX mesh: <http://id.nlm.nih.gov/mesh/>" + "\n" + 
			"PREFIX mesh2015: <http://id.nlm.nih.gov/mesh/2015/>" + "\n" + 
			"PREFIX mesh2016: <http://id.nlm.nih.gov/mesh/2016/>" + "\n" + 
			"PREFIX mesh2017: <http://id.nlm.nih.gov/mesh/2017/>" + "\n" + 
		
			"SELECT ?x ?label" + "\n" +  
			"WHERE {"  + "\n" + 
			"?x rdf:type meshv:Concept ."  + "\n" + 
			"?x rdfs:label ?label . " + "\n" +
			"}" + "\n" +
			"LIMIT 10";*/
    	
    	//Q5) SELECT INSTANCES (sin limit)
    		
    		//Wikidata:
    	
    		//Mesh
			
			/*String szQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" + "\n" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "\n" + 
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" + "\n" + 
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>" + "\n" + 
				"PREFIX meshv: <http://id.nlm.nih.gov/mesh/vocab#>" + "\n" + 
				"PREFIX mesh: <http://id.nlm.nih.gov/mesh/>" + "\n" + 
				"PREFIX mesh2015: <http://id.nlm.nih.gov/mesh/2015/>" + "\n" + 
				"PREFIX mesh2016: <http://id.nlm.nih.gov/mesh/2016/>" + "\n" + 
				"PREFIX mesh2017: <http://id.nlm.nih.gov/mesh/2017/>" + "\n" + 
				
				"SELECT ?x ?label" + "\n" +  
				"WHERE {"  + "\n" + 
				"?x rdf:type meshv:Concept ."  + "\n" + 
				"?x rdfs:label ?label . " + "\n" +
				"}";*/
    	
    	//Q6) SELECT CLASSES
    		
    		//Wikidata:
    	
			/*String szQuery = "PREFIX dbpo: <http://dbpedia.org/ontology/>" + "\n" + 
    		"PREFIX owl: <http://www.w3.org/2002/07/owl#>" + "\n" +
    		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "\n" +
    		"PREFIX dbpr: <http://dbpedia.org/resource/>" + "\n" +
    		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" + "\n" +
		
			"SELECT DISTINCT ?class ?label" + "\n" + 
			"WHERE {" + "\n" + 
			"?s rdf:type ?class ." + "\n" + 
			"?s rdfs:label ?label . " + "\n" +
			"}" + "\n" + 
			"ORDER BY ?class"+ "\n" +
			"LIMIT 1";/*
    	
    		//Mesh
    	
    		/*String szQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" + "\n" + 
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "\n" + 
			"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" + "\n" + 
			"PREFIX owl: <http://www.w3.org/2002/07/owl#>" + "\n" + 
			"PREFIX meshv: <http://id.nlm.nih.gov/mesh/vocab#>" + "\n" + 
			"PREFIX mesh: <http://id.nlm.nih.gov/mesh/>" + "\n" + 
			"PREFIX mesh2015: <http://id.nlm.nih.gov/mesh/2015/>" + "\n" + 
			"PREFIX mesh2016: <http://id.nlm.nih.gov/mesh/2016/>" + "\n" + 
			"PREFIX mesh2017: <http://id.nlm.nih.gov/mesh/2017/>" + "\n" + 
		
			"SELECT DISTINCT ?class ?label" + "\n" + 
			"FROM <http://id.nlm.nih.gov/mesh>" + "\n" + 
			"WHERE {" + "\n" + 
			"?s rdf:type ?class ." + "\n" + 
			"?s rdfs:label ?label . " + "\n" +
			"}" + "\n" + 
			"ORDER BY ?class";*/
    	
    	/*String szQuery = "prefix dbpo: <http://dbpedia.org/ontology/>" + "\n" + 
		"prefix dbpr: <http://dbpedia.org/resource/>" + "\n" +
		"SELECT DISTINCT ?class" + "\n" +
		"WHERE { [] a ?class . }" + "\n" +
		"ORDER BY ?class";*/
    	
        // Arguments
        /*if (args != null && args.length == 1) {
            szQuery = new String(
                    Files.readAllBytes(Paths.get(args[0])),
                    Charset.defaultCharset());
        }

        // DBPedia Endpoint
        String szEndpoint = "http://dbpedia.org/sparql";
       //String szEndpoint = "http://id.nlm.nih.gov/mesh/sparql";

        // Query DBPedia
        try {
        	Main q = new Main();
            q.queryEndpoint(szQuery, szEndpoint);
        }
        catch (Exception ex) {
            System.err.println(ex);
        }*/
    	
    	/*String prefixesMesh = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" + "\n" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "\n" + 
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" + "\n" + 
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>" + "\n" + 
				"PREFIX meshv: <http://id.nlm.nih.gov/mesh/vocab#>" + "\n" + 
				//"PREFIX mesh: <http://id.nlm.nih.gov/mesh/>" + "\n" + 
				//"PREFIX mesh2015: <http://id.nlm.nih.gov/mesh/2015/>" + "\n" + 
				//"PREFIX mesh2016: <http://id.nlm.nih.gov/mesh/2016/>" + "\n" + 
				"PREFIX mesh2017: <http://id.nlm.nih.gov/mesh/2017/>" + "\n" +
				"PREFIX map: <http://kmsemphasys.uca.es:2020/resource/#>" + "\n" +
				"PREFIX db: <http://kmsemphasys.uca.es:2020/resource/>" + "\n" +
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" + "\n" +
				"PREFIX vocab: <http://kmsemphasys.uca.es:2020/resource/vocab/>" + "\n";*/
    	
    	/*ConceptForGenericRDF concept = new ConceptForGenericRDF("https://id.nlm.nih.gov/mesh/sparql", prefixesMesh);
    	concept.Identifier("http://id.nlm.nih.gov/mesh/2015/M0585109");
    	concept.LoadResource();
    	System.out.println("Label = " + concept.Label());
    	System.out.println("Linked concept = " + concept.RetrieveLinkedConcept("term"));
    	System.out.println("Prefe Linked concept = " + concept.RetrieveLinkedConcept("meshv:preferredTerm"));
    	System.out.println("Array Linked concept = " + concept.RetrieveLinkedConcepts("term").toString());
    	System.out.println("Array Prefe Linked concept = " + concept.RetrieveLinkedConcepts("preferredTerm").toString());
    	System.out.println("Properties = " + concept.AvailableProperties());
    	
    	
    	System.out.println("RetrieveStringValues = " + concept.RetrieveStringValues("meshv:term"));
    	System.out.println("RetrieveStringValue = " + concept.RetrieveStringValue("meshv:preferredTerm"));
    	
    	ConceptForGenericRDF term = new ConceptForGenericRDF("https://id.nlm.nih.gov/mesh/sparql", prefixesMesh);
    	term.Identifier("http://id.nlm.nih.gov/mesh/2015/T806066");
    	term.LoadResource();
    	System.out.println("RetrieveStringValue = " + term.RetrieveStringValue("meshv:prefLabel"));*/
    	
    	
    	/*Properties prop = new Properties();
    	InputStream input = null;

    	try {

    		input = new FileInputStream("config.properties");

    		// load a properties file
    		prop.load(input);

    		// get the property value and print it out
    		System.out.println(prop.getProperty("database"));
    		System.out.println(prop.getProperty("dbuser"));
    		System.out.println(prop.getProperty("dbpassword"));

    	} catch (IOException ex) {
    		ex.printStackTrace();
    	} finally {
    		if (input != null) {
    			try {
    				input.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}*/
    	
    	SPARQLClientForGenericRDF sparqlClient = new SPARQLClientForGenericRDF("http://sparql.bioontology.org/sparql/");
    	System.out.println("SuperClasses = " + sparqlClient.selectSubclassesEncoded("", "es", "en"));
    	
    	//SPARQLClientForGenericRDF sparqlClient = new SPARQLClientForGenericRDF("https://id.nlm.nih.gov/mesh/sparql");
    	//SPARQLClientForGenericRDF sparqlClient = new SPARQLClientForGenericRDF("http://kmsemphasys.uca.es:2020/sparql");
    	
    	//System.out.println("InstancesByLabel = " + sparqlClient.selectInstancesByLabel("medicine", "es", "en", 10, 0));
    	//sparqlClient.selectSubclasses("http://id.nlm.nih.gov/mesh/vocab#Descriptor", "es", "en");
    	//sparqlClient.selectSuperclasses("", "es", "en");
    	//sparqlClient.selectProperties("http://id.nlm.nih.gov/mesh/vocab#Concept", "es", "en");
    	//sparqlClient.selectInstances("http://kmsemphasys.uca.es:2020/resource/vocab/profesional_table", "es", "en", 10, 30);
    	//sparqlClient.selectInstances("meshv:Concept", "es", "en");
    	//sparqlClient.selectClasses("meshv:Concept", "", "");
    	//System.out.println("Superclasses = " + sparqlClient.selectSuperclasses("http://id.nlm.nih.gov/mesh/vocab#Descriptor", "es", "en"));
    	//System.out.println("Subclasses = " + sparqlClient.selectSubclassesEncoded("", "es", "en"));
    	
    	//ConceptForGenericRDF term = new ConceptForGenericRDF("https://id.nlm.nih.gov/mesh/sparql");
    	//term.Identifier("http://id.nlm.nih.gov/mesh/2015/M0585109");
    	//term.LoadResource();
    	//System.out.println("Label = " + term.Label());
    	//System.out.println("Registry number = " + term.RevisionId());
    	//System.out.println("Linked concept = " + term.RetrieveLinkedConcepts("http://id.nlm.nih.gov/mesh/vocab#term"));
    	//System.out.println("Properties = " + term.AvailableProperties());
    	//System.out.println("RetrieveStringValue = " + term.RetrieveStringValue("meshv:prefLabel"));
    	
    	//SPARQLClientForWikiData sparqlClientWiki = SPARQLClientForWikiData.getInstance();
    	//System.out.println("Instances with label (wiki) = " + sparqlClientWiki.selectInstancesByLabel("Tatiana", "es", "en", 10, 0)); 
    	
    	/*String prefixesWiki = "PREFIX dbpo: <http://dbpedia.org/ontology/>" + "\n" + 
    		"PREFIX owl: <http://www.w3.org/2002/07/owl#>" + "\n" +
    		"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "\n" +
    		"PREFIX dbpr: <http://dbpedia.org/resource/>" + "\n" +
    		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" + "\n";*/
    	
    	/*JenaSparqlClient sparqlClientWiki = JenaSparqlClient.getInstance("http://dbpedia.org/sparql", prefixesWiki);
    	sparqlClientWiki.selectSuperclasses("", "", "");*/
    }
}