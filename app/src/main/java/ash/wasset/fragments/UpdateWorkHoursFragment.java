package ash.wasset.fragments;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ash.wasset.R;
import ash.wasset.models.StandardWebServiceResponse;
import ash.wasset.models.WorkHoursModel;
import ash.wasset.serverconnection.Url;
import ash.wasset.serverconnection.volley.AppController;
import ash.wasset.serverconnection.volley.ConnectionVolley;
import ash.wasset.utils.CheckResponse;

/**
 * Created by ahmed on 1/4/17.
 */

public class UpdateWorkHoursFragment extends Fragment implements View.OnClickListener, Response.Listener, Response.ErrorListener{

    View view;
    TextView fromTextView, toTextView, dayNameTextView;
    CheckBox offCheckBox;
    Button updateButton;
    String getJson;
    WorkHoursModel workHoursModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.update_work_hours_fragment, container, false);
        initialization();
        decodeJson(getJson);
        set();
        return view;
    }

    private void initialization(){
        fromTextView = (TextView) view.findViewById(R.id.fromTextView);
        toTextView = (TextView) view.findViewById(R.id.toTextView);
        dayNameTextView = (TextView) view.findViewById(R.id.dayNameTextView);
        offCheckBox = (CheckBox) view.findViewById(R.id.offCheckBox);
        updateButton = (Button) view.findViewById(R.id.updateButton);
        getJson = getArguments().getString("json");
    }

    private void set(){
        dayNameTextView.setText(getResources().getStringArray(R.array.days)[Integer.parseInt(workHoursModel.getDay())]);
        fromTextView.setText(workHoursModel.getDayFrom());
        toTextView.setText(workHoursModel.getDayTo());
        fromTextView.setOnClickListener(this);
        toTextView.setOnClickListener(this);
        updateButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fromTextView:
                timePicker("from");
                break;
            case R.id.toTextView:
                timePicker("to");
                break;
            case R.id.updateButton:
                callServerToUpdate();
                break;
        }
    }

    private void timePicker(final String result){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if (result.equals("from"))
                    fromTextView.setText( selectedHour + ":" + selectedMinute);
                else
                    toTextView.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void callServerToUpdate(){
        Map<String,String> parms = new HashMap<String,String>();
        WorkHoursModel workHoursModell = new WorkHoursModel();
        workHoursModell.setId(workHoursModel.getId());
        workHoursModell.setDayFrom(fromTextView.getText().toString());
        workHoursModell.setDayTo(toTextView.getText().toString());
        workHoursModell.setOffDays(offCheckBox.isChecked()+"");
        Gson gson = new Gson();
        parms = gson.fromJson(gson.toJson(workHoursModell).toString(), parms.getClass());
        Log.e("Parm", parms.toString());
        ConnectionVolley connectionVolley = new ConnectionVolley(getActivity(), Request.Method.POST, Url.getInstance().updateWorkHoursURL, this, this, parms, true);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    private void decodeJson(String jsonString){
        workHoursModel = new WorkHoursModel();
        Gson gson = new Gson();
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        workHoursModel = gson.fromJson(jsonString, WorkHoursModel.class);
    }

    private String checkServiceName(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        return standardWebServiceResponse.getServiceName();
    }

    @Override
    public void onResponse(Object response) {
        Log.e("response", response.toString());
        try {
            if (checkServiceName(response.toString()).equals("UpdateWorkHours")){
                if (CheckResponse.getInstance().checkResponse(getActivity(), response.toString(), true)){
                    getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        VolleyLog.d("responseError", "Error: " + error.getMessage());
        try {
            ConnectionVolley.dialog.dismiss();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
