package Editor.UITool.Form;

import Editor.JFameUI;
import Editor.UITool.Form.Panel.ContentPanel;
import Editor.UITool.Form.Panel.IPosPanel;
import Editor.UITool.Physics.Box2DMouse;
import Editor.WrapLayout;
import GameGDX.GDX;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IChild.IPos;
import GameGDX.Scene;
import GameGDX.Util;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class IActorForm {
    public JPanel panel1;
    private JButton btRefresh;
    private JPanel pnContent;
    private JPanel pInfo;
    private JPanel pSize;
    private JPanel pPos;
    private JPanel pnComponent;

    private Runnable onRefresh;
    private JFameUI ui = new JFameUI();
    private Runnable unDrag;
    public GDX.Func<Boolean> isDrag;

    public IActorForm()
    {
        btRefresh.addActionListener(e->onRefresh.run());
    }
    public void SetData(IActor iActor, List<String> list)
    {
        if (isDrag.Run())
            SetDrag(iActor);
        else new Box2DMouse(iActor);

        pnComponent.removeAll();
        pInfo.removeAll();
        pSize.removeAll();
        pPos.removeAll();

        new ComponentForm(iActor,pnComponent);

        pInfo.setLayout(new WrapLayout());

        ContentPanel.i.SetContent(pInfo, iActor);
        ui.InitComponents(Arrays.asList("width","height","origin","originX","originY",
                        "scale","rotate","extendScreen","fillW","fillH","scaleX","scaleY")
                ,iActor.iSize,pSize);
        //int w = iActor.GetActor().getWidth();
        ui.NewLabel("size:"+(int)iActor.GetActor().getWidth()+"-"+(int)iActor.GetActor().getHeight(),pSize);

        ui.NewCheckBox("debug", iActor.GetActor().getDebug(), pSize, vl->iActor.GetActor().setDebug(vl));

        new IPosPanel(list,iActor.iPos,pPos);

        onRefresh = ()->{
            iActor.Refresh();
            SetData(iActor,list);
        };

        ui.Repaint(panel1);
    }
    private void SetDrag(IActor iActor)
    {
        if (unDrag!=null) unDrag.run();
        Actor actor = iActor.GetActor();
        actor.toFront();
        actor.clearListeners();
        unDrag = ()->{
            actor.setDebug(false);
            actor.clearListeners();
        };

        Vector2 p0 = new Vector2();
        Vector2 p = new Vector2();

        actor.setDebug(true);
        actor.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button!=0) return false;
                p0.set(Scene.GetMousePos());
                p.set(Scene.GetPosition(actor));
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                Vector2 dir = Util.GetDirect(p0,Scene.GetMousePos());
                Vector2 pos = dir.add(p);
                Util.Round(pos);
                Scene.SetPosition(actor,pos);
                SetPosition(iActor.iPos,Scene.GetPosition(actor,iActor.iPos.align.value));
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                onRefresh.run();
            }
        });

    }
    private void SetPosition(IPos iPos, Vector2 p)
    {
        if (!SetUnit(iPos.x,p.x)) iPos.delX = p.x-iPos.GetX0();
        if (!SetUnit(iPos.y,p.y)) iPos.delY = p.y-iPos.GetY0();
    }
    private boolean SetUnit(IPos.Unit unit,float x)
    {
        if (unit instanceof IPos.Value)
        {
            IPos.Value vl = (IPos.Value)unit;
            vl.value = x;
            return true;
        }
        return false;
    }
}
