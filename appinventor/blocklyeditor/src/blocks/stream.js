/**
 * @license
 * @fileoverview Stream blocks for VEDILS
 * @author SPI-FM
 */

'use strict';

goog.provide('Blockly.Blocks.stream');

goog.require('Blockly.Blocks.Utilities');

Blockly.Blocks['filter'] = {
		  category: "Stream",
		  helpUrl: 'Block filter',
		  init: function() {
		    this.setColour(Blockly.STREAM_CATEGORY_HUE);
		    this.appendValueInput('LIST')
		    .setCheck(Blockly.Blocks.Utilities.YailTypeToBlocklyType("list",Blockly.Blocks.Utilities.INPUT)).appendField('filter stream', 'TITLE') .setAlign(Blockly.ALIGN_RIGHT);
		    this.appendDummyInput('DESCRIPTION').appendField('for each').appendField(new
		          Blockly.FieldParameterFlydown('item', true, Blockly.FieldFlydown.DISPLAY_BELOW),
		    	  'VAR').appendField('check condition').setAlign(Blockly.ALIGN_RIGHT);
		    this.appendIndentedValueInput('TEST');
		    this.setOutput(true);
		    this.setPreviousStatement(false);
		    this.setNextStatement(false);
		    this.setTooltip('filter');
		    },
		    typeblock: [{ translatedName: 'filterBlock' }]
};

Blockly.Blocks['map'] = {
		category : "Stream",
		helpUrl : "Block map", 
		init : function() {
		this.setColour(Blockly.STREAM_CATEGORY_HUE);
		this.appendValueInput('LIST')
		.setCheck(Blockly.Blocks.Utilities.YailTypeToBlocklyType("list",Blockly.Blocks.Utilities.INPUT)).appendField('map stream', 'TITLE')
		.setAlign(Blockly.ALIGN_RIGHT);
		this.appendDummyInput('DESCRIPTION')
		.appendField('for each')
		.appendField(new Blockly.FieldParameterFlydown("item",
		                                                 true, // name is editable
		                                                 Blockly.FieldFlydown.DISPLAY_BELOW),
		'VAR').appendField('apply mapping').setAlign(Blockly.ALIGN_RIGHT);
		this.appendIndentedValueInput('TO');
		this.setOutput(true);
		this.setPreviousStatement(false); 
		this.setNextStatement(false);  
		this.setTooltip('map');
		},
		typeblock: [{ translatedName: 'mapBlock' }]
};

Blockly.Blocks['reduce'] = {
		category : "Stream",
		helpUrl : "Block reduce",
		init : function() {
			this.setColour(Blockly.STREAM_CATEGORY_HUE); 
			this.appendValueInput('LIST')
			       .setCheck(Blockly.Blocks.Utilities.YailTypeToBlocklyType("list",Blockly.Blocks.Utilities.INPUT))
			       .appendField("reduce stream")
			       .setAlign(Blockly.ALIGN_RIGHT);
			this.appendDummyInput('DESCRIPTION').appendField("for each").appendField(new
			           Blockly.FieldParameterFlydown("item",
			                                                 true, // name is editable
			                                                 Blockly.FieldFlydown.DISPLAY_BELOW),
			'VAR1') .appendField("merging with") .appendField(new
			           Blockly.FieldParameterFlydown("item2",
			                                                 true, // name is editable
			                                                 Blockly.FieldFlydown.DISPLAY_BELOW),
			'VAR2') .setAlign(Blockly.ALIGN_RIGHT);
			this.appendIndentedValueInput('COMBINE'); 
			this.setOutput(true);
			this.setPreviousStatement(false); 
			this.setNextStatement(false);
			this.setTooltip("reduce");
		},
		getVars: function() {
			var names = []
			names.push(this.getFieldValue('VAR1'));
			names.push(this.getFieldValue('VAR2'));
			return names;
		},
		blocksInScope: function() {
			var combineBlock = this.getInputTargetBlock('COMBINE');
			if(combineBlock) {
				return [combineBlock];
			} else {
				return [];
			}
		},
		declaredNames: function() {
			return this.getVars();
		},
		renameVar: function(oldName, newName) {
			if(Blockly.Names.equals(oldName, this.getFieldValue('VAR1'))) {
				this.setFieldValue(newName, 'VAR1');
			}
			if(Blockly.Names.equals(oldName, this.getFieldValue('VAR2'))) {
				this.setFieldValue(newName, 'VAR2');
			}
		},
		typeblock: [{ translatedName: Blockly.Msg.LANG_LISTS_REDUCE_TITLE_REDUCE }]
};

Blockly.Blocks['reduceWithFunctions'] = {
		category : "Stream",
		helpUrl : "Block reduce",
		init : function() {
			this.setColour(Blockly.STREAM_CATEGORY_HUE); 
			var options = [['Maximum', 'MAX'], ['Minimum', 'MIN'], ['Count', 'COUNT'], ['Sum', 'SUM'], ['Product', 'PRODUCT'], ['Average', 'AVERAGE'], ['Standard deviation', 'STANDARD_DEVIATION'], ['Variance', 'VARIANCE']];
			this.appendValueInput('LIST')
			       .setCheck(Blockly.Blocks.Utilities.YailTypeToBlocklyType("list",Blockly.Blocks.Utilities.INPUT))
			       .appendField("reduce")
			       .appendField(new Blockly.FieldDropdown(options), 'FUNCTION')
			       .appendField('operation')
			       .appendField('stream')
			       .setAlign(Blockly.ALIGN_RIGHT); 
			this.setOutput(true);
			this.setPreviousStatement(false); 
			this.setNextStatement(false);
			this.setTooltip("reduceWithFunctions");
		},
		typeblock: [{ translatedName: "reduceWithFunctionsBlock" }]
};

Blockly.Blocks['sort'] = {
		category : "Stream",
		helpUrl : "Block sort-comparator", 
		init : function() {
			this.setColour(Blockly.STREAM_CATEGORY_HUE); 
			this.appendValueInput('LIST')
			.setCheck(Blockly.Blocks.Utilities.YailTypeToBlocklyType("list",Blockly.Blocks.Utilities.INPUT))
			.appendField("sort stream", 'TITLE').setAlign(Blockly.ALIGN_RIGHT);
			this.appendDummyInput('DESCRIPTION').appendField('comparing') 
			.appendField(new Blockly.FieldParameterFlydown('item1',
                                                 true,
                                                 Blockly.FieldFlydown.DISPLAY_BELOW),
			'VAR1').appendField('and')
			.appendField(new Blockly.FieldParameterFlydown('item2',
                                                 true,
                                                 Blockly.FieldFlydown.DISPLAY_BELOW),
			'VAR2').appendField('with').setAlign(Blockly.ALIGN_RIGHT);
			this.appendIndentedValueInput('COMPARE');
			this.setOutput(true);
			this.setPreviousStatement(false);
			this.setNextStatement(false); 
			this.setTooltip('sort'); 
		},
		typeblock: [{ translatedName: 'sortBlock' }]
};

Blockly.Blocks['sortWithFunctions'] = {
		category : "Stream",
		helpUrl : "Block sort-comparator", 
		init : function() {
			this.setColour(Blockly.STREAM_CATEGORY_HUE);
			var options = [['Ascending order', 'ASC'], ['Descending order', 'DESC']];
			this.appendValueInput('LIST')
			.setCheck(Blockly.Blocks.Utilities.YailTypeToBlocklyType("list",Blockly.Blocks.Utilities.INPUT))
			.appendField("sort", 'TITLE')
			.appendField(new Blockly.FieldDropdown(options), 'FUNCTION')
		    .appendField('stream')
		    .setAlign(Blockly.ALIGN_RIGHT);
			this.setOutput(true);
			this.setPreviousStatement(false);
			this.setNextStatement(false); 
			this.setTooltip('sort');
		},
		typeblock: [{ translatedName: 'sortBlock' }]
};

Blockly.Blocks['limit'] = {
		  category: "Stream",
		  helpUrl: Blockly.Msg.LANG_COLOUR_PICKER_HELPURL,
		  init: function() {
		    this.setColour(Blockly.STREAM_CATEGORY_HUE);
		    this.appendValueInput('LIST')
		       .setCheck(Blockly.Blocks.Utilities.YailTypeToBlocklyType("list",Blockly.Blocks.Utilities.INPUT))
		       .appendField("limit stream")
		       .setAlign(Blockly.ALIGN_RIGHT);
		    this.appendValueInput("VALUE").setCheck(null).appendField('value').setAlign(Blockly.ALIGN_RIGHT);
		    this.setTooltip('limit');
		    this.setOutput(true);
		    this.setPreviousStatement(false);
		  },
		  typeblock: [{ translatedName: 'limitBlock' }]
};