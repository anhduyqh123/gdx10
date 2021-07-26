package Editor.UITool.Form;

import Editor.JFameUI;
import Editor.UITool.UIConfig;
import Editor.UITool.Form.Panel.ContentPanel;
import Editor.UITool.ObjectData;
import Editor.UITool.ObjectPack;
import Extend.Box2d.GBox2d;
import GameGDX.AssetLoading.AssetNode;
import GameGDX.Assets;
import GameGDX.GDX;
import GameGDX.GUIData.*;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.Reflect;
import GameGDX.Scene;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class DataForm {
    public JPanel panel1;
    private JTree tree;
    private JTextField tfName;
    private JButton btNew;
    private JButton deleteButton;
    private JButton btPaste;
    private JButton cloneButton;
    private JButton upButton;
    private JButton downButton;
    private JButton addToButton;
    private JButton saveButton;
    private JComboBox cbType;
    private JButton btSelect;
    private JComboBox cbList;
    private JComboBox cbPack;
    private JButton btPrefab;
    private JButton pEdit;

    private Tree gTree;

    private String selectedPack = "";
    private IGroup grSelected;
    private List<String> selectedList = new ArrayList<>();


    private JFameUI ui = new JFameUI();

    public GDX.Runnable2<IActor,List<String>> onSelect;
    private IActor selectedMain;
    private ObjectPack data;
    private ObjectData objectData = new ObjectData();
    private UIConfig uiConfig = UIConfig.i;

    private final List<String> allPack = new ArrayList<>(Assets.GetData().GetKeys());

    public DataForm()
    {
        objectData.Load(allPack);

        gTree = new Tree(tree);
        gTree.onSelect = vl->{
            OnSelect(vl);
            onSelect.Run(gTree.GetSelectedObject(),GetChildren());
        };
        gTree.getData = ()->GetNode("Root",data);

        Class[] types = ContentPanel.i.GetTypes();
        cbType.setModel(new DefaultComboBoxModel(ui.ClassToName(types)));

        RefreshData();

        Click(btNew,()->{
            IActor iActor = (IActor) Reflect.NewInstance(types[cbType.getSelectedIndex()]);
            NewChild(iActor);
        });
        Click(btPrefab,()->{
            String name = selectedList.get(0);
            IActor iActor = objectData.Get(selectedPack).GetIActor(name).Clone();
            iActor.prefab = GetPrefab(selectedPack,name);
            NewChild(iActor);
        });
        Click(deleteButton,this::Delete);
        Click(btSelect,this::SelectList);
        Click(addToButton,this::AddTo);
        Click(cloneButton,this::Clone);
        Click(upButton,()->Move(-1));
        Click(downButton,()->Move(1));
        Click(btPaste,this::Paste);
        Click(saveButton,this::Save);

        Click(pEdit,()->{
            String pack = cbPack.getSelectedItem()+"";
            ui.NewJFrame("Package",new PackForm(uiConfig.GetPacks(pack)).panel1,
            ()->LoadPackage(pack));
        });

        tfName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER) Rename();
            }
        });
    }
    private List<String> GetChildren()
    {
        List<String> list = new ArrayList<>();
        IGroup iGroup = gTree.GetParentObject(gTree.GetSelectedObject());
        if (iGroup instanceof ObjectPack) return list;
        for(String n : iGroup.GetChildName())
        {
            if (n.equals(gTree.GetSelectedName())) return list;
            list.add(n);
        }
        return list;
    }
    private String GetPrefab(String pack,String name)
    {
        if (pack.equals("default") && pack.equals(cbPack.getSelectedItem())) return name;
        return pack+"/"+name;
    }
    public void RefreshData()
    {
        String[] pack = allPack.toArray(new String[0]);
        ui.ComboBox(cbPack,pack,pack[0],vl->{
            LoadPackage(vl);
            data = objectData.Get(vl);
            data.Renew();
            gTree.Refresh();
            gTree.SetSelection(data);
        });
    }
    private void LoadPackage(String pack)
    {
        List<String> list = uiConfig.GetPacks(pack);
        for(String p : list) Assets.RemovePackage(p);
        for (AssetNode n : Assets.GetAssetPackage(pack).GetNodes(AssetNode.Kind.Object))
            GUIData.i.Remove(n.name);
        Assets.LoadPackages(null,list.toArray(new String[0]));

    }
    private void Click(JButton bt,Runnable run)
    {
        bt.addActionListener(e->ui.Try(run,panel1));
    }
    private DefaultMutableTreeNode GetNode(String name, IActor iActor)
    {
        DefaultMutableTreeNode node = gTree.NewNode(name, iActor);
        if (iActor instanceof IGroup)
        {
            IGroup gGroup = (IGroup) iActor;
            for(String childName : gGroup.GetChildName())
                node.add(GetNode(childName,gGroup.GetIActor(childName)));
        }
        return node;
    }
    private void OnSelect(String name)
    {
        tfName.setText(name);
        IActor object = gTree.GetObject(gTree.GetMainNode());
        if (object.equals(selectedMain) && gTree.GetName(object).equals(gTree.GetName(selectedMain))) return;
        if (selectedMain!=null) selectedMain.GetActor().remove();
        GBox2d.Clear();
        selectedMain = object;
        selectedMain.SetConnect(n-> Scene.ui2);
        selectedMain.Refresh();
        //selectedMain.InitMain();
    }
    private void NewChild(IActor newObject)
    {
        String name = tfName.getText();
        IActor iActor = gTree.GetSelectedObject();
        if (!(iActor instanceof IGroup)) iActor = gTree.GetParentObject(iActor);
        IGroup group = (IGroup) iActor;
        if (group.Contain(name))
        {
            ui.NewDialog("tên biến trong 1 group không được trùng nhau!",panel1);
            return;
        }
        group.AddChildAndConnect(name,newObject);
        group.Refresh();
        gTree.Refresh();
        gTree.SetSelection(newObject);
    }
    private void Delete()
    {
        IGroup iGroup = gTree.GetParentObject(gTree.GetSelectedObject());
        gTree.GetName(gTree.GetSelectedList(),name->iGroup.Remove(name));
        iGroup.Refresh();
        gTree.Refresh();
        gTree.SetSelection(iGroup);
    }
    private void SelectList()
    {
        selectedList.clear();
        grSelected = gTree.GetParentObject(gTree.GetSelectedObject());
        gTree.GetName(gTree.GetSelectedList(), name->selectedList.add(name));
        cbList.setModel(new DefaultComboBoxModel(selectedList.toArray()));
        selectedPack = cbPack.getSelectedItem()+"";
    }
    private void Paste()
    {
        IGroup iGroup = gTree.GetSelectedObject();
        IActor iActor = null;
        for(String name : selectedList)
        {
            iActor = grSelected.GetIActor(name).Clone();
            String newName = name;
            if (iGroup.Contain(newName)) newName+="clone";
            iGroup.AddChildAndConnect(newName, iActor);
        }
        iGroup.Refresh();
        gTree.Refresh();
        if (iActor==null) iActor = iGroup;
        gTree.SetSelection(iActor);
    }
    private void AddTo()
    {
        IGroup iGroup = gTree.GetSelectedObject();
        for(String name : selectedList)
        {
            IActor iActor = grSelected.GetIActor(name);
            grSelected.Remove(name);
            iGroup.AddChildAndConnect(name, iActor);
        }
        iGroup.Refresh();
        gTree.Refresh();
        gTree.SetSelection(iGroup);
        SelectList();
    }
    private void Clone()
    {
        String name = tfName.getText();
        IActor iActor = gTree.GetSelectedObject();
        IGroup iGroup = gTree.GetParentObject(iActor);
        IActor newIActor = iActor.Clone();
        iGroup.AddChildAndConnect(name, newIActor);
        iGroup.Refresh();
        gTree.Refresh();
        gTree.SetSelection(newIActor);
    }
    private void Move(int dir)
    {
        String name = gTree.GetSelectedName();
        IGroup iGroup = gTree.GetParentObject(gTree.GetSelectedObject());
        iGroup.Move(name,dir);
        gTree.Refresh();
        gTree.SetSelection(iGroup.GetIActor(name));
        iGroup.Refresh();
    }
    private void Rename()
    {
        String newName = tfName.getText();
        String oldName = gTree.GetSelectedName();
        IGroup iGroup = gTree.GetParentObject(gTree.GetSelectedObject());
        iGroup.Rename(oldName,newName);
        gTree.Refresh();
        gTree.SetSelection(iGroup.GetIActor(newName));
    }
    private void Save()
    {
        String name = gTree.GetName(gTree.GetMainNode());
        data.Save(name,()->ui.NewDialog("Save success!",panel1));
    }
}
