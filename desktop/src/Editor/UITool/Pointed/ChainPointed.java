package Editor.UITool.Pointed;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.List;

public class ChainPointed extends PolygonPointed{
    public ChainPointed(List<Vector2> points, Group group) {
        super(points, group);
    }
    protected Vector2 GetPos(Vector2 p, int del)
    {
        int index = points.indexOf(p)+del;
        if (index>=points.size()) index = points.size()-1;
        if (index<0) index = 0;
        return points.get(index);
    }

    @Override
    protected void SetRender(List<Vector2> list) {
        renderer.Clear();
        for(int i=0;i<list.size()-1;i++)
        {
            Vector2 p0 = list.get(i);
            Vector2 p1 = list.get(i+1);
            renderer.NewLine(GetPos(p0),GetPos(p1));
        }
    }
}
