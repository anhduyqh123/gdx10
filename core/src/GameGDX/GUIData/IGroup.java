package GameGDX.GUIData;

import GameGDX.GDX;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.*;

public class IGroup extends IActor {
    protected Map<String, IActor> map = new HashMap<>();
    protected List<String> list = new ArrayList<>();
    public String sizeName = "";

    @Override
    protected Actor NewActor() {
        return new Group();
    }

    public void Move(String childName, int dir)
    {
        int index = list.indexOf(childName);
        int nIndex = index+dir;
        if (nIndex<0) nIndex = 0;
        if (nIndex>=list.size()) nIndex = list.size()-1;
        String nName = list.get(nIndex);
        list.set(nIndex,childName);
        list.set(index,nName);
    }
    public boolean Contain(String name)
    {
        return map.containsKey(name);
    }

    protected void AddChild(String name, IActor child)
    {
        if (Contain(name)) return;
        map.put(name,child);
        list.add(name);
    }
    public void AddChildAndConnect(String name, IActor child)
    {
        AddChild(name,child);
        child.SetConnect(n->GetActor(n));

        SetMain(getMain);
    }
    public void Remove(String name)
    {
        Actor child = GetActor(name);
        if (child!=null) child.remove();
        map.remove(name);
        list.remove(name);
    }

    public void Rename(String oldName,String newName)
    {
        IActor child = map.get(oldName);
        map.remove(oldName);
        map.put(newName,child);

        int index = list.indexOf(oldName);
        list.set(index,newName);
    }
    public <T extends IActor> T GetIActor(String name)
    {
        if (Contain(name)) return (T)map.get(name);
        for(IActor ic : map.values())
            if (ic instanceof IGroup)
            {
                IActor result = ((IGroup)ic).GetIActor(name);
                if (result!=null) return (T)result;
            }
        return null;
    }
    public <T extends IActor> T GetIActor(String name, Class<T> type)
    {
        return GetIActor(name);
    }

    //Get IActor
    public IGroup GetIGroup(String name)
    {
        return GetIActor(name);
    }
    public ILabel GetILabel(String name)
    {
        return GetIActor(name);
    }
    public IImage GetIImage(String name)
    {
        return GetIActor(name);
    }
    public ITable GetITable(String name)
    {
        return GetIActor(name);
    }
    public IParticle GetIParticle(String name)
    {
        return GetIActor(name);
    }

    public <T extends Actor> T GetActor(String name,Class<T> type)
    {
        return GetActor(name);
    }
    public <T extends Actor> T GetActor(String name){
        if (name.equals("")) return GetActor();
        IActor iActor = GetIActor(name);
        if (iActor !=null) return iActor.GetActor();
        return null;
    }

    protected Vector2 GetDefaultSize(String name) {
        try {
            return GetIActor(name).GetSize();
        }catch (Exception e){}
        return null;
    }

    public void Refresh()
    {
        super.Refresh();
        for(int i=0;i<list.size();i++)
        {
            GetIActor(list.get(i)).Refresh();
            GetActor(list.get(i)).setZIndex(i);
        }
    }
    public Collection<String> GetChildName()
    {
        return list;
    }

    @Override
    public void SetConnect(GDX.Func1 connect) {
        iSize.getDefaultSize = ()->GetDefaultSize(sizeName);
        super.SetConnect(connect);
        for(String name : list)
            map.get(name).SetConnect(n->GetActor(n));
    }

    @Override
    public void SetMain(GDX.Func<IActor> getMain) {
        super.SetMain(getMain);
        for(IActor child : map.values()) child.SetMain(getMain);
    }

    @Override
    public void RunAction(String actionName) {
        super.RunAction(actionName);
        for(IActor child : map.values())
            child.RunAction(actionName);
    }

    @Override
    public void StopAction() {
        super.StopAction();
        for(IActor child : map.values())
            child.StopAction();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof IGroup)) return false;
        if (!super.equals(object)) return false;

        IGroup iGroup = (IGroup) object;

        if (!map.equals(iGroup.map)) return false;
        if (!list.equals(iGroup.list)) return false;
        return sizeName.equals(iGroup.sizeName);
    }
}
