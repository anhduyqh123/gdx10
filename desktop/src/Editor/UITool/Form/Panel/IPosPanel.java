package Editor.UITool.Form.Panel;

import Editor.JFameUI;
import Editor.WrapLayout;
import GameGDX.GUIData.IChild.IPos;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;

import javax.swing.*;
import java.util.List;

public class IPosPanel {
    private JFameUI ui = new JFameUI();
    private JPanel pn;
    private String[] names;
    public IPosPanel(List<String> list,IPos iPos, JPanel panel)
    {
        this.pn = panel;
        names = list.toArray(new String[0]);

        //JPanel jBox = ui.NewBorderPanel(200,190,pn);
        JPanel jBox = NewPanel(200,pn);
        for(Field f : ClassReflection.getFields(IPos.class))
        {
            String name = f.getName();
            if (name.equals("x")|| name.equals("y")) NewBoxPanel(f,iPos);
            else ui.NewComponent(f,iPos,jBox);
        }
    }
    private JPanel NewPanel(int width,JPanel parent)
    {
        JPanel panel = new JPanel(new WrapLayout());
        panel.setSize(width,10);
        ui.SetBorder(panel);
        parent.add(panel);
        return panel;
    }
    private void NewBoxPanel(Field field,Object object)
    {
        try {
            //JPanel jBox = ui.NewPanel(200,200,pn);
            JPanel jBox = NewPanel(200,pn);
            ui.SetBorder(field.getName(),jBox);
            Object value = field.get(object);

            String[] iValue = {"Value","Ratio","Target"};
            if (names.length<=0) iValue = new String[]{"Value","Ratio"};
            String stValue = "Value";

            if (value instanceof IPos.Ratio) stValue = "Ratio";
            if (value instanceof IPos.Target) stValue = "Target";

            JComboBox cbType = ui.NewComboBox("type",iValue,stValue,jBox);
            //content = ui.NewBorderPanel(170,100,jBox);
            JPanel content = NewPanel(170,jBox);
            cbType.addActionListener(e->{
                Object ob = new IPos.Value();
                String vl = (String) cbType.getSelectedItem();
                if (vl.equals("Ratio")) ob = new IPos.Ratio();
                if (vl.equals("Target")) ob = new IPos.Target();
                ui.SetField(field,object,ob);
                NewValuePanel(ob,content);
            });
            NewValuePanel(value,content);
        }catch (Exception e){}
    }

    private void NewValuePanel(Object ob,JPanel jPanel)
    {
        jPanel.removeAll();
        if (ob instanceof IPos.Target) InitTarget(ob,jPanel);
        else ui.InitComponents(ob,jPanel);
        ui.Repaint(jPanel);
    }
    private void InitTarget(Object ob,JPanel panel)
    {
        IPos.Target target = (IPos.Target) ob;
        ui.NewComponent("align",ob,panel);
        if (target.name.equals("")) target.name = names[0];
        ui.NewComboBox("name",names,target.name,panel,vl->target.name=vl)
                .setEditable(true);
    }
}
