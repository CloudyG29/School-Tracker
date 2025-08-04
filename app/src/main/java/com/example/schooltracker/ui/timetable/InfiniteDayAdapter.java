package com.example.schooltracker.ui.timetable;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.schooltracker.ui.dayview.DayFragment;

import java.time.LocalDate;

public class InfiniteDayAdapter extends FragmentStateAdapter {

    public static final int TOTAL_DAYS = 31; // or 180 if you're feeling spicy
    public static final int CENTER_POSITION = TOTAL_DAYS / 2;


    public InfiniteDayAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        LocalDate date = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date = LocalDate.now().plusDays(position - CENTER_POSITION);
        }
        return DayFragment.newInstance(date.toString());
    }

    @Override
    public int getItemCount() {
        return TOTAL_DAYS;
    }
}
