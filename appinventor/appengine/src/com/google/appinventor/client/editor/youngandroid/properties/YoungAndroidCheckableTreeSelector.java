// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.client.editor.youngandroid.properties;

import com.google.appinventor.client.widgets.properties.AdditionalChoicePropertyEditor;
import com.google.gwt.user.client.Window;

/**
 * Abstract Property editor to help subclasses.
 * By SPI-FM.
 */
public abstract class YoungAndroidCheckableTreeSelector extends AdditionalChoicePropertyEditor {

	protected String recordedItems;
	protected CheckableTree tree;

	@Override
	protected void openAdditionalChoiceDialog() {
		super.openAdditionalChoiceDialog();
		tree.setFocus(true);
	}

	@Override
	protected String getPropertyValueSummary() {
		recoverSelectedItems(property.getValue());
		return property.getValue();
	}

	protected void recordSelectedItems() {
		if (!this.recordedItems.equals("")) {
			this.recordedItems = "";
		}

		for (int i = 0; i < tree.getItem(0).getChildCount(); i++) // Child nodes of "All".
		{
			CheckableTreeItem itemL1 = (CheckableTreeItem) tree.getItem(0).getChild(i);

			if (itemL1.getStatus() == CheckableTreeItem.SELECTION_ALL
					|| itemL1.getStatus() == CheckableTreeItem.SELECTION_SOME) {

				// Search the child nodes
				for (int j = 0; j < itemL1.getChildCount(); j++) {

					CheckableTreeItem itemL2 = (CheckableTreeItem) itemL1.getChild(j);

					if (itemL2.getChildCount() == 0) {

						if (itemL2.getStatus() == CheckableTreeItem.SELECTION_ALL || itemL2.getStatus() == CheckableTreeItem.SELECTION_SOME) {
							this.recordedItems = this.recordedItems + itemL2.getUserObject() + " - ";
						}

					} else {
						// Search the child nodes
						for (int k = 0; k < itemL2.getChildCount(); k++) {

							CheckableTreeItem itemL3 = (CheckableTreeItem) itemL2.getChild(k);
							if (itemL3.getStatus() == CheckableTreeItem.SELECTION_ALL) { // Are leaf
								this.recordedItems = this.recordedItems + itemL3.getUserObject() + " - ";
							}
						}
					}
				}
			}
		}
	}
	
	protected void recoverSelectedItems(String selectedItems) {
		
		CheckableTreeItem all = (CheckableTreeItem) tree.getItem(0);
		int childNodesSelectedAll = 0;
		int childNodesSelectedSome = 0;
		
		for (int i = 0; i < all.getChildCount(); i++) // Child nodes of "All". 
		{
			int childNodesSelectedAllL2 = 0;
			int childNodesSelectedSomeL2 = 0;
			CheckableTreeItem item = (CheckableTreeItem) all.getChild(i);

			// Search the child nodes
			for (int j = 0; j < item.getChildCount(); j++) {

				CheckableTreeItem itemL2 = (CheckableTreeItem) item.getChild(j);
				
				if(itemL2.getChildCount() == 0) {
					if (selectedItems.contains(itemL2.getUserObject().toString())) { // Are leaf
						itemL2.setStatus(CheckableTreeItem.SELECTION_ALL);
						childNodesSelectedAllL2++;
					}
				} else {
					int childNodesSelectedL2 = 0;
					
					for (int k = 0; k < itemL2.getChildCount(); k++) {

						CheckableTreeItem itemL3 = (CheckableTreeItem) itemL2.getChild(k);
						if (selectedItems.contains(itemL3.getUserObject().toString())) { // Are leaf
							itemL3.setStatus(CheckableTreeItem.SELECTION_ALL);
							childNodesSelectedL2++;
						}
					}
					
					if(childNodesSelectedL2 == itemL2.getChildCount() && itemL2.getChildCount() != 0) {
						itemL2.setStatus(CheckableTreeItem.SELECTION_ALL);
						childNodesSelectedAllL2++;
					} else if(childNodesSelectedL2 != 0) {
						itemL2.setStatus(CheckableTreeItem.SELECTION_SOME);
						childNodesSelectedSomeL2++;
					}
				}
			}
			
			if(childNodesSelectedAllL2 == item.getChildCount() && item.getChildCount() != 0) {
				item.setStatus(CheckableTreeItem.SELECTION_ALL);
				childNodesSelectedAll++;
			} else if(childNodesSelectedAllL2 != 0 || childNodesSelectedSomeL2 != 0) {
				item.setStatus(CheckableTreeItem.SELECTION_SOME);
				childNodesSelectedSome++;
			}
		}
		
		if(childNodesSelectedAll == all.getChildCount()) {
			all.setStatus(CheckableTreeItem.SELECTION_ALL);
		} else if(childNodesSelectedSome != 0 || childNodesSelectedAll != 0) {
			all.setStatus(CheckableTreeItem.SELECTION_SOME);
		}
	}

	@Override
	protected boolean okAction() {
		// When option "OK" is clicked the elements of tree are recorded.
		recordSelectedItems();
		if (this.recordedItems.equals("")) {
			property.setValue("Nothing");
			Window.alert("Nothing selected.");
			return true;
		}
		property.setValue("Record elements: " + this.recordedItems);
		return true;
	}
}