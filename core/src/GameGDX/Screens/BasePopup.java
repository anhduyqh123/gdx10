package GameGDX.Screens;

import GameGDX.GUIData.IImage;
import GameGDX.Scene;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class BasePopup extends BaseScreen{

    protected Image overlay;

    @Override
    protected void InitMain() {
        overlay = IImage.NewImage(Color.BLACK,this);
        overlay.getColor().a = 0.5f;
        main = new Group();
        main.setSize(Scene.width,Scene.height);
        this.addActor(main);
    }

    @Override
    protected void ShowAction() {
        main.addAction(ScaleAction(0,1,0.4f,Interpolation.bounceOut,showDone));
    }

    @Override
    protected void HideAction() {
        main.addAction(ScaleAction(1,0,0.2f,Interpolation.fade,hideDone));
    }

    protected Action ScaleAction(float from, float to, float duration, Interpolation interpolation, Runnable done)
    {
        Action ac1 = Actions.scaleTo(from,from);
        Action ac2 = Actions.scaleTo(to,to,duration,interpolation);
        Action ac3 = Actions.run(done);
        return Actions.sequence(ac1,ac2,ac3);
    }
}
