package Editor.UITool.Form;

import Editor.JFameUI;
import Editor.UITool.Form.Panel.ComponentPanel;
import Extend.Box2d.*;
import Extend.Box2d.IContact.IBegin;
import Extend.Box2d.IContact.IInitBody;
import Extend.Box2d.IJoint.*;
import GameGDX.GUIData.IChild.Component;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.Reflect;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;

public class ComponentForm {
    private JPanel panel1;
    private JList list1;
    private JButton newButton;
    private JButton deleteButton;
    private JComboBox cbType;
    private JTextField tfName;
    private JButton selectButton;

    private JFameUI ui = new JFameUI();

    public ComponentForm(IActor iActor, JPanel parent)
    {
        parent.add(panel1);
        Map<String,Component> map = iActor.GetComponentData();;

        Class[] types = {IBody.class, IWater.class, IPlatform.class,
                IDistance.class, IRope.class, IWheel.class, IRevolute.class, IPrismatic.class,IGear.class,
                Rope.class, IInitBody.class, IBegin.class};
        String[] arr = ui.ClassToName(types);
        ui.ComboBox(cbType,arr,arr[0]);

        IList iList = new IList(list1,newButton,deleteButton);
        iList.getData = ()->new ArrayList<>(map.keySet());;
        iList.onAdd = ()->{
            int index = cbType.getSelectedIndex();
            Component p = Reflect.NewInstance(types[index]);
            String name = tfName.getText();
            map.put(name,p);
            iActor.Refresh();
            return name;
        };
        iList.onDelete = map::remove;
        iList.onSelect = name->{
            tfName.setText(name);
        };
        iList.Init();

        selectButton.addActionListener(e->{
            Component p = map.get(iList.GetSelected());
            new ComponentPanel(p,iActor);
        });
    }
}
