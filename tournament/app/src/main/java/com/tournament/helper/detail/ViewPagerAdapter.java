package com.tournament.helper.detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
  private List<Fragment> mFragments;
  private String[] titles = new String[]{"Matches", "Draw"};

  public ViewPagerAdapter(FragmentManager fm) {
    super(fm);
    mFragments = new ArrayList<>();
  }

  public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
    super(fm);
    mFragments = fragments;
  }

  @Override
  public Fragment getItem(int position) {
    return mFragments.get(position);
  }

  @Override
  public int getCount() {
    return mFragments.size();
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return titles[position];
  }
}