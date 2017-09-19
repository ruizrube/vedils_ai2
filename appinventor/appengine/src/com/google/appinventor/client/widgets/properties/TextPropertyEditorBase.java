// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.client.widgets.properties;

import static com.google.appinventor.client.Ode.MESSAGES;

import com.google.appinventor.client.Ode;
import com.google.appinventor.client.boxes.ProjectListBox;
import com.google.appinventor.client.editor.simple.components.MockComponent;
import com.google.appinventor.client.editor.youngandroid.YaFormEditor;
import com.google.appinventor.client.output.OdeLog;
import com.google.appinventor.common.utils.StringUtils;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBoxBase;

/**
 * Property editor base for text box editors.
 *
 */
public class TextPropertyEditorBase extends PropertyEditor {

  /**
   * Thrown to indicate that text input is invalid.
   */
  public static class InvalidTextException extends Exception {
    public InvalidTextException(String message) {
      super(message);
    }
  }

  // This is the GWT object that supports the property editor
  // It can be TextBoxBase object, currently in App Inventor only
  // TextBox and TextArea
  protected TextBoxBase textEdit;
  protected Anchor hyperlink;
  protected final Image imageHyperlink;

  private boolean hasFocus;
  private final YaFormEditor editor;
  protected String storageMode;

  /**
   * Creates a new instance of the property editor.
   */
  public TextPropertyEditorBase(final TextBoxBase widget, final YaFormEditor editor) {

	this.editor = editor;
	this.storageMode = MESSAGES.fusionTablesStorageMode();
    textEdit = widget;
    imageHyperlink = new Image(Ode.getImageBundle().showTable());
    imageHyperlink.setWidth("1.5em");
    imageHyperlink.setHeight("1.5em");

    textEdit.addKeyPressHandler(new KeyPressHandler() {
      @Override
      public void onKeyPress(KeyPressEvent event) {
        handleKeyPress(event.getCharCode());
      }
    });
    textEdit.addKeyUpHandler(new KeyUpHandler() {
      @Override
      public void onKeyUp(KeyUpEvent event) {
        handleKeyUp(event.getNativeKeyCode());
      }
    });
    textEdit.addValueChangeHandler(new ValueChangeHandler() {
      @Override
      public void onValueChange(ValueChangeEvent event) {
        validateText();
      }
    });

    // NOTE(lizlooney) - The following handlers for focus, blur, and click are needed to workaround
    // a bug with WebKit browsers (chrome and safari) where clicking in the TextBox causes it to
    // gain focus, but then immediately lose focus (blur). To work around the problem, we keep
    // track of whether the TextBox has focus using a FocusHandler and a BlurHandler. Then, we use
    // a ClickHandler and if we get a ClickEvent and the TextBox does not have focus, we explicitly
    // call setFocus.
    textEdit.addFocusHandler(new FocusHandler() {
      @Override
      public void onFocus(FocusEvent event) {
        hasFocus = true;
      }
    });
    textEdit.addBlurHandler(new BlurHandler() {
      @Override
      public void onBlur(BlurEvent event) {
        hasFocus = false;
        // Calling validateText here means that we will save the changed property value (if it is
        // valid) when this property editor loses focus (for example, when the user clicks on
        // another property editor).
        validateText();
      }
    });
    textEdit.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        if (!hasFocus) {
          textEdit.setFocus(true);
        }
      }
    });
    
    if(editor != null) { //By SPI-FM: If the property belongs to ActivityTracker, we add the URL of active table.
    	HTMLPanel htmlPanel = new HTMLPanel("");
    	hyperlink = new Anchor();
    	hyperlink.getElement().appendChild(imageHyperlink.getElement());
    	hyperlink.setTarget("https://");
    	hyperlink.setTitle(Ode.getMessages().ActivityTrackerShowTableMessage());
    	hyperlink.setVisible(false);
    	hyperlink.getElement().getStyle().setCursor(Style.Cursor.POINTER);
    	hyperlink.addClickHandler(new ClickHandler() {
    		@Override
    		public void onClick(ClickEvent event) {
    			MockComponent currentComponent = editor.selectedComponent;
    			
    			if(currentComponent != null) {
    				OdeLog.log("ActivityTracker componentName = " + currentComponent.getName());
					storageMode = currentComponent.getPropertyValue("StorageMode");
    			}
    			
    			updateValue();
    			Window.open(hyperlink.getTarget(), "_blank", "");
    		}
    	});
        
        textEdit.setWidth("11em");
    	htmlPanel.add(textEdit);
    	htmlPanel.add(hyperlink);
    	initWidget(htmlPanel);
    } else {
    	initWidget(textEdit);  //kludge for now fix this with instanceOf?
    }
    setHeight("2em");
  }

  @Override
  protected void onUnload() {
    // onUnload is called immediately before a widget becomes detached from the browser's document.
    // Calling validateText here means that we will save the changed property value (if it is
    // valid) when the user clicks on another component.
    validateText();
    super.onUnload();
  }

  @Override
  protected void updateValue() {
	textEdit.setText(property.getValue());
	if(editor != null) {
		if(!property.getValue().isEmpty()) {
			if(storageMode.equals("0")) { //Fusion Tables mode
				hyperlink.setTarget("https://fusiontables.google.com/data?docid="+property.getValue());
	    		hyperlink.setVisible(true);
			} else {
				String packageName = "";
	    		if(Ode.getInstance().getCurrentYoungAndroidProjectId() != 0) {
	    			packageName = StringUtils.getProjectPackage(
	      	              Ode.getInstance().getUser().getUserEmail(), Ode.getInstance().getProjectManager().getProject(Ode.getInstance().getCurrentYoungAndroidProjectId()).getProjectName());
	    		} else { //Project list page
	    			packageName = StringUtils.getProjectPackage(
	        	          Ode.getInstance().getUser().getUserEmail(), Ode.getInstance().getProjectManager().getProject(ProjectListBox.getProjectListBox().getProjectList().getProjects().get(0).getProjectId()).getProjectName());
	    		}
	    		//hyperlink.setTarget("http://127.0.0.1:28017/" + packageName.replaceAll("\\.", "_") + "/" + property.getValue() + "/");
	    		hyperlink.setTarget("http://vedilsanalytics.uca.es:8083/localhost/"+ packageName.replaceAll("\\.", "_") + "/" + property.getValue() + "//1/");
	    		hyperlink.setVisible(true);
			}
    	} else {
    		hyperlink.setTarget("https://");
    		hyperlink.setVisible(false);
    	}
	 }
  }

  private void handleKeyPress(char keyCode) {
    if (keyCode == KeyCodes.KEY_ENTER || keyCode == KeyCodes.KEY_TAB) {
      // Pressing <tab>, <enter> or <return> will surrender focus.
      textEdit.cancelKey();
      textEdit.setFocus(false);
    } else if (!validateKeyCode(keyCode)) {
      textEdit.cancelKey();
    }
  }

  private void handleKeyUp(int keyCode) {
    if (keyCode == KeyCodes.KEY_ESCAPE) {
      // Pressing <esc> will reset the content of the editor to the previous property value as well
      // as surrender focus.
      updateValue();  // Restore previous property value.
      textEdit.cancelKey();
      textEdit.setFocus(false);
    }
  }

  /*
   * Validates the text in the textEdit and if it is valid, sets the property
   * value to the text.
   */
  private void validateText() {
    String text = textEdit.getText();
    try {
      validate(text);
      property.setValue(text);
    } catch (InvalidTextException e) {
      String error = e.getMessage();
      if (error == null || error.isEmpty()) {
        error = MESSAGES.malformedInputError();
      }
      Window.alert(error);
      updateValue();  // Restore previous property value.
    }
  }

  /**
   * Validates the given key code.
   *
   * <p/>The implementation here does no validation. Subclasses may override
   * this method to provide actual validation.
   *
   * @param keyCode  key code to validate
   * @return true if the keycode is allowed, false if it is not allowed.
   */
  protected boolean validateKeyCode(char keyCode) {
    return true;
  }

  /**
   * Validates the given text. Throw an InvalidTextException if the text is
   * invalid.
   *
   * <p/>The implementation here does no validation. Subclasses may override
   * this method to provide actual validation.
   *
   * @param text  input string to validate
   * @throws InvalidTextException if the text is invalid
   */
  protected void validate(String text) throws InvalidTextException {}
}
