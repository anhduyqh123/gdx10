package Editor.UITool.Physics;

import Editor.JFameUI;
import Editor.UITool.Physics.Util.*;
import GameGDX.GUIData.IChild.IActor;

import javax.swing.*;

public class UtilForm {
    public JPanel panel1;
    private JComboBox cbType;
    private JButton createButton;
    private JPanel pnBoard;
    private IActor iActor;

    private JFameUI ui = new JFameUI();
    private UtilNode node;

    public UtilForm(IActor iActor)
    {
        JFrame jFrame = ui.NewJFrame("Util Form",panel1);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        this.iActor = iActor;

        String[] arr = {"Clone","Rope","Revolute","Distance"};
        ui.ComboBox(cbType, arr,arr[0],cb->{
            if (cb.equals(arr[0])) InitPanel(new NClone());
            if (cb.equals(arr[1])) InitPanel(new NRope());
            if (cb.equals(arr[2])) InitPanel(new NRevolute());
            if (cb.equals(arr[3])) InitPanel(new NDistance());
        });

        ui.Button(createButton,()->{
            if (node==null) return;
            node.Create(iActor);
            ui.NewDialog("success!",panel1);
        });
    }
    private void InitPanel(UtilNode node)
    {
        this.node = node;
        ui.ClearPanel(pnBoard);
        ui.InitComponents(node,pnBoard);
    }

}
