package com.example.schooltracker.ui.dayview;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.schooltracker.R;
import com.example.schooltracker.model.Event;
import com.example.schooltracker.ui.timetable.TimetableAdapter;

import java.time.LocalDate;
import java.util.ArrayList;
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
        TimetableAdapter adapter = new TimetableAdapter(requireContext(), new ArrayList<Event>());
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
        LocalDate eventDate = LocalDate.parse(e.date);

        switch (e.repetition) {
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
