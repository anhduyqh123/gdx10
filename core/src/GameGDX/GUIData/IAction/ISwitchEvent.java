package GameGDX.GUIData.IAction;

import GameGDX.Assets;
import GameGDX.GMusic;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IImage;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class ISwitchEvent extends IAction{
    public enum Type{
        Sound,
        Music,
        Vibrate
    }
    private Type type = Type.Sound;
    private String nameOn = "";
    private String nameOff = "";
    private boolean isSwitch;

    public ISwitchEvent()
    {
        name = "event";
    }
    @Override
    public Action Get() {
        return null;
    }

    @Override
    public Action Get(IActor iActor) {
        IImage img = (IImage) iActor;
        return Actions.run(()->{
            if (isSwitch) Switch();
            img.SetTexture(Assets.GetTexture(GetState()?nameOn:nameOff));
        });
    }
    private boolean GetState()
    {
        if (type== ISwitchEvent.Type.Sound) return GMusic.IsSound();
        if (type== ISwitchEvent.Type.Music) return GMusic.IsMusic();
        return GMusic.IsVibrate();
    }
    private void Switch()
    {
        if (type==Type.Sound){
            GMusic.SwitchSound();
            GMusic.PlaySound("pop");
        }
        if (type==Type.Music){
            GMusic.PlaySound("pop");
            GMusic.SwitchMusic();
        }
        if (type==Type.Vibrate){
            GMusic.SwitchVibrate();
            GMusic.DoVibrate(100);
        }
    }
}
