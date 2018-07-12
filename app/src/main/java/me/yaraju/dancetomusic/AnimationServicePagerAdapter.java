package me.yaraju.dancetomusic;

import android.app.Activity;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import me.yaraju.dancetomusic.services.GifInfo;
import me.yaraju.dancetomusic.services.GifService;
import me.yaraju.dancetomusic.services.OnFailureHandler;
import me.yaraju.dancetomusic.services.OnSuccessHandler;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class AnimationServicePagerAdapter extends FragmentPagerAdapter {

    private final TypedArray imgs;
    private SoundDetector soundDetector;
    private List<String> gifUrls = new ArrayList<>();

    public AnimationServicePagerAdapter(Activity activity, FragmentManager fm, SoundDetector soundDetector) {
        super(fm);
        imgs = activity.getResources().obtainTypedArray(R.array.all);
        this.soundDetector = soundDetector;
        Log.d("test", "just a dummy line");
        gifUrls.add("https://media3.giphy.com/media/fVR8DbRIlhhMk/100.gif");
    }

    @Override
    public Fragment getItem(final int position) {

        final AnimationDanceFragment[] fragment = new AnimationDanceFragment[1];
        fragment[0] = AnimationDanceFragment.newInstance(gifUrls.get(position % gifUrls.size()));
        soundDetector.addSoundChangeListener(fragment[0]);
        new GifService().fetchRandom("cats dance", new OnSuccessHandler() {

            @Override
            public void onSuccess(GifInfo data) {
                System.out.println(data.getUrl());
                gifUrls.add(data.getUrl());
                notifyDataSetChanged();

            }
        }, new OnFailureHandler() {
            @Override
            public void onFail(Throwable throwable) {

            }
        });


        return fragment[0];
    }

    @Override
    public int getCount() {
        return gifUrls.size();
    }
}
