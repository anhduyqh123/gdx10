package com.game.desktop;

import Extend.Box2d.IShape;
import GameGDX.Reflect;
import GameGDX.Util;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.Field;
import com.game.MyGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
//		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
//		config.width = 640;
//		config.height = 360;
//		new LwjglApplication(new MyGame(), config);
		int a = 8;
		//2//1//0
		int x = (int) MathUtils.log2(1);
		System.out.println(Integer.toBinaryString(7));
	}
	private static int Loop(int num)
	{
		if (num<=0) return -1;
		int x = (int) MathUtils.log2(num);
		int left = num-(int)Math.pow(2,x);
		System.out.println(x);
		return Loop(left);
	}
}
