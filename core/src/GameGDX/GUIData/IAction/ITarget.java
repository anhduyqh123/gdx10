package GameGDX.GUIData.IAction;

import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IGroup;
import GameGDX.Util;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.Arrays;
import java.util.List;

public class ITarget extends IParallel{
    public enum Type{
        Neighbor,
        Child
    }
    public String targetName = "";//""=> parent,"all"->all child
    public Type type = Type.Neighbor;

    public ITarget() {
        name = "target";
    }

    @Override
    public Action Get(IActor iActor) {
//        IActor iTarget = IActor.GetIActor(iActor.GetActor(targetName));
//        Action action = super.Get(iTarget);
//        return Actions.run(()->iTarget.GetActor().addAction(action));
        return Actions.run(()->{
            List<IActor> targets = GetTarget(iActor);
            Util.For(targets,this::AddAction);
        });
    }
    private void AddAction(IActor iActor)
    {
        ForChild(i->i.SetIActor(iActor));
        iActor.GetActor().addAction(super.Get(iActor));
    }
    @Override
    public void Run(IActor iActor) {
//        IActor iTarget = IActor.GetIActor(iActor.GetActor(targetName));
//        super.Run(iTarget);
        Util.For(GetTarget(iActor), super::Run);
    }
    private List<IActor> GetTarget(IActor iActor)
    {
        if (type==Type.Neighbor) return Arrays.asList(IActor.GetIActor(iActor.GetActor(targetName)));
        IGroup iGroup = (IGroup) iActor;
        if (targetName.equals("all")) return iGroup.GetChildren();
        return Arrays.asList(iGroup.FindIChild(targetName));
    }
}
