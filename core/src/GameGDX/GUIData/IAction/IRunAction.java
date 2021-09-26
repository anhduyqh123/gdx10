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
    public Action Get() {
        return null;
    }

    @Override
    public Action Get(IActor iActor) {
        GDX.Runnable<IActor> run = GetRun();
        return Actions.run(()->{
            if (run!=null) run.Run(iActor);
        });
    }
    private GDX.Runnable<IActor> GetRun()
    {
        if (map.containsKey(name)) return map.get(name);
        return runnable;
    }
}
