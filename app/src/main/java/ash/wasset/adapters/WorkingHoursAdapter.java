package ash.wasset.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ash.wasset.R;
import ash.wasset.models.WorkHoursModel;

/**
 * Created by ahmed on 11/27/16.
 */

public class WorkingHoursAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<WorkHoursModel> workHoursModelArrayList;
    ViewHolder holder;

    public WorkingHoursAdapter() {
    }

    public WorkingHoursAdapter(Activity activity, ArrayList<WorkHoursModel> workHoursModelArrayList) {
        this.activity = activity;
        this.workHoursModelArrayList = workHoursModelArrayList;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = activity.getLayoutInflater().inflate(R.layout.working_hours_custom, parent, false);
        holder = new ViewHolder();
        holder.dayNameTextView = (TextView) convertView.findViewById(R.id.dayNameTextView);
        holder.fromTextView = (TextView) convertView.findViewById(R.id.fromTextView);
        holder.toTextView = (TextView) convertView.findViewById(R.id.toTextView);
        holder.dayOffTextView = (TextView) convertView.findViewById(R.id.dayOffTextView);

        holder.dayNameTextView.setText(activity.getResources().getStringArray(R.array.days)[Integer.parseInt(workHoursModelArrayList.get(position).getDay())]);
        holder.fromTextView.setText(workHoursModelArrayList.get(position).getDayFrom());
        holder.toTextView.setText(workHoursModelArrayList.get(position).getDayTo());
        if (workHoursModelArrayList.get(position).getOffDays().equals("true")){
            holder.dayOffTextView.setText(activity.getResources().getString(R.string.off));
            holder.dayOffTextView.setTextColor(Color.RED);
            holder.fromTextView.setText("--");
            holder.toTextView.setText("--");
        } else{
            holder.dayOffTextView.setText(activity.getResources().getString(R.string.on));
            holder.dayOffTextView.setTextColor(Color.GREEN);
        }

        if (position % 2 == 1) {
            convertView.setBackgroundColor(Color.LTGRAY);
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }

        return convertView;
    }

    public int getCount() {
        return workHoursModelArrayList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        private TextView dayNameTextView;
        private TextView fromTextView;
        private TextView toTextView;
        private TextView dayOffTextView;
    }
}
