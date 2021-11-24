package GameGDX.GUIData.IAction;

import GameGDX.GAudio;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class ISwitchEvent extends IAction{
    public enum Type{
        Sound,
        Music,
        Vibrate
    }
    private Type type = Type.Sound;

    public ISwitchEvent()
    {
        name = "switch_event";
    }

    @Override
    public Action Get(IActor iActor) {
        return Actions.run(this::Run);
    }

    @Override
    public void Run(IActor iActor) {

    }

    private void Run()
    {
        if (type==Type.Sound) GAudio.i.SwitchSound();
        if (type==Type.Music) GAudio.i.SwitchMusic();
        if (type==Type.Vibrate) GAudio.i.SwitchVibrate();
    }
}
