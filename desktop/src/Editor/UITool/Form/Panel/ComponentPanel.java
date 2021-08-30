package Editor.UITool.Form.Panel;

import Editor.UITool.Form.PhysicsForm;
import Editor.UITool.Physics.ContactForm;
import Editor.UITool.Physics.JointForm;
import Extend.Box2d.IBody;
import Extend.Box2d.IContact.IContact;
import Extend.Box2d.IJoint.IJoint;
import GameGDX.GDX;
import GameGDX.GUIData.IChild.Component;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IGroup;

import java.util.ArrayList;
import java.util.List;

public class ComponentPanel {

    public ComponentPanel(Component cp, IActor iActor)
    {
        if (cp instanceof IBody)
        {
            IBody body = (IBody) cp;
            GDX.PostRunnable(()->new PhysicsForm(body,iActor));
        }
        if (cp instanceof IJoint)
        {
            IGroup iGroup = (IGroup)iActor;
            List<String> list = new ArrayList<>(iGroup.GetChildName());
            new JointForm(list,(IJoint) cp,()->iGroup.Refresh());
        }
        if (cp instanceof IContact)
        {
            new ContactForm((IContact)cp);
        }
    }
}
