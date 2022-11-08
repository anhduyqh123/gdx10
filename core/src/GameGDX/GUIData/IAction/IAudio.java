package GameGDX.GUIData.IAction;

import GameGDX.GAudio;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IAudio extends IAction{
    public enum Type
    {
        Sound,
        Music,
        Vibrate
    }

    public Type type = Type.Sound;
    public float volume = 1;//-1 is switch

    public IAudio()
    {
        name = "iAudio";
    }

    @Override
    public Action Get(IActor iActor) {
        return Actions.run(()->Run(iActor));
    }

    @Override
    public void Run(IActor iActor) {
        if (volume<=-1) Switch();
        else Run();
    }

    private void Run()
    {
        if (type==Type.Sound) GAudio.i.SetSoundVolume(volume);
        if (type==Type.Music) GAudio.i.SetMusicVolume(volume);
        if (type==Type.Vibrate) GAudio.i.SetVibrateVolume(volume);
    }
    private void Switch()
    {
        if (type== Type.Sound) GAudio.i.SwitchSound();
        if (type== Type.Music) GAudio.i.SwitchMusic();
        if (type== Type.Vibrate) GAudio.i.SwitchVibrate();
    }
}
