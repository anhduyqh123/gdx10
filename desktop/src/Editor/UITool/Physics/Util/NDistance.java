package Editor.UITool.Physics.Util;

import Extend.Box2d.IJoint.IDistance;
import GameGDX.GUIData.IChild.IAlign;
import GameGDX.GUIData.IGroup;

public class NDistance extends NRope{
    protected void Connect(int index,IGroup iGroup,String a1,String a2)
    {
        IDistance iDis = new IDistance();
        iDis.objectA = a1;
        iDis.objectB = a2;
        iDis.anchorA.iAlign = IAlign.right;
        iDis.anchorB.iAlign = IAlign.left;
        iGroup.GetComponentData().put("dis"+index,iDis);
    }
}
