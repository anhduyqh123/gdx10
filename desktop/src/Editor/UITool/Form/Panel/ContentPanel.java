package Editor.UITool.Form.Panel;

import Editor.JFameUI;
import Editor.UITool.Form.IEmitterForm;
import Extend.CircleProgress.ICircleProgress;
import Extend.Spine.GSpine;
import Extend.Spine.ISpine;
import GameGDX.AssetLoading.AssetNode;
import GameGDX.Assets;
import GameGDX.GUIData.*;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.Reflect;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContentPanel {

    public static ContentPanel i;
    private String[] GetFonts()
    {
        List<String> list = new ArrayList<>();
        for(AssetNode n : Assets.Get(AssetNode.Kind.BitmapFont))
            list.add(n.name);
        return list.toArray(new String[0]);
    }
    public Class[] GetTypes()
    {
        Class[] types = {IGroup.class, IImage.class, ILabel.class, ITable.class,IScrollPane.class,IParticle.class, IActor.class,
                ISpine.class,IProgressBar.class,IScrollImage.class, ICircleProgress.class};
        return types;
    }
    public JPanel SetContent(JPanel panel, IActor iActor)
    {
        if (iActor instanceof IProgressBar) return new JIProgressBar(iActor,panel);
        if (iActor instanceof IImage) return new JIImage(iActor,panel);
        if (iActor instanceof ILabel) return new JILabel(iActor,panel);
        if (iActor instanceof ITable) return new JITable(iActor,panel);
        if (iActor instanceof IGroup) return new JIGroup(iActor,panel);
        if (iActor instanceof IParticle) return new JIParticle(iActor,panel);
        if (iActor instanceof ISpine) return new JISpine(iActor,panel);
        return new JIActor(iActor,panel);
    }
    protected class JIActor extends JPanel
    {
        protected JFameUI ui = new JFameUI();
        protected IActor iActor;
        protected JPanel panel;

        public JIActor(){}
        public JIActor(IActor iActor, JPanel panel)
        {
            this.iActor = iActor;
            this.panel = panel;
            Init(panel);
        }
        protected void Init(JPanel panel)
        {
            ui.NewLabel(iActor.getClass().getSimpleName(),panel).setForeground(Color.BLUE);
            ui.InitComponents(Arrays.asList("visible","touchable"), iActor,panel);
            ui.NewColorPicker(panel, iActor.hexColor, hex->{
                iActor.hexColor=hex;
                iActor.Refresh();
            });
            InitField(iActor.getClass(),panel);
            if (!iActor.prefab.equals(""))
                ui.NewLabel("prefab:"+ iActor.prefab,panel);
        }
        protected boolean InValidField(String name)
        {
            return true;
        }
        protected void InitField(Class type,JPanel panel)
        {
            for(Field field : ClassReflection.getDeclaredFields(type))
            {
                if (field.isStatic()) continue;
                if (field.getType().isInterface()) continue;
                if (InValidField(field.getName())) continue;
                ui.NewComponent(field, iActor,panel);
            }
        }
    }
    class JIGroup extends JIActor
    {
        public JIGroup(IActor iActor, JPanel panel)
        {
            super(iActor, panel);
            IGroup iGroup = (IGroup) iActor;
            List<String> children = new ArrayList<>(iGroup.GetChildName());
            children.add(0,"");
            String[] childName = children.toArray(new String[0]);
            ui.NewComboBox("sizeName",childName,iGroup.sizeName,panel,
                    vl->iGroup.sizeName = vl);
        }

        @Override
        protected boolean InValidField(String name) {
            if (name.equals("sizeName")) return true;
            return false;
        }
    }
    class JIImage extends JIActor
    {
        protected JPanel left,right;
        public JIImage(IActor iActor, JPanel panel)
        {
            this.iActor = iActor;
            this.panel = panel;
            IImage iImage = (IImage) iActor;

            left = ui.NewPanel(200,140,panel);
            right = ui.NewPanel(500,140,panel);
            ui.SetGap(left,0,0);
            ui.SetGap(right,0,0);
            InitITexturePanel(iImage,left);
            Init(right);
        }

        @Override
        protected boolean InValidField(String name) {
            if (name.equals("hexColor")) return true;
            if (name.equals("iTexture")) return true;
            return false;
        }

        private void InitITexturePanel(IImage iImage, JPanel parent)
        {
            JPanel pn = ui.NewBigPanel("texture",180,80,parent);
            ui.SetGap(pn,0,0);
            ITexture_Panel(iImage,pn);
        }
        private void ITexture_Panel(IImage iImage, JPanel panel)
        {
            panel.removeAll();
            String[] st = {"Texture","NinePath"};
            String s0 = st[0];
            int h = 80;
            if (iImage.iTexture instanceof IImage.INinePath){
                s0 = st[1];
                h = 160;
            }
            ui.SetSize(panel,180,h);
            ui.NewComboBox(st,s0,panel,vl->{
                if (vl.equals(st[0])) iImage.iTexture = new IImage.ITexture();
                if (vl.equals(st[1])) iImage.iTexture = new IImage.INinePath();
                ITexture_Panel(iImage,panel);
            });
            ui.InitComponents(Arrays.asList("name"), iImage.iTexture,panel);

            if (iImage.iTexture instanceof IImage.INinePath){
                List<String> list = Arrays.asList("left","right","top","bottom");
                for(String n : list)
                {
                    Field field = Reflect.GetField(IImage.INinePath.class,n);
                    Object value = Reflect.GetValue(field,iImage.iTexture);
                    ui.NewTextField(n,value,30,panel,vl->ui.SetField(field,iImage.iTexture,vl));
                }
            }

            ui.Repaint(panel);
        }
    }
    class JIProgressBar extends JIImage
    {
        public JIProgressBar(IActor iActor, JPanel panel) {
            super(iActor, panel);
            InitField(IScrollImage.class,right);
        }
    }
    class JILabel extends JIActor
    {
        public JILabel(IActor iActor, JPanel panel)
        {
            super(iActor,panel);

            ILabel iLabel = (ILabel) iActor;
            ui.NewComboBox(GetFonts(), iLabel.GetFontName(),panel,iLabel::SetFont);
        }

        @Override
        protected boolean InValidField(String name) {
            if (name.equals("font")) return true;
            return false;
        }
    }
    class JIParticle extends JIActor
    {
        public JIParticle(IActor iActor, JPanel panel)
        {
            super(iActor,panel);
            IParticle par = (IParticle) iActor;
            ui.NewButton("Play",panel,par::Start);
            ui.NewButton("Stop",panel,par::Stop);
            ui.NewButton("Emitter",panel,()->{
                ui.NewWindow("Emitter",new IEmitterForm(par.iEmitters).panel1,
                        500,400,par::RefreshEmitter);
            });
        }

        @Override
        protected boolean InValidField(String name) {
            return false;
        }
    }
    class JITable extends JIActor
    {
        public JITable(IActor iActor, JPanel panel)
        {
            super(iActor,panel);
            ITable table = (ITable) iActor;
            JTextField tfSL = ui.NewTextField("SL","1",panel);
            ui.NewButton("Clone",panel).addActionListener(e->table.CloneChild(Integer.parseInt(tfSL.getText())));
        }

        @Override
        protected boolean InValidField(String name) {
            return false;
        }
    }

    class JISpine extends JIActor
    {
        public JISpine(IActor iActor, JPanel panel)
        {
            super(iActor,panel);
            GSpine spine = iActor.GetActor();
            String[] ani = spine.GetAnimationNames();
            if (ani!=null)
                ui.NewComboBox("animation",ani,ani[0],panel,st->spine.SetAnimation(st,true));
        }

        @Override
        protected boolean InValidField(String name) {
            if (name.equals("animation")) return true;
            return false;
        }
    }
}
