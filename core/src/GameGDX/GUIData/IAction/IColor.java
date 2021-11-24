package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IColor extends IBaseAction{

    public String hexColor = Color.WHITE.toString();

    public IColor()
    {
        name = "color";
    }
    private Color GetColor()
    {
        return Color.valueOf(hexColor);
    }
    public Action Get(Color color) {
        return Actions.color(color,duration, iInter.value);
    }

    @Override
    public Action Get(IActor iActor) {
        if (current) return Get(iActor.GetActor().getColor());
        else return Get(GetColor());
    }
}
