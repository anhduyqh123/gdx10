package Editor.LanguageTool;

import java.util.*;

public class JLocale {

    private List<String> codes = new ArrayList<>();
    private Map<String,List<String>> map = new HashMap<>();

    public JLocale()
    {
        for(Locale lc : Locale.getAvailableLocales())
        {
            String code = lc.getLanguage();
            if (code.equals("")) continue;
            if (!codes.contains(code)) codes.add(code);
            AddCountry(code,lc.getDisplayLanguage());
        }
    }
    private void AddCountry(String key,String country)
    {
        if (!map.containsKey(key)) map.put(key,new ArrayList<>());
        map.get(key).add(country);
    }
    private String GetCountry(String code)
    {
        return map.get(code).get(0);
    }
    public String[] GetDisplayCodes()
    {
        return GetDisplayCodes(codes);
    }
    public String[] GetDisplayCodes(List<String> codes)
    {
        String[] arr = new String[codes.size()];
        int i=0;
        for(String code : codes)
            arr[i++] = GetDisplay(code);
        return arr;
    }
    public String GetCode(String display)
    {
        return display.split("-")[0];
    }
    public String GetDisplay(String code)
    {
        return code+"-"+GetCountry(code);
    }
}
