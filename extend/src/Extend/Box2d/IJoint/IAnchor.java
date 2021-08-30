package Extend.Box2d.IJoint;

import Extend.Box2d.GBox2d;
import GameGDX.GUIData.IChild.IAlign;
import GameGDX.Reflect;
import GameGDX.Scene;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class IAnchor {
    public IAlign iAlign = IAlign.center;
    public float delX,delY;

    public Vector2 GetPos(Actor actor)
    {
        Vector2 local = Scene.GetPosition(actor,iAlign.value).add(delX,delY);
        return Scene.GetStagePosition(actor,local);
        //return Scene.GetStagePosition(actor,iAlign.value).add(delX,delY);
    }
    public Vector2 GetPhysicPos(Actor actor)
    {
        return GBox2d.GameToPhysics(GetPos(actor));
    }

    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }
}
