package ash.wasset.serverconnection.volley;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;
import ash.wasset.R;
import android.util.Base64;

import ash.wasset.utils.LoginSharedPreferences;

/**
 * Created by shafeek on 21/09/16.
 */
public class ConnectionVolley extends StringRequest {

    private String url;
    Map<String, String> params;
    Map<String, String> headers;
    Context context;
    public static Dialog dialog;
    boolean dialogStatus;

    public ConnectionVolley(Context context, int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener, Map<String, String> params, boolean dialogStatus) {
        super(method, url, listener, errorListener);
        this.params = params;
        this.context = context;
        this.dialogStatus = dialogStatus;
        this.headers = new HashMap<>();
        loadingDialog();
    }

    public ConnectionVolley(Context context, int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener, Map<String, String> params, Map<String, String> headers, boolean dialogStatus) {
        super(method, url, listener, errorListener);
        this.params = params;
        this.context = context;
        this.dialogStatus = dialogStatus;
        this.headers = headers;
        loadingDialog();
    }

    public ConnectionVolley(String url, Response.Listener<String> listener, Response.ErrorListener errorListener, Map<String, String> params) {
        super(url, listener, errorListener);
        this.params = params;
    }

    protected void loadingDialog() {
        if (dialogStatus) {
            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.progress_dialog);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
    }

    @Override
    protected void deliverResponse(String response) {
        super.deliverResponse(response);
        try {
            dialog.dismiss();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        return super.parseNetworkResponse(response);
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        String credentials = "username:password";
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", auth);
        return headers;

//        LoginSharedPreferences loginSharedPreferences = new LoginSharedPreferences();
//        headers.put("Authorization", "bearer " + loginSharedPreferences.getAccessToken(context));
//        return headers;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}
