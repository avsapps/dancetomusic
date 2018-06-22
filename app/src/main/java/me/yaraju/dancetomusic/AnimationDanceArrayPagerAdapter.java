package me.yaraju.dancetomusic;

import android.app.Activity;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class AnimationDanceArrayPagerAdapter extends FragmentPagerAdapter {

    private final TypedArray imgs;
    private SoundDetector soundDetector;

    public AnimationDanceArrayPagerAdapter(Activity activity, FragmentManager fm, SoundDetector soundDetector) {
        super(fm);
        imgs = activity.getResources().obtainTypedArray(R.array.all);
        this.soundDetector = soundDetector;
        Log.d("test", "just a dummy line");
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        int resourceId = imgs.getResourceId(position, 0);
        AnimationDanceFragment fragment = AnimationDanceFragment.newInstance(resourceId);
        soundDetector.addSoundChangeListener(fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return imgs.length();
    }
}
