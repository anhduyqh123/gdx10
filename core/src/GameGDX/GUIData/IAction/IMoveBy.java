package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IMoveBy extends IBaseAction{
    public float delX,delY;

    public IMoveBy()
    {
        name = "moveBy";
    }
    @Override
    public Action Get() {
        return Actions.moveBy(delX,delY,duration, iInter.value);
    }

    @Override
    public Action Get(IActor iActor) {
        return Get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IMoveBy)) return false;
        if (!super.equals(o)) return false;
        IMoveBy iMoveBy = (IMoveBy) o;
        return Float.compare(iMoveBy.delX, delX) == 0 && Float.compare(iMoveBy.delY, delY) == 0;
    }
}
