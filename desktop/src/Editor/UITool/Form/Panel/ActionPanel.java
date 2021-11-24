package Editor.UITool.Form.Panel;

import Editor.JFameUI;
import Editor.UITool.Form.PointForm;
import Editor.UITool.MyGame;
import Editor.UITool.Physics.MarkForm;
import Extend.Box2d.IAction.IExplosion;
import Extend.GShape.GShapeRenderer;
import Extend.GShape.Shape;
import GameGDX.GDX;
import GameGDX.GUIData.IAction.*;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IGroup;
import GameGDX.Reflect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.reflect.Field;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ActionPanel {
    private JPanel pn;
    private JFameUI ui = new JFameUI();
    public GDX.Runnable2<String,String> onRename;
    private IActor iActor;

    public ActionPanel(IActor iActor,IAction data,JPanel pn)
    {
        this.iActor = iActor;
        this.pn = pn;
        Refresh(data);
    }
    protected void Refresh(IAction data)
    {
        pn.removeAll();
        Init(data);
        ui.Repaint(pn);
    }
    private void Init(IAction data)
    {
        if (data==null) return;
        GetContent(data).onRepaint = ()->Refresh(data);
        Field f = Reflect.GetField(IAction.class,"name");
        JTextField text = ui.NewTextField("name", Reflect.GetValue(f,data),pn);
        text.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER)
                    onRename.Run(data.name,text.getText());
            }
        });
    }
    private JIAction GetContent(IAction data)
    {
        if (data instanceof IMove) return new JIMove(data,pn);
        if (data instanceof IColor) return new JIColor(data,pn);
        if (data instanceof IMovePath) return new JIMovePath(data,pn);
        if (data instanceof IExplosion) return new JExplosion(data,pn);
        if (data instanceof IPlayAudio) return new JIPlayAudio(data,pn);
        return new JIAction(data,pn);
    }
    public class JIAction
    {
        protected IAction data;
        protected JFameUI ui = new JFameUI();
        public Runnable onRepaint;

        public JIAction() { }
        public JIAction(IAction data, JPanel pn)
        {
            this.data = data;
            ui.InitComponents(GetFields(),data,pn);
        }
        protected List<String> GetFields()
        {
            List<String> list = ui.GetFields(data);
            list.remove("name");
            return list;
        }
    }
    class JIMove extends JIAction
    {
        public JIMove(IAction data, JPanel pn) {
            super(data, pn);
            IMove iMove = (IMove)data;
            IGroup iParent = iActor.GetIParent();
            List<String> names = new ArrayList<>();
            if (iParent!=null) names.addAll(iParent.GetChildName());

            //names.remove(iActor.GetName());
            new IPosPanel(names, iMove.iPos,pn);
            //new IPosPanel(Collections.emptyList(), iMove.iPos,pn);
        }

        @Override
        protected List<String> GetFields() {
            return Arrays.asList("duration","iInter","current","useX","useY");
        }
    }
    class JIColor extends JIAction
    {
        public JIColor(IAction data, JPanel pn) {
            super(data, pn);
            IColor iColor = (IColor)data;
            ui.NewColorPicker(pn,iColor.hexColor,hex->iColor.hexColor=hex);
        }
        @Override
        protected List<String> GetFields() {
            return Arrays.asList("duration","iInter","current");
        }
    }
    class JIMovePath extends JIAction
    {
        public JIMovePath(IAction data, JPanel pn) {
            super(data, pn);
            IMovePath iMovePath = (IMovePath)data;
            ui.NewButton("Show Points",pn,()->new PointForm(iMovePath.points));

        }
    }
    class JIPlayAudio extends JIAction
    {
        public JIPlayAudio(IAction data, JPanel pn) {
            super(data, pn);
            IPlayAudio iPlay = (IPlayAudio)data;
            String[] types = {"Sound","Music"};
            String vl0 = types[0];
            if (iPlay.base instanceof IPlayAudio.IMusic) vl0 = types[1];
            ui.NewComboBox("type",types,vl0,pn,vl->{
                if (vl.equals(types[0])) iPlay.base = new IPlayAudio.ISound();
                if (vl.equals(types[1])) iPlay.base = new IPlayAudio.IMusic();
                Refresh(data);
            });
        }
    }

    //box2d
    class JExplosion extends JIAction
    {
        public JExplosion(IAction data, JPanel pn) {
            super(data, pn);
            IExplosion iAction = (IExplosion)data;
            ui.NewButton("category",pn,()->
                    new MarkForm(iAction.category, iAction.mark,(c, m)->{
                        iAction.category=c;
                        iAction.mark=m;
                    }));
            Shape.Circle circle = new Shape.Circle();
            ui.NewCheckBox("debug",false,pn,b->{
                if (b)
                {
                    Vector2 p = iAction.anchor.GetPos(iActor.GetActor());
                    circle.pos.set(p);
                    circle.radius = iAction.radius;
                    MyGame.i.renderer.AddShape(circle);
                }
                else MyGame.i.renderer.RemoveShape(circle);
            });
        }
        @Override
        protected List<String> GetFields() {
            List<String> list = ui.GetFields(data);
            list.remove("category");
            list.remove("mark");
            return list;
        }
    }
}
