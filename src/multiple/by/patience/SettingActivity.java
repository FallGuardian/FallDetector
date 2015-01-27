package multiple.by.patience;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class SettingActivity extends Activity {
	private CheckBox callout;
	private CheckBox sendout;
	private CheckBox network;
	private CheckBox dynamic;
	private Button back;
	private Button restart;
	private Button click;
	private Button up;
	Reference get;
	Web pipa;
	public static boolean message=false;//SMS
	public static boolean upload=true;//data upload
	public static boolean dynamicEnable=false;//dynamic movement upload, default is enable
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		back=(Button) findViewById(R.id.button3);
		restart=(Button) findViewById(R.id.button1);
		click=(Button) findViewById(R.id.button2);
		up=(Button) findViewById(R.id.button4);
		callout=(CheckBox)findViewById(R.id.checkBox1);
		sendout=(CheckBox)findViewById(R.id.checkBox2);
		network=(CheckBox)findViewById(R.id.checkBox3);
		dynamic=(CheckBox)findViewById(R.id.checkBox4);
		get=new Reference(this,"personalinfo");
		pipa=new Web(this);
			
		back.setOnClickListener(finity);
		restart.setOnClickListener(re);
		click.setOnClickListener(send);
		up.setOnClickListener(send2);
		callout.setOnCheckedChangeListener(disable);
		sendout.setOnCheckedChangeListener(disable2);
		network.setOnCheckedChangeListener(disable3);
		dynamic.setOnCheckedChangeListener(disable4);
			
		if(WarningActivity.authorize){
			callout.setChecked(true);
		}
		else{
			callout.setChecked(false);
		}
		if(message){
			sendout.setChecked(true);
		}
		else{
			sendout.setChecked(false);
		}
		if(upload){
			network.setChecked(true);
		}
		else{
			network.setChecked(false);
		}
		if(dynamicEnable){
			dynamic.setChecked(true);
		}
		else{
			dynamic.setChecked(false);
		}
	}

	public void onDestroy() {
        super.onDestroy();
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}
		
	private Button.OnClickListener finity = new Button.OnClickListener() {
		public void onClick(View arg0) {
			finish();
		}
	};
	
	private Button.OnClickListener re = new Button.OnClickListener() {
		public void onClick(View arg0) {
			get.set_firstback();
		}
	};
	
	private Button.OnClickListener send = new Button.OnClickListener() {
		public void onClick(View arg0) {
			pipa.motionrecord(Acconly.accx,Acconly.accy,Acconly.accz,Acconly.gyrox,
			    			   Acconly.gyroy,Acconly.gyroz,Acconly.time,Acconly.gyrotime,
			    			   2);
			return;
		}
	};
		  
	private Button.OnClickListener send2 = new Button.OnClickListener() {
		public void onClick(View arg0) {
			pipa.motionrecord(Acconly.accx,Acconly.accy,Acconly.accz,Acconly.gyrox,
			    			   Acconly.gyroy,Acconly.gyroz,Acconly.time,Acconly.gyrotime,
			    			   0);
			return;
		}
	};
		 
	private CheckBox.OnCheckedChangeListener disable=new CheckBox.OnCheckedChangeListener(){
		public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
			if(callout.isChecked()==true){
				WarningActivity.authorize=true;
			}else{
				WarningActivity.authorize=false;
			}
		  
		}
	};
		  
	private CheckBox.OnCheckedChangeListener disable2=new CheckBox.OnCheckedChangeListener(){
		public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
			if(sendout.isChecked()==true){
				message=true;
			}else{
				message=false;
			}
		}
	};
			  
	private CheckBox.OnCheckedChangeListener disable3=new CheckBox.OnCheckedChangeListener(){
		public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
			if(network.isChecked()==true){
				upload=true;
			}else{
				upload=false;
			}
		}
	};
	private CheckBox.OnCheckedChangeListener disable4=new CheckBox.OnCheckedChangeListener(){
		public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
			if(dynamic.isChecked()==true){
			dynamicEnable=true;
			}else{
				dynamicEnable=false;
			}  
		}
	};
				  		  
}
