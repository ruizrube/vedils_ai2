// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.client.editor.youngandroid.properties;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Property editor for selecting methods name for Aggregated Data.
 * By SPI-FM.
 */
public final class YoungAndroidCheckableTreeSelectorForAggregatedData extends YoungAndroidCheckableTreeSelector {

	public YoungAndroidCheckableTreeSelectorForAggregatedData() {
		ScrollPanel selectorPanel = new ScrollPanel();
		selectorPanel.setSize("190px", "290px");

		tree = new CheckableTree();
		tree.setWidth("60px");
		TreeItem all = new CheckableTreeItem("all");
		all.setHTML("<b> all </b>");

		List<String> aggregators = Arrays.asList("Count", "Maximum", "Minimum", "Sum", "Average");
		List<String> dateAggregators = Arrays.asList("Maximum", "Minimum");

		// Activity
		TreeItem activityTree = new CheckableTreeItem("Activity");
		activityTree.setHTML("<b> Activity </b>");
		activityTree.setState(true, false);

		TreeItem item = new CheckableTreeItem("Count");
		item.setHTML("Count");
		item.setUserObject("COUNT()");
		item.setTitle("Count");
		activityTree.addItem(item);
		all.addItem(activityTree);

		// Date
		CheckableTreeItem dateTimeTree = new CheckableTreeItem("Date");
		dateTimeTree.setHTML("<b> Date </b>");
		all.addItem(dateTimeTree);

		for (String aux : dateAggregators) {
			item = new CheckableTreeItem(aux);
			item.setHTML(aux);
			item.setTitle(aux);
			item.setUserObject(aux + "(Date)");
			dateTimeTree.addItem(item);
		}

		all.addItem(dateTimeTree);

		// VAlue argumnts
		TreeItem specificTree = new CheckableTreeItem("Specific Data");
		specificTree.setHTML("<b> Specific Data </b>");
		specificTree.setState(true, false);

		CheckableTreeItem subTree;
		String fieldName = "";

		for (int i = 1; i <= 4; i++) {
			if (i != 4) {
				fieldName = "InputParam" + i;
				subTree = new CheckableTreeItem(fieldName);
				subTree.setHTML("<b> " + fieldName + " </b>");
				subTree.setState(true, false);
			} else {
				fieldName = "OutputParam";
				subTree = new CheckableTreeItem("OutputParam");
				subTree.setHTML("<b> " + fieldName + " </b>");
				subTree.setState(true, false);
			}
			for (String aux : aggregators) {
				item = new CheckableTreeItem(aux);
				item.setHTML(aux);
				item.setTitle(aux);
				item.setUserObject(aux + "(Number" + fieldName + ")");
				subTree.addItem(item);
			}
			specificTree.addItem(subTree);
		}
		all.addItem(specificTree);

		tree.addItem(all);
		selectorPanel.add(tree);

		this.recordedItems = "";

		initAdditionalChoicePanel(selectorPanel);

	}
}