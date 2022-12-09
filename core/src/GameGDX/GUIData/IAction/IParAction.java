package GameGDX.GUIData.IAction;

import GameGDX.Actors.Particle;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IParAction extends IAction{
    public enum State{
        Reset,
        Play,
        PlayAndDestroy,
        Stop
    }
    public State state = State.Play;
    public IParAction()
    {
        name = "particle";
    }

    @Override
    public Action Get(IActor iActor) {
        return Actions.run(()->Run(iActor));
    }

    public void Run(IActor iActor) {
        SetState(iActor.GetActor());
    }
    private void SetState(Particle par)
    {
        if (state== State.Play) par.Start();
        if (state== State.PlayAndDestroy) par.Start(true);
        if (state== State.Stop) par.Stop();
        if (state== State.Reset) par.Reset();
    }
}
