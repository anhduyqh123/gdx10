package Editor.UITool.Form;

import Editor.JFameUI;
import Editor.UITool.EConfig;
import Editor.UITool.Pointed.ChainPointed;
import Editor.UITool.Pointed.CirclePointed;
import Editor.UITool.Pointed.PolygonPointed;
import Extend.Box2d.*;
import GameGDX.GDX;
import GameGDX.Scene;
import GameGDX.Screens.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PhysicsForm {
    private JPanel panel1;
    private JPanel pnBody;
    private JPanel pnFixture;
    private JComboBox cbShape;
    private JComboBox cbCategory;
    private JList list1;
    private JButton btAdd;
    private JButton btDelete;
    private JPanel pnFix;
    private JList list2;

    private JFameUI ui = new JFameUI();
    private Group group = new Group();


    public PhysicsForm(IObject iObject)
    {
        GBox2d.SetCategory(EConfig.e.Get("category").asStringArray());

        IBody iBody = iObject.iBody;
        GObject actor = iObject.GetActor();
        Init(iObject);

        List<String> list = ui.GetDeclaredFields(iBody);
        list.remove("fixtures");
        list.remove("category");list.remove("mark");
        ui.InitComponents(list,iBody,pnBody);

        IFixture iFixture = iBody.fixtures.get(0);
        List<String> list1 = ui.GetDeclaredFields(iFixture);
        list1.remove("iShape");
        ui.InitComponents(list1,iFixture,pnFixture);

        String[] arr = EConfig.e.Get("category").asStringArray();
        ui.ComboBox(cbCategory,arr,GBox2d.GetCategory((short) iBody.category),
                name->iBody.category=GBox2d.GetCategoryBit(name));


        Class[] classes = {IShape.ICircle.class, IShape.IPolygon.class,IShape.IChain.class};
        String[] types = ui.ClassToName(classes);
        ui.ComboBox(cbShape,types,iFixture.iShape.getClass().getSimpleName());
        cbShape.addActionListener(e->{
            int index = cbShape.getSelectedIndex();
            if (index==0) iFixture.iShape = new IShape.ICircle(actor.getWidth());
            if (index==1) iFixture.iShape = new IShape.IPolygon(actor.getWidth(),actor.getHeight());
            if (index==2) iFixture.iShape = new IShape.IChain(actor.getWidth());

            GDX.PostRunnable(()->{
                group.clearChildren();
                InitGroup(actor);
                InitShape(iFixture.iShape);
            });
        });

        InitGroup(actor);
        InitShape(iFixture.iShape);
        InitMark(iBody);
    }
    private void Init(IObject iObject)
    {
        GObject gObject = iObject.GetActor();
        gObject.SetBody(null);

        GBox2d.debug = false;
        Runnable run = Screen.Returns(Arrays.asList(Scene.ui2.getChildren().toArray()));
        Scene.ui2.clearChildren();

        JFrame jFrame = ui.NewJFrame("body",panel1,()->{
            GBox2d.debug = true;
            group.remove();
            run.run();
            iObject.Refresh();
        });
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
    private void InitGroup(Actor actor)
    {
        group.setDebug(true);
        Scene.SetBounds(group,Scene.width/2,Scene.height/2, Align.center,actor.getWidth(),actor.getHeight(),Scene.ui2);
        group.addActor(actor);
        actor.setPosition(0,0);
    }
    private void InitMark(IBody iBody)
    {
        String[] all = EConfig.e.Get("category").asStringArray();
        ui.ListSetModel(list1,Arrays.asList(all));
        List<String> cats = GetCategory(iBody.mark,all);
        ui.ListSetModel(list2,cats);

        btAdd.addActionListener(e->{
            String name = (String) list1.getSelectedValue();
            if (name==null || cats.contains(name)) return;
            cats.add(name);
            iBody.mark = GetBit(cats);
            ui.ListSetModel(list2,cats);
        });
        btDelete.addActionListener(e->{
            String name = (String) list2.getSelectedValue();
            if (name==null) return;
            cats.remove(name);
            iBody.mark = GetBit(cats);
            ui.ListSetModel(list2,cats);
        });

    }
    private int GetBit(List<String> list)
    {
        int bit = 0;
        for(String s : list) bit+=GBox2d.GetCategoryBit(s);
        return bit;
    }
    private List<String> GetCategory(int bit,String[] all)
    {
        List<String> list = new ArrayList<>();
        if (bit<0){
            list.addAll(Arrays.asList(all));
            return list;
        }
        String st = Integer.toBinaryString(bit);
        for (int i=0;i<st.length();i++)
            if (st.charAt(i)=='1') list.add(all[i]);
        return list;
    }

    //Shape
    private Actor InitShape(IShape shape)
    {
        if (shape instanceof IShape.IChain) return new ChainPointed((IShape.IPolygon) shape,group);
        if (shape instanceof IShape.IPolygon)return new PolygonPointed((IShape.IPolygon) shape,group);
        return new CirclePointed((IShape.ICircle) shape,group);
    }
}
