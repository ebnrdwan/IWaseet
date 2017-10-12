package ash.wasset.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPrefrenanceClass
{
	
   private	static SharedPreferences pref;
	 
	public static void  saveData(Context con, String key, String value)
	{
		pref=con.getSharedPreferences("_Preferences",con.MODE_PRIVATE);
		
		SharedPreferences.Editor prefeditor=pref.edit();
		
		prefeditor.putString(key, value);
		
		prefeditor.commit();

	}
	
	public static String getValue(Context con, String key)
	{
		pref=con.getSharedPreferences("_Preferences",con.MODE_PRIVATE);

		return pref.getString(key, "");
	}
	
	public static void removeData(Context con, String key)
	{
        pref=con.getSharedPreferences("_Preferences",con.MODE_PRIVATE);
		
		SharedPreferences.Editor prefeditor=pref.edit();
		
		prefeditor.remove(key);
		
		prefeditor.commit();
	}
	
	public static void clearPrefs(Context con)
	{
        pref=con.getSharedPreferences("_Preferences",con.MODE_PRIVATE);
		
		SharedPreferences.Editor prefeditor=pref.edit();
		
		prefeditor.clear();
		
		prefeditor.commit();
	}
}
