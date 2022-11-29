package Extend.Util;

import GameGDX.GDX;
import GameGDX.Translate;

import java.util.Arrays;

/**
 * Version 1.1 by BaDuy
 */

public class GLocale {
    public static GLocale i;
    private final String[] locales,replace;
    private String locale;
    public GLocale(String[] locales, String[] replace, String defaultLocale)
    {
        i = this;
        this.locales = locales;
        this.replace = replace;
        ReplaceCountry(defaultLocale);
        defaultLocale = FindValidLocale(defaultLocale);
        locale = GDX.GetPrefString("locale",defaultLocale);
    }
    public String GetCode()
    {
        return locale.split("_")[0];
    }
    public void Set(String locale)
    {
        this.locale = locale;
        GDX.SetPrefString("locale",locale);
        Translate.i.SetCode(GetCode());
    }
    public String Get()
    {
        return locale;
    }
    public String[] GetLocales()
    {
        return locales;
    }
    private void ReplaceCountry(String locale)
    {
        if (!Arrays.asList(replace).contains(locale)) return;
        for(int i=0;i<locales.length;i++)
            if (TheSameLanguage(locale,locales[i]))
            {
                locales[i] = locale;
                return;
            }
    }
    //find the locale valid
    private String FindValidLocale(String locale)
    {
        for(String lc : locales)
            if (lc.equals(locale)) return lc;
        for(String lc : locales)
            if (TheSameLanguage(lc,locale)) return lc;
        return locales[0];
    }
    private boolean TheSameLanguage(String locale1,String locale2)
    {
        String lang1 = locale1.split("_")[0];
        String lang2 = locale2.split("_")[0];
        return lang1.equals(lang2);
    }
}
