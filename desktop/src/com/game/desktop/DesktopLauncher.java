package com.game.desktop;

import Extend.Box2d.IBody;
import Extend.Box2d.IWater;
import GameGDX.GUIData.IChild.Component;
import GameGDX.Json;
import GameGDX.Reflect;
import com.badlogic.gdx.utils.reflect.ClassReflection;

public class DesktopLauncher {
	public static void main (String[] arg) {
//		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
//		config.width = 640;
//		config.height = 360;
//		new LwjglApplication(new MyGame(), config);

		new XmlLevel("caveMask",null);
	}
}
