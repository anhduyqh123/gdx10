package Editor.UITool.Form;

import Editor.GDXTool;
import Editor.JFameUI;
import Editor.UITool.Config;
import GameGDX.AssetLoading.AssetNode;
import GameGDX.AssetLoading.AssetPackage;
import GameGDX.AssetLoading.GameData;
import GameGDX.Assets;
import GameGDX.GUIData.GUIData;

import javax.swing.*;
import java.util.List;

public class PackForm {
    private JList pList;
    private JComboBox cbPack;
    private JButton pAdd;
    private JButton pDelete;
    public JPanel panel1;
    private JButton btSave;
    private JButton btUp;
    private JButton btMini;
    private JButton btGenerateAtlas;
    private JRadioButton rAllTextures;
    private JRadioButton rMainTexture;
    private JRadioButton rParticleAtlas;
    private JRadioButton rAtlas;
    private JRadioButton rTexture;
    private JButton btEncode;
    private JButton btDecode;
    private JButton btUnMini;
    private JFameUI ui = new JFameUI();

    public PackForm(List<String> packs)
    {
        InitPackages(packs);
        Click(btSave, ()->{
            Config.Save();
            ui.NewDialog("Success!",panel1);
        });
        Click(btGenerateAtlas,()->{
            GenerateAtlas();
            ui.NewDialog("Success!",panel1);
        });
        Click(btEncode,()->{
            Encode();
            ui.NewDialog("Encode Success!",panel1);
        });
        Click(btDecode,()->{
            Decode();
            ui.NewDialog("Decode Success!",panel1);
        });
        Click(btMini,()->{
            MiniObject();
            ui.NewDialog("Mini Success!",panel1);
        });
        Click(btUnMini,()->{
            UnMiniObject();
            ui.NewDialog("UnMini Success!",panel1);
        });
    }
    private void MiniObject()
    {
        String pack = cbPack.getSelectedItem()+"";
        AssetPackage aPack = Assets.GetAssetPackage(pack);
        for(AssetNode n : aPack.GetNodes(AssetNode.Kind.Object))
            GUIData.i.MiniSave(n);
    }
    private void UnMiniObject()
    {
        String pack = cbPack.getSelectedItem()+"";
        AssetPackage aPack = Assets.GetAssetPackage(pack);
        for(AssetNode n : aPack.GetNodes(AssetNode.Kind.Object))
            GUIData.i.UnMiniSave(n);
    }
    private void GenerateAtlas()
    {
        String pack = cbPack.getSelectedItem()+"";
        AssetPackage aPack = Assets.GetAssetPackage(pack);
        if (rAllTextures.isSelected()) GDXTool.GenerateAtlas(aPack.GetTextureUrl(),aPack.GetAtlasUrl());
        if (rMainTexture.isSelected()) GDXTool.GenerateMainAtlas(aPack.GetTextureUrl(),aPack.GetAtlasUrl());
        if (rParticleAtlas.isSelected()) GDXTool.GenerateAtlas(aPack.GetParticleUrl(),aPack.GetParticleUrl());
    }
    private void Encode()
    {
        String pack = cbPack.getSelectedItem()+"";
        AssetPackage aPack = Assets.GetAssetPackage(pack);
        if (rAtlas.isSelected()) GDXTool.Encode(aPack.GetAtlasUrl());
        if (rTexture.isSelected()) GDXTool.Encode(aPack.GetTextureUrl());
    }
    private void Decode()
    {
        String pack = cbPack.getSelectedItem()+"";
        AssetPackage aPack = Assets.GetAssetPackage(pack);
        if (rAtlas.isSelected()) GDXTool.Decode(aPack.GetAtlasUrl());
        if (rTexture.isSelected()) GDXTool.Decode(aPack.GetTextureUrl());
    }
    private void Click(JButton bt,Runnable run)
    {
        bt.addActionListener(e->ui.Try(run,panel1));
    }
    private void ListSetModel(JList list, List<String> data)
    {
        DefaultListModel l = new DefaultListModel<>();
        for(String key : data)
            l.addElement(key);
        list.setModel(l);
    }
    private void InitPackages(List<String> packs)
    {
        GameData data = Assets.GetData();
        ui.ComboBox(cbPack,data.GetKeys().toArray(new String[0]));
        ListSetModel(pList, packs);
        if (packs.size()>0)
            pList.setSelectedValue(packs.get(0),true);

        Click(pAdd,()->{
            String st = cbPack.getSelectedItem()+"";
            if (!packs.contains(st)) packs.add(st);
            ListSetModel(pList, packs);
            pList.setSelectedValue(st,true);
        });
        Click(pDelete,()->{
            String st = pList.getSelectedValue()+"";
            packs.remove(st);
            ListSetModel(pList, packs);
            if (packs.size()>0)
                pList.setSelectedValue(packs.get(0),true);
        });
        Click(btUp,()->{
            String st = pList.getSelectedValue()+"";
            int id1 = packs.indexOf(st);
            int id2 = id1-1;
            if (id2<0) id2 = packs.size()-1;
            Swap(id1,id2,packs);
            ListSetModel(pList, packs);
            pList.setSelectedValue(st,true);
        });
    }
    private void Swap(int id1,int id2,List list)
    {
        Object s1 = list.get(id1);
        Object s2 = list.get(id2);
        list.set(id1,s2);
        list.set(id2,s1);
    }
}
