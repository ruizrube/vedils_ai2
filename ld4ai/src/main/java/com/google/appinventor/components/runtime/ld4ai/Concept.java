/**
 * 
 */
package com.google.appinventor.components.runtime.ld4ai;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.wikidata.wdtk.datamodel.interfaces.Claim;
import org.wikidata.wdtk.datamodel.interfaces.EntityIdValue;
import org.wikidata.wdtk.datamodel.interfaces.GlobeCoordinatesValue;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.MonolingualTextValue;
import org.wikidata.wdtk.datamodel.interfaces.QuantityValue;
import org.wikidata.wdtk.datamodel.interfaces.Snak;
import org.wikidata.wdtk.datamodel.interfaces.SnakGroup;
import org.wikidata.wdtk.datamodel.interfaces.Statement;
import org.wikidata.wdtk.datamodel.interfaces.StatementGroup;
import org.wikidata.wdtk.datamodel.interfaces.StringValue;
import org.wikidata.wdtk.datamodel.interfaces.TimeValue;
import org.wikidata.wdtk.datamodel.interfaces.Value;

/**
 * @author ruizrube
 *
 */
public class Concept {

	private WikiDataClient dataProvider = WikiDataClient.getInstance();

	private String preferredLanguage = "es";

	private String secondLanguage = "en";

	private String identifier;

	private ItemDocument obj;

	public Concept() {
	}

	public Concept(String identifier) {
		this.identifier = identifier;
	}

	public String PreferredLanguage() {
		return preferredLanguage;
	}

	public void PreferredLanguage(String preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}

	public String SecondLanguage() {
		return secondLanguage;
	}

	public void SecondLanguage(String secondLanguage) {
		this.secondLanguage = secondLanguage;
	}

	public String Label() {

		String result = "";
		if (obj != null) {
			result = obj.findLabel(preferredLanguage);
			if (result == null) {
				result = obj.findLabel(secondLanguage);
			}
			if (result == null) {
				result = "";
			}

		}
		return result;
	}

	public long RevisionId() {
		long result = -1;
		if (obj != null) {
			result = obj.getRevisionId();
		}
		return result;
	}

	public String Description() {
		String result = "";
		if (obj != null) {
			result = obj.findDescription(preferredLanguage);
			if (result == null) {
				result = obj.findDescription(secondLanguage);
			}
			if (result == null) {
				result = "";
			}
		}
		return result;
	}

	public void Identifier(String id) {
		this.identifier = id;
	}

	public String Identifier() {
		return identifier;
	}

	public String ImageURL() {
		String result = "";
		List<String> aux = retrieveInstanceStringMultipleProperty(this.obj, "P18", this.preferredLanguage,
				this.secondLanguage);

		if (!aux.isEmpty()) {
			result = aux.get(0);

			result = result.replace(" ", "_");
			String md5 = obtainMD5(result);

			if (!md5.equals("")) {
				result = "https://upload.wikimedia.org/wikipedia/commons/" + md5.charAt(0) + "/" + md5.charAt(0)
						+ md5.charAt(1) + "/" + result;
			}
			// "https://upload.wikimedia.org/wikipedia/commons/a/ab/image_name.ext";

		}

		return result;
	}

	public String Type() {
		String result = "";
		List<String> aux = retrieveInstanceURIMultipleProperty(this.obj, "P31");

		if (aux != null && !aux.isEmpty()) {
			result = aux.get(0);
		}
		return result;
	}

	public List<String> RetrieveTypes() {
		List<String> result = retrieveInstanceURIMultipleProperty(this.obj, "P31");
		return result;

	}

	public String Alias() {
		String result = "";
		List<String> aliases = RetrieveAliases();

		if (aliases.size() > 0) {
			result = aliases.get(0);
		}

		return result;
	}

	public List<String> RetrieveAliases() {
		List<String> result = new ArrayList<String>();
		List<MonolingualTextValue> aux = null;
		if (obj != null) {
			aux = obj.getAliases().get(preferredLanguage);
			if (aux == null) {
				aux = obj.getAliases().get(secondLanguage);
			}
		}
		if (aux != null) {
			for (MonolingualTextValue textValue : aux) {
				result.add(textValue.getText());
			}
		}
		return result;

	}

	public VedilsStatement RetrieveClaim(String property) {

		VedilsStatement result = null;

		Statement[] aux = retrieveStatementMultipleProperty(this.obj, property);

		if (aux.length > 0) {
			result = new VedilsStatement(aux[0]);
		}
		return result;

	}

	public VedilsStatement[] RetrieveClaims(String property) {
		Statement[] aux = retrieveStatementMultipleProperty(this.obj, property);

		List<VedilsStatement> statements = new ArrayList<VedilsStatement>();

		for (Statement statement : aux) {
			statements.add(new VedilsStatement(statement));
		}
		return statements.toArray(new VedilsStatement[statements.size()]);
	}

	public String RetrieveLinkedConcept(String property) {

		// Concept result = null;
		String result = null;
		List<String> aux = retrieveInstanceURIMultipleProperty(this.obj, property);

		if (!aux.isEmpty() && !aux.get(0).equals("")) {
			result = aux.get(0);
		}
		return result;

	}

	public List<String> RetrieveLinkedConcepts(String property) {

		List<String> uris = retrieveInstanceURIMultipleProperty(this.obj, property);

		return uris;

	}

	public List<String> RetrieveStringValues(String property) {
		return retrieveInstanceStringMultipleProperty(this.obj, property, this.preferredLanguage, this.secondLanguage);
	}

	public String RetrieveLabelProperty(String property) {
		return retrieveInstanceLabel(property, this.secondLanguage, this.secondLanguage);

	}

	public String RetrieveStringValue(String property) {

		String result = "";
		List<String> aux = retrieveInstanceStringMultipleProperty(this.obj, property, this.preferredLanguage,
				this.secondLanguage);

		if (!aux.isEmpty()) {
			result = aux.get(0);
		}

		return result;
	}

	public float[] RetrieveNumberValues(String property) {
		return retrieveInstanceNumberMultipleProperty(this.obj, property);
	}

	public float RetrieveNumberValue(String property) {

		float result = -1f;

		float[] aux = retrieveInstanceNumberMultipleProperty(this.obj, property);

		if (aux.length > 0) {
			result = aux[0];
		}

		return result;

	}

	public List<String> AvailableProperties() {
		List<String> result = new ArrayList<String>();

		if (obj != null) {
			for (StatementGroup s : obj.getStatementGroups()) {
				result.add(s.getProperty().getId());
			}
		}
		return result;

	}

	@Override
	public String toString() {
		return "Concept [identifier=" + identifier + "]";
	}

	private String obtainMD5(String plaintext) {
		MessageDigest m;
		String hashtext = "";
		try {
			m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(plaintext.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1, digest);
			hashtext = bigInt.toString(16);
			// Now we need to zero pad it if you actually want the full 32
			// chars.
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hashtext;
	}

	public String ExplainConcept() {
		return "Concept [identifier=" + identifier + "]\n"
				+ retrieveInstanceAllData(identifier, preferredLanguage, secondLanguage);
	}

	public String ClassifyText(String title) {

		String result = "";
		List<ItemDocument> aux = dataProvider.classifyText(title);

		if (aux.size() > 0) {
			result = aux.get(0).getItemId().getId();
		}

		return result;

	}

	public void LoadResource() {

		this.obj = dataProvider.LoadResource(this.identifier);
	}

	public String obtainString(Value value, String preferredLanguage, String secondLanguage) {

		String result = "";
		if (value != null) {
			if ((value instanceof QuantityValue)) {

				result = ((QuantityValue) value).getNumericValue().toString();
			} else if ((value instanceof GlobeCoordinatesValue)) {
				result = ((GlobeCoordinatesValue) value).toString();
			} else if ((value instanceof TimeValue)) {
				result = ((TimeValue) value).toString();
			} else if ((value instanceof StringValue)) {
				result = ((StringValue) value).getString();
			} else if ((value instanceof EntityIdValue)) {
				// result = ((EntityIdValue) value).getIri();
				result = retrieveInstanceLabel(((EntityIdValue) value).getId(), preferredLanguage, secondLanguage);
			} else {
				result = value.toString();
			}

			// DatatypeIdValue, EntityIdValue, GlobeCoordinatesValue,
			// IriIdentifiedValue, ItemIdValue, MonolingualTextValue,
			// PropertyIdValue, QuantityValue, StringValue, TimeValue

		}
		return result;
	}

	private String retrieveInstanceLabel(String entity, String preferredLanguage, String secondLanguage) {
		String result = "";
		MonolingualTextValue aux;
		Map<String, MonolingualTextValue> labels = dataProvider.obtainLabelsDocument(entity);
		if (labels != null) {
			aux = labels.get(preferredLanguage);
			if (aux != null) {
				result = aux.getText();
			} else {
				aux = labels.get(secondLanguage);
				if (aux != null) {
					result = aux.getText();
				}
			}
		}
		return result;
	}

	private List<String> retrieveInstanceStringMultipleProperty(ItemDocument doc, String property,
			String preferredLanguage, String secondLanguage) {

		List<String> result = new ArrayList<String>();
		if (doc != null) {

			StatementGroup group = doc.findStatementGroup(property);
			if (group != null) {
				for (Statement statement : group) {
					result.add(obtainString(statement.getValue(), preferredLanguage, secondLanguage));
				}
			}

		}

		return result;

	}

	private List<String> retrieveInstanceURIMultipleProperty(ItemDocument doc, String property) {

		List<String> result = new ArrayList<String>();
		if (doc != null) {
			StatementGroup group = doc.findStatementGroup(property);
			if (group != null) {
				for (Statement statement : group) {
					Value value = statement.getValue();

					if (value != null && value instanceof EntityIdValue) {
						result.add(((EntityIdValue) value).getId());
					}

				}
			}

		}

		return result;

	}

	private float[] retrieveInstanceNumberMultipleProperty(ItemDocument doc, String property) {

		List<Float> result = new ArrayList<Float>();
		if (doc != null) {

			StatementGroup group = doc.findStatementGroup(property);
			if (group != null) {
				for (Statement statement : group) {

					if ((statement.getValue() instanceof QuantityValue)) {
						result.add(new Float(((QuantityValue) statement.getValue()).getNumericValue().floatValue()));
					} else {
						result.add(new Float(-1));
					}
				}
			}

		}
		return ArrayUtils.toPrimitive(result.toArray(new Float[0]), 0.0F);

	}

	private Statement[] retrieveStatementMultipleProperty(ItemDocument doc, String property) {
		List<Statement> result = new ArrayList<Statement>();
		if (doc != null) {
			StatementGroup group = doc.findStatementGroup(property);
			if (group != null) {
				for (Statement statement : group) {
					result.add(statement);
				}
			}
		}

		return result.toArray(new Statement[result.size()]);
	}

	private String retrieveInstanceAllData(String entity, String preferredLanguage, String secondLanguage) {
		String result = "";
		ItemDocument doc = dataProvider.obtainItemDocument(entity);

		if (doc != null) {

			for (StatementGroup group : doc.getStatementGroups())
				result += retrievePropertyAllData(entity, group.getProperty().getId(), preferredLanguage,
						secondLanguage);

		}

		return result;

	}

	private String retrievePropertyAllData(String entity, String property, String preferredLanguage,
			String secondLanguage) {

		String result = "";
		ItemDocument doc = dataProvider.obtainItemDocument(entity);

		if (doc != null) {
			for (Statement statement : doc.findStatementGroup(property)) {
				result += "\n--- SENTENCIA --- PROPERTY -- " + property;
				result += "\nID " + statement.getStatementId() + " - ";

				Claim myClaim = statement.getClaim();
				if (myClaim != null) {
					result += "\nCLAIM ";
					result += "\nMain Snack: " + myClaim.getMainSnak() + "\n";
					for (SnakGroup group : statement.getClaim().getQualifiers()) {
						result += "\nGroup: " + group.getProperty();
						for (Snak snack : group.getSnaks()) {
							result += " Snack Property: " + snack.getPropertyId() + "Snack Value:" + snack.getValue();
						}
					}
				}
				result += "\nRANK " + statement.getRank() + " - ";
				result += "\nREFS " + statement.getReferences() + " - ";
				result += "\nVALUE " + statement.getValue() + " - ";
				result += "\n";
			}

		}
		return result;

	}

}
