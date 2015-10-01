package multiple.by.patience;

import java.util.ArrayList;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.app.Activity;
import android.content.ContentResolver;
//import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class ContactActivity extends Activity {//basically don't touch this one
	private Button cancel;
	private Button wayback;
	public TextView now;
	String name="";
	String name1="";
	String name2="";
	String s1;
	Handler repeated = new Handler();
	boolean flag=false;
	boolean start=true;
	ArrayList<String> ar = new ArrayList<String>();
	ArrayList<String> ar1 = new ArrayList<String>();
	ArrayList<String> ar2 = new ArrayList<String>();
	int j=0;
	int i=0;
	Cursor cursor;
	Reference get;
	Spinner longlist1;
	Spinner longlist2;
	Spinner longlist3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		
		cancel=(Button)findViewById(R.id.button1);
		wayback=(Button)findViewById(R.id.button2);
		now=(TextView)findViewById(R.id.textView1);
		longlist1=(Spinner)findViewById(R.id.spinner1);
		longlist2=(Spinner)findViewById(R.id.spinner2);
		longlist3=(Spinner)findViewById(R.id.spinner3);
		
		get=new Reference(this,"Preference");
		
		if(get.loadcontact(1)!="")ar1.add(get.loadcontact(1));//first one
		else ar1.add("compulsory");
		
		if(get.loadcontact(2)!="")ar.add(get.loadcontact(2));//second one
		else ar.add("optional");
		
		if(get.loadcontact(3)!="")ar2.add(get.loadcontact(3));//third one
		else ar2.add("optional");
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item , ar);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item , ar1);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item , ar2);
		
		//ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,new String[]{"紅茶","奶茶","綠茶"});
		 
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    longlist1.setAdapter(adapter1);
	    longlist2.setAdapter(adapter);
	    longlist3.setAdapter(adapter2);
		cancel.setOnClickListener(out);
		wayback.setOnClickListener(leave);
		
		longlist1.setOnItemSelectedListener(click);
		longlist2.setOnItemSelectedListener(click1);
		longlist3.setOnItemSelectedListener(click2);
		
		repeated.postDelayed(report, 100);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact, menu);
		return true;
	}
	
	private Spinner.OnItemSelectedListener click = new Spinner.OnItemSelectedListener(){
        public void onItemSelected(@SuppressWarnings("rawtypes") AdapterView adapterView, View view, int position, long id){
            name=adapterView.getSelectedItem().toString();
            //if(flag)now.setText("You have pointed\n"+name+"\n"+name1+"\n"+name2+"\nto be your life savior.\n");
            ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLUE);
            ((TextView) adapterView.getChildAt(0)).setTextSize(24);
        }
        public void onNothingSelected(@SuppressWarnings("rawtypes") AdapterView arg0) {
            //Toast.makeText(ContactActivity.this, "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
        }
    };
    private Spinner.OnItemSelectedListener click1 = new Spinner.OnItemSelectedListener(){
        public void onItemSelected(@SuppressWarnings("rawtypes") AdapterView adapterView, View view, int position, long id){
            name1=adapterView.getSelectedItem().toString();
            //if(flag)now.setText("You have pointed\n"+name+"\n"+name1+"\n"+name2+"\nto be your life savior.\n");
            ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLUE);
            ((TextView) adapterView.getChildAt(0)).setTextSize(24);
        }
        public void onNothingSelected(@SuppressWarnings("rawtypes") AdapterView arg0) {
            //Toast.makeText(ContactActivity.this, "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
        }
    };
    private Spinner.OnItemSelectedListener click2 = new Spinner.OnItemSelectedListener(){
        public void onItemSelected(@SuppressWarnings("rawtypes") AdapterView adapterView, View view, int position, long id){
            name2=adapterView.getSelectedItem().toString();
            ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLUE);
            ((TextView) adapterView.getChildAt(0)).setTextSize(24);
            //if(flag)now.setText("You have pointed\n"+name+"\n"+name1+"\n"+name2+"\nto be your life savior.\n");
        }
        public void onNothingSelected(@SuppressWarnings("rawtypes") AdapterView arg0) {
            Toast.makeText(ContactActivity.this, "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
        }
    };
    
	
    private Button.OnClickListener out = new Button.OnClickListener() {
	       public void onClick(View arg0) {
	    	   if(name.matches("compulsory")){
	    		   Toast.makeText(ContactActivity.this, "choose one dude!", Toast.LENGTH_LONG).show();
	    	   }else{
	    			   sendtext(name,1);
	    			   sendtext(name1,2);
	    			   sendtext(name2,3);
	    		   finish();
	    	   }
	      }
	  };
	  
	  private Button.OnClickListener leave = new Button.OnClickListener() {
	       public void onClick(View arg0) {
	    	   finish();
	      }
	  };
	  
	  private void sendtext(String bb,int which){
		  String tempnum;
   	   if(bb.compareTo("optional")==0){return;}
   	   
   	   char[] a=new char[9];//number
   	   //name=autoComplete.getText().toString();
   	   int where;
   	   where=bb.indexOf("\n");
   	   where++;
   	   if(Character.isDigit(bb.charAt(where))){
   	   where++;
   	   }
   	   else{
   		   where=1;
   	   }
   	   for(int y=0;y<9;y++){
   		   a[y]=bb.charAt(where+y);
   	   if(! Character.isDigit(a[y])){
   		   where=where+1;
   		   a[y]=bb.charAt(where+y);
   	   }}
   	   
   	   tempnum = "0"+a[0]+a[1]+a[2]+a[3]+a[4]+a[5]+a[6]+a[7]+a[8];
   	   
   	   if(SettingActivity.message){
   	   try {
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(tempnum, null, "successful", null, null);
				Toast.makeText(getApplicationContext(), "SMS Sent!",
							Toast.LENGTH_LONG).show();
			  } catch (Exception e) {
					Toast.makeText(getApplicationContext(),
							"SMS faild, please try again later!",
							Toast.LENGTH_LONG).show();
						e.printStackTrace();
					  }
   	   }
   	   get.savecontact(bb, tempnum,which);
		  return;
	  }
	  
	  private Runnable report = new Runnable() {
			public void run() {
				if(flag){
					//now.setText("You have pointed\n"+name+"\n"+name1+"\n"+name2+"\nto be your life savior.\n");
					now.setVisibility(View.GONE);
					longlist1.setEnabled(true);
					longlist2.setEnabled(true);
					longlist3.setEnabled(true);
					repeated.removeCallbacks(report);
				}
				else{
					
					longlist1.setEnabled(false);
					longlist2.setEnabled(false);
					longlist3.setEnabled(false);
					now.setText("loading...");
					repeated.postDelayed(report, 100);
				}
				if(start){
					int currentapiVersion = android.os.Build.VERSION.SDK_INT;
					if(currentapiVersion<11){
						getcontact();
					}else{
						getcontacthigher();
					}
					start=false;
				}
				
			}
		};
	  
	  private void getcontact(){
		  
		  ContentResolver contentResolver = this.getContentResolver();
			String[] projection = new String[]{Contacts.People.NAME,Contacts.People.NUMBER};
			cursor = contentResolver.query(Contacts.People.CONTENT_URI, projection, null, null, Contacts.People.DEFAULT_SORT_ORDER);
			ar1.add("0911021037");
		  new Thread(new Runnable() {
	 	       public void run() {
		for ( i = 0; i < cursor.getCount(); i++) {
			//
			cursor.moveToPosition(i);
			//Log.d("Contacts:", cursor.getString(0));
			if(cursor.getString(0)!=null&&cursor.getString(1)!=null){
			ar1.add(cursor.getString(0)+"\n"+cursor.getString(1));
			ar.add(cursor.getString(0)+"\n"+cursor.getString(1));
			ar2.add(cursor.getString(0)+"\n"+cursor.getString(1));
			}
		}
		flag=true;
	 	      }//run
		 }).start();//thread
		  return;
	  }
	  
private void getcontacthigher(){
	ar1.add("119");
	new Thread(new Runnable() {
	       public void run() {
	    	   
	String strPhoneNumber="";
	String contactId="";
	String IsPhone="";
	int nameFieldColumnIndex=0;
	String name="";
	Cursor phoneNumber;
	Cursor people=getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
			null, null, null, null);
	
	people.moveToFirst();
	contactId=people.getString(people.getColumnIndex(ContactsContract.Contacts._ID));
	IsPhone = people.getString(people.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
	nameFieldColumnIndex = people.getColumnIndex(PhoneLookup.DISPLAY_NAME);
	name = people.getString(nameFieldColumnIndex);
	
	if( Integer.parseInt(IsPhone)==1 )
	{
		phoneNumber = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
		strPhoneNumber = phoneNumber.getString(phoneNumber.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		ar1.add(name+" "+strPhoneNumber);
		ar.add(name+" "+strPhoneNumber);
		ar2.add(name+" "+strPhoneNumber);
		phoneNumber.close();
	}
	
	while(people.moveToNext()){
		contactId=people.getString(people.getColumnIndex(ContactsContract.Contacts._ID));
		IsPhone = people.getString(people.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
		nameFieldColumnIndex = people.getColumnIndex(PhoneLookup.DISPLAY_NAME);
		name = people.getString(nameFieldColumnIndex);
		if( Integer.parseInt(IsPhone)==1 )
		{
			phoneNumber = getContentResolver().query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,
					null, null);
			while (phoneNumber.moveToNext()){
			strPhoneNumber = phoneNumber.getString(
					phoneNumber.getColumnIndex(
							ContactsContract.CommonDataKinds.Phone.NUMBER));
			ar1.add(name+" "+strPhoneNumber);
			ar2.add(name+" "+strPhoneNumber);
			ar.add(name+" "+strPhoneNumber);
			}
			phoneNumber.close();
		}
	}
	people.close();
	
	flag=true;
	       }//run
	 }).start();//thread
		  return;
	  }
	
}
