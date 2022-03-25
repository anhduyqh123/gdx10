package Editor.UITool.Form.Panel;

import Editor.JFameUI;
import Editor.UITool.Form.MaskForm;
import Editor.UITool.Form.PhysicsForm;
import Editor.UITool.Form.ShapeForm;
import Editor.UITool.Physics.JointForm;
import Editor.UITool.Physics.MarkForm;
import Extend.Box2d.IBody;
import Extend.Box2d.IJoint.IJoint;
import Extend.Box2d.IRayCast;
import Extend.GShape.IMask;
import Extend.GShape.IShape;
import GameGDX.GDX;
import GameGDX.GUIData.IChild.Component;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IGroup;

import javax.swing.*;
import java.util.List;


public class ComponentPanel {

    public ComponentPanel(Component cp, IActor iActor)
    {
        if (cp instanceof IBody)
        {
            IBody body = (IBody) cp;
            GDX.PostRunnable(()->new PhysicsForm(body,iActor));
            return;
        }
        if (cp instanceof IJoint)
        {
            IGroup iGroup = (IGroup)iActor;
            new JointForm(iGroup.GetChildName(),(IJoint) cp,()->iGroup.Refresh());
            return;
        }
        if (cp instanceof IRayCast)
        {
            NewIRayCast((IRayCast) cp);
            return;
        }
        if (cp instanceof IMask)
        {
            NewIMark(cp);
            return;
        }
        if (cp instanceof IShape)
        {
            NewIShape(cp);
            return;
        }
        NewFrame(cp);
    }
    private void NewIRayCast(IRayCast iRayCast)
    {
        JFameUI ui = new JFameUI();
        JPanel pn = ui.NewPanel(500,400);
        List<String> list = ui.GetFields(iRayCast);
        list.remove("mark");
        list.remove("category");
        ui.InitComponents(list,iRayCast,pn);
        //ui.NewButton("mark",pn,()->new MarkForm(iRayCast.mark, m->iRayCast.mark=m));
        ui.NewButton("category",pn,()->
                new MarkForm(iRayCast.category, iRayCast.mark,(c,m)->{
                    iRayCast.category=c;
                    iRayCast.mark=m;
                }));
        ui.NewJFrame("Component",pn);
    }
    private void NewFrame(Component cp)
    {
        JFameUI ui = new JFameUI();
        JPanel pn = ui.NewPanel(500,400);
        ui.InitComponents(cp,pn);
        ui.NewJFrame("Componenet",pn);
    }
    private void NewIMark(Component cp)
    {
        new MaskForm((IMask) cp);
    }
    private void NewIShape(Component cp)
    {
        new ShapeForm((IShape) cp);
    }
}
