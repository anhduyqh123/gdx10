package Editor.UITool.Physics;

import Editor.JFameUI;
import Editor.UITool.EConfig;
import Extend.Box2d.GBox2d;
import Extend.Box2d.IFixture;
import GameGDX.GDX;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MarkForm {
    private JList list2;
    private JList list1;
    private JButton btAdd;
    private JButton btDelete;
    private JPanel panel1;
    private JComboBox cbCategory;

    private JFameUI ui = new JFameUI();
    private int category,mark;

    public MarkForm(int ca,int ma, GDX.Runnable2<Integer,Integer> onChange)
    {
        this.category = ca;
        this.mark = ma;

        JFrame jFrame = ui.NewJFrame("Mark Form",panel1);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        String[] all = EConfig.e.GetCategory();

        ui.ComboBox(cbCategory,all, GBox2d.GetCategory((short)category),
            name->{
                category=GBox2d.GetCategoryBit(name);
                onChange.Run(category,mark);
            });

        ui.ListSetModel(list1, Arrays.asList(all));
        List<String> cats = GetCategory(mark,all);
        ui.ListSetModel(list2,cats);

        btAdd.addActionListener(e->{
            String name = (String) list1.getSelectedValue();
            if (name==null || cats.contains(name)) return;
            cats.add(name);
            mark = GetBit(cats);
            onChange.Run(category,mark);
            ui.ListSetModel(list2,cats);
        });
        btDelete.addActionListener(e->{
            String name = (String) list2.getSelectedValue();
            if (name==null) return;
            cats.remove(name);
            mark = GetBit(cats);
            onChange.Run(category,mark);
            ui.ListSetModel(list2,cats);
        });
    }
    private int GetBit(List<String> list)
    {
        int bit = 0;
        for(String s : list)
            bit+= GBox2d.GetCategoryBit(s);
        return bit;
    }
    private List<String> GetCategory(int bit,String[] all)
    {
        List<String> list = new ArrayList<>();
        if (bit<0){
            list.addAll(Arrays.asList(all));
            return list;
        }
        String st = Integer.toBinaryString(bit);
        int len = st.length();
        for (int i=0;i<st.length();i++)
            if (st.charAt(len-i-1)=='1') list.add(all[i]);
        return list;
    }
}
