package Editor.UITool.Pointed;

import Extend.Box2d.IShape;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.Reflect;
import GameGDX.Scene;
import GameGDX.Screens.Screen;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;

import java.util.Arrays;

public class Display {
    private Group group = new Group();
    private Actor actor;
    private Pointed shape;
    private Runnable close;

    public Display(IActor iActor)
    {
        actor = iActor.GetActor();
        Runnable run = Screen.Returns(Arrays.asList(Scene.ui2.getChildren().toArray()));
        Scene.ui2.clearChildren();

        close = ()->{
            group.remove();
            run.run();
            iActor.Refresh();
        };

        group.setDebug(true);
        Vector3 camPos = Scene.GetUICamera().position;
        Scene.SetBounds(group,camPos.x,camPos.y, Align.center,actor.getWidth(),actor.getHeight(),Scene.ui2);
        group.addActor(actor);
        actor.setPosition(0,0);
    }
    public void Close()
    {
        close.run();
    }
    public void Resize(float size)
    {
        shape.Resize(size);
    }
    //Shape
    public void ChainShape(Object oShape)
    {
        group.clearChildren();
        group.addActor(actor);
        shape = new ChainPointed(Reflect.GetValue("points",oShape),group);
    }
    public void PolygonShape(Object oShape)
    {
        group.clearChildren();
        group.addActor(actor);
        shape = new PolygonPointed(Reflect.GetValue("points",oShape),group);
    }
    public void CircleShape(Object oShape)
    {
        group.clearChildren();
        group.addActor(actor);
        shape = new CirclePointed(oShape,group);
    }
}
