package com.game.desktop;

import GameGDX.Json;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.XmlReader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlToJson {

    public XmlToJson(String path)
    {
        FileHandle dir = new FileHandle(path);
        for (FileHandle f : dir.list())
        {
            if (f.extension().equals("plist"))
                Read(f);
        }
    }
    private void Read(FileHandle file)
    {
        System.out.println(file.name()+"...");
        String path = file.parent().path();

        XmlReader.Element element = new XmlReader().parse(file.reader());
        XmlReader.Element dict = element.getChildByName("dict");
        JsonValue js = new JsonValue(JsonValue.ValueType.object);
        ForE(js,dict);

        Unpack(js,path);
    }
    private void ForE(JsonValue js,XmlReader.Element element)
    {
        for(int i=0;i<element.getChildCount()/2;i++)
            AddJson(js,element.getChild(i*2),element.getChild(i*2+1));
    }
    private void AddJson(JsonValue parent, XmlReader.Element key, XmlReader.Element value)
    {
        String name = key.getText();
        JsonValue js;
        if (value.getName().equals("dict")){
            js = new JsonValue(JsonValue.ValueType.object);
            ForE(js,value);
        }
        else{
            String text = value.getText();
            if (text==null) text = value.getName();
            js = new JsonValue(text);
        }
        parent.addChild(name,js);
    }

    //unpack
    private void Unpack(JsonValue jsData,String input)
    {
        try {
            String nameFile = jsData.get("metadata").getString("textureFileName");
            FileHandle imgFile = new FileHandle(input+"/"+nameFile);

            final BufferedImage source = ImageIO.read(imgFile.file());
            for (JsonValue i : jsData.get("frames"))
            {
                String name = i.name;
                String frame = i.getString("frame");
                List<Vector2> list = GetFrame(frame);
                boolean isRotate = i.getBoolean("rotated");
                if (isRotate)
                {
                    Vector2 p = list.get(1);
                    p.set(p.y,p.x);
                }
                BufferedImage buff = source.getSubimage((int)list.get(0).x,(int)list.get(0).y, (int)list.get(1).x,(int)list.get(1).y);
                if (isRotate) buff = rotateImage(buff,90);

                String url = imgFile.parent().path()+"/"+imgFile.nameWithoutExtension()+"/"+name+ ".png";
                FileHandle file = new FileHandle(url);
                file.writeString("",false);
                ImageIO.write(buff, "png", file.file());
            }
        }catch (Exception e){e.printStackTrace();}
    }
    private List<Vector2> GetFrame(String st)
    {
        st = st.substring(1,st.length()-1);
        st = st.replace("{","(").replace("}",")");
        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(st);
        List<Vector2> list = new ArrayList<>();
        while(m.find()) {
            String[] s = m.group(1).split(",");
            Vector2 p = new Vector2(Integer.parseInt(s[0]),Integer.parseInt(s[1]));
            list.add(p);
        }
        return list;
    }

    private BufferedImage rotateImage(BufferedImage buffImage, double angle) {
        double radian = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(radian));
        double cos = Math.abs(Math.cos(radian));

        int width = buffImage.getWidth();
        int height = buffImage.getHeight();

        int nWidth = (int) Math.floor((double) width * cos + (double) height * sin);
        int nHeight = (int) Math.floor((double) height * cos + (double) width * sin);

        BufferedImage rotatedImage = new BufferedImage(
                nWidth, nHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = rotatedImage.createGraphics();

        graphics.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        graphics.translate((nWidth - width) / 2, (nHeight - height) / 2);
        // rotation around the center point
        graphics.rotate(radian, (double) (width / 2), (double) (height / 2));
        graphics.drawImage(buffImage, 0, 0, null);
        graphics.dispose();

        return rotatedImage;
    }
}
