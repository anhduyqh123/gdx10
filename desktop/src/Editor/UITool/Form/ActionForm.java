package Editor.UITool.Form;

import Editor.JFameUI;
import Editor.UITool.Form.Panel.ActionPanel;
import Editor.WrapLayout;
import Extend.Box2d.IAction.*;
import Extend.Shake.IShake;
import GameGDX.GDX;
import GameGDX.GUIData.IAction.*;
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

    public ActionForm()
    {
        pnInfo.setLayout(new WrapLayout());

        gTree = new Tree(tree);
        gTree.onSelect = vl->{
            IAction iac = gTree.GetSelectedObject();
            lbType.setText(iac.getClass().getSimpleName());
            new ActionPanel(iac,pnInfo).onRename = (o,n)->{
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

        Class[] types = {IDelay.class, IMove.class,IScale.class,IRotate.class,IParallel.class,ISequence.class,
                IRunAction.class,IForever.class,IRepeat.class,IAlpha.class,IColor.class,IParAction.class,ISoundAction.class,
                IMovePath.class,IMoveArc.class,ITextureAction.class,ICountAction.class,IDoAction.class,
                IOther.class,ITarget.class,IVisible.class,ISwitchEvent.class,IJsonAction.class, IShake.class,
                IAngular.class, IVelocity.class, IExplosion.class, IContact.class};
        List<String> names = new ArrayList<>();
        for(Class t : types) names.add(t.getSimpleName());
        Object[] arr = names.toArray();
        ui.ComboBox(cbType,arr,ISequence.class.getSimpleName());

        String[] vl1 = {"Child","Parent"};
        cb.setModel(new DefaultComboBoxModel(vl1));

        Click(btNew,()->{
            int index= cbType.getSelectedIndex();
            IAction newAc = (IAction) Reflect.NewInstance(types[index]);
            if (cb.getSelectedIndex()==0) New(newAc);
            else {
                if (newAc instanceof IMultiAction) New2((IMultiAction) newAc);
            }
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

    public void SetData(IActionList list)
    {
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
