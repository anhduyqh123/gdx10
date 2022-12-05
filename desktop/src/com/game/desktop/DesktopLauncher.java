package com.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 360;
		config.height = 640;

		Vector2 v2 = new Vector2(1,2);
		Vector3 v3 = new Vector3(1,2,3);
		Color v4 = new Color(1,1,1,1);

		//new LwjglApplication(new MyGame(), config);
	}
}
