package Extend;

import GameGDX.Assets;
import GameGDX.GDX;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IChild.IAlign;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class ITextField extends IActor {
    public String font = "font";
    public String text = "text";
    public IAlign alignment = IAlign.center;

    @Override
    protected Actor NewActor() {

        return new TextField(text,NewStyle());
    }
    private TextField.TextFieldStyle NewStyle()
    {
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font = Assets.GetFont(font);
        style.fontColor = GetColor();
        return style;
    }
    @Override
    public void SetConnect(GDX.Func1 connect) {
        super.SetConnect(connect);
        iSize.getDefaultSize = () ->{
            TextField lb = GetActor();
            return new Vector2(lb.getPrefWidth(),lb.getPrefHeight());
        };
    }
    @Override
    public void RefreshContent() {
        TextField tf = GetActor();
        tf.setStyle(NewStyle());
        tf.setAlignment(alignment.value);
    }

    @Override
    public void Refresh() {
        super.Refresh();
        RefreshContent();
    }

    @Override
    protected void Clear() {

    }

    @Override
    protected void AfterRefresh() {

    }
}
