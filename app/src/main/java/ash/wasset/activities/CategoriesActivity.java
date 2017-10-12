package ash.wasset.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import java.util.HashMap;
import java.util.Map;

import ash.wasset.R;
import ash.wasset.serverconnection.Url;
import ash.wasset.serverconnection.volley.AppController;
import ash.wasset.serverconnection.volley.ConnectionVolley;
import ash.wasset.fragments.CategoriesFragment;

public class CategoriesActivity extends AppCompatActivity implements View.OnClickListener
        //, SearchView.OnQueryTextListener,
        ,Response.Listener, Response.ErrorListener, AdapterView.OnItemClickListener   {
    String getJson;

    private void initialization() {
        getJson = getIntent().getStringExtra("json");
    }


    @Override
    public void onClick(View view)
    {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 1:
        }
    }


    @Override
    public void onResponse(Object response) {
        Log.e("response", response.toString());
    }

    private void onClick(){

    }
    @Override
    public void onErrorResponse(VolleyError error) {
        VolleyLog.d("responseError", "Error: " + error.getMessage());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_activity);
        initialization();
        callServerToGetCategories();
        categoriesFragment(getJson);

    }

    void callServerToGetCategories(){
        Map<String,String> parms = new HashMap<String,String>();
        ConnectionVolley connectionVolley = new ConnectionVolley(this, Request.Method.POST, Url.getInstance().categoriesURL, this, this, parms, true);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    private void categoriesFragment(String json){
        CategoriesFragment categoriesFragment = new CategoriesFragment();
        Bundle args = new Bundle();
        args.putString("json", json);
        categoriesFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.contentView, categoriesFragment).commit();
    }




}
