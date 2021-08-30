package Editor.UITool.Form;

import Editor.JFameUI;
import Editor.UITool.EConfig;
import Editor.UITool.Physics.FixtureForm;
import Editor.UITool.Physics.FixtureListPanel;
import Editor.UITool.Physics.MarkForm;
import Editor.UITool.Pointed.ChainPointed;
import Editor.UITool.Pointed.CirclePointed;
import Editor.UITool.Pointed.Pointed;
import Editor.UITool.Pointed.PolygonPointed;
import Editor.WrapLayout;
import Extend.Box2d.*;
import GameGDX.GDX;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.Scene;
import GameGDX.Screens.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

public class PhysicsForm {
    private JPanel panel1;
    private JPanel pnBody;
    private JPanel pnFixture;
    private JPanel pnFix;
    private JList list3;
    private JButton btNew3;
    private JButton btDelete3;
    private JPanel pnMark;

    private JFameUI ui = new JFameUI();
    private Group group = new Group();
    private Actor actor;
    private Pointed shape;


    public PhysicsForm(IBody iBody,IActor iActor)
    {
        String[] categories = EConfig.e.Get("category").asStringArray();
        GBox2d.SetCategory(categories);

        actor = iActor.GetActor();
        iBody.DestroyBody();
        Init(iActor);

        List<String> list = ui.GetFields(iBody);
        list.remove("fixtures");
        //list.remove("category");
        //ui.NewComboBox("category",categories,iBody.category,pnBody, vl->iBody.category =vl);

        pnBody.setLayout(new WrapLayout());
        ui.InitComponents(list,iBody,pnBody);
        JTextField tf = ui.NewTextField("size",Pointed.size,pnBody);
        tf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER)
                {
                    float size = Float.parseFloat(tf.getText());
                    shape.Resize(size);
                }
            }
        });

        InitGroup(actor);
        InitFixtures(iBody.fixtures);
    }
    private void Init(IActor iActor)
    {
        GBox2d.debug = false;
        Runnable run = Screen.Returns(Arrays.asList(Scene.ui2.getChildren().toArray()));
        Scene.ui2.clearChildren();

        JFrame jFrame = ui.NewJFrame("body",panel1,()->{
            GBox2d.debug = true;
            group.remove();
            run.run();
            iActor.Refresh();
        });
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
    private void InitGroup(Actor actor)
    {
        group.setDebug(true);
        Vector3 camPos = Scene.GetUICamera().position;
        Scene.SetBounds(group,camPos.x,camPos.y, Align.center,actor.getWidth(),actor.getHeight(),Scene.ui2);
        group.addActor(actor);
        actor.setPosition(0,0);
    }
    private void InitFixture(IFixture iFixture)
    {
        pnFixture.removeAll();
        FixtureForm fixtureForm = new FixtureForm(iFixture,actor,pnFixture);
        fixtureForm.onNewShape = ()->{
            shape = InitShape(iFixture.iShape);
        };
        ui.Repaint(pnFixture);

        InitMark(iFixture);
        shape = InitShape(iFixture.iShape);
    }

    private void InitFixtures(List<IFixture> list)
    {
        FixtureListPanel panel = new FixtureListPanel(list);
        list3.addListSelectionListener(e->{
            int index = list3.getSelectedIndex();
            if (index<0) return;
            GDX.PostRunnable(()->InitFixture(list.get(index)));
        });
        panel.getIndex = ()->list3.getSelectedIndex();
        panel.onRefresh = index->{
            ui.ListSetModel(list3,panel.GetData());
            if (index<0) return;
            list3.setSelectedIndex(index);
        };
        panel.onRefresh.Run(0);
        btNew3.addActionListener(e->panel.New());
        btDelete3.addActionListener(e->panel.Delete());

    }
    private void InitMark(IFixture iFixture)
    {
        pnMark.removeAll();
        new MarkForm(iFixture,pnMark);
        ui.Repaint(pnMark);
    }

    //Shape
    private Pointed InitShape(IShape shape)
    {
        group.clearChildren();
        group.addActor(actor);
        if (shape instanceof IShape.IChain) return new ChainPointed((IShape.IPolygon) shape,group);
        if (shape instanceof IShape.IPolygon)return new PolygonPointed((IShape.IPolygon) shape,group);
        return new CirclePointed((IShape.ICircle) shape,group);
    }
}
