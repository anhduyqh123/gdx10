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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ITextureAction)) return false;
        if (!super.equals(o)) return false;
        ITextureAction that = (ITextureAction) o;
        return txtName.equals(that.txtName);
    }
}
