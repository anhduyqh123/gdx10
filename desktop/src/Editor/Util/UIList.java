package Editor.Util;

import Editor.JFameUI;
import GameGDX.GDX;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UIList<T> {
    public GDX.Func<T> newValue;
    public GDX.Runnable<T> onSelect,onDelete = t->{};
    public GDX.Func1<String,T> getName;
    private JList jList;
    private List<T> list;
    private Map<String,T> map = new HashMap<>();

    public void Init(JList jList,List<T> list)
    {
        this.jList = jList;
        this.list = list;
        new JFameUI().JListClearListener(jList);
        jList.addListSelectionListener(e->{
            int index = jList.getSelectedIndex();
            if (index<0||list.size()<=0) return;
            onSelect.Run(list.get(index));
        });
        Refresh();
        if (list.size()<=0) return;
        jList.setSelectedValue(GetName(list.get(0)),true);
    }
    public void Refresh()
    {
        DefaultListModel l = new DefaultListModel<>();
        for (T i : list)
        {
            String name = GetName(i);
            map.put(name,i);
            l.addElement(name);
        }
        jList.setModel(l);
    }
    private String GetName(T t)
    {
        if (getName!=null) return getName.Run(t);
        return "i"+list.indexOf(t);
    }
    public void New()
    {
        T t = newValue.Run();
        list.add(t);
        Refresh();
        jList.setSelectedValue(GetName(t),true);
    }
    public void Delete()
    {
        for (String s : GetSelected()) Delete(s);
        Refresh();
        if (list.size()<=0) return;
        jList.setSelectedValue(GetName(list.get(0)),true);
    }
    private void Delete(String name)
    {
        T t = map.get(name);
        list.remove(t);
        onDelete.Run(t);
    }
    private List<String> GetSelected()
    {
        return jList.getSelectedValuesList();
    }
}
