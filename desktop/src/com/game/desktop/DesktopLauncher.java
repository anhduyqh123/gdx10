package com.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTFrame;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.game.MyGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 640;
		config.height = 360;
		new LwjglApplication(new MyGame(), config);

		//new LwjglAWTFrame(new MyGame(),"xxx",640,360);
	}
}
