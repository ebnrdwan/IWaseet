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
import ash.wasset.adapters.WorkingHoursAdapter;
import ash.wasset.models.StandardWebServiceResponse;
import ash.wasset.models.WorkHoursModel;

/**
 * Created by ahmed on 12/22/16.
 */

public class ServiceProviderHoursWorkFragment extends Fragment {

    View view;
    ListView workingHoursListView;
    String getJson;
    ArrayList<WorkHoursModel> workHoursModelArrayList;
    WorkingHoursAdapter workingHoursAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.service_provider_hours_work_fragment, container, false);
        initialization();
        decodeJson(getJson);
        checkLenth();
        callAdapter();

        return view;
    }

    private void initialization(){
        workingHoursListView = (ListView) view.findViewById(R.id.workingHoursListView);
        getJson = getArguments().getString("json");
    }

    private void checkLenth(){
        if (workHoursModelArrayList.size() == 0)
            workingHoursListView.setVisibility(View.GONE);
    }

    private void decodeJson(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        workHoursModelArrayList = new ArrayList<>();
        gson = new Gson();
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        workHoursModelArrayList = gson.fromJson(builder.create().toJson(standardWebServiceResponse.getData()), new TypeToken<List<WorkHoursModel>>(){}.getType());
    }

    private void callAdapter(){
        workingHoursAdapter = new WorkingHoursAdapter(getActivity(), workHoursModelArrayList);
        workingHoursListView.setAdapter(workingHoursAdapter);
    }
}
