package Editor.UITool.Physics;

import Editor.JFameUI;
import Editor.WrapLayout;
import Extend.Box2d.IJoint.IJoint;

import javax.swing.*;
import java.util.List;

public class JointForm {
    private JPanel panel1;
    private JPanel pnJoint;

    private JFameUI ui = new JFameUI();

    public JointForm(List<String> names, IJoint iJoint, Runnable onClose)
    {
        JFrame jFrame = ui.NewJFrame("JointForm",panel1,onClose);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pnJoint.setLayout(new WrapLayout());
        InitIJoint(names,iJoint);
    }
    private void InitIJoint(List<String> names, IJoint iJoint)
    {
        //pnJoint.removeAll();
        ui.NewLabel(iJoint.getClass().getSimpleName(),pnJoint);
        List<String> fields = ui.GetFields(iJoint);
//        fields.remove("objectA");fields.remove("objectB");
//        if (iJoint.objectA.equals("")) iJoint.objectA = names.get(0);
//        if (iJoint.objectB.equals("")) iJoint.objectB = names.get(0);
//        ui.NewComboBox("objectA",names.toArray(new String[0]),iJoint.objectA,pnJoint,vl->iJoint.objectA=vl);
//        ui.NewComboBox("objectB",names.toArray(new String[0]),iJoint.objectB,pnJoint,vl->iJoint.objectB=vl);
        ui.InitComponents(fields,iJoint,pnJoint);
        //ui.Repaint(pnJoint);
    }
}
