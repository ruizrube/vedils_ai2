package com.google.appinventor.client.editor.youngandroid.properties;

import com.google.appinventor.client.editor.simple.SimpleComponentDatabase;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Property editor for selecting methods name for ActivityTracker.
 * By SPI-FM.
 */
public class YoungAndroidCheckableTreeSelectorForActivityTracker extends YoungAndroidCheckableTreeSelector {

	public YoungAndroidCheckableTreeSelectorForActivityTracker(final SimpleComponentDatabase COMPONENT_DATABASE, final String componentType) {
		ScrollPanel selectorPanel = new ScrollPanel();
		selectorPanel.setSize("190px", "290px");
		
		tree = new CheckableTree();
		tree.setWidth("60px");
		TreeItem all = new CheckableTreeItem("all");
		all.setHTML("<b> all </b>");

		makeTreeForComponentType(COMPONENT_DATABASE, componentType, all);

		tree.addItem(all);
		selectorPanel.add(tree);

		this.recordedItems = "";
		initAdditionalChoicePanel(selectorPanel);
	}
}
