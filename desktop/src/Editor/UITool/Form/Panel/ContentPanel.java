package Editor.UITool.Form.Panel;

import Editor.JFameUI;
import Editor.UITool.Form.AnimForm;
import Extend.CircleProgress.ICircleProgress;
import Extend.Frame.GFrame;
import Extend.Frame.IFrame;
import Extend.ITabGroup;
import Extend.ITextField;
import Extend.PagedScroll.IPaged2Scroll;
import Extend.PagedScroll.IPagedScroll;
import Extend.Spine.GSpine;
import Extend.Spine.ISpine;
import GameGDX.AssetLoading.AssetNode;
import GameGDX.Assets;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.*;
import GameGDX.Reflect;
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
                ISpine.class, IFrame.class,IProgressBar.class,IScrollImage.class, ICircleProgress.class, ITabGroup.class, ITextField.class,
                IPagedScroll.class, IPaged2Scroll.class};
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
        if (iActor instanceof IFrame) return new JIFrame(iActor,panel);
        return new JIActor(iActor,panel);
    }
    protected class JIActor extends JPanel
    {
        protected JFameUI ui = new JFameUI();
        protected IActor iActor;
        protected JPanel panel;
        protected List<String> fields = new ArrayList<>();
        protected List<String> removeFields = new ArrayList<>();

        public JIActor(){}
        public JIActor(IActor iActor, JPanel panel)
        {
            this.iActor = iActor;
            this.panel = panel;
            fields = ui.GetFields(iActor);
            InitInValidFields();
            fields.removeAll(removeFields);
            Init(panel);
        }
        protected void Init(JPanel panel)
        {
            ui.NewLabel(iActor.getClass().getSimpleName(),panel).setForeground(Color.BLUE);
            ui.NewColorPicker(panel, iActor.hexColor, hex->{
                iActor.hexColor=hex;
                iActor.Refresh();
            });
            if (!iActor.prefab.equals(""))
                //ui.NewLabel("prefab:"+ iActor.prefab,panel);
                ui.NewTextField("",iActor.prefab,panel,vl->iActor.prefab=vl);

            ui.InitComponents(fields,iActor,panel);
        }
        protected void InitInValidFields()
        {
            InitInValidFields("iSize","iPos","acList","hexColor","prefab");
        }
        protected void InitInValidFields(String... arr)
        {
            for (String s : arr)
                removeFields.add(s);
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
        protected void InitInValidFields() {
            super.InitInValidFields();
            InitInValidFields("sizeName");
        }
    }
    class JIImage extends JIActor
    {
        public JIImage(IActor iActor, JPanel panel)
        {
            super(iActor, panel);
        }

        @Override
        protected void Init(JPanel panel) {
            IImage iImage = (IImage) iActor;
            InitITexturePanel(iImage,panel);
            super.Init(panel);
        }

        @Override
        protected void InitInValidFields() {
            super.InitInValidFields();
            InitInValidFields("iTexture");
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
            int h = 120;
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
            ui.InitComponents(Arrays.asList("name","multiLanguage"), iImage.iTexture,panel);

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
            //InitField(panel);
        }
    }
    class JILabel extends JIActor
    {
        public JILabel(IActor iActor, JPanel panel)
        {
            super(iActor,panel);

            ILabel iLabel = (ILabel) iActor;
            ui.NewComboBox(GetFonts(), iLabel.font,panel,f->{
                iLabel.font = f;
                iLabel.SetFont(f);
            });
        }

        @Override
        protected void InitInValidFields() {
            super.InitInValidFields();
            InitInValidFields("font");
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
//            ui.NewButton("Emitter",panel,()->{
//                ui.NewWindow("Emitter",new IEmitterForm(par.iEmitters).panel1,
//                        500,400,par::RefreshEmitter);
//            });
        }
    }
    class JITable extends JIActor
    {
        public JITable(IActor iActor, JPanel panel)
        {
            super(iActor,panel);
            ITable table = (ITable) iActor;
            //JTextField tfSL = ui.NewTextField("SL","1",panel);
            //ui.NewButton("Clone",panel).addActionListener(e->table.CloneChild(Integer.parseInt(tfSL.getText())));
        }
    }

    class JISpine extends JIActor
    {
        public JISpine(IActor iActor, JPanel panel)
        {
            super(iActor,panel);
            ISpine iSpine = (ISpine)iActor;
            GSpine spine = iActor.GetActor();
            String[] skins = spine.GetSkinNames();
            if (skins!=null)
                ui.NewComboBox("skins",skins,iSpine.skin,panel,st->{
                    iSpine.skin = st;
                    spine.SetSkin(st);
                });

            List<String> ani = spine.GetAnimationNames();
            ani.add(0,"");
            String[] arr = ani.toArray(new String[0]);
            ui.NewComboBox("animation",arr,iSpine.animation,panel,st->{
                iSpine.animation = st;
                spine.SetAnimation(st,true);
            });
        }

        @Override
        protected void InitInValidFields() {
            super.InitInValidFields();
            InitInValidFields("animation","skin");
        }
    }
    class JIFrame extends JIActor
    {
        public JIFrame(IActor iActor, JPanel panel)
        {
            super(iActor,panel);
            IFrame iFrame = (IFrame)iActor;
            GFrame frame = iActor.GetActor();
            String[] ani = frame.GetAnimationNames();
            if (ani.length>0)
            {
                JComboBox cb = ui.NewComboBox("animation",ani,ani[0],panel, frame::SetAnimation);
                cb.setSelectedIndex(0);
            }
            ui.NewButton("IAnim",panel,()->new AnimForm(iFrame.iAniMap,()->{
                iActor.Refresh();
                panel.removeAll();
                SetContent(panel, iActor);
                ui.Repaint(panel);
            }));
        }

        @Override
        protected void InitInValidFields() {
            super.InitInValidFields();
            InitInValidFields("iAniMap");
        }
    }
    class JIMask extends JIGroup
    {
        public JIMask(IActor iActor, JPanel panel)
        {
            super(iActor,panel);
            //ui.NewButton("Shape",panel,()->new MaskForm((IMask) iActor));
        }
        @Override
        protected void InitInValidFields() {
            super.InitInValidFields();
            InitInValidFields("shape","renderer");
        }
    }
}
