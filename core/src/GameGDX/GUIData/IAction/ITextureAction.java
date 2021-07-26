package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IImage;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class ITextureAction extends IAction{

    public String txtName = "";
    public ITextureAction()
    {
        name = "texture";
    }

    @Override
    public Action Get() {
        return null;
    }

    @Override
    public Action Get(IActor iActor) {
        IImage iImage = (IImage) iActor;
        return Actions.run(()->iImage.SetTexture(txtName));
    }
}
