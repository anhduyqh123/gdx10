package GameGDX.Screens;

import GameGDX.GUIData.IImage;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Popup extends Screen{

    protected Image overlay;
    public Popup(String name)
    {
        super(name);
        overlay = IImage.NewImage(Color.BLACK,this);
        overlay.getColor().a = 0.5f;
        overlay.toBack();
    }
    public Image GetOverlay()
    {
        return overlay;
    }
}
