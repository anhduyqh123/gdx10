package Editor.UITool.Form;

import Editor.JFameUI;
import Editor.UITool.Form.Panel.ComponentPanel;
import Extend.Box2d.*;
import Extend.Box2d.IJoint.*;
import Extend.GShape.IMask;
import Extend.GShape.IMaskGroup;
import Extend.GShape.IShape;
import Extend.ILineRenderer;
import GameGDX.GUIData.IChild.Component;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.Reflect;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
    private JButton btClone;

    private JFameUI ui = new JFameUI();

    public ComponentForm(IActor iActor, JPanel parent)
    {
        parent.add(panel1);
        Map<String,Component> map = iActor.GetComponentData();;

        Class[] types = {IBody.class, IWater.class, IPlatform.class,IConveyor.class,
                IDistance.class, IRope.class, IWheel.class, IRevolute.class, IPrismatic.class,IPulley.class,IGear.class,
                IRayCast.class, ILineRenderer.class, IMask.class, IMaskGroup.class, IShape.class};
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
            p.New();
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

        tfName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    String old = (String) list1.getSelectedValue();
                    Component p = map.get(iList.GetSelected());
                    map.remove(old);
                    map.put(tfName.getText(),p);
                    iList.Refresh();
                    list1.setSelectedValue(tfName.getText(),true);
                }
            }
        });

        btClone.addActionListener(e->{
            if (map.containsKey(tfName.getText())){
                ui.NewDialog("The same name",panel1);
                return;
            }
            Component p = map.get(iList.GetSelected());
            Component newP = Reflect.Clone(p);
            map.put(tfName.getText(),newP);
            iList.Refresh();
            list1.setSelectedValue(tfName.getText(),true);
        });
    }
}
