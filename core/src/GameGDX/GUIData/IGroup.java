package GameGDX.GUIData;

import GameGDX.GDX;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.graphics.Color;
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
        child.SetConnect(this::GetActor);

        //SetMain(getMain);
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

    @Override
    public void RefreshContent() {
        ForEach(IActor::RefreshContent);
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
    public void ForEach(GDX.Runnable<IActor> cb)
    {
        for(String n : list) cb.Run(GetIActor(n));
    }

    @Override
    public void SetConnect(GDX.Func1 connect) {
        super.SetConnect(connect);
        ForEach(i->i.SetConnect(this::GetActor));
        iSize.getDefaultSize = ()->GetDefaultSize(sizeName);
    }

//    @Override
//    public void SetMain(GDX.Func<IActor> getMain) {
//        super.SetMain(getMain);
//        ForEach(i->i.SetMain(getMain));
//    }

    @Override
    public void RunAction(String actionName) {
        super.RunAction(actionName);
        ForEach(i->i.RunAction(actionName));
    }

    @Override
    public void StopAction() {
        super.StopAction();
        ForEach(IActor::StopAction);
    }
}
