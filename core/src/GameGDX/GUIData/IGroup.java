package GameGDX.GUIData;

import GameGDX.GDX;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;

import java.util.*;

public class IGroup extends IActor {
    //GetIChild ->Get child by name
    //FindChild ->Find child by name
    //GetIActor ->Get neighbor actor

    protected Map<String, IActor> map = new HashMap<>();
    protected List<String> list = new ArrayList<>();
    public String sizeName = "";

    //Clone
    public <T extends IActor> T Clone(int index)
    {
        return Clone(GetIChild(index));
    }
    public <T extends IActor> T Clone(String name)
    {
        return Clone(GetIChild(name));
    }
    private <T extends IActor> T Clone(IActor iActor)
    {
        T clone = iActor.Clone();
        clone.SetConnect(this::GetChild);
        clone.Refresh();
        return clone;
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
    public Map<String, IActor> GetMap()
    {
        return map;
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
        child.SetConnect(this::GetChild);
    }
    public void Remove(String name)
    {
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

    @Override
    protected Actor NewActor() {
        return new Group(){
            @Override
            public void act(float delta) {
                super.act(delta);
                Update(delta);
            }
            @Override
            public void draw(Batch batch, float parentAlpha) {
                if (isTransform()) applyTransform(batch, computeTransform());
                OnDraw(batch,parentAlpha,()->{
                    super.drawChildren(batch, parentAlpha);
                });
                if (isTransform()) resetTransform(batch);
                //OnDraw(batch,parentAlpha,()->super.draw(batch, parentAlpha));
            }
        };
    }

    public <T extends IActor> T GetIChild(int index){
        String name = list.get(index);
        return GetIChild(name);
    }
    public <T extends IActor> T GetIChild(String name)
    {
        return (T)map.get(name);
    }
    //Find IActor
    public <T extends IActor> T FindIChild(String name)
    {
        if (Contain(name)) return GetIChild(name);
        for(IActor ic : map.values())
            if (ic instanceof IGroup)
            {
                IActor result = ((IGroup)ic).FindIChild(name);
                if (result!=null) return (T)result;
            }
        return null;
    }
    public <T extends IActor> T FindIChild(String name, Class<T> type)
    {
        return FindIChild(name);
    }
    public IGroup FindIGroup(String name)
    {
        return FindIChild(name);
    }
    public ILabel FindILabel(String name)
    {
        return FindIChild(name);
    }
    public IImage FindIImage(String name)
    {
        return FindIChild(name);
    }
    public ITable FindITable(String name)
    {
        return FindIChild(name);
    }
    public IParticle FindIParticle(String name)
    {
        return FindIChild(name);
    }

    public  <T extends Actor> T GetChild(String name){
        if (name.equals("")) return GetActor();
        IActor iActor = GetIChild(name);
        if (iActor !=null) return iActor.GetActor();
        return null;
    }
    public  <T extends Actor> T FindChild(String name)
    {
        return FindIChild(name).GetActor();
    }

    protected Vector2 GetDefaultSize(String name) {
        try {
            return FindIChild(name).GetSize();
        }catch (Exception e){}
        return null;
    }

    @Override
    public void RefreshContent() {
        ForEach(IActor::RefreshContent);
    }

    public void Refresh()
    {
        InitActor();
        BaseRefresh();
        //RefreshEvent();

        ClearAction();
        //ForComponent((k,p)->p.BeforeRefresh());

        RefreshChildren();
        RefreshComponent();
        RefreshEvent();
    }
    protected void RefreshChildren()
    {
        for(int i=0;i<list.size();i++)
        {
            GetIChild(list.get(i)).Refresh();
            GetChild(list.get(i)).setZIndex(i);
        }
    }
    @Override
    protected void RefreshComponent() {
        getComponent = null;
        ForComponent((k,p)->{
            p.getMain = ()->this;
            p.findIChild = this::FindIChild;
            p.Refresh();
        });
    }

    public List<String> GetChildName()
    {
        return list;
    }
    public void ForEach(GDX.Runnable<IActor> cb)
    {
        for(String n : list) cb.Run(GetIChild(n));
    }
    public <T extends IActor> List<T> GetChildren()
    {
        List<T> children = new ArrayList<>();
        ForEach(i->children.add((T)i));
        return children;
    }

    @Override
    public void SetConnect(GDX.Func1 connect) {
        super.SetConnect(connect);

        for(String n : list)
        {
            GetIChild(n).SetConnect(this::GetChild);
            GetIChild(n).SetName(n);
        }
        //ForEach(i->i.SetConnect(this::GetChild));
        iSize.getDefaultSize = ()->GetDefaultSize(sizeName);
    }
    public void Disconnect()
    {
        super.Disconnect();
        ForEach(IActor::Disconnect);
    }

    public void Remove()
    {
        super.Remove();
        ForEach(IActor::Remove);
    }

    @Override
    public void RemoveEvent(String name) {
        super.RemoveEvent(name);
        ForEach(i->i.RemoveEvent(name));
    }

    protected void RunEventAction(String event)//init,destroy
    {
        if (!acList.Contains(event)) return;
        super.RunAction(event);
    }

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

    @Override
    public void SetColor(Color color) {
        super.SetColor(color);
        ForEach(i->i.SetColor(color));
    }
}
