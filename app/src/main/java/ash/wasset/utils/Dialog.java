package ash.wasset.utils;


import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Dialog 
{
	static Snackbar snackbar;
	static SweetAlertDialog pDialog;

	public static void showPubkicSnackBar(Context context, final View coordinatorLayout, String message)
	{
		snackbar= Snackbar
				.make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE)
				.setAction("تم", new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						snackbar.dismiss();
					}
				});

		snackbar.show();
	}
	
	public static  void  showAletDialog(Context context, String Message , String title, int DialogType)
	{
		SweetAlertDialog pDialog = new SweetAlertDialog(context, DialogType);
		pDialog.setTitleText(title);
		pDialog.setCancelable(false);
		pDialog.setContentText(Message);
		pDialog.show();
	}


	public static void ShowProgressDialog(Context context, boolean isCancable)
	{

			pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
			pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
			pDialog.setTitleText("Loading");
			pDialog.setCancelable(isCancable);
			pDialog.show();

	}

	public static void dismissProgressDialog()
	{
	
		pDialog.dismiss();
	}
	
	
	

}
