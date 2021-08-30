package Editor.UITool.Pointed;

import Extend.GShapeRenderer;
import GameGDX.GDX;
import GameGDX.GUIData.IImage;
import GameGDX.Scene;
import GameGDX.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.HashMap;
import java.util.Map;

public class Pointed extends Actor {
    public static float size = 20;
    protected Map<Image, Vector2> map = new HashMap<>();
    protected Image selected;
    protected GShapeRenderer renderer;
    protected Group group;

    public Pointed(Group group)
    {
        this.group = group;
        Scene.SetBounds(this,group.getWidth()/2,group.getHeight()/2, Align.center,group.getWidth(),group.getHeight(),group);
        renderer = new GShapeRenderer(Scene.GetUICamera(),group);
    }
    protected void Selected(Image img)
    {
        if (selected!=null) selected.setColor(Color.WHITE);
        selected = img;
        selected.setColor(Color.BLUE);
    }
    protected Image NewPoint(Vector2 pos, GDX.Runnable<Vector2> onDrag)
    {
        Image img = IImage.NewImage(Color.WHITE,pos, Align.center,size,size,group);
        map.put(img,pos);

        img.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Selected(img);
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                Vector2 p = new Vector2(Gdx.input.getX(),Gdx.input.getY());
                p = group.screenToLocalCoordinates(p);
                Util.Round(p);
                img.setPosition(p.x,p.y,Align.center);
                onDrag.Run(p);
            }
        });
        return img;
    }
    protected Vector2 GetPos(Vector2 p)
    {
        return group.localToStageCoordinates(new Vector2(p));
    }
    public void Resize(float s)
    {
        size = s;
        for (Image i : map.keySet())
        {
            Vector2 p = Scene.GetPosition(i,Align.center);
            i.setSize(size,size);
            Scene.SetPosition(i,p,Align.center);
        }
    }
}
