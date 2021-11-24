package Editor.UITool.Physics;

import Editor.JFameUI;
import Editor.UITool.EConfig;
import Extend.Box2d.GBox2d;
import Extend.Box2d.IFixture;
import Extend.Box2d.IShape;
import GameGDX.GDX;
import com.badlogic.gdx.scenes.scene2d.Actor;

import javax.swing.*;
import java.util.List;

public class FixtureForm {
    private JComboBox cbShape;
    private JComboBox cbCategory;
    private JPanel panel1;
    private JFameUI ui = new JFameUI();
    public Runnable onNewShape;

    public FixtureForm(IFixture iFixture, Actor actor, JPanel parent)
    {
        parent.add(panel1);
//        String[] arr = EConfig.e.Get("category").asStringArray();
//        ui.ComboBox(cbCategory,arr, GBox2d.GetCategory((short) iFixture.category),
//                name->iFixture.category=GBox2d.GetCategoryBit(name));

        Class[] classes = {IShape.ICircle.class, IShape.IPolygon.class,IShape.IChain.class, IShape.IEdge.class};
        String[] types = ui.ClassToName(classes);
        ui.ComboBox(cbShape,types,iFixture.iShape.getClass().getSimpleName());
        cbShape.addActionListener(e->{
            int index = cbShape.getSelectedIndex();
            if (index==0) iFixture.iShape = new IShape.ICircle(actor.getWidth());
            if (index==1) iFixture.iShape = new IShape.IPolygon(actor.getWidth(),actor.getHeight());
            if (index==2) iFixture.iShape = new IShape.IChain(actor.getWidth());
            if (index==3) iFixture.iShape = new IShape.IEdge(actor.getWidth());
            GDX.PostRunnable(onNewShape);

//            GDX.PostRunnable(()->{
//                onNewShape.run();
//                group.clearChildren();
//                InitGroup(actor);
//                InitShape(iFixture.iShape);
//            });
        });

        ui.NewButton("category",panel1,()->
                new MarkForm(iFixture.category, iFixture.mark,(c,m)->{
                    iFixture.category=c;
                    iFixture.mark=m;
                }));

        List<String> list1 = ui.GetDeclaredFields(iFixture);
        list1.remove("iShape");list1.remove("category");list1.remove("mark");
        ui.InitComponents(list1,iFixture,panel1);
    }
}
