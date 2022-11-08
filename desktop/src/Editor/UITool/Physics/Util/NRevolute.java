package Editor.UITool.Physics.Util;

import Extend.Box2d.IJoint.IAnchor;
import Extend.Box2d.IJoint.IDistance;
import Extend.Box2d.IJoint.IRevolute;
import GameGDX.GUIData.IChild.IAlign;
import GameGDX.GUIData.IGroup;

public class NRevolute extends NRope {
    public IAnchor anchorA = new IAnchor();
    public IAnchor anchorB = new IAnchor();
    {
        anchorA.iAlign = IAlign.right;
        anchorB.iAlign = IAlign.left;
    }
    protected void Connect(int index, IGroup iGroup, String a1, String a2)
    {
        IRevolute iRevo = new IRevolute();
        iRevo.objectA = a1;
        iRevo.objectB = a2;
        iRevo.anchorA = anchorA;
        iRevo.anchorB= anchorB;
        iGroup.GetComponentData().put("revo"+index,iRevo);
    }
}
