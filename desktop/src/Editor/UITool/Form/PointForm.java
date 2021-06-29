package Editor.UITool.Form;

import Editor.JFameUI;
import Editor.UITool.Form.Panel.IPosPanel;
import Editor.WrapLayout;
import GameGDX.GUIData.IAction.IMovePath;
import GameGDX.GUIData.IImage;
import GameGDX.Scene;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PointForm {
    private JPanel panel1;
    private JList list1;
    private JButton btNew;
    private JButton btDelete;
    private JButton btRefresh;
    private JPanel pnInfo;
    private JFameUI ui = new JFameUI();
    private List<IMovePath.IPosX> data;
    private Group group = new Group();

    public PointForm(List<IMovePath.IPosX> data)
    {
        this.data = data;
        pnInfo.setLayout(new WrapLayout());

        list1.addListSelectionListener(e->{
            int index = list1.getSelectedIndex();
            if (index<0) return;
            pnInfo.removeAll();
            IMovePath.IPosX iPosX = data.get(index);
            ui.InitComponents(Arrays.asList("current","useX","useY"),iPosX,pnInfo);
            new IPosPanel(Arrays.asList(), iPosX.iPos,pnInfo);
            ui.Repaint(pnInfo);
        });
        Refresh();
        if (data.size()>0)
            list1.setSelectedValue(GetView(0),true);

        Click(btNew,()->{
            IMovePath.IPosX iPosX = new IMovePath.IPosX();
            iPosX.iPos.Set(new Vector2(Scene.width/2,Scene.height/2));
            data.add(iPosX);
            Refresh();
            list1.setSelectedValue(GetView(data.size()-1),true);
        });
        Click(btDelete,()->{
            int index = list1.getSelectedIndex();
            data.remove(index);
            Refresh();
            if (data.size()>0)
                list1.setSelectedValue(GetView(0),true);
        });
        Click(btRefresh,this::ShowPoint);

        Scene.ui2.addActor(group);
        ShowPoint();
        ui.NewJFrame("points",panel1,group::remove);
    }
    private void ShowPoint()
    {
        group.clearChildren();
        for(IMovePath.IPosX p : data) NewPoint(p);
    }
    private void NewPoint(IMovePath.IPosX p)
    {
        Vector2 pos = p.iPos.Get();
        Image img = IImage.NewImage(Color.BROWN,pos, Align.center,20,20,group);
//        img.addListener(new ClickListener(){
//            @Override
//            public void touchDragged(InputEvent event, float x, float y, int pointer) {
//                Vector2 pos = new Vector2(Gdx.input.getX(),Gdx.input.getY());
//                pos = group.screenToLocalCoordinates(pos);
//                img.setPosition(pos.x,pos.y,Align.center);
//                p.set(pos);
//            }
//        });
    }
    private void Click(JButton bt,Runnable run)
    {
        bt.addActionListener(e->ui.Try(run,panel1));
    }
    private void Refresh()
    {
        ui.ListSetModel(list1,GetData());
        ShowPoint();
    }
    private List<String> GetData()
    {
        List<String> list = new ArrayList<>();
        for (int i=0;i<data.size();i++) list.add(GetView(i));
        return list;
    }
    private String GetView(int index)
    {
        return "point"+index;
    }
}
