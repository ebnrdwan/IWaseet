package ash.wasset.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import ash.wasset.R;
import ash.wasset.adapters.PriceListAdapter;
import ash.wasset.models.PriceListModel;
import ash.wasset.models.StandardWebServiceResponse;

/**
 * Created by ahmed on 1/5/17.
 */

public class PriceListFragment extends Fragment {

    View view;
    ListView priceListListView;
    String getJson;
    ArrayList<PriceListModel> priceListModels;
    PriceListAdapter priceListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.price_list_fragment, container, false);
        initialization();
        decodeJson(getJson);
        callAdapter();
        return view;
    }

    private void initialization(){
        priceListListView = (ListView) view.findViewById(R.id.priceListListView);
        getJson = getArguments().getString("json");
    }

    private void decodeJson(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        priceListModels = new ArrayList<>();
        gson = new Gson();
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        priceListModels = gson.fromJson(builder.create().toJson(standardWebServiceResponse.getData()), new TypeToken<List<PriceListModel>>(){}.getType());
    }

    private void callAdapter(){
        priceListAdapter = new PriceListAdapter(getActivity(), priceListModels);
        priceListListView.setAdapter(priceListAdapter);
    }
}
