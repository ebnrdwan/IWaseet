package ash.wasset.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ash.wasset.R;

public class ServiceProviderMainFragment extends Fragment implements ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener {

    View view;
    ViewPager homeViewPager;
    ActionBar actionBar;
    AppSectionsPagerAdapter appSectionsPagerAdapter;
    TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.service_provider_main_fragment, container, false);
        initialization();
        set();
        tabLayoutSetting();
        return view;
    }

    private void initialization(){
        homeViewPager = (ViewPager) view.findViewById(R.id.homeViewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        appSectionsPagerAdapter = new AppSectionsPagerAdapter(getActivity().getSupportFragmentManager(), getActivity());
    }

    private void tabLayoutSetting(){
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.requests)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.calls)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.reviews)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setOnTabSelectedListener(this);
    }

    private void set(){
        homeViewPager.setAdapter(appSectionsPagerAdapter);
        homeViewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        homeViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tabLayout.getTabAt(position).select();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        FragmentActivity fa;

        public AppSectionsPagerAdapter(FragmentManager fm, FragmentActivity fa) {
            super(fm);
            this.fa = fa;
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new ClientLocationRequestsFragment();

                case 1:
                    return new ClientCallRequestsFragment();
                    //return new ClientCallRequestsFragment();
                case 2:
                    return new ClientReviewsFragment();
                    // The other sections of the app are dummy placeholders.
                    //return new ClientCallRequestsFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Section " + (position + 1);
        }
    }
}
