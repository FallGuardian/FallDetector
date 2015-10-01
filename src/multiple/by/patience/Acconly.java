package multiple.by.patience;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.R.integer;
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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

public class Acconly extends Service {
	
	SensorManager sensorManager;
	boolean accelerometerPresent = false;
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

	public static int ptraccold;
	//public static boolean hand=false;
	//public static int warn=0;
	
	int checktime;
	int frequency;
	public static int ptracc;
	public static float[] accx=new float[many];
	public static float[] accy=new float[many];
	public static float[] accz=new float[many];
	public static float[] time=new float[many];
	public static int ptrgyroold;
	public static int ptrgyro;
	public static float[] gyrox=new float[many];
	public static float[] gyroy=new float[many];
	public static float[] gyroz=new float[many];
	public static float[] gyrotime=new float[many];
	static float SVM[]=new float[many];
	float TA[]=new float[many];//many+2
    static float Av[]=new float[many];//many+2
    int SVMcount;

	static float[] gyrox_back=new float[many];
	static float[] gyroy_back=new float[many];
	static float[] gyroz_back=new float[many];
	static float[] accx_back=new float[many];
	static float[] accy_back=new float[many];
	static float[] accz_back=new float[many];
	static float[] time_back=new float[many];
	static float[] gyrotime_back=new float[many];
	public static boolean back_arrayAvilable = true;
	//boolean firsttime=true;
	// phoneSensersType classify the phone in 2 genre,
	// type:0 , smart-phone only equipment Accelerator Sensor
	// type:1 , smart-phone only equipment Accelerator Sensor and Gyro Sensor
	int phoneSensersType;
	public static Algorithm algorithm;
	static Web connect;
	public static Context app;
	
	private Handler handler = new Handler();
	private Handler update = new Handler();
	
	// Test for Service
	private Handler manitorHandler = new Handler();
	static int uploadPeriod = 4000;
	
	
	
	@Override
    public void onCreate() {
        super.onCreate();
        app=this;
        sensorManager = (SensorManager)getSystemService("sensor");
        
        PowerManager manager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);

        registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        
        UpdateLocation();
        Log.i("AccOnly", "in onCreate");
        
    }
	

	public int onStartCommand (Intent intent, int flags, int startId) {
		
		Log.i("onStartCommand"," yoyoyoyoyoyoyo");
		//warn=0;
		
		// Get the info of phone type, Both gryo and acc sensor , acc sensor
	
    	phoneSensersType=(Integer) intent.getExtras().get("phoneSensorsType");
    	
    	algorithm=new Algorithm(this,phoneSensersType);
    	connect=new Web(this);
    	
    	checktime=500;
    	frequency=0;//0 fastest 1 20ms
    	
    	if(back_arrayAvilable){
    		MainActivity.glow=true;
        	init(phoneSensersType);
    	}
    	
        startForeground(491, new Notification());
        mWakeLock.acquire();
        // @ Guess is start handler to keep monitor motion data
         handler.postDelayed(showTime, 3000);
        
        // Test for startMonitor
//        startMonitor();
        // @ Guessing activates askVersion 
        update.postDelayed(getnew, 7200000);
        
            return START_STICKY;
    }


	public Boolean checkSensorAvailable(int phoneSensorsType){
		Boolean available = false;
		switch(phoneSensorsType){
			case 0:
		        if(sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() > 0 
		        			&& sensorManager.getSensorList(Sensor.TYPE_GYROSCOPE).size() > 0)
		        	available =  true;
		        else
		        	available = false;
		        break;
			case 1:
			default:
				if(sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() > 0)
					available =  true;
				else
					available = false;
		}
		return available;
	}
	public void sensorBindListener(int phoneSensorsType){
		
		switch(phoneSensorsType){
			case 0:
				accelerometerSensor = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
				GYROSensor = sensorManager.getSensorList(Sensor.TYPE_GYROSCOPE).get(0);
				sensorManager.registerListener(accListener, accelerometerSensor, frequency);
	    	    sensorManager.registerListener(gryoListener, GYROSensor, frequency);
		        break;
			case 1:
			default:
				accelerometerSensor = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
				sensorManager.registerListener(accListener, accelerometerSensor, frequency);
				break;
			
		}
	
	}
	private SensorEventListener accListener = new SensorEventListener() {
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
	};
	private SensorEventListener gryoListener = new SensorEventListener() {
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
	};
	private void init(int phoneSensorsType){
		
		cleanSensorBuffer();
		//hand=false;
		List<Sensor> sensorList;
		List<Sensor> GYROList;
		
		switch(phoneSensorsType){
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
		
		return;
	}
	private void cleanSensorBuffer(){
		
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
	}
	// To Unregister sensorManager to save battery energy
	private void disarm(int phoneSensorsType){
		switch(phoneSensorsType){
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
        disarm(phoneSensersType);
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
	public static int i;
	public static int k;
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
	   		time[i%many]= (System.currentTimeMillis()/1000L);
	   		
	   		 if(i<40000)i++;
	   		 else i=0;
	   		 if(phoneSensersType==1){
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
	      		gyrotime[k%many]= (System.currentTimeMillis()/1000L);
	      		
	      		algorithm.SVMfunc(k);
	      		algorithm.AVfunc(k);
		   		if(k<=40000)k++;
		   		else k=0;
	      		 
	      	 }};
	   	 
	public void genuine(){
		
		// This moment will decide the whole observation data scope
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Log.i("genuine()",sdf.format(date));
		copy();
		
		handler.removeCallbacks(showTime);
	    Intent dialogIntent = new Intent(getBaseContext(), WarningActivity.class);
      	dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//how to get activity back
      	getApplication().startActivity(dialogIntent);
      	Log.i("genuine", "started Warning");
      	accelerometerPresent=false;
      	
      	disarm(phoneSensersType);
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
	    	
	    	// Checking accelerometer status
	    	if(accelerometerPresent && back_arrayAvilable){
	    	    ptracc=i;
	    		ptrgyro=k;
	    		
	    		Date date = new Date();
	    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    		Log.i("genuine()",sdf.format(date));
	    		
	    		if(algorithm.calculate()/*||hand*/){
	    			genuine();
	    			
	    		}
	    		else {
//	    			a=algorithm.bucketcustom(algorithm.limitSVM);
//	    			if(algorithm.SVMcount>8&&a){
	    			Log.i("shotTime","in alg test not fall");
	    				handler.postDelayed(showTime, checktime);
	    				
//		    		}
//	    			else{
//	    				handler.postDelayed(showTime, checktime);
//	    			}
	    			
	    		}
	    		// checking "enable upload" checkbox in SettingActivity
	    		if(SettingActivity.dynamicEnable){
	    			connect.update(ptraccold, ptracc, accx, accy, accz, gyrox, gyroy, gyroz);
	    			Log.i("showTime","call Web update");
	    		}
	    	    ptraccold=ptracc;
				ptrgyroold=ptrgyro;
	    	}
	    	else{
	            
	            init(phoneSensersType);
	    	}
	    	
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
//				Log.i("In Genunie copy()", "------------------------");
//				Log.i("In Genunie copy()", Arrays.toString(accx_back));
//				Log.i("In Genunie copy()", Arrays.toString(accy_back));
//				Log.i("In Genunie copy()", Arrays.toString(accz_back));
		    	return;
		 }
	    
	    
	    public static void send(int g){
	    	
	    	back_arrayAvilable = false;
//	    	Log.i("In Call send()", "------------------------");
//	    	Log.i("before send", Arrays.toString(accx_back));
//			Log.i("before send", Arrays.toString(accy_back));
//			Log.i("before send", Arrays.toString(accz_back));
	    	connect.motionrecord(accx_back, accy_back, accz_back, gyrox_back, gyroy_back, gyroz_back, gyrotime_back, time_back,g);
//	    	Log.i("affer send", Arrays.toString(accx_back));
//			Log.i("affer send", Arrays.toString(accy_back));
//			Log.i("affer send", Arrays.toString(accz_back));
	    	
	    	return;
	    }
	    public static void sendnow(int g){
	    	connect.motionrecord(accx, accy, accz, gyrox, gyroy, gyroz, gyrotime, time, g);
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
	                    disarm(phoneSensersType);
	                    init(phoneSensersType);
	                }
	            };
	     
	            new Handler().postDelayed(runnable, 2000);
	        }
	    };

}
