package SDK;

import android.app.Activity;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.game.R;
import com.google.android.gms.ads.*;

public class AdMob {
    private AdView adView;
    private boolean isVideoLoaded,isInterstitialLoaded,bannerVisible=true;

    private Activity context;
    private FrameLayout rootView;
    public AdMob(Activity context,FrameLayout rootView)
    {
        this.context = context;
        this.rootView = rootView;
        MobileAds.initialize(context, initializationStatus -> {
        });

        InitBanner();
    }
    private void InitBanner()
    {
        adView = new AdView(context);
        AdSize adSize = getAdSize();
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
        adViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        adViewParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        adViewParams.height = adSize.getHeightInPixels(context);
        relativeLayout.addView(adView, adViewParams);
        adView.loadAd(new AdRequest.Builder().build());

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
        bannerVisible = visible;
        context.runOnUiThread(()->{
            if (visible) adView.setVisibility(AdView.VISIBLE);
            else adView.setVisibility(AdView.GONE);
        });
    }
}
