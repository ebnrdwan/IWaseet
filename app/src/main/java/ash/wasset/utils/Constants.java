package ash.wasset.utils;


import java.lang.reflect.Field;
import java.util.HashMap;

public class Constants {

	static  public HashMap<String,String> printFieldNames(Object t) {

		HashMap<String,String> map=new HashMap<>();

		for(Field f : t.getClass().getFields()) {

			try {

				map.put(f.getName(), (String) f.get(t));

			} catch (Exception e) {
				e.printStackTrace();
//				return null;

			}

		}

		return map;
	}
}