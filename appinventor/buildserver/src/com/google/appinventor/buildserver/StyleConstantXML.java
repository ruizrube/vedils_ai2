package com.google.appinventor.buildserver;

public class StyleConstantXML {
	
	 private StyleConstantXML() {
	  }

	  public final static String TEST_THEME =
		 "<resources>\n\n" +
		 "<color name=\"custom\">#b0b0ff</color>\n"+
	      "<style name=\"AppTheme\" parent=\"@android:style/Theme.Material.Light\">\n" +
	      "<item name=\"android:actionBarStyle\">@style/MyActionBar</item>\n" +
	      "</style>\n"+
	      "<style name=\"MyActionBar\" parent=\"@android:style/Widget.Material.Light.ActionBar\">\n"+
	      "<item name=\"android:background\">#00ff00</item>\n"+
	      "</style>\n\n"+
	      "</resources>\n\n";
	  

}
