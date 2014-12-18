package multiple.by.patience;

import java.util.Date;
import java.util.List;
import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class Acconly extends Service {
	
	SensorManager sensorManager;
	boolean accelerometerPresent;
	Sensor accelerometerSensor;
	Sensor GYROSensor;
	private LocationManager locationManager;
	private LocationListener locationListener;
	public Double longitude;
	public Double latitude;
	
	private WakeLock mWakeLock = null;
	public static final String TAG = Acconly.class.getName();
	
	public static int many=400;
	public static int whatevercounter=0;
	public static int i;
	public static int k;
	public static int ptraccold;
	//public static boolean hand=false;
	//public static int warn=0;
	
	int checktime;
	int frequency;
	public static int ptracc;
	public static float[] accx=new float[many+2];
	public static float[] accy=new float[many+2];
	public static float[] accz=new float[many+2];
	public static float[] time=new float[many+2];
	public static int ptrgyroold;
	public static int ptrgyro;
	public static float[] gyrox=new float[many+2];
	public static float[] gyroy=new float[many+2];
	public static float[] gyroz=new float[many+2];
	public static float[] gyrotime=new float[many+2];
	static float SVM[]=new float[402];
	float TA[]=new float[402];//many+2
    static float Av[]=new float[402];//many+2
    int SVMcount;

	static float[] gyrox_back=new float[402];
	static float[] gyroy_back=new float[402];
	static float[] gyroz_back=new float[402];
	static float[] accx_back=new float[402];
	static float[] accy_back=new float[402];
	static float[] accz_back=new float[402];
	static float[] time_back=new float[many+2];
	static float[] gyrotime_back=new float[many+2];
	//boolean firsttime=true;
	int which;
	public static Algorithm algorithm;
	static Web connect;
	public static Context app;
	
	private Handler handler = new Handler();
	private Handler update = new Handler();
	//Handler save = new Handler();
	
	@Override
    public void onCreate() {
        super.onCreate();
        app=this;
        sensorManager = (SensorManager)getSystemService("sensor");
        
        PowerManager manager =
            (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);

        registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        
        UpdateLocation();
        
    }
	

	public int onStartCommand (Intent intent, int flags, int startId) {
		//warn=0;
		MainActivity.glow=true;
    	which=(Integer) intent.getExtras().get("phone");
    	algorithm=new Algorithm(this,which);
    	connect=new Web(this);
    	
    	checktime=500;
    	frequency=0;//0 fastest 1 20ms
        init(which);
    	
        startForeground(491, new Notification());
        mWakeLock.acquire();
        handler.postDelayed(showTime, 3000);
        update.postDelayed(getnew, 7200000);
            return START_STICKY;
    }
	
	
	private void init(int that){
		
		//hand=false;
		List<Sensor> sensorList;
		List<Sensor> GYROList;
		
		switch(that){
		case 0:
			sensorList = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
			GYROList = sensorManager.getSensorList(Sensor.TYPE_GYROSCOPE);
	        if(sensorList.size() > 0&& GYROList.size() > 0){
	        	accelerometerPresent = true;
	        	accelerometerSensor = sensorList.get(0);
	        	GYROSensor = GYROList.get(0);

	    		sensorManager.registerListener(accelerometerListener, accelerometerSensor, frequency);
	    	    sensorManager.registerListener(GYROListener, GYROSensor, frequency);
	    	    
	        }
	        else{
	    	    accelerometerPresent = false;
	        }
		break;
		
		case 1:
		default:
			sensorList = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
	        if(sensorList.size() > 0){
	        	accelerometerPresent = true;
	        	accelerometerSensor = sensorList.get(0);
	    		sensorManager.registerListener(accelerometerListener, accelerometerSensor, frequency);
	        }
	        else{
	    	    accelerometerPresent = false;
	        }
			
		break;
		}
		for(int y=0;y<many;y++){
        	accx[y]=0;
        	accy[y]=0;
        	accz[y]=0;
        	gyrox[y]=0;
        	gyroy[y]=0;
        	gyroz[y]=0;
        	time[y]=0;
        	gyrotime[y]=0;
        	accx_back[y]=0;
        	accy_back[y]=0;
        	accz_back[y]=0;
        	gyrox_back[y]=0;
        	gyroy_back[y]=0;
        	gyroz_back[y]=0;
        	time_back[y]=0;
        	gyrotime_back[y]=0;
        	SVM[y]=0;
        	Av[y]=0;
        }
        ptraccold=0;
        i=0;
        k=0;
		return;
	}
	
	private void disarm(int that){
		switch(that){
		case 0:
			sensorManager.unregisterListener(accelerometerListener);
			sensorManager.unregisterListener(GYROListener);
			break;
		case 1:
			default:
			sensorManager.unregisterListener(accelerometerListener);
		break;
		}
		locationManager.removeUpdates(locationListener);
    	return;
	}
	
	public void onDestroy() {
		MainActivity.glow=false;
		unregisterReceiver(mReceiver);
		handler.removeCallbacks(showTime);
        handler.removeCallbacks(stoprecording);
        disarm(which);
        mWakeLock.release();
    	stopForeground(true);
        super.onDestroy();
    }
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	public void UpdateLocation(){
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); 
		locationListener = new LocationListener() { 
		public void onLocationChanged(Location newLocation) {
		longitude = newLocation.getLongitude() * 1000000;
		latitude = newLocation.getLatitude() * 1000000;
		}
		@Override
		public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

		}
		@Override
		public void onStatusChanged(String provider, int status,
		Bundle extras) {
		// TODO Auto-generated method stub

		} 
		};
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		}
	

	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		longitude = location.getLongitude() * 1000000;
		latitude = location.getLatitude() * 1000000;
		}
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Toast.makeText(app, "you sure?", Toast.LENGTH_LONG).show();
		}
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

		}
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

		}
	
	private SensorEventListener accelerometerListener = new SensorEventListener(){
	   	 @Override
	   	 public void onAccuracyChanged(Sensor arg0, int arg1) {
	   	  // TODO Auto-generated method stub
	   	 }
	   	 @Override
	   	 public void onSensorChanged(SensorEvent event) {
	   	  // TODO Auto-generated method stub
	   		accx[i%many]=event.values[0];
	   		accy[i%many]=event.values[1];
	   		accz[i%many]=event.values[2];
	   		time[i%many]= (System.currentTimeMillis()%100000);
	   		
	   		 if(i<40000)i++;
	   		 else i=0;
	   		 if(which==1){
	   			algorithm.SVMfunc(i);
	   		 }
	   	 }};

	private SensorEventListener GYROListener = new SensorEventListener(){
	      	 @Override
	      	 public void onAccuracyChanged(Sensor arg0, int arg1) {
	      	  // TODO Auto-generated method stub
	      	 }
	      	 @Override
	      	 public void onSensorChanged(SensorEvent event) {
	      	  // TODO Auto-generated method stub
	      		gyrox[k%many]=event.values[0];
	      		gyroy[k%many]=event.values[1];
	      		gyroz[k%many]=event.values[2];
	      		gyrotime[k%many]= (System.currentTimeMillis()%10000)/1000;
	      		
	      		algorithm.SVMfunc(k);
	      		algorithm.AVfunc(k);
		   		if(k<=40000)k++;
		   		else k=0;
	      		 
	      	 }};
	   	 
	public void genuine(){
		copy();
		//handler.postDelayed(stoprecording, 2);//transmit
		
		handler.removeCallbacks(showTime);
	    Intent dialogIntent = new Intent(getBaseContext(), WarningActivity.class);
      	dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//how to get activity back
      	getApplication().startActivity(dialogIntent);
      	accelerometerPresent=false;
      	
      	disarm(which);
    	TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		  String countryCode = tm.getSimCountryIso();
		  SharedPreferences settings = getSharedPreferences("Preference", 0);
  		  String name = settings.getString("number1", "");
  		  String msg="Your associate fell, Here's his/her location:\nlatitude:"
  				  +latitude+"\nlongitude:"+longitude;
	      
  		if(SettingActivity.message){
  	   	   try {
  					SmsManager smsManager = SmsManager.getDefault();
  					smsManager.sendTextMessage(countryCode+name, null, msg, null, null);
  					Toast.makeText(getApplicationContext(), "SMS Sent!",
  								Toast.LENGTH_LONG).show();
  				  } catch (Exception e) {
  						Toast.makeText(getApplicationContext(),
  								"SMS faild, please try again later!",
  								Toast.LENGTH_LONG).show();
  							e.printStackTrace();
  						  }
  	   	   }
  		  
	 	Intent intent = new Intent(Acconly.this, Acconly.class);
    stopService(intent);
	}
	
	private Runnable showTime = new Runnable() {//keep on checking
	    public void run() {

	    	Log.i(String.valueOf(accelerometerPresent),"accelerometerPresent");
	    	// Checking accelerometer status
	    	if(accelerometerPresent){
	    	    ptracc=i;
	    		ptrgyro=k;
	    		
	    		if(algorithm.calculate()/*||hand*/){
	    			genuine();
	    			
	    		}
	    		else {
	    			//a=algorithm.bucketcustom(algorithm.limitSVM);
	    			//if(algorithm.SVMcount>8&&a){
	    				handler.postDelayed(showTime, checktime);
		    		//	}
	    			//else{handler.postDelayed(showTime, checktime);}
	    			
	    			}
	    		// checking "enable upload" checkbox in SettingActivity
	    		if(SettingActivity.load){
	    			connect.update(ptraccold, ptracc, accx, accy, accz, gyrox, gyroy, gyroz);
	    		}
	    	    ptraccold=ptracc;
				ptrgyroold=ptrgyro;
	    	}
	    	else{
	            Log.i("time:", new Date().toString());
	            init(which);
	    	}
	    	//Log.i("i&k", String.valueOf(SVM[k%400])+" "+String.valueOf(k));
	    }
	    
	    };
	    
	private Runnable stoprecording = new Runnable() {
	        public void run() {
	        	
	        }
	        
	    };
	private Runnable getnew = new Runnable() {
	        public void run() {
	        	connect.askversion();
	        	algorithm.update(app);
	        	update.postDelayed(getnew, 7200000);
	        }
	    };
	    private void copy(){
	    	System.arraycopy(accx, 0, accx_back, 0, accx.length);
			System.arraycopy(accy, 0, accy_back, 0, accy.length);
			System.arraycopy(accz, 0, accz_back, 0, accz.length);
			System.arraycopy(gyrox, 0, gyrox_back, 0, gyrox.length);
			System.arraycopy(gyroy, 0, gyroy_back, 0, gyroy.length);
			System.arraycopy(gyroz, 0, gyroz_back, 0, gyroz.length);
			System.arraycopy(gyrotime, 0, gyrotime_back, 0, gyrotime.length);
			System.arraycopy(time, 0, time_back, 0, time.length);
			
	    	return;
	    	
	    }
	    
	    
	    public static void send(int g){
	    	connect.motionrecord(accx_back, accy_back, accz_back, gyrox_back, gyroy_back, gyroz_back, gyrotime_back, time_back,g);
	    	return;
	    }
	    public static void sendnow(int g){
	    	connect.motionrecord(accx, accy, accz, gyrox, gyroy, gyroz, gyrotime, time,g);
	    	return;
	    }

	public BroadcastReceiver mReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            Log.i("screen off", "onReceive("+intent+")");
	            
	            if (!intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
	                return;
	            }
	     
	            Runnable runnable = new Runnable() {
	                public void run() {
	                    disarm(which);
	                    init(which);
	                }
	            };
	     
	            new Handler().postDelayed(runnable, 2000);
	        }
	    };

}
