package Editor.UITool.Physics;

import Extend.Box2d.GBox2d;
import Extend.Box2d.IBody;
import GameGDX.GDX;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.Scene;
import GameGDX.Util;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Box2DMouse {

    private MouseJoint mouseJoint;
    public Box2DMouse(IActor iActor)
    {
        IBody iBody = iActor.GetComponent(IBody.class);
        if (iBody==null) return;
        MouseJointDef def = MouseJointDef();
        iActor.GetActor().addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button!=0) return false;
                Body body = iBody.GetBody();
                if (body==null) return false;
                def.bodyB = body;
                Vector2 p = GBox2d.GameToPhysics(Scene.GetMousePos());
                def.target.set(p);
                GDX.PostRunnable(()->{
                    mouseJoint = (MouseJoint) GBox2d.CreateJoint(def);
                });
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (mouseJoint==null) return;
                Drag(Scene.GetMousePos());
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (mouseJoint!=null)
                    GBox2d.DestroyJoint(mouseJoint);
                mouseJoint = null;
            }
        });
    }
    private Body NewEdge()
    {
        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(-500,-200,500,-200);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = GBox2d.NewBody(bodyDef);
        body.createFixture(edgeShape,0);
        edgeShape.dispose();
        return body;
    }
    private MouseJointDef MouseJointDef()
    {
        MouseJointDef jointDef = new MouseJointDef();
        jointDef.bodyA = NewEdge();
        jointDef.frequencyHz = 30;
        jointDef.dampingRatio = 0.2f;
        jointDef.maxForce = 20;
        jointDef.collideConnected = true;

        return jointDef;
    }
    private void Drag(Vector2 gamePos)
    {
        if (mouseJoint==null) return;
        Vector2 p = GBox2d.GameToPhysics(gamePos);
        mouseJoint.setTarget(p);
    }
}
