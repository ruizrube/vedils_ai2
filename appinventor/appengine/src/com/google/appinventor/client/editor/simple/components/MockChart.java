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
public class MockChart extends MockVisibleComponent {

	/**
	 * Component type name.
	 */
	public static final String TYPE = "Chart";

	// Property names that we need to treat specially
	private static final String PROPERTY_NAME_USESLOCATION = "UsesLocation";
	// Property names that we need to treat specially
	private static final String PROPERTY_NAME_QUERY = "Query";

	private static final Object PROPERTY_NAME_CHARTTYPE = "ChartType";

	private static final int BAR = 0;

	private static final int LINE = 1;

	// Large icon image for use in designer. Smaller version is in the palette.
	private final Image barLargeImage = new Image(images.barChartbig());
	private final Image lineLargeImage = new Image(images.lineChartbig());
	private final Image pieLargeImage = new Image(images.pieChartbig());
		
	SimplePanel webViewerWidget; 
	
	/**
	 * Creates a new MockActivityGraph component.
	 *
	 * @param editor
	 *            editor of source file the component belongs to
	 */
	public MockChart(SimpleEditor editor) {
		super(editor, TYPE, images.chart());

		// Initialize mock WebViewer UI
		webViewerWidget = new SimplePanel();
		webViewerWidget.setStylePrimaryName("ode-SimpleMockContainer");
		// TODO(halabelson): Center vertically as well as horizontally
		webViewerWidget.addStyleDependentName("centerContents");
		webViewerWidget.setWidget(barLargeImage);
		webViewerWidget.setHeight("100%");
		initComponent(webViewerWidget);
	}

	private void setChartTypeProperty(String newValue) {
		
		if(newValue.equals(BAR + "")){
			webViewerWidget.setWidget(barLargeImage);
		} else if(newValue.equals(LINE + "")){
			webViewerWidget.setWidget(lineLargeImage);
			
		}
		// TODO Auto-generated method stub	
	}
	
	// If these are not here, then we don't see the icon as it's
	// being dragged from the pelette
	@Override
	public int getPreferredWidth() {
		return barLargeImage.getWidth();
	}

	@Override
	public int getPreferredHeight() {
		return barLargeImage.getHeight();
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
		} else if (propertyName.equals(PROPERTY_NAME_CHARTTYPE)) {
		      setChartTypeProperty(newValue);
		      refreshForm();
		}

	}

	
}
