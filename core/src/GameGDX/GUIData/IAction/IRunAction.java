package GameGDX.GUIData.IAction;

import GameGDX.GDX;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.HashMap;
import java.util.Map;

public class IRunAction extends IAction {
    private static Map<String, GDX.Runnable<IActor>> map = new HashMap<>();
    public static <T extends IActor> void Add(String name,GDX.Runnable<T> run)
    {
        map.put(name, (GDX.Runnable<IActor>) run);
    }

    public GDX.Runnable<IActor> runnable;

    public IRunAction()
    {
        name = "run";
    }

    @Override
    public Action Get(IActor iActor) {
        GDX.Runnable<IActor> run = GetRun(iActor);
        return Actions.run(()->{
            if (run!=null) run.Run(iActor);
        });
    }

    @Override
    public void Run(IActor iActor) {
        GDX.Runnable<IActor> run = GetRun(iActor);
        if (run!=null) run.Run(iActor);
    }

    private GDX.Runnable<IActor> GetRun(IActor iActor)
    {
        if (runnable!=null) return runnable;
        GDX.Runnable<IActor> run = iActor.GetRunnable(name);
        if (run!=null) return run;
        return map.get(name);
    }
}
