package ash.wasset.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ash.wasset.R;
import ash.wasset.models.Categories;
import ash.wasset.serverconnection.Url;
import ash.wasset.serverconnection.volley.AppController;
import ash.wasset.serverconnection.volley.ConnectionVolley;
import ash.wasset.utils.GeneralClass;

/**
 * Created by ahmed on 11/27/16.
 */

public class CategoriesListAdapter extends BaseAdapter implements Response.Listener, Response.ErrorListener {

    Activity activity;
    ArrayList<Categories> categoriesModel;
    ViewHolder holder;

    public CategoriesListAdapter() {
    }

    public CategoriesListAdapter(Activity activity, ArrayList<Categories> categoriesModel) {
        this.activity = activity;
        this.categoriesModel = categoriesModel;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = activity.getLayoutInflater().inflate(R.layout.category_list_custom, parent, false);
        holder = new ViewHolder();
        holder.categoryImageView = (ImageView) convertView.findViewById(R.id.categoryImageView);
        holder.categoryNameTextView = (TextView) convertView.findViewById(R.id.categoryNameTextView);
        holder.categoryCheckBox = (CheckBox) convertView.findViewById(R.id.categoryCheckBox);

        Picasso.with(activity)
                .load(Url.getInstance().categoriesImageURL + categoriesModel.get(position).getImagePath())
                .placeholder(R.mipmap.logo)
                .into(holder.categoryImageView);
        if (GeneralClass.language.equals("en")){
            holder.categoryNameTextView.setText(categoriesModel.get(position).getEnglishName());
        } else if (GeneralClass.language.equals("ar")){
            holder.categoryNameTextView.setText(categoriesModel.get(position).getArabicName());
        }

        if (categoriesModel.get(position).isChecked()){
            holder.categoryCheckBox.setChecked(true);
        } else {
            holder.categoryCheckBox.setChecked(false);
        }

        holder.categoryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true){
                    callServerToAddCategory(categoriesModel.get(position).getId()+"");
                } else {
                    callServerToRemoveCategory(categoriesModel.get(position).getId()+"");
                }
            }
        });
        return convertView;
    }

    void callServerToAddCategory(String id){
        Map<String,String> parms = new HashMap<String,String>();
        parms.put("Id", id);
        ConnectionVolley connectionVolley = new ConnectionVolley(activity, Request.Method.POST, Url.getInstance().addCategoriesURL, this, this, parms, false);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    void callServerToRemoveCategory(String id){
        Map<String,String> parms = new HashMap<String,String>();
        parms.put("Id", id);
        ConnectionVolley connectionVolley = new ConnectionVolley(activity, Request.Method.POST, Url.getInstance().removeCategoriesURL, this, this, parms, false);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    public int getCount() {
        return categoriesModel.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        private ImageView categoryImageView;
        private TextView categoryNameTextView;
        private CheckBox categoryCheckBox;
    }

    @Override
    public void onResponse(Object response) {
        Log.e("dddd",response.toString());
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        VolleyLog.d("responseError", "Error: " + error.getMessage());
    }
}
