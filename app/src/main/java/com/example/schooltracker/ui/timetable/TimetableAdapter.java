package com.example.schooltracker.ui.timetable;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schooltracker.R;
import com.example.schooltracker.model.AppDatabase;
import com.example.schooltracker.model.Event;
import com.example.schooltracker.model.TimetableItem;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.ViewHolder> {

    private final List<Event> eventList;
    private EventActionListener listener;

    public TimetableAdapter(Context context, List<Event> eventList, EventActionListener listener) {
        this.eventList = eventList;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectText, timeText, roomText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectText = itemView.findViewById(R.id.subjectText);
            timeText = itemView.findViewById(R.id.timeText);
            roomText = itemView.findViewById(R.id.roomText);
        }
    }

    @NonNull
    @Override
    public TimetableAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_timetable, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimetableAdapter.ViewHolder holder, int position) {
        Event event = eventList.get(position);

        holder.subjectText.setText(event.title);
        holder.timeText.setText(event.startTime + " - " + event.endTime);
        holder.roomText.setText(event.location);

        // 👉 show bottom sheet on click
        holder.itemView.setOnClickListener(v -> showBottomSheet(v.getContext(), event));
    }



    public void updateData(List<Event> newList) {
        eventList.clear();
        eventList.addAll(newList);
        notifyDataSetChanged();
    }

    private void showBottomSheet(Context context, Event event) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        View sheetView = LayoutInflater.from(context)
                .inflate(R.layout.bottom_sheet_event, null);

        TextView title = sheetView.findViewById(R.id.eventTitle);
        TextView dateTime = sheetView.findViewById(R.id.eventDateTime);
        TextView location = sheetView.findViewById(R.id.eventLocation);
        TextView edit = sheetView.findViewById(R.id.editEvent);
        TextView delete = sheetView.findViewById(R.id.deleteEvent);

        title.setText(event.title);
        dateTime.setText(event.startTime + " → " + event.endTime);
        location.setText(event.location);

        edit.setOnClickListener(v -> {
            dialog.dismiss();
            // 👉 Launch your Edit Event screen/dialog here
        });

        delete.setOnClickListener(v -> {
            dialog.dismiss();
            // 👉 Call your ViewModel/DAO to delete the event
            // e.g., viewModel.deleteEvent(event);
        });

        dialog.setContentView(sheetView);
        dialog.show();
    }


    @Override
    public int getItemCount() {
        return eventList.size();
    }

}

