package com.example.schooltracker.ui.timetable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.schooltracker.ui.dayview.DayFragment;


public class DayPagerAdapter extends FragmentStateAdapter {

    private final String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    public DayPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return DayFragment.newInstance(days[position]);
    }

    @Override
    public int getItemCount() {
        return days.length;
    }
}

