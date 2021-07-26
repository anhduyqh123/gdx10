package Editor.UITool.Form;

import Editor.JFameUI;
import Editor.UITool.EConfig;
import Editor.UITool.UIConfig;
import GameGDX.Config;
import com.badlogic.gdx.utils.JsonValue;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigForm {
    private JPanel panel1;
    private JPanel pnConfig;
    private JButton btSave;
    private JButton btLoad;
    private JList list1;
    private JTextField tfName;
    private JButton btAdd;
    private JButton btDelete;
    private JComboBox cbType;
    private JPanel pnValue;
    private JButton btSave2;

    private JFameUI ui = new JFameUI();

    public ConfigForm()
    {
        JFrame jFrame = ui.NewJFrame("config",panel1);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        ui.InitComponents(Arrays.asList("screen_width","screen_height",
                "game_width","game_height"), UIConfig.i,pnConfig);

        Click(btSave,()->{
            UIConfig.Save();
            ui.NewDialog("Save Success!",panel1);
        });
        Click(btLoad,()-> UIConfig.i.Reload());

        InitConfig();
    }
    private void Click(JButton bt,Runnable run)
    {
        bt.addActionListener(e->ui.Try(run,panel1));
    }


    private void InitConfig()
    {
        Refresh();
        list1.addListSelectionListener(e->{
            int index = list1.getSelectedIndex();
            if (index<0) return;
            String name = (String) list1.getSelectedValue();
            tfName.setText(name);
            SetData(EConfig.e.Get(name));
        });
        String[] type = {"base","list"};
        ui.ComboBox(cbType,type);

        Click(btAdd,()->Add(tfName.getText(),(String) cbType.getSelectedItem()));
        Click(btDelete,()->{
            String name = (String) list1.getSelectedValue();
            EConfig.e.Remove(name);
            Refresh();
            list1.setSelectedIndex(0);
        });
        Click(btSave2,()->{
            EConfig.e.Save();
            ui.NewDialog("success!",panel1);
        });

        tfName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER) {
                    String old = (String) list1.getSelectedValue();
                    String name = tfName.getText();
                    EConfig.e.Rename(old,name);
                    Refresh();
                    list1.setSelectedValue(name,true);
                }
            }
        });

    }
    private void Refresh()
    {
        JsonValue data = EConfig.e.GetData();
        List<String> names = new ArrayList<>();
        for (JsonValue i : data) names.add(i.name);
        ui.ListSetModel(list1,names);
    }
    private void Add(String name,String type)
    {
        if (EConfig.e.Contains(name)) return;
        JsonValue js = new JsonValue("");
        if (type.equals("list")) js = new JsonValue(JsonValue.ValueType.array);
        EConfig.e.Add(name,js);
        Refresh();
        list1.setSelectedValue(name,true);
    }
    private void SetData(JsonValue value)
    {
        if (value.isArray()) SetArray(value);
        else SetObject(value);
    }
    private void SetArray(JsonValue value)
    {
        pnValue.removeAll();
        ListForm listForm = new ListForm(pnValue);
        listForm.getData = ()->{
            List<String> names = new ArrayList<>();
            for (JsonValue i : value) names.add(i.asString());
            return names;
        };
        listForm.onAdd = n->value.addChild(new JsonValue(n));
        listForm.onDelete = i->value.remove(i);
        listForm.onSelect = n->{};
        ui.Repaint(pnValue);
    }
    private void SetObject(JsonValue value)
    {
        pnValue.removeAll();
        ui.NewTextField("value",value.asString(),pnValue,vl->value.set(vl));
        ui.Repaint(pnValue);
    }
}
