// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.client.widgets.properties;

import static com.google.appinventor.client.Ode.MESSAGES;

import com.google.appinventor.client.editor.simple.SimpleComponentDatabase;
import com.google.appinventor.client.editor.youngandroid.properties.CheckableTreeItem;
import com.google.appinventor.shared.simple.ComponentDatabaseInterface.EventDefinition;
import com.google.appinventor.shared.simple.ComponentDatabaseInterface.MethodDefinition;
import com.google.appinventor.shared.simple.ComponentDatabaseInterface.PropertyDefinition;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Property editor for situations where a property involves additional choices,
 * e.g. when choosing a layout, the choice of layout requires other dependent
 * properties to be set (e.g. table layout - number of rows and columns).
 *
 */
public abstract class AdditionalChoicePropertyEditor extends PropertyEditor {
  // a little padding to keep the panel from hitting the
  // edge of the screen
  private static final int ADDITIONAL_CHOICE_ONSCREEN_PADDING = 10;

  // UI elements
  private final TextBox summary;
  private PopupPanel popup;

  /**
   * Creates a new additional choice dialog.
   */
  protected AdditionalChoicePropertyEditor() {
    summary = new TextBox();
    summary.setReadOnly(true);
    summary.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        openAdditionalChoiceDialog();
      }
    });

    initWidget(summary);
  }

  /**
   * Initializes the additional choice panel.
   *
   * <p>This method must be called from any implementor's constructor.
   *
   * @param panel  panel containing additional choices
   */
  protected void initAdditionalChoicePanel(Panel panel) {
    Button cancelButton = new Button(MESSAGES.cancelButton());
    cancelButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        closeAdditionalChoiceDialog(false);
      }
    });
    Button okButton = new Button(MESSAGES.okButton());
    okButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        closeAdditionalChoiceDialog(true);
      }
    });

    HorizontalPanel buttonPanel = new HorizontalPanel();
    buttonPanel.add(cancelButton);
    buttonPanel.add(okButton);
    buttonPanel.setWidth("100%");
    buttonPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);

    VerticalPanel contentPanel = new VerticalPanel();
    contentPanel.add(panel);
    contentPanel.add(buttonPanel);

    popup = new PopupPanel(false, true);
    popup.setAutoHideEnabled(true);
    popup.setWidget(contentPanel);
    popup.setStylePrimaryName("ode-MultipleChoicePropertyEditor");
  }

  @Override
  protected void updateValue() {
    summary.setText(getPropertyValueSummary() + "...");
  }

  /**
   * Returns a textual summary of the edited property's current value.
   *
   * <p>This is displayed to the user in the property editor pane before the
   * user decides to actually modify the property.
   */
  protected String getPropertyValueSummary() {
    return property.getValue();
  }

  /**
   * Sets the number of visible characters in the text box for the summary.
   *
   * @param length the number of visible characters
   */
  protected void setSummaryVisibleLength(int length) {
    summary.setVisibleLength(length);
  }

  /**
   * Opens the additional choice dialog.
   */
  protected void openAdditionalChoiceDialog() {
    popup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
      public void setPosition(int offsetWidth, int offsetHeight){
        // adjust the x and y positions so that the entire panel
        // is on-screen
        int xPosition = getAbsoluteLeft();
        int yPosition = getAbsoluteTop();
        int xExtrude =
          xPosition + offsetWidth - Window.getClientWidth() - Window.getScrollLeft();
        int yExtrude =
          yPosition + offsetHeight - Window.getClientHeight() - Window.getScrollTop();
        if (xExtrude > 0) {
          xPosition -= (xExtrude + ADDITIONAL_CHOICE_ONSCREEN_PADDING);
        }
        if (yExtrude > 0) {
          yPosition -= (yExtrude + ADDITIONAL_CHOICE_ONSCREEN_PADDING);
        }
        popup.setPopupPosition(xPosition, yPosition);
      }
    });
  }

  protected final boolean isOpen() {
    return popup != null && popup.isShowing();
  }

  protected final void closeAdditionalChoiceDialog(boolean ok) {
    if (ok) {
      if (!okAction()) {
        // Dialog is not allowed to close.
        return;
      }
    } else {
      updateValue(); // Restore previous property value
    }
    popup.hide();
  }

  /**
   * Invoked when the additional choice dialog was exited by pressing the OK
   * button, or because closeAdditionalChoiceDialog was called with true.
   *
   * @return true if the dialog is allowed to close
   */
  protected abstract boolean okAction();
  
  
  public void makeTreeForComponentType(final SimpleComponentDatabase COMPONENT_DATABASE,
			final String componentType, TreeItem all) {

		// Getters and Setters
		TreeItem set = new CheckableTreeItem("Setters");
		set.setHTML("<b> Setters </b>");
		TreeItem get = new CheckableTreeItem("Getters");
		get.setHTML("<b> Getters </b>");

		for (PropertyDefinition property : COMPONENT_DATABASE.getPropertyDefinitions(componentType)) {
			if (!property.getName().equals("ComponentName") && !property.getName().equals("ActivitiesToTrack")) {
				TreeItem itemPropertyget = new CheckableTreeItem("Get - " + property.getName());
				itemPropertyget.setHTML(property.getName());
				itemPropertyget.setTitle("Get - " + property.getName());
				itemPropertyget.setUserObject("Get - " + property.getName());
				get.addItem(itemPropertyget);
				TreeItem itemPropertyset = new CheckableTreeItem("Set - " + property.getName());
				itemPropertyset.setHTML(property.getName());
				itemPropertyset.setTitle("Set - " + property.getName());
				itemPropertyset.setUserObject("Set - " + property.getName());
				set.addItem(itemPropertyset);
			}
		}

		all.addItem(set);
		all.addItem(get);

		// Functions
		TreeItem functions = new CheckableTreeItem("Functions");
		functions.setHTML("<b> Functions </b>");
		functions.setState(true, false);

		for (MethodDefinition function : COMPONENT_DATABASE.getMethodDefinitions(componentType)) {
			TreeItem itemFunction = new CheckableTreeItem(function.getName());
			itemFunction.setHTML(function.getName());
			itemFunction.setTitle(function.getName());
			itemFunction.setUserObject(function.getName());
			functions.addItem(itemFunction);
		}

		all.addItem(functions);

		// Events
		TreeItem events = new CheckableTreeItem("Events");
		events.setHTML("<b> Events </b>");

		for (EventDefinition event : COMPONENT_DATABASE.getEventDefinitions(componentType)) {
			TreeItem itemEvent = new CheckableTreeItem(event.getName());
			itemEvent.setHTML(event.getName());
			itemEvent.setTitle(event.getName());
			itemEvent.setUserObject(event.getName());
			events.addItem(itemEvent);
		}

		all.addItem(events);

	}
}
