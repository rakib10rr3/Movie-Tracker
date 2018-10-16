package com.rakib.moviewer;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rakib.moviewer.util.SharedPref;


/**
 * A simple {@link Fragment} subclass.
 */
public class SeriesFragment extends Fragment {


    public SeriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_series, container, false);

        Toolbar toolbar = inflatedView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        SeriesPagerAdapter moviesPagerAdapter = new SeriesPagerAdapter(getChildFragmentManager());
        ViewPager seriesViewPager = (ViewPager) inflatedView.findViewById(R.id.series_view_pager);
        seriesViewPager.setAdapter(moviesPagerAdapter);

        TabLayout tabLayout = (TabLayout) inflatedView.findViewById(R.id.series_tab);
        tabLayout.setupWithViewPager(seriesViewPager);

        return inflatedView;
    }

    public class SeriesPagerAdapter extends FragmentPagerAdapter {

        public SeriesPagerAdapter(FragmentManager fragment){
            super(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new WatchedSeriesFragment();
                //Toast.makeText(getContext(),"1",Toast.LENGTH_SHORT).show();
                case 1:
                    return new WatchListedSeriesFragment();
                //Toast.makeText(getContext(),"2",Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Watched";
                case 1:
                    return "WatchListed";
            }
            return null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPref.init(getActivity().getApplicationContext());
        SharedPref.write("fragment","F3");
    }
}
