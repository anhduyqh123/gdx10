package Editor.UI2Tool.Form;

import Editor.UITool.Form.Panel.ContentPanel;
import Extend.Spine.GSpine;
import Extend.Spine.ISpine;
import GameGDX.GUIData.IChild.IActor;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Content2Panel extends ContentPanel {

    @Override
    public Class[] GetTypes() {
        List<Class> list = new ArrayList<>(Arrays.asList(super.GetTypes()));
        list.add(ISpine.class);
        return list.toArray(new Class[0]);
    }

    @Override
    public JPanel SetContent(JPanel panel, IActor iActor) {
        if (iActor instanceof ISpine) return new JISpine(iActor,panel);
        return super.SetContent(panel, iActor);
    }

    class JISpine extends JIActor
    {
        public JISpine(IActor iActor, JPanel panel)
        {
            super(iActor,panel);
            GSpine spine = iActor.GetActor();
            String[] ani = spine.GetAnimationNames();
            if (ani!=null)
                ui.NewComboBox("animation",ani,ani[0],panel,st->spine.SetAnimation(st,true));
        }

        @Override
        protected boolean InValidField(String name) {
            if (name.equals("animation")) return true;
            return false;
        }
    }
}
