package Extend.Swipe;

import GameGDX.Assets;
import GameGDX.GDX;
import GameGDX.GUIData.IChild.Component;
import GameGDX.GUIData.IChild.IAlign;
import GameGDX.GUIData.IImage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ITrail extends Component {
    public int maxPoint = 10;
    public float thickness = 30f;//the thickness of the line
    public float endcap = 10f;//the endcap scale
    public String texture = "";
    public float delX,delY;
    public IAlign iAlign = IAlign.center;

    public GDX.Func<TrailRenderer> renderer;

    @Override
    public void Refresh() {
        renderer = null;
        GDX.PostRunnable(()->{
            TrailRenderer trail = new TrailRenderer(maxPoint);
            trail.tris.thickness = thickness;
            trail.tris.endcap = endcap;
            trail.SetTexture(texture.equals("")?IImage.emptyTexture:Assets.GetTexture(texture).getTexture());
            Vector2 pos = GetIActor().GetPos(iAlign.value).add(delX,delY);
            trail.Start(pos.x,pos.y);
            renderer = ()->trail;
        });
    }

    @Override
    protected void Update(float delta) {
        if (renderer==null) return;
        Vector2 pos = GetIActor().GetPos(iAlign.value).add(delX,delY);
        renderer.Run().Update(pos.x,pos.y);
    }

    @Override
    public void Draw(Batch batch, float parentAlpha, Runnable onDraw) {
        onDraw.run();
        if (renderer==null) return;
        Actor actor = GetActor();
        batch.end();
        batch.begin();
        renderer.Run().Draw(actor.getColor(),actor.getStage().getCamera());
        batch.end();
        batch.begin();
    }
}
