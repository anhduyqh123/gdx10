package Extend;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;

import java.util.ArrayList;
import java.util.List;

public class MaskGroup extends Group {

    private List<Rectangle> rectList = new ArrayList<>();
    private Camera camera;

    public MaskGroup(Group parent)
    {
        parent.addActor(this);
        camera = parent.getStage().getCamera();
    }
    public void Add(Actor... actors)
    {
        for(Actor e : actors)
            rectList.add(NewRect(e));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for(Rectangle rect : rectList)
            if (IsPushed(rect,batch))
            {
                super.draw(batch, parentAlpha);
                batch.flush();
                ScissorStack.popScissors();
            }
    }
    private boolean IsPushed(Rectangle rect,Batch batch)
    {
        Rectangle scissors = new Rectangle();
        ScissorStack.calculateScissors(camera, batch.getTransformMatrix(), rect, scissors);
        return ScissorStack.pushScissors(scissors);
    }

    //static
    private static Rectangle NewRect(Actor actor)
    {
        return new Rectangle(actor.getX(),actor.getY(),actor.getWidth(),actor.getHeight());
    }
}
