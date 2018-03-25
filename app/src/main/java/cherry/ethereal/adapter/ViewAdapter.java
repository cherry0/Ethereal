package cherry.ethereal.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by many on 2018/3/24.
 */

public class ViewAdapter extends FragmentPagerAdapter {

    private List<Fragment> Fragments;

    public ViewAdapter(FragmentManager fragmentManager, List<Fragment> _fragments) {
        super(fragmentManager);
        Fragments = _fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return Fragments.get(position);
    }

    @Override
    public int getCount() {
        return Fragments.size();
    }
}