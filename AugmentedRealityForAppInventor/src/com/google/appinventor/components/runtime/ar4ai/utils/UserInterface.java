package com.google.appinventor.components.runtime.ar4ai.utils;

import com.google.appinventor.components.runtime.ar4ai.ARActivity;
import com.google.appinventor.components.runtime.ar4ai.UIVariables;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class UserInterface extends RelativeLayout {
	
	private Button rightButton, leftButton;
	private TextView text;

	public UserInterface(final Context context, UIVariables uivariables) {
		super(context);
		rightButton = new Button(context);
		rightButton.setVisibility(INVISIBLE);
		rightButton.setId(45235);
		rightButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ARActivity.AR_ACTIVITY_EVENT_CAMERA);
				intent.putExtra("status", ARActivity.AR_ACTIVITY_EVENT_CAMERA_RIGHTBUTTON);
				LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
			}
			
		});
		RelativeLayout.LayoutParams b1params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		b1params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		b1params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		addView(rightButton, b1params);
		leftButton = new Button(context);
		leftButton.setVisibility(INVISIBLE);
		leftButton.setId(235);
		leftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ARActivity.AR_ACTIVITY_EVENT_CAMERA);
				intent.putExtra("status", ARActivity.AR_ACTIVITY_EVENT_CAMERA_LEFTBUTTON);
				LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
			}
		});
		RelativeLayout.LayoutParams b2params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		b2params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		b2params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		addView(leftButton, b2params);
		text = new TextView(context);
		text.setTextSize(20f);
		//text.setBackgroundColor(Color.GRAY);
		text.setTextColor(Color.WHITE);
		//text.setTextAppearance(android.R.attr.textAppearanceLarge);
		RelativeLayout.LayoutParams tparams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		tparams.addRule(ALIGN_BASELINE, leftButton.getId());
		tparams.addRule(ALIGN_BOTTOM, leftButton.getId());
		tparams.addRule(RIGHT_OF, leftButton.getId());
		tparams.addRule(LEFT_OF, rightButton.getId());
		addView(text, tparams);
		if (uivariables != null) {
			if (uivariables.isRightBtEnabled())
				rightButton.setVisibility(VISIBLE);
			rightButton.setText(uivariables.getRightBtText());
			if (uivariables.isLeftBtEnabled())
				leftButton.setVisibility(VISIBLE);
			leftButton.setText(uivariables.getLeftBtText());
			text.setText(uivariables.getFloatingText());
			if (uivariables.getFloatingText() != null && !uivariables.getFloatingText().equals(""))
				text.setBackgroundColor(Color.BLACK);
		}
	}
	
	public void updateInterface(UIVariables uivariables) {
		if (uivariables.isRightBtEnabled() && rightButton.getVisibility() == INVISIBLE)
			rightButton.setVisibility(VISIBLE);
		else if (!uivariables.isRightBtEnabled() && rightButton.getVisibility() == VISIBLE)
			rightButton.setVisibility(INVISIBLE);
		rightButton.setText(uivariables.getRightBtText());
		if (uivariables.isLeftBtEnabled() && leftButton.getVisibility() == INVISIBLE)
			leftButton.setVisibility(VISIBLE);
		else if (!uivariables.isLeftBtEnabled() && leftButton.getVisibility() == VISIBLE)
			leftButton.setVisibility(INVISIBLE);
		leftButton.setText(uivariables.getLeftBtText());
		text.setText(uivariables.getFloatingText());
		if (uivariables.getFloatingText() != null && !uivariables.getFloatingText().equals(""))
			text.setBackgroundColor(Color.BLACK);
		else
			text.setBackgroundColor(Color.TRANSPARENT);
	}
}
