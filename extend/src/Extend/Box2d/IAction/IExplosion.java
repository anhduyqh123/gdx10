package Extend.Box2d.IAction;

import Extend.Box2d.GBox2d;
import Extend.Box2d.IJoint.IAnchor;
import Extend.Box2d.RayCast;
import GameGDX.GUIData.IAction.IAction;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IExplosion extends IAction {
    public IAnchor anchor = new IAnchor();
    public float blastPower=1,radius=100;
    public int rayNum = 10,category=1,mark=-1;

    public IExplosion()
    {
        name = "explosion";
    }

    @Override
    public Action Get(IActor iActor) {
        return Actions.run(()->Run(iActor));
    }
    public void Run(IActor iActor)
    {
        RayCast(anchor.GetPos(iActor.GetActor()),radius,blastPower,rayNum);
    }

    private void ApplyBlastImpulse(Body body, Vector2 blastCenter, Vector2 applyPoint, float blastPower) {
        blastCenter = GBox2d.GameToPhysics(blastCenter);
        applyPoint = GBox2d.GameToPhysics(applyPoint);
        Vector2 blastDir = new Vector2(applyPoint).sub(blastCenter);
        float distance = blastDir.len();
        //ignore bodies exactly at the blast point - blast direction is undefined
        if ( distance == 0 ) return;
        float invDistance = 1 / distance;
        float impulseMag = blastPower * invDistance * invDistance;
        body.applyLinearImpulse( blastDir.scl(impulseMag), applyPoint,true);
    }
    private void FindAllBodies(Vector2 center,float radius,float blastPower) //find all bodies with fixtures
    {
        GBox2d.world.QueryAABB(fixture -> {
            Body body = fixture.getBody();
            Vector2 pB = body.getWorldCenter();
            if (pB.sub(center).len()>=radius) return true;
            ApplyBlastImpulse(body,center,pB,blastPower);
            return true;
        }, center.x - radius, center.y-radius, center.x+radius, center.y+radius);
    }
    private void RayCast(Vector2 center,float radius,float blastPower,int numRay)
    {
        RayCast rayCast = new RayCast(name);
        rayCast.category = category;
        rayCast.mark = mark;

        for (int i=0;i<numRay;i++)
        {
            float angle = (i / (float)numRay) * 360f;
            float radian = (float) Math.toRadians(angle);
            Vector2 rayDir = new Vector2((float) Math.sin(radian),(float) Math.cos(radian));
            rayDir.scl(radius);
            Vector2 rayEnd = new Vector2(center).add(rayDir);

            rayCast.SetPoint(center,rayEnd);
            rayCast.RunClosest((ib, p)->{
                ApplyBlastImpulse(ib.GetBody(),center,p,blastPower);
            });
        }
    }
}
