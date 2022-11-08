package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IColor extends IBaseAction{

    public String hexColor = Color.WHITE.toString();
    public boolean used = true;//by used current

    public IColor()
    {
        name = "color";
    }
    private Color GetColor()
    {
        return Color.valueOf(hexColor);
    }
    public Action Get(Color color) {
        return Actions.color(color,GetDuration(), iInter.value);
    }

    @Override
    public Action Get(IActor iActor) {
        Color color = used?GetColor():iActor.GetActor().getColor();
        if (GetDuration()<=0) return Actions.run(()->iActor.SetColor(color));
        return Get(color);
    }
}
