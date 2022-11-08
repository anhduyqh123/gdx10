package Editor.UITool.Physics.Util;

import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IGroup;

import java.util.ArrayList;
import java.util.List;

public class NClone extends UtilNode {
    public int num = 4;
    public float distance = 100;

    public void Create(IActor iActor)
    {
        IGroup iGroup = (IGroup) iActor;
        List<IActor> list = new ArrayList<>();
        iGroup.ForEach(list::add);
        int j = 0;
        for (int i=list.size();i<num;i++)
        {
            IActor child = list.get(j++);
            if (j>=list.size()) j=0;
            IActor clone = child.Clone();
            clone.iPos.delX = iGroup.GetChildren().size()*distance;
            iGroup.AddChildAndConnect("i"+i,clone);
        }
        iGroup.Refresh();
    }
}
