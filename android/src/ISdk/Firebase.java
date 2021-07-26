package ISdk;

import GameGDX.Reflect;
import android.app.Activity;
import android.os.Bundle;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class Firebase {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Activity context;
    public Runnable onFetch;

    public Firebase(Activity context)
    {
        this.context = context;
        InitAnalytics();
        InitRemoteConfig();
    }
    private void InitAnalytics()
    {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }
    public void Track(String content)
    {
        try {
            Bundle bundle = new Bundle();
            mFirebaseAnalytics.logEvent(content, bundle);
        }catch (Exception e){}
    }

    private void InitRemoteConfig()
    {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(context, task -> {
                    if (onFetch!=null)
                        onFetch.run();
                });
    }
    public String GetString(String key,String defValue)
    {
        try {
            String result = mFirebaseRemoteConfig.getString(key);
            if (!result.equals("")) return result;
        }catch (Exception e){}
        return defValue;
    }
    public boolean GetBoolean(String key,boolean defValue)
    {
        try {
            String result = mFirebaseRemoteConfig.getString(key);
            if (!result.equals("")) return Boolean.parseBoolean(result);
        }catch (Exception e){}
        return defValue;
    }
    public int GetInteger(String key,int defValue)
    {
        try {
            String result = mFirebaseRemoteConfig.getString(key);
            if (!result.equals("")) return Integer.parseInt(result);
        }catch (Exception e){}
        return defValue;
    }
    public <T> T GetConfig(String key,T value0)
    {
        try {
            String result = mFirebaseRemoteConfig.getString(key);
            if (result.equals("")) return value0;
            if (value0 instanceof Integer) return (T)Reflect.ToBaseType(result,Integer.class);
            if (value0 instanceof Boolean) return (T)Reflect.ToBaseType(result,Boolean.class);
            return (T)result;
        }catch (Exception e){}
        return value0;
    }
}
