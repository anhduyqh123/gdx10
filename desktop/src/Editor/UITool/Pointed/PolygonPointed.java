package Editor.UITool.Pointed;

import Extend.Box2d.IShape;
import GameGDX.GDX;
import GameGDX.Util;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.List;

public class PolygonPointed extends Pointed {
    protected List<Vector2> points;

    public PolygonPointed(IShape.IPolygon iPolygon, Group group)
    {
        super(group);
        this.points = iPolygon.points;
        addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selected==null) return;
                Vector2 p = new Vector2(x,y);
                Selected(NewPoint(p));
            }
        });

        for(Vector2 p : points)
            NewPoint(p,x->{
                p.set(x);
                SetRender(points);
            });
        SetRender(points);
    }
    protected Image NewPoint(Vector2 pos, GDX.Runnable<Vector2> onDrag)
    {
        Image img = super.NewPoint(pos, onDrag);

        img.addListener(new ClickListener(Input.Buttons.RIGHT){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                img.remove();
                selected = null;
                Vector2 p = map.get(img);
                points.remove(p);
                SetRender(points);
            }
        });
        return img;
    }
    protected Image NewPoint(Vector2 p)
    {
        Vector2 p0 = map.get(selected);
        int index = points.indexOf(p0);
        float dis1 = GetDistance(p,p0,GetPos(p0,1));
        float dis2 = GetDistance(p,p0,GetPos(p0,-1));
        if (dis1<dis2) index = index+1;
        points.add(index,p);
        SetRender(points);
        return NewPoint(p,x->{
            p.set(x);
            SetRender(points);
        });
    }
    protected float GetDistance(Vector2 pos,Vector2 p1, Vector2 p2)
    {
        if (p1.equals(p2)) return Float.MAX_VALUE;
        return Util.GetDistance(pos,p1,p2);
    }
    protected Vector2 GetPos(Vector2 p,int del)
    {
        int index = points.indexOf(p)+del;
        if (index>=points.size()) index = 0;
        if (index<0) index = points.size()-1;
        return points.get(index);
    }
    protected void SetRender(List<Vector2> list)
    {
        renderer.Clear();
        for(int i=0;i<list.size()-1;i++)
        {
            Vector2 p0 = list.get(i);
            Vector2 p1 = list.get(i+1);
            renderer.NewLine(GetPos(p0),GetPos(p1));
        }
        renderer.NewLine(GetPos(list.get(list.size()-1)),GetPos(list.get(0)));
    }
}
