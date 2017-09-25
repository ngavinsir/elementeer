package id.ngavinsir.splashscreen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GAVIN on 9/20/2017.
 */

public class WeightAdapter extends FragmentPagerAdapter implements ViewPager.PageTransformer{

    private List<Fragment> frgs = new ArrayList<>();

    public WeightAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return frgs.get(position);
    }

    public Fragment getF(int pos)
    {
        return frgs.get(pos);
    }

    @Override
    public int getCount() {
        return frgs.size();
    }

    public void addFragment(Fragment fragment) {
        frgs.add(fragment);
    }

    @Override
    public void transformPage(View page, float position) {
        float scale = 1f;
        if (position > 0) {
            scale = scale - position * 1.28f;
        } else {
            scale = scale + position * 1.28f;
        }
        if (scale < 0) scale = 0;
        if(position == 0) scale = 1f;
        ((TypeLayout)page.findViewById(R.id.custom_lay)).setScaleBoth(scale);
    }
}
