package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.ILabel;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class ILabelAction extends IAction{

    public String text = "",font = "";
    public boolean multiLanguage;

    public ILabelAction()
    {
        name = "label";
    }

    @Override
    public Action Get(IActor iActor) {
        return Actions.run(()->Run(iActor));
    }
    public void Run(IActor iActor)
    {
        ILabel iLabel = (ILabel)iActor;
        if (!font.equals("")) iLabel.SetFont(font);
        iLabel.SetText(multiLanguage?ILabel.GetTranslate(text):text);
    }
}
