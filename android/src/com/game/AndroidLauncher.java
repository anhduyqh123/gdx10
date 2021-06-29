package com.game;

import SDK.AdMob;
import android.os.Bundle;

import android.view.View;
import android.widget.FrameLayout;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		View gameView = initializeForView(new MyGame(), config);

		FrameLayout rootView = new FrameLayout(this);
		rootView.addView(gameView);
		this.setContentView(rootView);
		new AdMob(this,rootView);
	}
}
