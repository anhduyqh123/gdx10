package Editor.UITool.Physics;

import Editor.JFameUI;
import Editor.UITool.Form.IList;
import Extend.Box2d.IContact.IContact;
import Extend.Box2d.IContact.IInitBody;
import Extend.Box2d.IContact.XParam;
import Extend.Frame.IAnim;
import GameGDX.Reflect;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ContactForm {
    private JPanel panel1;
    private JList list1;
    private JComboBox cbType;
    private JButton btAdd;
    private JButton deleteButton;
    private JPanel pnInfo;
    private JPanel pnMain;

    private JFameUI ui = new JFameUI();

    public ContactForm(IContact data)
    {
        JFrame jFrame = ui.NewJFrame("JointForm",panel1);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        List<String> fields = ui.GetFields(data);
        fields.remove("paramList");
        ui.InitComponents(fields,data,pnMain);

        Class[] types = {XParam.Velocity.class,XParam.AngularVelocity.class};
        String[] arr = ui.ClassToName(types);
        ui.ComboBox(cbType,arr);

        List<XParam> list = data.paramList;
        IList iList = new IList(list1,btAdd,deleteButton);
        iList.getData = ()->{
            List<String> l = new ArrayList<>();
            int i=0;
            for (XParam x : list) l.add("param_"+i++);
            return l;
        };
        iList.onAdd = ()->{
            int index = cbType.getSelectedIndex();
            XParam xParam = Reflect.NewInstance(types[index]);
            list.add(xParam);
            return "param_"+(list.size()-1);
        };
        iList.onDelete = n->{
            int index = Integer.parseInt(n.replace("param_",""));
            list.remove(index);
        };
        iList.onSelect = n->{
            int index = Integer.parseInt(n.replace("param_",""));
            Init(list.get(index));
        };
        iList.Init();
    }
    private void Init(XParam xParam)
    {
        pnInfo.removeAll();
        ui.InitComponents(xParam,pnInfo);
        ui.Repaint(pnInfo);
    }
}
