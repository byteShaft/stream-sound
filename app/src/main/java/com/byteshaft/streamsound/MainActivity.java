package com.byteshaft.streamsound;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.byteshaft.streamsound.fragments.PlayerFragment;
import com.byteshaft.streamsound.fragments.PlayerListFragment;
import com.byteshaft.streamsound.fragments.SocialMediaFragment;
import com.byteshaft.streamsound.service.NotificationService;
import com.byteshaft.streamsound.service.PlayService;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(getIconForEach(i));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PlayService.getInstance().stopSelf();
        NotificationService.getsInstance().stopSelf();
    }

    private int getIconForEach(int tabNum) {
        switch (tabNum) {
            case 0:
                return R.drawable.play_list;
            case 1:
                return R.drawable.player;
            case 2:
                return R.drawable.social;
            default:
                return R.drawable.play_list;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            System.out.println(position);
            switch (position) {
                case 0:
                    return new PlayerListFragment();
                case 1:
                    return new PlayerFragment();
                case 2:
                    return new SocialMediaFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "List";
                case 1:
                    return "player";
                case 2:
                    return "Social";
            }
            return null;
        }
    }
}
