package com.example.schooltracker.ui.dayview;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.schooltracker.R;
import com.example.schooltracker.model.Event;
import com.example.schooltracker.ui.timetable.EventActionListener;
import com.example.schooltracker.ui.timetable.TimetableAdapter;
import com.example.schooltracker.ui.timetable.TimetableFragment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DayFragment extends Fragment {

    private static final String ARG_DAY = "day";
    private String dayName;

    public static DayFragment newInstance(String dayName) {
        DayFragment f = new DayFragment();
        Bundle b = new Bundle();
        b.putString(ARG_DAY, dayName);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dayName = getArguments().getString(ARG_DAY);
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inf.inflate(R.layout.fragment_day, container, false);

        RecyclerView rv = root.findViewById(R.id.dayRecycler);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        TimetableAdapter adapter = new TimetableAdapter(requireContext(), new ArrayList<>(), new EventActionListener() {
            TimetableFragment T;
            @Override
            public void onEdit(Event event) {
                // Pass the date from the event and the existing event itself
                T.showEventDialog(event.getDate(), event);
            }

            @Override
            public void onDelete(Event event) {
                DayViewModel vm = new ViewModelProvider(
                        requireActivity(),
                        ViewModelProvider.AndroidViewModelFactory
                                .getInstance(requireActivity().getApplication())
                ).get(DayViewModel.class);

                vm.deleteEvent(event);
            }



        });

        rv.setAdapter(adapter);

        DayViewModel vm = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())
        ).get(DayViewModel.class);

        String isoDate = parseIsoDate(dayName);
        vm.getAllEvents().observe(getViewLifecycleOwner(), allEvents -> {
            List<Event> filtered = new ArrayList<>();
            for (Event e : allEvents) {
                if (shouldShowOnDate(e, isoDate)) {
                    filtered.add(e);
                }
            }
            adapter.updateData(filtered);
        });

        return root;
    }

    private boolean shouldShowOnDate(Event e, String isoDate) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return false;

        LocalDate targetDate = LocalDate.parse(isoDate);
        LocalDate eventDate = LocalDate.parse(e.getDate());

        switch (e.getRepetition()) {
            case "None":
                return eventDate.equals(targetDate);
            case "Daily":
                return !targetDate.isBefore(eventDate);
            case "Weekly":
                return !targetDate.isBefore(eventDate) &&
                        eventDate.getDayOfWeek() == targetDate.getDayOfWeek();
            default:
                return false;
        }
    }

    private String parseIsoDate(String dateString) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return LocalDate.parse(dateString).toString();
        }
        return dateString;
    }


}
