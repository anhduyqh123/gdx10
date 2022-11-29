package Editor.UITool.Form;

import Editor.JFameUI;
import GameGDX.GUIData.IParticle;

import javax.swing.*;
import java.util.List;

public class IEmitterForm {
    public JPanel panel1;
    private JList list1;
    private JButton btNew;
    private JButton btDelete;
    private JPanel pnMain;

    private JFameUI ui = new JFameUI();
    private List<IParticle.IEmitter> data;

    public IEmitterForm(List<IParticle.IEmitter> data)
    {
//        pnMain.setLayout(new WrapLayout());
//        this.data = data;
//        list1.addListSelectionListener(e->{
//            int index = list1.getSelectedIndex();
//            if (index<0) return;
//            OnSelect(data.get(index));
//        });
//        btNew.addActionListener(e->New());
//        btDelete.addActionListener(e->Delete());
//
//        Refresh();
//        if (data.size()<=0) return;
//        list1.setSelectedIndex(0);
    }
//    private void Refresh()
//    {
//        DefaultListModel l = new DefaultListModel<>();
//        for(IParticle.IEmitter i : data)
//            l.addElement("emitter"+i.index);
//        list1.setModel(l);
//    }
//    private void New()
//    {
//        data.add(new IParticle.IEmitter());
//        Refresh();
//        list1.setSelectedIndex(data.size()-1);
//    }
//    private void Delete()
//    {
//        int index = list1.getSelectedIndex();
//        data.remove(index);
//        Refresh();
//        if (data.size()<=0) return;
//        list1.setSelectedIndex(data.size()-1);
//    }
//    private void OnSelect(IParticle.IEmitter e)
//    {
//        pnMain.removeAll();
//        ui.InitComponents(Arrays.asList("index","sprite"),e,pnMain);
//        ui.NewCheckBox("IsSize",e.size!=null,pnMain,vl->{
//            if (vl) e.size = new IParticle.Value();
//            else e.size = null;
//            OnSelect(e);
//        });
//        ui.NewCheckBox("IsOffset",e.offset!=null,pnMain,vl->{
//            if (vl) e.offset = new IParticle.Value();
//            else e.offset = null;
//            OnSelect(e);
//        });
//        NewValuePanel("Size", e.size);
//        NewValuePanel("Offset",e.offset);
//        ui.Repaint(pnMain);
//    }
//    private void NewValuePanel(String name,IParticle.Value value)
//    {
//        if (value==null) return;
//        JPanel pn = ui.NewBigPanel(name,180,180,pnMain);
//        ui.InitComponents(value,pn);
//    }
}
