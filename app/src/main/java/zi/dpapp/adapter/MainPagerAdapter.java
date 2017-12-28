package zi.dpapp.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import zi.dpapp.FacebookFragment;
import zi.dpapp.InstagramFragment;
import zi.dpapp.R;
import zi.dpapp.TwitterFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {

        private Context mContext;

        public MainPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            mContext = context;
        }

        // This determines the fragment for each tab
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new FacebookFragment();
            } else if (position == 1){
                return new TwitterFragment();
            } else{
                return new InstagramFragment();
            }
        }

        // This determines the number of tabs
        @Override
        public int getCount() {
            return 3;
        }

        // This determines the title for each tab
        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            switch (position) {
                case 0:
                    return "Facebook";
                case 1:
                    return "Twitter";
                case 2:
                    return "Instagram";
                default:
                    return null;
            }
        }
}
