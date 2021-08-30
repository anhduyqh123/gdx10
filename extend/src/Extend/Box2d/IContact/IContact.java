package Extend.Box2d.IContact;

import Extend.Box2d.IBody;
import GameGDX.GUIData.IChild.Component;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

public class IContact extends Component {
    public List<XParam> paramList = new ArrayList<>();

    @Override
    public void Refresh(Actor actor) {

    }

    protected void AddListener(IBody.IBodyListener listener)
    {
        IBody iBody = GetIActor().GetComponent(IBody.class);
        iBody.AddListener(listener);
    }

    protected void Run()
    {
        IBody iBody = GetIActor().GetComponent(IBody.class);
        for (XParam p : paramList)
        {
            p.getIBody = ()->iBody;
            p.Run();
        }
    }
}
