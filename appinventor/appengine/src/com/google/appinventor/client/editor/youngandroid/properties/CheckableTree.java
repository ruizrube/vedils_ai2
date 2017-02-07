package com.google.appinventor.client.editor.youngandroid.properties;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeImages;
import com.google.gwt.user.client.ui.TreeItem;

@SuppressWarnings("deprecation")
public class CheckableTree extends Tree {

	private List<StatusChangeHandler> handlers = new ArrayList<StatusChangeHandler>();

	public class CheckableTreeSelectionHandler implements
			SelectionHandler<TreeItem> {
		private static final int SELECTION_ALL = CheckableTreeItem.SELECTION_ALL;
		private static final int SELECTION_NONE = CheckableTreeItem.SELECTION_NONE;
		private static final int SELECTION_SOME = CheckableTreeItem.SELECTION_SOME;
		private static final int SELECTION_UNKOWN = 0;

		@Override
		public void onSelection(SelectionEvent<TreeItem> event) {
			TreeItem item = event.getSelectedItem();
			int oldValue, newValue;
			oldValue = statusOf(item);
			if (oldValue == SELECTION_UNKOWN)
				return;
			if (oldValue == SELECTION_SOME || oldValue == SELECTION_NONE) {
				newValue = SELECTION_ALL;
			} else {
				newValue = SELECTION_NONE;
			}

			setStatus(item, newValue);
			statusChanged(item, oldValue, newValue);

			if (item.getParentItem() != null)
				updateParentStatus(item.getParentItem(), oldValue, newValue);

			for (int i = 0; i < item.getChildCount(); i++) {
				int status = statusOf(item.getChild(i));
				if (status == SELECTION_UNKOWN || status == newValue)
					continue;
				setChildrenStatus(item.getChild(i), newValue);
			}
		}

		private void setChildrenStatus(TreeItem item, int newValue) {
			int status = statusOf(item);
			if (status == SELECTION_UNKOWN || status == newValue)
				return;

			setStatus(item, newValue);
			statusChanged(item, status, newValue);
			for (int i = 0; i < item.getChildCount(); i++) {
				int status1 = statusOf(item.getChild(i));
				if (status1 == SELECTION_UNKOWN || status1 == newValue)
					continue;
				setChildrenStatus(item.getChild(i), newValue);
			}
		}

		private void updateParentStatus(TreeItem item, int oldValue,
				int newValue) {
			if (item == null || oldValue == newValue)
				return;
			int thisOldValue = statusOf(item);
			if (thisOldValue == SELECTION_UNKOWN)
				return;
			int thisNewValue;
			if (oldValue != SELECTION_SOME && thisOldValue == SELECTION_SOME) {
				thisNewValue = newValue;
				for (int i = 0; i < item.getChildCount(); i++) {
					int status = statusOf(item.getChild(i));
					if (status == SELECTION_UNKOWN) {
						continue;
					} else if (status == SELECTION_SOME) {
						thisNewValue = SELECTION_SOME;
						break;
					} else if (status != newValue) {
						thisNewValue = SELECTION_SOME;
					}
				}
			} else if (oldValue != SELECTION_SOME
					&& thisOldValue != SELECTION_SOME) {
				if (item.getChildCount() > 1) {
					thisNewValue = SELECTION_SOME;
				} else {
					thisNewValue = newValue;
				}
			} else {
				thisNewValue = newValue;
				for (int i = 0; i < item.getChildCount(); i++) {
					int status = statusOf(item.getChild(i));
					if (status == SELECTION_UNKOWN) {
						continue;
					} else if (status == SELECTION_SOME) {
						thisNewValue = SELECTION_SOME;
						break;
					} else if (status != newValue) {
						thisNewValue = SELECTION_SOME;
					}
				}
			}

			if (thisOldValue != thisNewValue) {
				setStatus(item, thisNewValue);
				statusChanged(item, thisOldValue, thisNewValue);
				if (item.getParentItem() != null)
					updateParentStatus(item.getParentItem(), thisOldValue,
							thisNewValue);
			}
		}

		private int statusOf(TreeItem item) {
			if (item instanceof CheckableTreeItem) {
				return ((CheckableTreeItem) item).getStatus();
			} else {
				return SELECTION_UNKOWN;
			}
		}

		private void setStatus(TreeItem item, int status) {
			if (item instanceof CheckableTreeItem) {
				((CheckableTreeItem) item).setStatus(status);
			}

		}

		public void statusChanged(TreeItem item, int oldValue, int newValue) {
			if (item instanceof CheckableTreeItem) {
				for (StatusChangeHandler handler : handlers) {
					handler.statusChanged((CheckableTreeItem)item, oldValue, newValue);
				}
			}
		}
	}
	
	public CheckableTree() {
		super();
		addSelectionHandler(new CheckableTreeSelectionHandler());
	}
	
	public CheckableTree(CheckableTreeSelectionHandler c) {
		super();
		addSelectionHandler(c);
	}


	public CheckableTree(TreeImages images) {
		super(images);
		addSelectionHandler(new CheckableTreeSelectionHandler());
	}

	public CheckableTree(TreeImages images, boolean useLeafImages) {
		super(images, useLeafImages);
		addSelectionHandler(new CheckableTreeSelectionHandler());
	}

	public void addStatusChangeHandler(StatusChangeHandler handler) {
		if (handler == null)
			return;
		if (handlers.contains(handler))
			return;
		handlers.add(handler);
	}

	public void removeStatusChangeHandler(StatusChangeHandler handler) {
		if (handler != null)
			return;
		handlers.remove(handler);
	}

}
