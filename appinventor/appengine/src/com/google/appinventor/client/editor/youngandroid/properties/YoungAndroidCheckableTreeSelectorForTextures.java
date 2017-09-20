package com.google.appinventor.client.editor.youngandroid.properties;

import java.util.Arrays;
import java.util.List;

import com.google.appinventor.client.Ode;
import com.google.appinventor.client.editor.youngandroid.YaFormEditor;
import com.google.appinventor.client.explorer.project.Project;
import com.google.appinventor.shared.rpc.project.ProjectNode;
import com.google.appinventor.shared.rpc.project.youngandroid.YoungAndroidAssetsFolder;
import com.google.appinventor.shared.rpc.project.youngandroid.YoungAndroidProjectNode;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TreeItem;

public class YoungAndroidCheckableTreeSelectorForTextures extends YoungAndroidCheckableTreeSelector {
	

	//private String[] fileTypes;
	//private YoungAndroidAssetsFolder assetsFolder;
	//private final ListWithNone choices;
	// UI elements
		//private final ListBox assetsList;

		//private final ListWithNone choices;

		private final YoungAndroidAssetsFolder assetsFolder;

		// Lista de archivos que soporta el componente
		private final String[] fileTypes;
	

	/*private void addChoiceToList(ProjectNode node) {
		if (fileTypes != null) {
			boolean found = false;
			int i = 0;
			while (!found && i < fileTypes.length) {
				if (node.getName().toLowerCase().endsWith(fileTypes[i])) {
					choices.addItem(node.getName());
					found = true;
				} else
					i++;
			}
		} else
			choices.addItem(node.getName());
	}*/
	public YoungAndroidCheckableTreeSelectorForTextures(final YaFormEditor editor, final String[] fileTypes) {
		ScrollPanel selectorPanel = new ScrollPanel();
		selectorPanel.setSize("190px", "290px");

		tree = new CheckableTree();
		tree.setWidth("60px");
		TreeItem all = new CheckableTreeItem("all");
		all.setHTML("<b> all </b>");
		this.fileTypes = fileTypes;
		Project project = Ode.getInstance().getProjectManager().getProject(editor.getProjectId());
		assetsFolder = ((YoungAndroidProjectNode) project.getRootNode()).getAssetsFolder();

		TreeItem userInfoTree = new CheckableTreeItem("Texture List");
		userInfoTree.setHTML("<b> Texture List</b>");
		userInfoTree.setState(true, false);
		
		if (assetsFolder != null) {
			for (ProjectNode node : assetsFolder.getChildren()) {
				for(int i=0;i<fileTypes.length;i++)
				{
					if (node.getName().toLowerCase().endsWith(fileTypes[i])) {
						TreeItem item = new CheckableTreeItem(node.getName());
						item.setHTML(node.getName());
						item.setTitle(node.getName());
						item.setUserObject(node.getName());			
						userInfoTree.addItem(item);
						}
				}
				
				//addChoiceToList(node);
				// choices.addItem(node.getName());
			}
		}
		all.addItem(userInfoTree);
		
		tree.addItem(all);
		selectorPanel.add(tree);

		this.recordedItems = "";
		initAdditionalChoicePanel(selectorPanel);


		//// Interaction Data

//		TreeItem interactionDataTree = new CheckableTreeItem("Interaction Data");
//		interactionDataTree.setHTML("<b> Interaction Data </b>");
//		interactionDataTree.setState(true, false);
//
//		for (String componentType : COMPONENT_DATABASE.getComponentNames()) {
//
//			TreeItem item = new CheckableTreeItem(componentType);
//			item.setHTML(componentType);
//			item.setTitle(componentType);
//			makeTreeForComponentType(COMPONENT_DATABASE, componentType, item);
//
//			interactionDataTree.addItem(item);
//
//		}
		//		all.addItem(interactionDataTree);
	}
}
