package Editor.UITool.Physics.Util;

import Extend.Box2d.IJoint.IRope;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IGroup;

import java.util.List;

public class NRope extends UtilNode {
    public void Create(IActor iActor)
    {
        IGroup iGroup = (IGroup) iActor;
        List<IActor> children = iGroup.GetChildren();
        int len = children.size();
        for (int i=1;i<len;i++)
            Connect(i,iGroup,"i"+(i-1),"i"+i);
        iGroup.Refresh();
    }
    protected void Connect(int index,IGroup iGroup,String a1,String a2)
    {
        IRope iRope = new IRope();
        iRope.objectA = a1;
        iRope.objectB = a2;
        iGroup.GetComponentData().put("rope"+index,iRope);
    }
}
