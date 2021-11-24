package GameGDX.GUIData.IAction;

import GameGDX.Assets;
import GameGDX.GAudio;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class ISoundAction extends IAction{
    public enum Play
    {
        Play,
        Loop,
        Pause,
        Stop
    }
    public enum Stage
    {
        UI,
        Game
    }
    public enum Type
    {
        Sound,
        Music,
        Vibrate
    }

    public Stage stage = Stage.UI;
    public Type type = Type.Sound;
    public Play play = Play.Play;
    public String value = "";

    public ISoundAction()
    {
        name = "sound";
    }

    @Override
    public Action Get(IActor iActor) {
        return Actions.run(()->Run(iActor));
    }
    public void Run(IActor iActor)
    {
        try {
            if (type==Type.Music) RunMusic();
            else PlaySound(iActor);
            GAudio.i.PlaySound(value);
        }catch (Exception e){}
    }
    private void RunMusic()
    {
        if (play==Play.Play) GAudio.i.StartMusic(value);
        if (play==Play.Stop) Assets.GetMusic(value).stop();
        if (play==Play.Pause) Assets.GetMusic(value).pause();
    }
    private void PlaySound(IActor iActor)
    {
        if (stage==Stage.UI) GAudio.i.PlaySound(value);
        else iActor.PlaySound(value,play==Play.Loop);
    }
}
