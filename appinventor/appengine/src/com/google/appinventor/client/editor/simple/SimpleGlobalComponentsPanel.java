package com.google.appinventor.client.editor.simple;

import static com.google.appinventor.client.Ode.MESSAGES;

import java.util.ArrayList;
import java.util.List;

import com.google.appinventor.client.editor.simple.components.MockComponent;
import com.google.appinventor.client.editor.simple.components.MockForm;
import com.google.appinventor.client.editor.simple.palette.SimplePaletteItem;
import com.google.appinventor.client.widgets.dnd.DragSource;
import com.google.appinventor.client.widgets.dnd.DropTarget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

//SPI&FM global components
public final class SimpleGlobalComponentsPanel extends Composite implements DropTarget {
	
	// UI elements
	//private final Label heading;
	//private final FlowPanel componentsPanel;
	//private static List<MockComponent> globalComponents;
	
	// Backing mocked Simple form component
	private MockForm form;
	
	public SimpleGlobalComponentsPanel() {
		// Initialize UI
		 
	    VerticalPanel panel = new VerticalPanel();
	    panel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
	    //panel.setStyleName("ya-globalNonVisibleComponentsPanel");
	    
	    //heading = new Label("");
	    //heading.setStyleName("ya-NonVisibleComponentsHeader");
	    //heading.setText(MESSAGES.globalComponentsHeader());
	    //panel.add(heading);
	    
	    /*componentsPanel = new FlowPanel();
	    componentsPanel.setStyleName("ode-SimpleUiDesignerNonVisibleComponents");
	    
	    if(globalComponents == null) {
	    	globalComponents = new ArrayList<MockComponent>();
	    } else {
	    	for(MockComponent component: globalComponents) {
	    		componentsPanel.add(component);
	    		form.addComponent(component);
	    		addComponent(component);
	    	}
	    }*/
	    
	    //panel.add(componentsPanel);
	    
	    initWidget(panel);
	}
	
	/**
	* Associates a Simple form component with this panel.
	*
	* @param form  backing mocked form component
	*/
	public void setForm(MockForm form) {
		this.form = form;
	}
	
	/**
	* Adds a new global component to this panel. Note that this method
	* will not add the component to the form component! This needs to be done
	* separately.
	*
	* @param component  Simple mock component to be added
	*/
	public void addComponent(MockComponent component) {
	    //componentsPanel.add(component);
	}
	
	/**
	* Removes a new global component from this panel. Note that this method
	* will not remove the component from the form component! This needs to be
	* done separately.
	*
	* @param component  Simple mock component to be removed
	*/
	public void removeComponent(MockComponent component) {
	    //componentsPanel.remove(component);
	}
	
	// DropTarget implementation
	
	@Override
	public Widget getDropTargetWidget() {
		return this;
	}

	@Override
	public boolean onDragEnter(DragSource source, int x, int y) {
		// Accept palette items corresponding to non-visible components only
	    return (source instanceof SimplePaletteItem) &&
	        !((SimplePaletteItem) source).isVisibleComponent();
	}

	@Override
	public void onDragContinue(DragSource source, int x, int y) {
		// no action
	}

	@Override
	public void onDragLeave(DragSource source) {
		// no action
		
	}

	@Override
	public void onDrop(DragSource source, int x, int y, int offsetX, int offsetY) {
		MockComponent sourceComponent = ((SimplePaletteItem) source).createMockComponent();

	    // Add component to the form
	    form.addComponent(sourceComponent);

	    // Add component to this panel
	    addComponent(sourceComponent);
	    sourceComponent.select();
	}
}
