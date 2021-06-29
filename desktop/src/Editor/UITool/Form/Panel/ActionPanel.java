package Editor.UITool.Form.Panel;

import Editor.JFameUI;
import Editor.UITool.Form.PointForm;
import GameGDX.GDX;
import GameGDX.GUIData.IAction.*;
import GameGDX.Reflect;
import com.badlogic.gdx.utils.reflect.Field;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ActionPanel {

    private JPanel pn;
    private JFameUI ui = new JFameUI();
    public GDX.Runnable2<String,String> onRename;
    public ActionPanel(IAction data,JPanel pn)
    {
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
            new IPosPanel(Collections.emptyList(), iMove.iPos,pn);
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
}
