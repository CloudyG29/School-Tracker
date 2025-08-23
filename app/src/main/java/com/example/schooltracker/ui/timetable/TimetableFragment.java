package com.example.schooltracker.ui.timetable;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schooltracker.MyNotificationReceiver;
import com.example.schooltracker.R;
import com.example.schooltracker.model.AppDatabase;
import com.example.schooltracker.model.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimetableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimetableFragment extends Fragment {
    public String StartTime = "";
    public String EndTime = "";

   public int StartHour;
   public int StartMin;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TimetableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimetableFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimetableFragment newInstance(String param1, String param2) {
        TimetableFragment fragment = new TimetableFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);

        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        Button todayButton = view.findViewById(R.id.todayButton);

        InfiniteDayAdapter adapter = new InfiniteDayAdapter(this);
        viewPager.setAdapter(adapter);

        // Link tab layout and view pager
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            LocalDate date = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                date = LocalDate.now().plusDays(position - InfiniteDayAdapter.CENTER_POSITION);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tab.setText(date.getDayOfWeek().toString().substring(0, 3) + "\n" + date.getDayOfMonth());
            }
        }).attach();

        viewPager.setCurrentItem(InfiniteDayAdapter.CENTER_POSITION, false);

        todayButton.setOnClickListener(v -> {
            viewPager.setCurrentItem(InfiniteDayAdapter.CENTER_POSITION, true);
        });
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            int currentPosition = viewPager.getCurrentItem();
            LocalDate currentDate = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                currentDate = LocalDate.now().plusDays(currentPosition - InfiniteDayAdapter.CENTER_POSITION);
            }
            String formattedDate = currentDate.toString(); // yyyy-MM-dd
            showEventDialog(formattedDate, null);
        });



        viewPager.setOffscreenPageLimit(1);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, StartHour);
            calendar.set(Calendar.MINUTE, StartMin);
            calendar.set(Calendar.SECOND, 0);

            Intent intent = new Intent(requireContext(), MyNotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    requireContext(),
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.POST_NOTIFICATIONS},
                            101
                    );

                }
            }


        return view;
    }

    public void showEventDialog(String selectedDate, @Nullable Event existingEvent) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.add_event_dialog, null);
        EditText title = dialogView.findViewById(R.id.editTitle);
        EditText location = dialogView.findViewById(R.id.editLocation);
        EditText date = dialogView.findViewById(R.id.editDate);
        Spinner repeatSpinner = dialogView.findViewById(R.id.spinnerRepeat);
        Spinner colorSpinner = dialogView.findViewById(R.id.spinnerColor);
        Button startBtn = dialogView.findViewById(R.id.btnStartTime);
        Button endBtn = dialogView.findViewById(R.id.btnEndTime);

        final String[] startTime = {""};
        final String[] endTime = {""};

        // Color setup
        Map<String, Integer> colorMap = new HashMap<>();
        colorMap.put("Red", Color.RED);
        colorMap.put("Green", Color.GREEN);
        colorMap.put("Blue", Color.BLUE);
        colorMap.put("Yellow", Color.YELLOW);
        colorMap.put("Purple", Color.parseColor("#9C27B0"));
        colorMap.put("Teal", Color.parseColor("#009688"));

        String[] colorNames = colorMap.keySet().toArray(new String[0]);
        ColorSpinnerAdapter colorAdapter = new ColorSpinnerAdapter(requireContext(), colorNames, colorMap);
        colorSpinner.setAdapter(colorAdapter);

        ArrayAdapter<String> repeatAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item,
                new String[]{"None", "Daily", "Weekly"});
        repeatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatSpinner.setAdapter(repeatAdapter);

        // Prefill if editing
        if (existingEvent != null) {
            title.setText(existingEvent.getTitle());
            location.setText(existingEvent.getLocation());
            date.setText(existingEvent.getDate());
            startTime[0] = existingEvent.getStartTime();
            endTime[0] = existingEvent.getEndTime();
            startBtn.setText(startTime[0]);
            endBtn.setText(endTime[0]);

            // Set repeat spinner
            int repeatPos = repeatAdapter.getPosition(existingEvent.getRepetition());
            repeatSpinner.setSelection(repeatPos);

            // Set color spinner
            for (int i = 0; i < colorNames.length; i++) {
                if (colorMap.get(colorNames[i]) == existingEvent.getColor()) {
                    colorSpinner.setSelection(i);
                    break;
                }
            }
        } else {
            date.setText(selectedDate);
        }

        // Time pickers
        startBtn.setOnClickListener(v -> {
            TimePickerDialog picker = new TimePickerDialog(requireContext(), (view, hour, minute) -> {
                startTime[0] = String.format("%02d:%02d", hour, minute);
                startBtn.setText(startTime[0]);
                //System.out.println(startTime[0]);
                StartTime = startTime[0];
            }, 12, 0, true);
            picker.show();
        });

        endBtn.setOnClickListener(v -> {
            TimePickerDialog picker = new TimePickerDialog(requireContext(), (view, hour, minute) -> {
                endTime[0] = String.format("%02d:%02d", hour, minute);
                endBtn.setText(endTime[0]);
                EndTime = endTime[0];
            }, 13, 0, true);
            picker.show();
        });

        // Date picker
        date.setInputType(InputType.TYPE_NULL);
        date.setOnClickListener(v -> {
            String[] parts = date.getText().toString().split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]) - 1;
            int day = Integer.parseInt(parts[2]);

            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                    (view1, y, m, d) -> {
                        String newDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", y, m + 1, d);
                        date.setText(newDate);
                    }, year, month, day);

            datePickerDialog.show();
        });

        // Dialog
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle(existingEvent != null ? "Edit Event" : "Add Event")
                .setView(dialogView)
                .setPositiveButton(existingEvent != null ? "Update" : "Save", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(dlg -> {
            Button saveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            saveButton.setOnClickListener(v -> {
                String t = title.getText().toString().trim();
                String d = date.getText().toString().trim();
                String l = location.getText().toString().trim();
                String st = startTime[0];
                String et = endTime[0];
                String r = repeatSpinner.getSelectedItem().toString();
                String colorName = colorSpinner.getSelectedItem().toString();

                if (t.isEmpty()) {
                    title.setError("Title is required");
                    return;
                }
                if (l.isEmpty()) {
                    location.setError("Location is required");
                    return;
                }
                if (st.isEmpty()) {
                    Toast.makeText(requireContext(), "Start time is required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et.isEmpty()) {
                    Toast.makeText(requireContext(), "End time is required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (st.compareTo(et) >= 0) {
                    Toast.makeText(requireContext(), "End time must be after start time", Toast.LENGTH_SHORT).show();
                    return;
                }

                int color = colorMap.getOrDefault(colorName, Color.GRAY);

                if (existingEvent != null) {
                    existingEvent.setTitle(t);
                    existingEvent.setLocation(l);
                    existingEvent.setDate(d);
                    existingEvent.setStartTime(st);
                    existingEvent.setEndTime(et);
                    existingEvent.setRepetition(r);
                    existingEvent.setColor(color);

                    new Thread(() -> AppDatabase.getInstance(requireContext()).eventDao().update(existingEvent)).start();
                    Toast.makeText(requireContext(), "Event Updated!", Toast.LENGTH_SHORT).show();
                } else {
                    Event event = new Event(t, st, et, l, d, r, color);
                    new Thread(() -> AppDatabase.getInstance(requireContext()).eventDao().insert(event)).start();
                    Toast.makeText(requireContext(), "Event Saved!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                StartTime = st;
                StartHour = Integer.parseInt(StartTime.split(":")[0]);
                StartMin = Integer.parseInt(StartTime.split(":")[1]);
                //System.out.println(st);
                EndTime = et;
            });
        });

        dialog.show();
    }







}