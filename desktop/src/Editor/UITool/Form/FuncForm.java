package Editor.UITool.Form;

import Editor.JFameUI;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IGroup;
import GameGDX.Util;

import javax.swing.*;
import java.util.Collections;

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
        Class[] types = {Clone.class,Reverse.class,Other.class};
        ui.ComboBox(cbFunc,ui.ClassToName(types),"Clone",st->{
            int index = cbFunc.getSelectedIndex();
            if (index==0) func = new Clone();
            if (index==1) func = new Reverse();
            if (index==2) func = new Other();
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
    public class Reverse extends Func
    {
        public void Run()
        {
            IGroup iGroup = (IGroup) iActor;
            Collections.reverse(iGroup.GetChildName());
            iGroup.Refresh();
            refresh.run();
        }
    }
    public class Other extends Func
    {
        public void Run()
        {
            IGroup iGroup = (IGroup) iActor;
            IGroup left = iGroup.FindIGroup("left");
            IGroup right = iGroup.FindIGroup("right");
            IGroup top = iGroup.FindIGroup("top");
            IGroup bot = iGroup.FindIGroup("bot");
            Start("r",left,top,right,bot,iGroup.FindIGroup("red"));
            Start("b",top,right,bot,left,iGroup.FindIGroup("blue"));
            Start("y",right,bot,left,top,iGroup.FindIGroup("yellow"));
            Start("g",bot,left,top,right,iGroup.FindIGroup("green"));
        }
        private void Start(String key,IGroup ig0,IGroup ig1,IGroup ig2,IGroup ig3,IGroup ig4)
        {
            Util.For(0,14,i->{
                if (i>=3) ig0.FindIChild("i"+i).GetParamMap().put(key+(i-3),"");
                if (i<2) ig0.FindIChild("i"+i).GetParamMap().put(key+(i+57),"");
                ig1.FindIChild("i"+i).GetParamMap().put(key+(i+12),"");
                ig2.FindIChild("i"+i).GetParamMap().put(key+(i+27),"");
                ig3.FindIChild("i"+i).GetParamMap().put(key+(i+42),"");
            });
            Util.For(0,4,i->
                    ig4.FindIChild("i"+i).GetParamMap().put(key+(i+59),""));
        }
    }
}
