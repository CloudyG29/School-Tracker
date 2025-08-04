// ColorSpinnerAdapter.java
package com.example.schooltracker.ui.timetable;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.schooltracker.R;

import java.util.Map;

public class ColorSpinnerAdapter extends BaseAdapter {

    private final Context context;
    private final String[] colorNames;
    private final Map<String, Integer> colorMap;

    public ColorSpinnerAdapter(Context context, String[] colorNames, Map<String, Integer> colorMap) {
        this.context = context;
        this.colorNames = colorNames;
        this.colorMap = colorMap;
    }

    @Override
    public int getCount() {
        return colorNames.length;
    }

    @Override
    public Object getItem(int position) {
        return colorNames[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private View getCustomView(View convertView, ViewGroup parent, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.spinner_color_item, parent, false);
        View colorCircle = view.findViewById(R.id.colorCircle);
        TextView colorName = view.findViewById(R.id.colorName);

        String name = colorNames[position];
        int color = colorMap.get(name);

        GradientDrawable bgShape = (GradientDrawable) colorCircle.getBackground();
        bgShape.setColor(color);

        colorName.setText(name);
        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(convertView, parent, position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(convertView, parent, position);
    }
}
