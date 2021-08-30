package Extend.Box2d;

import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IGroup;
import GameGDX.Reflect;

import java.util.ArrayList;
import java.util.List;

public class GRope extends IGroup {
    public String childName = "i";
    public int amount;

    @Override
    public void Refresh() {
        InitActor();
        BaseRefresh();
        RefreshChildren();
    }
    private void RefreshChildren()
    {
        if (list.size()<=0) return;
        IActor child = GetIActor(list.get(0));
        List<IActor> iActors = new ArrayList<>();
        float dis = child.GetSize().x;

        for(int i=0;i<amount;i++)
        {
            IActor clone = Reflect.Clone(child);
            clone.SetConnect(this::GetActor);
            clone.iPos.delX=i*dis;
            iActors.add(clone);
        }
        for (IActor i : iActors) i.Refresh();
    }
}
