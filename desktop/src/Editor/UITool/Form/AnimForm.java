package Editor.UITool.Form;

import Editor.JFameUI;
import Extend.Frame.IAnim;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;

public class AnimForm {
    private JPanel panel1;
    private JList list1;
    private JButton newButton;
    private JButton deleteButton;
    private JTextField tfName;
    private JPanel pnInfo;

    private JFameUI ui = new JFameUI();

    public AnimForm(Map<String, IAnim> data, Runnable onClose)
    {
        JFrame jFrame = ui.NewJFrame("animation",panel1,onClose);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        IList iList = new IList(list1,newButton,deleteButton);
        iList.getData = ()->new ArrayList<>(data.keySet());
        iList.onAdd = ()->{
            String name = tfName.getText();
            IAnim iAni = new IAnim();
            data.put(name,iAni);
            return name;
        };
        iList.onDelete = data::remove;
        iList.onSelect = name->{
            tfName.setText(name);
            Init(data.get(name));
        };
        iList.Init();
    }
    private void Init(IAnim iAni)
    {
        pnInfo.removeAll();
        ui.InitComponents(iAni,pnInfo);
        ui.Repaint(pnInfo);
    }
}
