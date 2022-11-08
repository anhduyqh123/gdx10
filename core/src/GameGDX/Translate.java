package GameGDX;

import com.badlogic.gdx.utils.JsonValue;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Translate {
    public static Translate i;
    private final JsonValue json = new JsonValue(JsonValue.ValueType.object);
    private final Map<String, Runnable> cbChange = new LinkedHashMap<>();
    public String code = "en";//get only
    public List<String> codes;//get only

    public Translate(){
        this(Assets.GetNode("translate").url);
    }
    public Translate(String url)
    {
        i = this;
        String data = GDX.GetString(url);
        data = data.replace("\r","");
        String[] rows = data.split("\n");
        //rows[0] = rows[0].replace("key,","");
        codes = Arrays.asList(rows[0].split(","));
        for (int i=1;i<rows.length;i++)
        {
            String[] arr = rows[i].split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            for (int j=1;j<arr.length;j++)
                Add(arr[0],codes.get(j),arr[j]);
        }
    }
    private String Format(String st)
    {
        char ch0 = st.charAt(0),ch1 = st.charAt(st.length()-1);
        if (st.contains(",") && ch0=='\"' && ch1=='\"') return st.substring(1,st.length()-2);
        return st;
    }
    private void Add(String key,String code,String value)
    {
        if (!json.has(key)) json.addChild(key,new JsonValue(JsonValue.ValueType.object));
        json.get(key).addChild(code,new JsonValue(Format(value)));
    }
    public void SetCode(int index)
    {
        SetCode(codes.get(index));
    }
    public void SetCode(String code)
    {
        if (!codes.contains(code)) this.code = codes.get(1);
        else this.code = code;
        for(Runnable i : cbChange.values()) i.run();
    }
    public String Get(String key)
    {
        return json.get(key).getString(code);
    }
    public void AddChangeCallback(String key,Runnable cb)
    {
        cbChange.put(key,cb);
        cb.run();
    }
    public JsonValue GetData()
    {
        return json;
    }
}
