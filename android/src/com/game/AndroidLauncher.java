package com.game;

import ISdk.AdMob;
import ISdk.FireStore;
import ISdk.Firebase;
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
		TestGame game = new TestGame();
		View gameView = initializeForView(new MyGame(), config);

		FrameLayout rootView = new FrameLayout(this);
		rootView.addView(gameView);
		this.setContentView(rootView);

		Firebase firebase = new Firebase(this);
		AdMob adMob = new AdMob(this,rootView);
		adMob.getConfig = firebase::GetConfig;
		adMob.InitBanner_Bot();
		adMob.InitInterstitial();
		adMob.InitVideoReward();
		//game.adMob = adMob;

		new FireStore();
	}
}
