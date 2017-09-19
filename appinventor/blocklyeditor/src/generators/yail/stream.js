'use strict';

goog.provide('Blockly.Yail.stream');

Blockly.Yail['filter'] = function() {
	  // filter function
	  var codeForList = Blockly.Yail.valueToCode(this, 'LIST', Blockly.Yail.ORDER_NONE) || null;
	  var codeForBody = Blockly.Yail.valueToCode(this, 'TEST', Blockly.Yail.ORDER_NONE) || null;
	  var nameForIterator = Blockly.Yail.YAIL_LOCAL_VAR_TAG + this.getFieldValue('VAR'); 
	  var code = Blockly.Yail.YAIL_FILTER +
      nameForIterator + Blockly.Yail.YAIL_SPACER + codeForBody +
              Blockly.Yail.YAIL_SPACER
          + codeForList + Blockly.Yail.YAIL_CLOSE_COMBINATION;
	  return [ code, Blockly.Yail.ORDER_ATOMIC ];
};

Blockly.Yail['map'] = function() {
	  // map function
	  var codeForList = Blockly.Yail.valueToCode(this, 'LIST', Blockly.Yail.ORDER_NONE) || null;
	  var codeForBody = Blockly.Yail.valueToCode(this, 'TO', Blockly.Yail.ORDER_NONE) || null;
	  var nameForIterator = Blockly.Yail.YAIL_LOCAL_VAR_TAG + this.getFieldValue('VAR'); 
	  var code = Blockly.Yail.YAIL_MAP +
    nameForIterator + Blockly.Yail.YAIL_SPACER + codeForBody +
            Blockly.Yail.YAIL_SPACER
        + codeForList + Blockly.Yail.YAIL_CLOSE_COMBINATION;
	  return [ code, Blockly.Yail.ORDER_ATOMIC ];
};

Blockly.Yail['reduce'] = function() {
	// reduce function
	var emptyCodeForList = Blockly.Yail.YAIL_CALL_YAIL_PRIMITIVE + "make-yail-list" +
	       Blockly.Yail.YAIL_SPACER;
	   emptyCodeForList += Blockly.Yail.YAIL_OPEN_COMBINATION + Blockly.Yail.YAIL_LIST_CONSTRUCTOR
	       + Blockly.Yail.YAIL_SPACER;
	   emptyCodeForList += Blockly.Yail.YAIL_CLOSE_COMBINATION + Blockly.Yail.YAIL_SPACER +
	       Blockly.Yail.YAIL_QUOTE + Blockly.Yail.YAIL_OPEN_COMBINATION;
	   emptyCodeForList += Blockly.Yail.YAIL_CLOSE_COMBINATION;
	   emptyCodeForList += Blockly.Yail.YAIL_SPACER + Blockly.Yail.YAIL_DOUBLE_QUOTE + "make a list" 
	   + Blockly.Yail.YAIL_DOUBLE_QUOTE + Blockly.Yail.YAIL_CLOSE_COMBINATION; 
	var nameForIterator1 = Blockly.Yail.YAIL_LOCAL_VAR_TAG + this.getFieldValue('VAR1'); 
	var nameForIterator2 = Blockly.Yail.YAIL_LOCAL_VAR_TAG + this.getFieldValue('VAR2'); 
	var codeForList = Blockly.Yail.valueToCode(this, 'LIST', Blockly.Yail.ORDER_NONE) || emptyCodeForList;
	var codeForBody = Blockly.Yail.valueToCode(this, 'COMBINE', Blockly.Yail.ORDER_NONE) || Blockly.Yail.YAIL_FALSE;
	   var code = Blockly.Yail.YAIL_REDUCE + 
	       nameForIterator2 + Blockly.Yail.YAIL_SPACER
	           + nameForIterator1 + Blockly.Yail.YAIL_SPACER + codeForBody +
	               Blockly.Yail.YAIL_SPACER
	           + codeForList + Blockly.Yail.YAIL_CLOSE_COMBINATION;
	    return [ code, Blockly.Yail.ORDER_ATOMIC ];
};

Blockly.Yail['reduceWithFunctions'] = function() {
	// reduceWithFunctions function
	var emptyCodeForList = Blockly.Yail.YAIL_CALL_YAIL_PRIMITIVE + "make-yail-list" +
	       Blockly.Yail.YAIL_SPACER;
	   emptyCodeForList += Blockly.Yail.YAIL_OPEN_COMBINATION + Blockly.Yail.YAIL_LIST_CONSTRUCTOR
	       + Blockly.Yail.YAIL_SPACER;
	   emptyCodeForList += Blockly.Yail.YAIL_CLOSE_COMBINATION + Blockly.Yail.YAIL_SPACER +
	       Blockly.Yail.YAIL_QUOTE + Blockly.Yail.YAIL_OPEN_COMBINATION;
	   emptyCodeForList += Blockly.Yail.YAIL_CLOSE_COMBINATION;
	   emptyCodeForList += Blockly.Yail.YAIL_SPACER + Blockly.Yail.YAIL_DOUBLE_QUOTE + "make a list" 
	   + Blockly.Yail.YAIL_DOUBLE_QUOTE + Blockly.Yail.YAIL_CLOSE_COMBINATION;
	   var nameFunction = this.getFieldValue('FUNCTION');
	   var codeForList = Blockly.Yail.valueToCode(this, 'LIST', Blockly.Yail.ORDER_NONE) || emptyCodeForList;
	   if(nameFunction == "COUNT") {
		   var code = Blockly.Yail.YAIL_COUNT + codeForList + Blockly.Yail.YAIL_CLOSE_COMBINATION;
	   } else if(nameFunction == "SUM") {
		   var code = Blockly.Yail.YAIL_SUMMATION + codeForList + Blockly.Yail.YAIL_CLOSE_COMBINATION;
	   } else if(nameFunction == "PRODUCT") {
		   var code = Blockly.Yail.YAIL_PRODUCTION + codeForList + Blockly.Yail.YAIL_CLOSE_COMBINATION;
	   } else if(nameFunction == "MAX") {
		   var code = Blockly.Yail.YAIL_MAXIMUM + codeForList + Blockly.Yail.YAIL_CLOSE_COMBINATION;
	   } else if(nameFunction == "MIN") {
		   var code = Blockly.Yail.YAIL_MINIMUM + codeForList + Blockly.Yail.YAIL_CLOSE_COMBINATION;
	   } else if(nameFunction == "AVERAGE") {
		   var code = Blockly.Yail.YAIL_AVERAGE + codeForList + Blockly.Yail.YAIL_CLOSE_COMBINATION;
	   } else if(nameFunction == "STANDARD_DEVIATION") {
		   var code = Blockly.Yail.YAIL_STANDARD_DEVIATION + codeForList + Blockly.Yail.YAIL_CLOSE_COMBINATION;
	   } else if(nameFunction == "VARIANCE") {
		   var code = Blockly.Yail.YAIL_VARIANCE + codeForList + Blockly.Yail.YAIL_CLOSE_COMBINATION;
	   }
	    return [ code, Blockly.Yail.ORDER_ATOMIC ];
};

Blockly.Yail['sort'] = function() {
	// sort function
	var emptyCodeForList = Blockly.Yail.YAIL_CALL_YAIL_PRIMITIVE + "make-yail-list" +
	       Blockly.Yail.YAIL_SPACER;
	   emptyCodeForList += Blockly.Yail.YAIL_OPEN_COMBINATION + Blockly.Yail.YAIL_LIST_CONSTRUCTOR
	       + Blockly.Yail.YAIL_SPACER;
	   emptyCodeForList += Blockly.Yail.YAIL_CLOSE_COMBINATION + Blockly.Yail.YAIL_SPACER +
	       Blockly.Yail.YAIL_QUOTE + Blockly.Yail.YAIL_OPEN_COMBINATION;
	   emptyCodeForList += Blockly.Yail.YAIL_CLOSE_COMBINATION;
	   emptyCodeForList += Blockly.Yail.YAIL_SPACER + Blockly.Yail.YAIL_DOUBLE_QUOTE + "make a list" 
	   + Blockly.Yail.YAIL_DOUBLE_QUOTE + Blockly.Yail.YAIL_CLOSE_COMBINATION; 
	var nameForIterator1 = Blockly.Yail.YAIL_LOCAL_VAR_TAG + this.getFieldValue('VAR1'); 
	var nameForIterator2 = Blockly.Yail.YAIL_LOCAL_VAR_TAG + this.getFieldValue('VAR2'); 
	var codeForList = Blockly.Yail.valueToCode(this, 'LIST', Blockly.Yail.ORDER_NONE) || emptyCodeForList;
	var codeForBody = Blockly.Yail.valueToCode(this, 'COMPARE', Blockly.Yail.ORDER_NONE) || Blockly.Yail.YAIL_FALSE;
	   var code = Blockly.Yail.YAIL_SORT +
	       nameForIterator1 + Blockly.Yail.YAIL_SPACER
	           + nameForIterator2 + Blockly.Yail.YAIL_SPACER + codeForBody +
	               Blockly.Yail.YAIL_SPACER
	           + codeForList + Blockly.Yail.YAIL_CLOSE_COMBINATION;
	    return [ code, Blockly.Yail.ORDER_ATOMIC ];
};


Blockly.Yail['sortWithFunctions'] = function() {
	// sortWithFunctions function
	var emptyCodeForList = Blockly.Yail.YAIL_CALL_YAIL_PRIMITIVE + "make-yail-list" +
	       Blockly.Yail.YAIL_SPACER;
	   emptyCodeForList += Blockly.Yail.YAIL_OPEN_COMBINATION + Blockly.Yail.YAIL_LIST_CONSTRUCTOR
	       + Blockly.Yail.YAIL_SPACER;
	   emptyCodeForList += Blockly.Yail.YAIL_CLOSE_COMBINATION + Blockly.Yail.YAIL_SPACER +
	       Blockly.Yail.YAIL_QUOTE + Blockly.Yail.YAIL_OPEN_COMBINATION;
	   emptyCodeForList += Blockly.Yail.YAIL_CLOSE_COMBINATION;
	   emptyCodeForList += Blockly.Yail.YAIL_SPACER + Blockly.Yail.YAIL_DOUBLE_QUOTE + "make a list" 
	   + Blockly.Yail.YAIL_DOUBLE_QUOTE + Blockly.Yail.YAIL_CLOSE_COMBINATION;
	   var nameFunction = this.getFieldValue('FUNCTION');
	   var codeForList = Blockly.Yail.valueToCode(this, 'LIST', Blockly.Yail.ORDER_NONE) || emptyCodeForList;
	   if(nameFunction == "ASC") {
		   var code = Blockly.Yail.YAIL_SORT_ASC + codeForList + Blockly.Yail.YAIL_CLOSE_COMBINATION;
	   } else if(nameFunction == "DESC") {
		   var code = Blockly.Yail.YAIL_SORT_DESC + codeForList + Blockly.Yail.YAIL_CLOSE_COMBINATION;
	   }
	    return [ code, Blockly.Yail.ORDER_ATOMIC ];
};
	
Blockly.Yail['limit'] = function() {
	  // limit function
	  var codeForList = Blockly.Yail.valueToCode(this, 'LIST', Blockly.Yail.ORDER_NONE) || null;
	  var codeForBody = Blockly.Yail.valueToCode(this, 'VALUE', Blockly.Yail.ORDER_NONE) || null;
	  var code = Blockly.Yail.YAIL_LIMIT + codeForBody +
          Blockly.Yail.YAIL_SPACER
      + codeForList + Blockly.Yail.YAIL_CLOSE_COMBINATION;
	  return [ code, Blockly.Yail.ORDER_ATOMIC ];
};