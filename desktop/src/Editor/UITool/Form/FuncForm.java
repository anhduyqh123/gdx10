package Editor.UITool.Form;

import Editor.JFameUI;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IGroup;

import javax.swing.*;

public class FuncForm {
    public JPanel panel1;
    private JButton btRun;
    private JComboBox cbFunc;
    private JPanel pnVar;

    private IActor iActor;
    private JFameUI ui = new JFameUI();
    private Func func;
    public Runnable refresh;
    public FuncForm()
    {
        Class[] types = {Clone.class};
        ui.ComboBox(cbFunc,ui.ClassToName(types),"Clone",st->{
            int index = cbFunc.getSelectedIndex();
            if (index==0) func = new Clone();
            ui.ClearPanel(pnVar);
            ui.InitComponents(func,pnVar);
        });
        ui.Button(btRun,()-> func.Run());
    }
    public void SetIActor(IActor iActor)
    {
        this.iActor = iActor;
    }
    public abstract class Func
    {
        public abstract void Run();
    }
    public class Clone extends Func
    {
        public int amount = 1;
        public void Run()
        {
            IGroup iGroup = (IGroup) iActor;
            IActor child = iGroup.GetIChild(0);
            String name = child.GetName();
            for (int i=0;i<amount;i++)
            {
                IActor clone = child.Clone();
                iGroup.AddChildAndConnect(name+i,clone);
            }
            iGroup.Refresh();
            refresh.run();
        }
    }
}
