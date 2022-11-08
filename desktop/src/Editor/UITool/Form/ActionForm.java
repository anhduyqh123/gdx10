package Editor.UITool.Form;

import Editor.JFameUI;
import Editor.UITool.Form.Panel.ActionPanel;
import Editor.WrapLayout;
import Extend.Box2d.IAction.*;
import Extend.Frame.IFrameAction;
import Extend.IFollow;
import Extend.Shake.IShake;
import Extend.Spine.IAnimation;
import GameGDX.GDX;
import GameGDX.GUIData.IAction.*;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.Reflect;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.List;

public class ActionForm {
    public static List<IAction> selectedList = new ArrayList<>();

    public JPanel panel1;
    private JTree tree;
    private JPanel pnInfo;
    private JPanel pnBT;
    private JButton btNew;
    private JButton btDelete;
    private JButton cloneButton;
    private JButton upButton;
    private JButton downButton;
    private JButton runButton;
    private JButton stopButton;
    private JComboBox cbType;
    private JComboBox cb;
    private JComboBox cbSelect;
    private JButton btSelect;
    private JButton btPaste;
    private JLabel lbType;

    private Tree gTree;
    private JFameUI ui = new JFameUI();

    public GDX.Runnable<String> onRun;
    public Runnable onStop;
    private IActionList list;
    private Class selectedType;
    private IActor iActor;

    public ActionForm()
    {
        pnInfo.setLayout(new WrapLayout());

        gTree = new Tree(tree);
        gTree.onSelect = vl->{
            IAction iac = gTree.GetSelectedObject();
            lbType.setText(iac.getClass().getSimpleName());
            ActionPanel actionPanel = new ActionPanel(iActor,iac,pnInfo);
            //actionPanel.iActor = iActor;
            actionPanel.onRename = (o,n)->{
                iac.name = n;
                if (gTree.GetParentObject(iac).equals(list))
                {
                    list.Remove(o);
                    list.Add(iac);
                }
                gTree.Refresh();
                gTree.SetSelection(iac);
            };
        };
        gTree.getData = ()->GetNode("Root",list);

        String[] vl1 = {"GDX","Box2D","Spine","Extend"};
        Class[] types1 = {IDelay.class, IMove.class,IScale.class,IRotate.class,IParallel.class,ISequence.class,
                IRunAction.class,IForever.class,IRepeat.class,IAlpha.class,IColor.class,IParAction.class, IPlayAudio.class,
                IMovePath.class,IMoveArc.class,ITextureAction.class,ILabelAction.class,ICountAction.class,IDoAction.class,
                IOther.class,ITarget.class,IAudio.class,IEvent.class,IJsonAction.class,IClone.class,IToParent.class, IParamCount.class};
        Class[] types2 ={IBodyValue.class,IVelocity.class, IContact.class,IBodyOther.class,
                IForce.class,IGravity.class,IExplosion.class};
        Class[] types3 ={IAnimation.class};
        Class[] types4 ={IShake.class, IFrameAction.class, IFollow.class};

        Class[][] types = {types1,types2,types3,types4};

        ui.ComboBox(cb,vl1,vl1[0],vl->{
            int index = cb.getSelectedIndex();
            Class[] type = types[index];

            List<String> names = new ArrayList<>();
            for(Class t : type) names.add(t.getSimpleName());

            ui.ComboBox(cbType,names.toArray(),ISequence.class.getSimpleName(),x->{
                int id = cbType.getSelectedIndex();
                if (id>=type.length) id = 0;
                selectedType = type[id];
            });
        });

        Click(btNew,()->{
            IAction newAc = Reflect.NewInstance(selectedType);
            New(newAc);
        });
        Click(btDelete,this::Delete);
        Click(cloneButton,this::Clone);
        Click(upButton,()->Move(-1));
        Click(downButton,()->Move(1));
        Click(runButton,()->{
            TreeNode main = gTree.GetMainNode();
            if (main ==null) return;
            IAction iAction = gTree.GetObject(main);
            onRun.Run(iAction.name);
        });
        Click(stopButton,()->onStop.run());
        Click(btSelect,this::SelectList);
        Click(btPaste,this::Paste);
    }
    private void RefreshSelected()
    {
        List<String> list = new ArrayList<>();
        for(IAction i : selectedList) list.add(i.name);
        String[] arr = list.toArray(new String[0]);
        if (arr.length<=0) return;
        ui.ComboBox(cbSelect,arr);
    }
    private void Paste()
    {
        IMultiAction mul = gTree.GetSelectedObject();
        IAction iAction = null;
        for (IAction i : selectedList)
        {
            iAction = Reflect.Clone(i);
            mul.Add(iAction);
        }

        gTree.Refresh();
        if (iAction==null) iAction = mul;
        gTree.SetSelection(iAction);
    }
    private void SelectList()
    {
        selectedList.clear();
        for(TreeNode n : gTree.GetSelectedList())
            selectedList.add(gTree.GetObject(n));
        RefreshSelected();
    }

    public void SetData(IActor iActor, IActionList list)
    {
        this.iActor = iActor;

        RefreshSelected();
        pnInfo.removeAll();
        ui.Repaint(pnInfo);
        this.list = list;
        gTree.Refresh();
        gTree.SetSelection(list);
    }
    private void Click(JButton bt,Runnable run)
    {
        bt.addActionListener(e->ui.Try(run,panel1));
    }
    private DefaultMutableTreeNode GetNode(String name, IAction iAction)
    {
        DefaultMutableTreeNode node = gTree.NewNode(name,iAction);
        if (iAction instanceof IMultiAction)
        {
            IMultiAction iMul = (IMultiAction) iAction;
            for(IAction i : iMul.GetAll())
                node.add(GetNode(i.name,i));
        }
        return node;
    }
    private void Delete()
    {
        IMultiAction iMul = gTree.GetParentObject(gTree.GetSelectedObject());
        for(TreeNode n : gTree.GetSelectedList())
            iMul.Remove(gTree.GetObject(n));
        gTree.Refresh();
        gTree.SetSelection(iMul);
    }
    private void Move(int dir)
    {
        IAction iAction = gTree.GetSelectedObject();
        IMultiAction iMul = gTree.GetParentObject(iAction);
        iMul.Move(dir,iAction);
        gTree.Refresh();
        gTree.SetSelection(iAction);
    }
    private void Clone()
    {
        IAction iAction = gTree.GetSelectedObject();
        IMultiAction iMul = gTree.GetParentObject(iAction);
        IAction clone = Reflect.Clone(iAction);
        clone.name +="_clone";
        iMul.Add(clone);
        gTree.Refresh();
        gTree.SetSelection(clone);
    }
    private void New(IAction iNew)
    {
        IAction iAction = gTree.GetSelectedObject();
        if (!(iAction instanceof IMultiAction)) iAction = gTree.GetParentObject(iAction);
        IMultiAction iMul = (IMultiAction) iAction;
        iMul.Add(iNew);
        gTree.Refresh();
        gTree.SetSelection(iNew);
    }
    private void New2(IMultiAction iNew)
    {
        IAction iAction = gTree.GetSelectedObject();
        IMultiAction iMul = gTree.GetParentObject(iAction);
        iMul.Remove(iAction);
        iMul.Add(iNew);
        iNew.Add(iAction);
        gTree.Refresh();
        gTree.SetSelection(iNew);
    }
}
