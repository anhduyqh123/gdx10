package Editor.UITool.Pointed;

import Extend.GShape.Shape;
import com.badlogic.gdx.scenes.scene2d.Group;

public class LinePointed extends Pointed{
    public LinePointed(Shape.Line line,Group group) {
        super(group);
        NewPoint(line.pos1,p->{
            line.pos1.set(p);
        });
        NewPoint(line.pos2,p->{
            line.pos2.set(p);
        });
    }
}
