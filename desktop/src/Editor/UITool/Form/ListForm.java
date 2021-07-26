package Editor.UITool.Form;

import GameGDX.GDX;

import javax.swing.*;
import java.util.List;

public class ListForm {
    private JList list1;
    private JPanel panel1;
    private JTextField tfName;
    private JButton btAdd;
    private JButton btDelete;

    public GDX.Func<List<String>> getData;
    public GDX.Runnable<String> onAdd,onSelect;
    public GDX.Runnable<Integer> onDelete;

    public ListForm(JPanel parent)
    {
        parent.add(panel1);

        list1.addListSelectionListener(e->{
            int index = list1.getSelectedIndex();
            if (index<0) return;
            onSelect.Run(GetSelected());
        });
        btAdd.addActionListener(e->{
            String name = tfName.getText();
            onAdd.Run(name);
            Refresh();
            list1.setSelectedValue(name,true);
        });
        btDelete.addActionListener(e->{
            int index = list1.getSelectedIndex();
            onDelete.Run(index);
            Refresh();
            list1.setSelectedIndex(0);
        });
    }
    public String GetSelected()
    {
        return (String) list1.getSelectedValue();
    }
    public void Refresh()
    {
        DefaultListModel l = new DefaultListModel<>();
        List<String> data = getData.Run();
        System.out.println("x"+data);
        for (String s : data) l.addElement(s);
        list1.setModel(l);
    }
}
