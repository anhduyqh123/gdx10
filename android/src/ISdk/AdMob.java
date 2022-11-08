package ISdk;

import GameGDX.GDX;
import GameGDX.Ref;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import com.game.R;
import com.google.android.gms.ads.*;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class AdMob {
    private AdView adView;
    private InterstitialAd mInterstitialAd;
    private RewardedAd mRewardedAd;

    private long lastInterstitialTime;
    private boolean bannerVisible=true;

    private Activity context;
    private FrameLayout rootView;

    public GDX.Func2<Object,String,Object> getConfig = (n,vl)->vl;

    public AdMob(Activity context,FrameLayout rootView)
    {
        this.context = context;
        this.rootView = rootView;
        MobileAds.initialize(context, initializationStatus -> {
        });
    }
    private <T> T GetConfig(String name,T value0)
    {
        return (T)getConfig.Run(name,value0);
    }
    private int GetInit(String name,int value0)
    {
        return GetConfig(name,value0);
    }
    private boolean GetBool(String name,boolean value0)
    {
        return GetConfig(name,value0);
    }

    //banner
    public void InitBanner_Top()
    {
        InitBanner(RelativeLayout.ALIGN_PARENT_TOP);
    }
    public void InitBanner_Bot()
    {
        InitBanner(RelativeLayout.ALIGN_PARENT_BOTTOM);
    }
    private void InitBanner(int align, AdSize adSize, GDX.Runnable<RelativeLayout.LayoutParams> cb)
    {
        if (!GetBool("is_banner",true)) return;
        adView = new AdView(context);
        adView.setAdSize(adSize);
        adView.setAdUnitId(context.getString(R.string.banner_id));
        adView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                adView.setVisibility(View.GONE);
                ShowBanner(bannerVisible);
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                adView.setVisibility(View.INVISIBLE);
            }
        });
        RelativeLayout relativeLayout = new RelativeLayout(context);
        rootView.addView(relativeLayout);

        RelativeLayout.LayoutParams adViewParams = new RelativeLayout.LayoutParams(AdView.LayoutParams.WRAP_CONTENT, AdView.LayoutParams.WRAP_CONTENT);
        adViewParams.addRule(align);
        adViewParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        cb.Run(adViewParams);
        relativeLayout.addView(adView, adViewParams);
        adView.loadAd(new AdRequest.Builder().build());
    }
    private void InitBanner(int align)
    {
        InitBanner(align,AdSize.FULL_BANNER,p->{});
//        AdSize adSize = getAdSize();
//        InitBanner(align,adSize,p->p.height = adSize.getHeightInPixels(context));
    }
    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = context.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }

    public void ShowBanner(boolean visible)
    {
        if (!GetBool("is_banner",true)) return;
        if (adView==null) return;
        bannerVisible = visible;
        context.runOnUiThread(()->{
            if (visible) adView.setVisibility(AdView.VISIBLE);
            else adView.setVisibility(AdView.GONE);
        });
    }

    //Interstitial
    public void InitInterstitial()
    {
        if (!GetBool("is_interstitial",true)) return;
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context,context.getString(R.string.fullscreen_id), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        InitInterstitial();
                    }
                    @Override
                    public void onAdShowedFullScreenContent() {
                        mInterstitialAd = null;
                    }
                });

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                mInterstitialAd = null;
            }
        });
    }
    public void ShowFullscreen() {
        long fullscreenTime = GetInit("fullscreenTime",80)* 1000L;//remote config
        if (System.currentTimeMillis() - lastInterstitialTime < fullscreenTime) return;
        context.runOnUiThread(()->{
            if (IsFullscreenReady()){
                mInterstitialAd.show(context);
                lastInterstitialTime = System.currentTimeMillis();
            }
        });
    }
    public boolean IsFullscreenReady()
    {
        if (mInterstitialAd!=null) return true;
        context.runOnUiThread(this::InitInterstitial);
        return false;
    }

    //video reward
    public void InitVideoReward()
    {
        if (!GetBool("is_videoreward",true)) return;
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(context, context.getString(R.string.video_id), adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                mRewardedAd = rewardedAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mRewardedAd = null;
            }
        });
    }
    public boolean IsVideoRewardReady()
    {
        if (mRewardedAd!=null) return true;
        context.runOnUiThread(this::InitVideoReward);
        return false;
    }
    public void ShowVideoReward(GDX.Runnable<Boolean> callback)
    {
        if (!IsVideoRewardReady()) return;
        context.runOnUiThread(()-> {
            Ref<Boolean> isItem = new Ref(false);
            mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdShowedFullScreenContent() {
                    mRewardedAd = null;
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    GDX.PostRunnable(() -> callback.Run(isItem.Get()));
                    InitVideoReward();
                }
            });
            mRewardedAd.show(context, rewardItem -> isItem.Set(true));
        });
    }
}
