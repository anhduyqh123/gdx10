package Editor.UITool.Form.Panel;

import Editor.JFameUI;
import GameGDX.GUIData.IChild.IPos;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;

import javax.swing.*;
import javax.swing.border.CompoundBorder;

public class IPosPanel {
    private JFameUI ui = new JFameUI();
    private JPanel pn;
    public IPosPanel(IPos iPos, JPanel panel)
    {
        this.pn = panel;

        JPanel jBox = ui.NewBorderPanel(200,190,pn);
        for(Field f : ClassReflection.getFields(IPos.class))
        {
            String name = f.getName();
            if (name.equals("x")|| name.equals("y")) NewBoxPanel(f,iPos);
            else ui.NewComponent(f,iPos,jBox);
        }
    }
    private void NewBoxPanel(Field field,Object object)
    {
        try {
            JPanel jBox = ui.NewPanel(200,200,pn);
            jBox.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3),
                    BorderFactory.createTitledBorder(field.getName())));
            JPanel content;
            Object value = field.get(object);

            String[] iValue = {"Value","Ratio","Target"};
            String stValue = "Value";

            if (value instanceof IPos.Ratio) stValue = "Ratio";
            if (value instanceof IPos.Target) stValue = "Target";

            JComboBox cbType = ui.NewComboBox("type",iValue,stValue,jBox);
            content = ui.NewBorderPanel(170,100,jBox);
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
        ui.InitComponents(ob,jPanel);
        ui.Repaint(jPanel);
    }
}
