package Editor.UITool;

import GameGDX.AssetLoading.AssetNode;
import GameGDX.AssetLoading.AssetPackage;
import GameGDX.Assets;
import GameGDX.GUIData.GUIData;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IGroup;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ObjectPack extends IGroup {
    private AssetPackage assetPackage;
    private String pack;
    public ObjectPack(String pack)
    {
        this.pack = pack;
        assetPackage = Assets.GetAssetPackage(pack);
        Renew();
    }
    public void Renew()
    {
        list.clear();
        map.clear();
        for(AssetNode n : assetPackage.GetNodes(AssetNode.Kind.Object))
        {
            IActor iActor = GUIData.i.Get(pack,n.name);
            AddChildAndConnect(n.name, iActor);
        }
    }
    public IActor Renew(String name)
    {
        Actor child = GetChild(name);
        if (child!=null) child.remove();
        IActor iActor = GUIData.i.Get(pack,name);
        AddChildAndConnect(name, iActor);
        return iActor;
    }

    @Override
    public void Remove(String name) {
        Actor child = GetChild(name);
        if (child!=null) child.remove();
        map.remove(name);
        AssetNode node = assetPackage.Get(name);
        try {
            FileHandle file = new FileHandle(node.url);
            file.delete();
        }catch (Exception e){}
    }

    @Override
    public void Rename(String oldName, String newName) {
        IActor child = map.get(oldName);
        map.remove(oldName);
        map.put(newName,child);
    }

    @Override
    public void Move(String childName, int dir) {

    }

    @Override
    public void AddChildAndConnect(String childName, IActor child) {
        map.put(childName,child);
        child.SetConnect(null);
    }

    @Override
    public void Refresh() {

    }

    @Override
    public List<String> GetChildName() {
        List<String> list = new ArrayList<>(map.keySet());
        Collections.sort(list);
        return list;
    }

    public void Save(String name,Runnable done)
    {
        String url = assetPackage.url+"/objects/"+name+".ob";
        IActor ic = map.get(name);
        GUIData.i.Save(url,ic);
        if (!assetPackage.Contain(name))
        {
            AssetNode node = new AssetNode(pack, AssetNode.Kind.Object,"",name);
            node.url = url;
            assetPackage.list.add(node);
            assetPackage.Install();
        }
        done.run();
    }
}
