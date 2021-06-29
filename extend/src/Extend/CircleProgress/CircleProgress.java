package Extend.CircleProgress;

import GameGDX.Actions.CountAction;
import GameGDX.GDX;
import GameGDX.GUIData.IImage;
import GameGDX.Scene;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CircleProgress extends Actor {
    public GDX.Runnable<Vector2> onChange;

    private final PolygonSprite sprite;
    private TextureRegion tr;
    private final List<Vector2> points = new ArrayList<>();
    private final Vector2 center = new Vector2();
    private Vector2 curPos;
    private float sizeX,sizeY,percent=1;

    public CircleProgress()
    {
        tr = new TextureRegion(IImage.emptyTexture);
        Init(tr.getRegionWidth(),tr.getRegionHeight());
        sprite = new PolygonSprite(GetRegion(360));
    }
    public void SetTexture(TextureRegion tr)
    {
        this.tr = tr;
        Init(tr.getRegionWidth(),tr.getRegionHeight());
    }
    private void Init(float width,float height)
    {
        sizeX = width/2;
        sizeY = height/2;
        center.set(width/2,height/2);
        points.clear();
        points.add(new Vector2(width/2,height));
        points.add(new Vector2(0,height));
        points.add(new Vector2(0,height/2));
        points.add(new Vector2(0,0));
        points.add(new Vector2(width/2,0));
        points.add(new Vector2(width,0));
        points.add(new Vector2(width,height/2));
        points.add(new Vector2(width,height));
    }
    public void CountValue(float percent,float duration)
    {
        float start = this.percent;
        float end = ValidPercent(percent);
        Action ac = CountAction.Get(this::SetValue,start,end,duration);
        addAction(ac);
    }
    private PolygonRegion GetRegion(float angle)
    {
        List<Vector2> triPos = GetTriPos(angle);
        float[] vert = GetVertices(GetPoints(triPos));
        EarClippingTriangulator triangulate = new EarClippingTriangulator();
        short[] tri = triangulate.computeTriangles(vert).toArray();
        return new PolygonRegion(tr,vert,tri);
    }
    private float ValidPercent(float percent)
    {
        if (percent>1) return 1;
        if (percent<0) return 0;
        return percent;
    }
    public void SetValue(float percent)
    {
        this.percent = ValidPercent(percent);
        sprite.setRegion(GetRegion(this.percent*360));
        if (onChange!=null) onChange.Run(Scene.GetPosition(this).add(curPos));
    }
    private Vector2 GetPos(float angle,float size,Vector2 p,Vector2 dir)
    {
        float x = (float)(size* Math.tan(Math.toRadians(angle)));
        dir.scl(x);
        curPos = dir.add(p);
        return curPos;
    }
    private List<Vector2> GetTriPos(float angle)
    {
        if (angle<=45) return Arrays.asList(points.get(0),GetPos(angle,sizeY,points.get(0),new Vector2(-1,0)),center);
        if (angle<=90) return Arrays.asList(points.get(1),GetPos(90-angle,sizeX,points.get(2),new Vector2(0,1)),center);
        if (angle<=135) return Arrays.asList(points.get(2),GetPos(angle-90,sizeX,points.get(2),new Vector2(0,-1)),center);
        if (angle<=180) return Arrays.asList(points.get(3),GetPos(180-angle,sizeY,points.get(4),new Vector2(-1,0)),center);
        if (angle<=225) return Arrays.asList(points.get(4),GetPos(angle-180,sizeY,points.get(4),new Vector2(1,0)),center);
        if (angle<=270) return Arrays.asList(points.get(5),GetPos(270-angle,sizeX,points.get(6),new Vector2(0,-1)),center);
        if (angle<=315) return Arrays.asList(points.get(6),GetPos(angle-270,sizeX,points.get(6),new Vector2(0,1)),center);
        return Arrays.asList(points.get(7),GetPos(360-angle,sizeY,points.get(0),new Vector2(1,0)),center);
    }
    private List<Vector2> GetPoints(List<Vector2> tri)
    {
        List<Vector2> list = new ArrayList<>();
        Vector2 p0 = tri.get(0);
        for(Vector2 p : points)
        {
            if (p0.equals(p)){
                list.addAll(tri);
                return list;
            }
            list.add(p);
        }
        return tri;
    }
    private float[] GetVertices(List<Vector2> list)
    {
        float[] vert = new float[list.size()*2];
        for (int i=0;i<list.size();i++)
        {
            vert[i*2] = list.get(i).x;
            vert[i*2+1] = list.get(i).y;
        }
        return vert;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setColor(getColor());
        sprite.setOrigin(getOriginX(),getOriginY());
        sprite.draw((PolygonSpriteBatch) batch,parentAlpha);
    }
    //override
    @Override
    protected void positionChanged() {
        sprite.setPosition(getX(),getY());
    }

    @Override
    protected void sizeChanged() {
        sprite.setSize(getWidth(),getHeight());
    }

    @Override
    protected void rotationChanged() {
        sprite.setRotation(getRotation());
    }

    @Override
    protected void scaleChanged() {
        sprite.setScale(getScaleX(),getScaleY());
    }
}
