package ash.wasset.utils;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;

import ash.wasset.models.StandardWebServiceResponse;

/**
 * Created by ahmed on 11/24/16.
 */

public class CheckResponse {

    StandardWebServiceResponse standardWebServiceResponse;
    Gson gson;

    private static CheckResponse checkResponse = new CheckResponse( );

    public static CheckResponse getInstance( ) {
        return checkResponse;
    }

    public boolean checkResponse(Context context, String jsonString, boolean showMessage) {
        standardWebServiceResponse = new StandardWebServiceResponse();
        gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        if (showMessage){
            if (GeneralClass.language.equals("en")){
                Toast.makeText(context, standardWebServiceResponse.getEnglishMessage(), Toast.LENGTH_LONG).show();
            } else if (GeneralClass.language.equals("ar")){
                Toast.makeText(context, standardWebServiceResponse.getArabicMessage(), Toast.LENGTH_LONG).show();
            }
        }
        if (standardWebServiceResponse.isSuccess()){
            return true;
        } else {
            return false;
        }
    }
}
