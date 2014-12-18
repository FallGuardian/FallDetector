package multiple.by.patience;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	public static int status;//for funMainActivity.
	public static boolean glow=false;
	float version=0;
	boolean counting=false;
	String name;
	String[] titles;
	private Button testoptions;
	private Button refresh;
	private Button send;
	private TextView whatsnew;
	private ImageView pic;
	private ImageView animate;
	private ImageView power;
	public static int phone;
	private int frame=0;
	Reference get;
	Reference contact;
	Context app=this;
	Web connect;
	private Handler handler = new Handler();
	
	char brand;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		get=new Reference(this,"algorversion");
		contact=new Reference(this,"personalinfo");
		
		testoptions = (Button) findViewById(R.id.button2);
		refresh=(Button) findViewById(R.id.button1);
		send=(Button) findViewById(R.id.button3);
		power=(ImageView) findViewById(R.id.imageView3);
		pic=(ImageView) findViewById(R.id.imageView1);
		animate=(ImageView) findViewById(R.id.imageView2);
		whatsnew = (TextView) findViewById(R.id.textView1);
		
		power.setOnClickListener(toggle);
		testoptions.setOnClickListener(warning);
		refresh.setOnClickListener(update);
		send.setOnClickListener(dump);
		pic.setOnClickListener(infoset);

        if(contact.first_time()){
			Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
	           startActivity(intent);
		}
		phone=whichphone();
		connect=new Web(this);
		handler.postDelayed(showTime, 500);
	}
	
	private Runnable showTime = new Runnable() {
		public void run() {
			String h;
			h="TA:"+get.get_float("TA");
			h=h+" SVM:"+get.get_float("SVM");
			h=h+" AV:"+get.get_float("AV");
			h=h+"AVact"+get.get_int("AVact");
			h=h+"SVMact"+get.get_int("SVMact");
			
			if(glow){
				h=h+"wait:"+Acconly.algorithm.wait;
				//Acconly.asd.update(app);
			}
			whatsnew.setText(h+brand);
			if(glow&&Acconly.accx[0]!=0){
        	
			switch(frame){
        	case 1:
            	animate.setImageResource(R.drawable.a);
            	break;
        	case 2:
            	animate.setImageResource(R.drawable.b);
            	break;
        	case 3:
            	animate.setImageResource(R.drawable.c);
            	break;
        	case 4:
            	animate.setImageResource(R.drawable.d);
            	break;
        	case 5:
            	animate.setImageResource(R.drawable.e);
            	break;
        	case 6:
            	animate.setImageResource(R.drawable.f);
            	break;
        	case 7:
            	animate.setImageResource(R.drawable.g);
            	break;
        	case 8:
            	animate.setImageResource(R.drawable.h);
            	break;
        	case 9:
            	animate.setImageResource(R.drawable.i);
            	frame=0;
            	break;
            default:
            	animate.setImageResource(R.drawable.a);
                frame=1;
                break;
        	}
        	frame++;
        		power.setImageResource(R.drawable.on);
        	}
        	else{
        	power.setImageResource(R.drawable.off);
        	}
        	handler.postDelayed(showTime, 200);
        }
    };

//----------------------------------
	public void onDestroy() {
		handler.removeCallbacks(showTime);
        super.onDestroy();
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private Button.OnClickListener infoset = new Button.OnClickListener() {
	       public void onClick(View arg0) {
	    	   Intent intent = new Intent(MainActivity.this, Intermediate.class);
		       startActivity(intent);
	      }
	};

	private Button.OnClickListener warning = new Button.OnClickListener() {
		  public void onClick(View arg0) {	    	   
		      Intent intent = new Intent(MainActivity.this, SettingActivity.class);
			  startActivity(intent);
	      }
	};
	
	private Button.OnClickListener update = new Button.OnClickListener() {
		  public void onClick(View arg0) {
			  connect.askversion();
			  String q = android.os.Build.BRAND;
			  brand = q.charAt(0);
			  /*if(glow){
			  Acconly.asd.update(Acconly.app);
			  }*/
	      }
	};
	private Button.OnClickListener dump = new Button.OnClickListener() {
		  public void onClick(View arg0) {
			  if(glow)Acconly.sendnow(0);
			  
	      }
	};

	private Button.OnClickListener toggle = new Button.OnClickListener() {
		public void onClick(View arg0) {
		    if(!glow){
				Intent intent = new Intent(MainActivity.this, Acconly.class);
				intent.putExtra("phone", phone);
				startService(intent);
			}
			else{
				Intent intent = new Intent(MainActivity.this, Acconly.class);
				stopService(intent);
			}
		}
	};
	

	private int whichphone(){
		SensorManager sm;
		Sensor SensorGyro;
				   
		sm = (SensorManager)getSystemService(android.content.Context.SENSOR_SERVICE);
		  	   
	  	SensorGyro = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
	  	if(SensorGyro == null){
		  	return 1;
	  	}else{
			return 0;
		}
	}
	
		    
}
