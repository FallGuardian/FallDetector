package multiple.by.patience;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class Intermediate extends Activity {

	Button user;
	Button contact;
	Button back;
	Button position;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intermediate);
		
		user = (Button) findViewById(R.id.button1);
		contact = (Button) findViewById(R.id.button2);
		back = (Button) findViewById(R.id.button3);
		position = (Button) findViewById(R.id.Button01);
		
		user.setOnClickListener(gotouser);
		contact.setOnClickListener(gotocontact);
		back.setOnClickListener(backtomain);
		position.setOnClickListener(gotopos);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.intermediate, menu);
		return true;
	}

	
	private Button.OnClickListener gotouser = new Button.OnClickListener() {
		public void onClick(View arg0) {
			Intent intent = new Intent(Intermediate.this, User_info.class);
	        startActivity(intent);
		 }
	};
	
	private Button.OnClickListener gotocontact = new Button.OnClickListener() {
		public void onClick(View arg0) {
			Intent intent = new Intent(Intermediate.this, ContactActivity.class);
	        startActivity(intent);
		 }
	};
	
	private Button.OnClickListener backtomain = new Button.OnClickListener() {
		public void onClick(View arg0) {
			finish();
		 }
	};
	private Button.OnClickListener gotopos = new Button.OnClickListener() {
		public void onClick(View arg0) {
			Intent intent = new Intent(Intermediate.this, PosActivity.class);
	        startActivity(intent);
		 }
	};
	
}
