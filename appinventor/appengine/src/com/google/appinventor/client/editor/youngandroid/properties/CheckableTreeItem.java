package com.google.appinventor.client.editor.youngandroid.properties;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ImageBundle;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("deprecation")
public class CheckableTreeItem extends TreeItem {
	public static final int SELECTION_ALL = 1;
	public static final int SELECTION_NONE = 2;
	public static final int SELECTION_SOME = 3;
	private int status = SELECTION_NONE;
	private Widget widget = null;
	private String html = null;
	private Image image = null;
	private static SelectionImages images = GWT.create(SelectionImages.class);

	public static interface SelectionImages extends ImageBundle {
		@Resource("selection_all.gif")
		AbstractImagePrototype selectionAll();

		@Resource("selection_none.gif")
		AbstractImagePrototype selectionNone();

		@Resource("selection_some.gif")
		AbstractImagePrototype selectionSome();
	}

	public CheckableTreeItem() {
		image = images.selectionNone().createImage();
		
	}

	public CheckableTreeItem(String text) {
		this();
		setText(text);
	}

	public CheckableTreeItem(Widget w) {
		this();
		setWidget(w);
	}
	
	@Override
	public void setState(boolean open, boolean fireEvents) {
		if(this.getState() && this.getStatus() != SELECTION_NONE) {
			return;
		}
		super.setState(open, fireEvents);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		if (status != SELECTION_ALL && status != SELECTION_NONE
				&& status != SELECTION_SOME) {
			return;
		}
		if (status == SELECTION_ALL) {
			images.selectionAll().applyTo(image);
		} else if (status == SELECTION_NONE) {
			images.selectionNone().applyTo(image);
		} else if (status == SELECTION_SOME) {
			images.selectionSome().applyTo(image);
		}
		this.status = status;
	}

	public void setWidget(Widget w) {
		if (widget != null)
			widget.removeFromParent();
		if (w == null) {
			widget = null;
			super.setWidget(null);
		} else {
			w.removeFromParent();
			HorizontalPanel panel = new HorizontalPanel();
			panel.add(image);
			panel.add(w);
			super.setWidget(panel);
			widget = w;
			html = null;
		}
	}

	public Widget getWidget() {
		return widget;
	}

	public void setText(String text) {
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(image);
		panel.add(new Label(text));
		super.setWidget(panel);
		html = text;
		widget = null;
	}

	public String getText() {
		return html;
	}

	public void setHTML(String html) {
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(image);
		panel.add(new HTML(html));
		super.setWidget(panel);
		this.html = html;
		widget = null;
	}

	public String getHTML() {
		return html;
	}

}
