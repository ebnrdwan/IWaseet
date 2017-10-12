package ash.wasset.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ash.wasset.R;
import ash.wasset.models.MainMenuModel;

/**
 * Created by ahmed on 11/27/16.
 */

public class MainMenuAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<MainMenuModel> mainMenuModels;
    ViewHolder holder;

    public MainMenuAdapter() {
    }

    public MainMenuAdapter(Activity activity, ArrayList<MainMenuModel> mainMenuModels) {
        this.activity = activity;
        this.mainMenuModels = mainMenuModels;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = activity.getLayoutInflater().inflate(R.layout.menu_list_custom, parent, false);
        holder = new ViewHolder();
        holder.menuItemIconImageView = (ImageView) convertView.findViewById(R.id.menuItemIconImageView);
        holder.menuItemNameTextView = (TextView) convertView.findViewById(R.id.menuItemNameTextView);

        holder.menuItemNameTextView.setText(mainMenuModels.get(position).getMenuName());
        holder.menuItemIconImageView.setImageResource(mainMenuModels.get(position).getMenuIcon());

        return convertView;
    }

    public int getCount() {
        return mainMenuModels.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        private ImageView menuItemIconImageView;
        private TextView menuItemNameTextView;
    }
}
