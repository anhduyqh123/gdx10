package GameGDX.GUIData.IAction;

import GameGDX.Assets;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IImage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

public class ITextureAction extends IAction{

    public String txtName = "";
    public boolean keepSize;

    public ITextureAction()
    {
        name = "texture";
    }

    @Override
    public Action Get(IActor iActor) {
        return Actions.run(()->Run(iActor));
    }
    public void Run(IActor iActor)
    {
        IImage iImage = (IImage)iActor;
        iImage.SetTexture(txtName);
        if (keepSize)
        {
            Vector2 p = iImage.GetPos(Align.center);
            TextureRegion tr = Assets.GetTexture(txtName);
            iImage.GetActor().setSize(tr.getRegionWidth(), tr.getRegionHeight());
            iImage.SetPos(p,Align.center);
        }
    }
}
