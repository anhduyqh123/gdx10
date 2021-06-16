package Editor.UITool.Form;

import GameGDX.GDX;

import javax.swing.*;
import javax.swing.tree.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tree {

    private JTree tree;
    private Map<TreeNode, Object> map = new HashMap<>();
    private Map<Object, TreeNode> map0 = new HashMap<>();
    private TreeNode selected;

    public GDX.Runnable<String> onSelect;
    public GDX.Func<MutableTreeNode> getData;

    public Tree(JTree tree)
    {
        this.tree = tree;
        tree.addTreeSelectionListener(e->{
            DefaultMutableTreeNode node = GetSelectedNode();
            if (node==null || node.isRoot()) return;
            SelectObject(node);
        });
    }

    public <T> T GetParentObject(Object object)
    {
        TreeNode node = map0.get(object);
        return (T)map.get(node.getParent());
    }
    public <T> T GetObject(TreeNode node)
    {
        return (T)map.get(node);
    }
    public <T> T GetSelectedObject()
    {
        return GetObject(GetSelectedNode());
    }
    public DefaultMutableTreeNode GetSelectedNode()
    {
        return (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
    }
    public String GetSelectedName()
    {
        return (String) GetSelectedNode().getUserObject();
    }
    public String GetName(TreeNode node)
    {
        DefaultMutableTreeNode n = (DefaultMutableTreeNode) node;
        return (String) n.getUserObject();
    }
    public void Refresh()
    {
        map.clear();
        tree.setModel(new DefaultTreeModel(getData.Run()));
    }
    public DefaultMutableTreeNode NewNode(String name,Object object)
    {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(name);
        map.put(node, object);
        map0.put(object,node);
        return node;
    }
    private void SelectObject(TreeNode newSelected)
    {
        if (selected!=null && selected.equals(newSelected)) return;
        selected = newSelected;
        onSelect.Run(GetSelectedName());
    }
    private DefaultMutableTreeNode GetRoot(DefaultMutableTreeNode node)
    {
        TreeNode root = node.getRoot();
        TreeNode current = node;
        while (current.getParent()!=root) current = current.getParent();
        return (DefaultMutableTreeNode)current;
    }
    public TreeNode GetMainNode()
    {
        DefaultMutableTreeNode node = GetSelectedNode();
        if (node==null || node.isRoot()) return null;
        do {
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
            if (parent.isRoot()) return node;
            node = parent;
        }while (true);
    }
    public List<TreeNode> GetSelectedList()
    {
        List<TreeNode> list = new ArrayList<>();
        for(TreePath path : tree.getSelectionPaths())
        {
            TreeNode node = (TreeNode) path.getLastPathComponent();
            list.add(node);
        }
        return list;
    }
    public void SetSelection(Object object)
    {
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        tree.setSelectionPath(new TreePath(model.getPathToRoot(map0.get(object))));
    }

    public void GetName(List<TreeNode> list, GDX.Runnable<String> cb)
    {
        for(TreeNode n : list)
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)n;
            String name = (String)node.getUserObject();
            cb.Run(name);
        }
    }
}
