package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import GameGDX.Actors.Particle;
import GameGDX.Scene;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

public class IParAction extends IAction{
    public enum State{
        New,
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
    public Action Get(IActor iActor) {
        return Actions.run(()->Run(iActor));
    }

    public void Run(IActor iActor) {
        if (state==State.New) New(iActor);
        else SetState(iActor.GetActor());
    }

    private void New(IActor iActor)
    {
        Vector2 p = Scene.GetPosition(iActor.GetActor(), Align.center);
        new Particle(name,p,iActor.GetActor("")).Start(true);
    }
    private void SetState(Particle par)
    {
        if (state== State.Play) par.Start();
        if (state== State.Stop) par.Stop();
        if (state== State.Reset) par.Reset();
    }
}
