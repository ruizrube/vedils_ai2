/**
 * 
 */
package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.ar4ai.VirtualObject;
import com.google.appinventor.components.runtime.util.MediaUtil;

/**
 * @author ivanruizrube
 *
 */
@SimpleObject
@DesignerComponent(nonVisible = true, version = 1, description = "Augmented Reality 3D Model Asset Component (by SPI-FM at UCA)", category = ComponentCategory.VEDILSAUGMENTEDREALITY, iconName = "images/ar3DModelAsset.png")
@UsesPermissions(permissionNames = "android.permission.INTERNET, android.permission.CAMERA, android.permission.ACCESS_NETWORK_STATE,android.permission.WRITE_EXTERNAL_STORAGE, android.permission.READ_EXTERNAL_STORAGE")

public class AR3DModelAsset extends ARVirtualObject{

	public AR3DModelAsset(ComponentContainer container) {
		super(container);
		this.getData().setVisualAssetType(VirtualObject.ASSET_3DMODEL);
	}
	
	

	/**
	 * Returns the path of the 3d model.
	 *
	 * @return the path of the 3d model
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String OverlaidModel3D() {
		return data.getOverlaid3DModel();
	}

	/**
	 * Specifies the path of the 3d model.
	 *
	 * <p/>
	 * See {@link MediaUtil#determineMediaSource} for information about what a
	 * path can be.
	 *
	 * @param path
	 *            the path of the 3d model
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ASSET_3DMODEL, defaultValue = "")
	@SimpleProperty(userVisible = true)
	public void OverlaidModel3D(String path) {
		data.setOverlaid3DModel((path == null) ? "" : path);
	}
	
	
	/**
	 * Returns the transparency color as an alpha-red-green-blue integer.
	 *
	 * @return background RGB color with alpha
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Returns the transparency color")
	public int Transparency() {
		return data.getTransparency();
	}

	/**
	 * Specifies the transparency color as an alpha-red-green-blue integer. If
	 * the parameter is {@link Component#COLOR_DEFAULT}
	 *
	 * @param argb
	 *            background RGB color with alpha
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR, defaultValue = Component.DEFAULT_VALUE_COLOR_DEFAULT)
	@SimpleProperty(description = "Specifies the transparency color.")
	public void Transparency(int argb) {
		data.setTransparency(argb);
	}
	
	
	/**
	 * Returns the path of the image texture.
	 *
	 * @return the path of the image picture
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public String ImageTexture() {
		return data.getImageTexture();
	}

	/**
	 * Specifies the image texture for the 3d model.
	 *
	 * <p/>
	 * See {@link MediaUtil#determineMediaSource} for information about what a
	 * path can be.
	 *
	 * @param path
	 *            the path of the 3d model
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ASSET_IMAGE, defaultValue = "")
	@SimpleProperty(userVisible = true)
	public void ImageTexture(String path) {
		data.setImageTexture((path == null) ? "" : path);

	}

	/**
	 * Returns the texture color for the 3dmodel as an alpha-red-green-blue
	 * integer.
	 *
	 * @return texture color for the 3dmodel
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Returns the texture color for the 3dmodel")
	public int ColorTexture() {
		return data.getColorTexture();
	}

	/**
	 * Specifies the texture color for the 3dmodel as an alpha-red-green-blue
	 * integer. If the parameter is {@link Component#COLOR_DEFAULT}
	 *
	 * @param argb
	 *            the texture color for the 3dmodel RGB color with alpha
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR, defaultValue = Component.DEFAULT_VALUE_COLOR_DEFAULT)
	@SimpleProperty(description = "Specifies the texture color.")
	public void ColorTexture(int argb) {
		data.setColorTexture(argb);
	}

	/**
	 * Returns the path of the material file.
	 *
	 * @return the path of the material file
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String Material() {
		return data.getMaterial();
	}

	/**
	 * Specifies the path of the material file.
	 *
	 * <p/>
	 * See {@link MediaUtil#determineMediaSource} for information about what a
	 * path can be.
	 *
	 * @param path
	 *            the path of the material file
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ASSET_MATERIAL, defaultValue = "")
	@SimpleProperty(userVisible = true)
	public void Material(String path) {
		data.setMaterial((path == null) ? "" : path);

	}
	

}
