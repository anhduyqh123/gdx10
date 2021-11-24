package GameGDX.GUIData.IAction;

import GameGDX.Assets;
import GameGDX.GAudio;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.Reflect;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IPlayAudio extends IAction{

    public Base base = new ISound();

    public IPlayAudio()
    {
        name = "play_audio";
    }

    @Override
    public Action Get(IActor iActor) {
        return Actions.run(()->Run(iActor));
    }

    @Override
    public void Run(IActor iActor) {
        base.Run(iActor);
    }

    public static abstract class Base
    {
        public String name = "";
        public abstract void Run(IActor iActor);
        @Override
        public boolean equals(Object obj) {
            return Reflect.equals(this,obj);
        }
    }
    public static class ISound extends Base
    {
        public enum Type
        {
            Play,
            Loop,
            Stop
        }
        public enum Stage
        {
            UI,
            Game
        }
        public Stage stage = Stage.UI;
        public Type type = Type.Play;

        public void Run(IActor iActor) {
            if (type==Type.Stop) GAudio.i.StopSound(name);
            else {
                if (stage== Stage.UI) GAudio.i.PlaySound(name);
                else iActor.PlaySound(name,type== Type.Loop);
            }
        }
    }
    public static class IMusic extends Base
    {
        public enum Play
        {
            Play,
            Pause,
            Stop
        }
        public Play play = Play.Play;

        @Override
        public void Run(IActor iActor) {
            if (play== Play.Play){
                GAudio.i.StopMusic();
                GAudio.i.StartMusic(name);
            }
            if (play== Play.Stop) Assets.GetMusic(name).stop();
            if (play== Play.Pause) Assets.GetMusic(name).pause();
        }
    }
}
