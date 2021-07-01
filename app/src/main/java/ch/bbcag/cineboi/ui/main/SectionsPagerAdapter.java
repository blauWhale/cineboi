package ch.bbcag.cineboi.ui.main;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import ch.bbcag.cineboi.DiscoverFragment;
import ch.bbcag.cineboi.FavoriteFragment;
import ch.bbcag.cineboi.WatchlistFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new DiscoverFragment();
                break;
            case 1:
                fragment = new WatchlistFragment();
                break;
            case 2:
                fragment = new FavoriteFragment();
                break;

        }
        return fragment;
    }


    @Override
    public int getCount() {
        return 3;
    }
}