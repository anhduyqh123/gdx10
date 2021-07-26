package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import GameGDX.Actors.Particle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IParAction extends IAction{
    public enum State{
        Reset,
        Play,
        Stop
    }
    public State state = State.Play;
    public IParAction()
    {
        name = "particle";
    }
    @Override
    public Action Get() {
        return null;
    }

    @Override
    public Action Get(IActor iActor) {
        return Actions.run(()->{
            Particle par = iActor.GetActor();
            if (state== State.Play) par.Start();
            if (state== State.Stop) par.Stop();
            if (state== State.Reset) par.Reset();
        });
    }
}
