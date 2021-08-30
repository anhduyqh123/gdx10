package Editor.UITool.Form;

import GameGDX.GDX;

import javax.swing.*;
import java.util.List;

public class IList {
    public GDX.Func<List<String>> getData;
    public GDX.Runnable<String> onDelete,onSelect;
    public GDX.Func<String> onAdd;

    private JList list;

    public IList(JList list,JButton btAdd,JButton btDelete)
    {
        this.list = list;
        list.addListSelectionListener(e->{
            int index = list.getSelectedIndex();
            if (index<0) return;
            onSelect.Run(GetSelected());
        });
        btAdd.addActionListener(e->{
            String name = onAdd.Run();
            Refresh();
            list.setSelectedValue(name,true);
        });
        btDelete.addActionListener(e->{
            onDelete.Run(GetSelected());
            Refresh();
            list.setSelectedIndex(0);
        });
    }
    public String GetSelected()
    {
        return (String) list.getSelectedValue();
    }
    private void Refresh()
    {
        DefaultListModel l = new DefaultListModel<>();
        List<String> data = getData.Run();
        for (String s : data) l.addElement(s);
        list.setModel(l);
    }
    public void Init()
    {
        Refresh();
        if (getData.Run().size()>0)
            list.setSelectedIndex(0);
    }
}
