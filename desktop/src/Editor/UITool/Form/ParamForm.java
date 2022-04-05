package Editor.UITool.Form;

import Editor.JFameUI;
import GameGDX.GDX;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParamForm {
    private static Map<String,String> selectMap = new HashMap<>();

    public JPanel panel1;
    private JList list1;
    private JButton btNew;
    private JButton btDelete;
    private JTextField tfValue;
    private JTextField tfName;
    private JButton btPaste;
    private JButton btSelect;
    private JComboBox cbSelect;

    private JFameUI ui = new JFameUI();
    private Map<String,String> map;
    private IList iList;

    public ParamForm()
    {
        TextField(tfName,vl->{
            String key = (String) list1.getSelectedValue();
            String value = map.get(key);
            map.remove(key);
            map.put(vl,value);
        });
        ui.TextField(tfValue,"",vl->{
            String key = (String) list1.getSelectedValue();
            map.put(key,vl);
        });

        iList = new IList(list1,btNew,btDelete);
        iList.getData = ()->new ArrayList<>(map.keySet());
        iList.onAdd = ()->{
            String name = tfName.getText();
            map.put(name,"");
            return name;
        };
        iList.onDelete = name->map.remove(name);
        iList.onSelect = this::OnSelect;

        btSelect.addActionListener(e->{
            Select(iList.GetSelectedList());
        });
        btPaste.addActionListener(e->{
            Paste();
        });
    }
    public void SetData(Map<String,String> map)
    {
        this.map = map;
        iList.Init();
    }
    private void OnSelect(String name)
    {
        tfName.setText(name);
        tfValue.setText(map.get(name));
    }
    private void TextField(JTextField textField, GDX.Runnable<String> onChange)
    {
        textField.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode()==KeyEvent.VK_ENTER) onChange.Run(textField.getText());
        }
        });
    }

    private void Select(List<String> list)
    {
        selectMap.clear();
        for (String k : list)
            selectMap.put(k,map.get(k));
        ui.ComboBox(cbSelect,list.toArray());
    }
    private void Paste()
    {
        for (String k : selectMap.keySet())
            map.put(k,selectMap.get(k));
        iList.Refresh();
    }
}
