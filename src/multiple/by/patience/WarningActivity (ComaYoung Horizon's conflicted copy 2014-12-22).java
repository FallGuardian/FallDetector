package multiple.by.patience;


import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
//import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class WarningActivity extends Activity {
	private SeekBar slide;  
	private Button fell3;
	private Button stand1;
	public static boolean authorize=false;
	
	public TextView info;
	private int closer=0;
	int count=0;
	boolean redlight=true;
	NotificationManager notificationManager;
	private Handler handler = new Handler();
	Web connect;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_warning);
		
		slide= (SeekBar)findViewById(R.id.seekBar1);  
		
		fell3 = (Button) findViewById(R.id.button1);
		stand1 = (Button) findViewById(R.id.button2);
		info=(TextView) findViewById(R.id.textView1);
		fell3.setOnClickListener(bad3);
		stand1.setOnClickListener(good3);
		slide.setOnSeekBarChangeListener(comeon);
		connect=new Web(this);
		
		NotificationCompat.Builder builder =  
		        new NotificationCompat.Builder(this)  
		        .setSmallIcon(R.drawable.ic_launcher)  
		        .setContentTitle("Notifications Example")  
		        .setContentText("This is a test notification");  
		Uri alarmSound;
		alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL);
		long[] pattern = {500,500,500,500,500,500,500,500,500};
		builder.setVibrate(pattern);
		
		builder.setSound(alarmSound);
		Intent notificationIntent = new Intent(this, WarningActivity.class); 
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,   
		        PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(contentIntent);  
		builder.setAutoCancel(true);
		builder.setLights(Color.BLUE, 500, 500);
		
		builder.setStyle(new NotificationCompat.InboxStyle());
		// Add as notification  
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);  
		notificationManager.notify(1, builder.build());
		
       	stand1.setEnabled(false);
       	fell3.setEnabled(false);
		handler.postDelayed(callforhelp, 100);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.warning, menu);
		return true;
	}
	
	
	private Button.OnClickListener good3 = new Button.OnClickListener() {
		public void onClick(View arg0) {
			notificationManager.cancelAll();
			if(SettingActivity.upload){
				Acconly.send(0);
			}
			if(!SettingActivity.load){
				Intent intent = new Intent(WarningActivity.this, Acconly.class);
				intent.putExtra("phone", MainActivity.phone);
				startService(intent);
			}
			finish();
		}
	};
	  
	  private Button.OnClickListener bad3 = new Button.OnClickListener() {
	       public void onClick(View arg0) {
	    	   
	    	   notificationManager.cancelAll();
	    	   if(SettingActivity.upload){
	    		   //if(Acconly.hand)Acconly.send(2);
	    		   //else 
	    			   Acconly.send(1);
	    	   }
	    	   if(!SettingActivity.load){
		           Intent intent = new Intent(WarningActivity.this, Acconly.class);
		           intent.putExtra("phone", MainActivity.phone);
		           startService(intent);
	    	   }
	    	   finish();
	      }
	  };
	  
	private SeekBar.OnSeekBarChangeListener comeon = new SeekBar.OnSeekBarChangeListener(){
		public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {  
			// TODO Auto-generated method stub  
			closer=progress;
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			seekBar.setProgress(0);
		}
	};
	  
	private Runnable callforhelp = new Runnable() {
		public void run() {
			//call
			if(false/*closer!=100*/){
				if(count>200){
					handler.removeCallbacks(callforhelp);
	        		SharedPreferences settings = getSharedPreferences("Preference", 0);
	        		String name = settings.getString("number1", "");
	        		if(SettingActivity.upload){
	        			Acconly.send(1);
	        		}
	        		if(authorize){
	        			Intent intent = new Intent(Intent.ACTION_CALL);
	        			intent.setData(Uri.parse("tel:" +name));
	        			//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        			startActivity(intent);
	        		}
	        	}
	        	else{
	        		count++;
	        		info.setText("count down"+String.valueOf((200-count)/10));
	        		handler.postDelayed(callforhelp, 100);
	        	}
			}
			else {
				stand1.setEnabled(true);
				fell3.setEnabled(true);
				info.setText("Did you fell !?");
				slide.setProgress(100);
				slide.setVisibility(View.GONE);
				handler.removeCallbacks(callforhelp);
			}
		}
	};
}
