package multiple.by.patience;
//personalinfo: name birth height weight | from activity:user_info
//				pos sex | from activity: PosActivity
//Preference  : is hidden in savecontact and load contact
//algorversion: int
//version, TA, SVM, AV, dots
//,"askinstall"
import android.content.Context;
import android.content.SharedPreferences;
//import android.util.Log;

public class Reference{
	SharedPreferences settings;
	
	Reference(Context context,String type){
		settings =context.getSharedPreferences(type,Context.MODE_PRIVATE);
	}
	
	//check first time
	public boolean first_time(){
		boolean result;
		result=settings.getBoolean("first_time", true);
		return result;
	}
	//not first time
	public void set_first(){
		settings.edit().putBoolean("first_time", false).commit();
		return;
	}
	//set back to first time
	public void set_firstback(){
		settings.edit().putBoolean("first_time", true).commit();
		return;
	}
	
	//save threshold
	
	//load contact
	public String loadcontact(int which){
		String name="";
		name = settings.getString("name"+String.valueOf(which), "");
		return name;
	}
	public String loadphone(int which){
		String name="";
		name = settings.getString("number"+String.valueOf(which), "");
		return name;
	}
	
	
	//save contact
	public void savecontact(String name,String number,int which){
		settings.edit().putString("number"+String.valueOf(which), number).commit();
 	   settings.edit().putString("name"+String.valueOf(which), name).commit();
 	   
		return;
	}
	//save string
	public void save_string(String tagname,String name){
		settings.edit().putString(tagname, name).commit();
		return;
	}
	//save int
	public void save_int(String tagname,int name){
		settings.edit().putInt(tagname, name).commit();
		return;
	}
	//get info string
	public String get_string(String tagname){
		String re="";
		re = settings.getString(tagname, "bug");
		return re;
	}
	//get info string
	
		public int get_int(String tagname){
			int re;
			re = settings.getInt(tagname, 2);
			return re;
		}
		
		public void save_float(String tagname,float name){
			settings.edit().putFloat(tagname, name).commit();
			return;
		}
		//get info string
		public float get_float(String tagname){
			float re;
			re = settings.getFloat(tagname, 0);
			return re;
		}
}
