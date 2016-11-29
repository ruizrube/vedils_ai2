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
		
	SimplePanel mockChartWidget; 
	
	/**
	 * Creates a new MockActivityGraph component.
	 *
	 * @param editor
	 *            editor of source file the component belongs to
	 */
	public MockChart(SimpleEditor editor) {
		super(editor, TYPE, images.chart());

		// Initialize mock Chart UI
		mockChartWidget = new SimplePanel();
		mockChartWidget.setStylePrimaryName("ode-SimpleMockContainer");
		// TODO(halabelson): Center vertically as well as horizontally
		mockChartWidget.addStyleDependentName("centerContents");
		mockChartWidget.setWidget(barLargeImage);
		initComponent(mockChartWidget);
	}

	private void setChartTypeProperty(String newValue) {
		
		if(newValue.equals(BAR + "")){
			mockChartWidget.setWidget(barLargeImage);
		} else if(newValue.equals(LINE + "")){
			mockChartWidget.setWidget(lineLargeImage);
			
		}
		// TODO Auto-generated method stub	
	}
	
	@Override
	public int getPreferredWidth() {
		if(mockChartWidget.getWidget().equals(barLargeImage)) {
			return barLargeImage.getWidth();
		} else {
			return lineLargeImage.getWidth();
		}
	}

	@Override
	public int getPreferredHeight() {
		if(mockChartWidget.getWidget().equals(barLargeImage)) {
			return barLargeImage.getHeight();
		} else {
			return lineLargeImage.getHeight();
		}
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
