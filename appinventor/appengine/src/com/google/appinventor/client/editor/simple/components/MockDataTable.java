/**
 * 
 */
package com.google.appinventor.client.editor.simple.components;

import com.google.appinventor.client.editor.simple.SimpleEditor;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author ruizrube
 *
 */
public class MockDataTable extends MockVisibleComponent {

	/**
	 * Component type name.
	 */
	public static final String TYPE = "DataTable";

	// Property names that we need to treat specially
	private static final String PROPERTY_NAME_USESLOCATION = "UsesLocation";

	// Large icon image for use in designer. Smaller version is in the palette.
	private final Image largeImage = new Image(images.datatablebig());

	/**
	 * Creates a new MockWebViewer component.
	 *
	 * @param editor
	 *            editor of source file the component belongs to
	 */
	public MockDataTable(SimpleEditor editor) {
		super(editor, TYPE, images.datatable());

		// Initialize mock WebViewer UI
		SimplePanel webViewerWidget = new SimplePanel();
		webViewerWidget.setStylePrimaryName("ode-SimpleMockContainer");
		// TODO(halabelson): Center vertically as well as horizontally
		webViewerWidget.addStyleDependentName("centerContents");
		webViewerWidget.setWidget(largeImage);
		webViewerWidget.setWidth("100%");
		initComponent(webViewerWidget);
	}

	// If these are not here, then we don't see the icon as it's
	// being dragged from the pelette
	@Override
	public int getPreferredWidth() {
		return largeImage.getWidth();
	}

	@Override
	public int getPreferredHeight() {
		return largeImage.getHeight();
	}

	// override the width and height hints, so that automatic will in fact be
	// fill-parent
	@Override
	int getWidthHint() {
		int widthHint = super.getWidthHint();
		if (widthHint == LENGTH_PREFERRED) {
			widthHint = LENGTH_FILL_PARENT;
		}
		return widthHint;
	}

	@Override
	int getHeightHint() {
		int heightHint = super.getHeightHint();
		if (heightHint == LENGTH_PREFERRED) {
			heightHint = LENGTH_FILL_PARENT;
		}
		return heightHint;
	}

	@Override
	public void onPropertyChange(String propertyName, String newValue) {
		super.onPropertyChange(propertyName, newValue);

		if (propertyName.equals(PROPERTY_NAME_USESLOCATION)) {
			editor.getProjectEditor().recordLocationSetting(this.getName(), newValue);
		}

	}

}
