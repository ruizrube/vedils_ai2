package com.google.appinventor.components.runtime.ld4ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.wikidata.wdtk.datamodel.interfaces.Snak;
import org.wikidata.wdtk.datamodel.interfaces.SnakGroup;
import org.wikidata.wdtk.datamodel.interfaces.Statement;
import org.wikidata.wdtk.datamodel.interfaces.StatementRank;
import org.wikidata.wdtk.datamodel.interfaces.Value;

public class VedilsStatement {

	private WikiDataClient dataProvider=WikiDataClient.getInstance();
	
	private Statement statement;

	Map<String,List<Value>> qualifiers=new HashMap<String,List<Value>>();
	
	public VedilsStatement() {
		
	}

	public VedilsStatement(Statement statement) {
		this.statement=statement;
		List<Value> aux;
		for (SnakGroup group : statement.getClaim().getQualifiers()) {
			if(qualifiers.containsKey(group.getProperty().getId())){
				aux=qualifiers.get(group.getProperty().getId());
			} else {
				aux=new ArrayList<Value>();
				
			}
		
			for(Snak snack:group.getSnaks()){
				aux.add(snack.getValue());
			}
		
			qualifiers.put(group.getProperty().getId(),aux);
			
		}

	}

	
	public String getIdentifier() {
		return statement.getStatementId();
	}
	
	
	public StatementRank getRank() {
		return statement.getRank();
	}

	public String getSubject() {
		return statement.getClaim().getSubject().getId();
	}

	public String getProperty() {
		return statement.getClaim().getMainSnak().getPropertyId().getId();
	}

	public String getValue() {
		return  statement.getClaim().getMainSnak().getValue().toString();
	}

	public List<String> getQualifiers() {
		
		List<String> result=new ArrayList<String>();
		
		result.addAll(qualifiers.keySet());
		
		return result;
	}
		
//	public String getQualifierStringValue(String qualifier) {
//		
//		String result=null;
//		List<Value> aux = qualifiers.get(qualifier);
//			
//		if(aux.size()>0){
//			result=dataProvider.obtainString(aux.get(0),"en","en");
//					
//		}
//		
//		return result;
//	}
//
//	
//	public List<String> getQualifierStringValues(String qualifier) {
//		
//		List<String>result=new ArrayList<String>();
// 		
//		List<Value> aux = qualifiers.get(qualifier);
//		for(Value value:aux){
//			result.add(dataProvider.obtainString(value,"en","en"));
//		}
//		
//		
//		return result;
//	}
}
