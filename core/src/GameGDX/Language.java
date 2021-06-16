package GameGDX;

import java.util.*;

public class Language {
    public static Language i;

    private Map<String, Node> map = new LinkedHashMap<>();
    private List<String> codes = new ArrayList<>();
    private String currentCode;

    private Map<String, GDX.Runnable<String>> cbChange = new LinkedHashMap<>();

    public void SetCode(int index)
    {
        if (codes.size()<=0) return;
        SetCode(codes.get(0));
    }
    public void SetCode(String newCode)
    {
        if (!codes.contains(newCode)) return;
        currentCode = newCode;
        for(GDX.Runnable run : cbChange.values()) run.Run(currentCode);
    }
    public static String GetCode()
    {
        return i.currentCode;
    }
    public static String GetContent(String key)
    {
        return i.map.get(key).GetContent(GetCode());
    }
    public static void AddChangeCallback(String key,GDX.Runnable cb)
    {
        i.cbChange.put(key,cb);
        cb.Run(GetCode());
    }

    public String ToJsonData()
    {
        return Json.ToJsonData(this,Arrays.asList("map","codes"));
    }

    public Node GetNode(String key)
    {
        return map.get(key);
    }

    public Node AddKey(String key)
    {
        return AddKey(key,new Node(codes));
    }
    public Node AddKey(String key,Node node)
    {
        if (map.containsKey(key)) return map.get(key);
        map.put(key,node);
        return map.get(key);
    }
    public void RenameKey(String oldKey,String newKey)
    {
        Node node = GetNode(oldKey);
        map.remove(oldKey);
        map.put(newKey,node);
    }
    public void RemoveKey(String key)
    {
        if (!map.containsKey(key)) return;
        map.remove(key);
    }
    public Set<String> GetKeys()
    {
        return map.keySet();
    }
    public boolean ContainsKey(String key)
    {
        return map.containsKey(key);
    }

    public List<String> GetCodes()
    {
        return codes;
    }
    public void AddCode(String code)
    {
        if (codes.contains(code)) return;
        codes.add(code);
        for(Node n : map.values())
            n.Add(code);
    }
    public void RemoveCode(String code)
    {
        if (!codes.contains(code)) return;
        codes.remove(code);
        for(Node n : map.values())
            n.Remove(code);
    }

    public static Language NewLanguage()
    {
        i = new Language();
        return i;
    }
    public static Language NewLanguage(String jsData)
    {
        i = Json.FromJson(Language.class,jsData);
        return i;
    }

    public static class Node
    {
        private Map<String,String> map = new LinkedHashMap<>();

        public Node(){}
        public Node(List<String> codes)
        {
            for(String code : codes)
                Add(code);
        }
        public String GetContent(String key)
        {
            return map.get(key);
        }
        public void Remove(String key)
        {
            if(!map.containsKey(key)) return;
            map.remove(key);
        }
        public void Add(String key)
        {
            if (map.containsKey(key)) return;
            map.put(key,"");
        }
        public void Put(String key,String content)
        {
            map.put(key,content);
        }
    }
}
