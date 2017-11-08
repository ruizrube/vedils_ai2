/**
 * 
 */
package com.google.appinventor.components.runtime.ld4ai;

import java.util.List;

/**
 * @author ruizrube
 *
 */
public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Only labels in French:
		// wbdf.getFilter().setLanguageFilter(Collections.singleton("fr"));

		// Concept concept = new Concept();
		// String entityId = concept.ClassifyText("Cádiz");
		// concept.Identifier(entityId);
		// concept.LoadResource();
		//
		// System.out.println("The id of the entity is: " + entityId);
		// System.out.println("The current revision of the entity
		// is:"+concept.RevisionId());
		// System.out.println("The label is: " + concept.Label());
		// System.out.println("The description is: " + concept.Description());
		// System.out.println("The image is in: " + concept.ImageURL());
		//
		// System.out.print("The alias are: ");
		// for(String alias:concept.RetrieveAliases()){
		// System.out.print(alias+". ");
		// }
		//
		// System.out.println("\n\nAvailable Properties: ");
		// for(String prop: concept.AvailableProperties()){
		//
		// System.out.println(concept.RetrieveLabelProperty(prop) + " ("+prop+")
		// = " + concept.RetrieveStringValue(prop));
		// System.out.println(",");
		// }

		// System.out.println(concept.ExplainConcept());
		// System.out.println("");
		//
		// System.out.println("\nSTRING SIMPLE PROPERTY: The PostalCode
		// (P281)");
		// obtainInfo(concept, "P281");
		//
		// System.out.println("NUMBER SIMPLE PROPERTY: Area (P2046)");
		// obtainInfo(concept, "P2046");
		//
		// System.out.println("NUMBER MULTIPLE PROPERTY: Population (P1082)");
		// obtainInfo(concept, "P1082");
		//
		// System.out.println("CONCEPT SIMPLE PROPERTY: Country (P17)");
		// obtainInfo(concept, "P17");
		//
		// System.out.println("CONCEPT MULTIPLE PROPERTY: Alcalde (P6)");
		// obtainInfo(concept, "P6");
		//

		SPARQLClient browser = new SPARQLClient();
		//
		System.out.println("\n\nLas clases HIJAS de la Ciudad son: ");
		for (List<String> pair : browser.selectSubclasses("Q515", "es", "en")) {
			System.out.println(pair.get(0) + "-->" + pair.get(1));
		}
		//
		// System.out.println("\n\nLas clases PADRE de la Ciudad son: ");
		// for (String[] pair : browser.selectSuperclasses("Q515","es","en")) {
		// System.out.println(pair[0]+"-->"+pair[1]);
		// }
		//
		// System.out.println("\n\nLas propiedades de la Ciudad son: ");
		// for (String[] pair : browser.selectProperties("Q515","es","en")) {
		// System.out.println(pair[0]+"-->"+pair[1]);
		// }
		//
		// System.out.println("\n\nLas instancias de la Ciudad son: ");
		// for (String[] pair : browser.selectInstances("Q515","es","en")) {
		// System.out.println(pair[0]+"-->"+pair[1]);
		// }

		Main main = new Main();
		main.pinta("Q35120");

		System.out.println("TOTAL: " + main.cont);

	}

	private int cont = 0;

	private void pinta(String cad) {
		List<List<String>> data = SPARQLClient.getInstance().selectSubclasses(cad, "es", "en");
		if (data != null) {
			for (List<String> aux : data) {
				System.out.println(">" + aux.get(1));
				pinta(aux.get(0));
				this.cont++;
			}
		}

	}

	private static void obtainInfo(Concept concept, String property) {
		System.out.println("--> String simple: " + concept.RetrieveStringValue(property));
		for (String s : concept.RetrieveStringValues(property)) {
			System.out.println("--> String multiple: " + s);
		}
		System.out.println("--> Number simple: " + concept.RetrieveNumberValue(property));
		for (float n : concept.RetrieveNumberValues(property)) {
			System.out.println("--> Number multiple: " + n);
		}

		System.out.println("--> Concept simple: " + concept.RetrieveLinkedConcept(property));
		for (String n : concept.RetrieveLinkedConcepts(property)) {
			System.out.println("--> Concept multiple: " + n);
		}

		// System.out.println("--> Claim simple: ");
		// obtainInfoClaim(concept.RetrieveClaim(property));
		//
		// for (VedilsStatement statement : concept.RetrieveClaims(property)) {
		// System.out.println("--> Claim multiple: ");
		// obtainInfoClaim(statement);
		//
		// }

		System.out.println("----------------------------------------\n");

	}

	private static void obtainInfoClaim(VedilsStatement statement) {
		System.out.println("ID: " + statement.getIdentifier());
		System.out.println("SUBJECT: " + statement.getSubject());
		System.out.println("PREDICATE: " + statement.getProperty());
		System.out.println("VALUE: " + statement.getValue());

		for (String qual : statement.getQualifiers()) {
			// System.out.println("Qualifier: " + qual + ". Value: " +
			// statement.getQualifierStringValue(qual));

		}

	}

}
