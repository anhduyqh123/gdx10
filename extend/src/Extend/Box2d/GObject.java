package Extend.Box2d;

import GameGDX.Actors.GSprite;
import GameGDX.GDX;
import GameGDX.Reflect;
import GameGDX.Scene;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class GObject extends GSprite {
    private Body body;
    public GDX.Runnable<GObject> onBeginContact,onEndContact;
    public String category = "";

    public Body GetBody()
    {
        return body;
    }
    public void SetBody(Body nBody)
    {
        if (!Reflect.Equals(body,nBody)) DestroyBody();
        body = nBody;
        if (body!=null) body.setUserData(this);
    }
    public void Refresh()
    {
        if (body==null) return;
        Vector2 pos = Scene.GetStagePosition(this);
        GBox2d.SetTransform(body,pos,getRotation());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (body!=null)
        {
            Vector2 pos = GBox2d.GetGamePosition(body);
            Scene.SetPosition(this,pos);
            setRotation((float) Math.toDegrees(body.getAngle()));
        }
    }
    public Vector2 GetCenter()
    {
        return GBox2d.GetGameCenter(body);
    }

    public void DestroyBody()
    {
        if (body!=null) GBox2d.Destroy(body);
        body = null;
    }

    @Override
    public boolean remove() {
        DestroyBody();
        return super.remove();
    }
    public void OnBeginContact(GObject object)
    {
        if (onBeginContact!=null)
            onBeginContact.Run(object);
    }
    public void OnEndContact(GObject object)
    {
        if (onEndContact!=null)
            onEndContact.Run(object);
    }
}