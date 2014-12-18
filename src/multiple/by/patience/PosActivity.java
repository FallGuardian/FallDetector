package multiple.by.patience;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class PosActivity extends Activity {
	Button back;
	private RadioGroup myradiogroup;
	private RadioGroup positions;
	private RadioButton man;
	private RadioButton woman;
	private RadioButton ch;
	private RadioButton wa;
	private RadioButton mth;
	private RadioButton pu;
	
	Reference get;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pos);
		back=(Button)findViewById(R.id.button1);
		myradiogroup=(RadioGroup)findViewById(R.id.radiogroup);
		man=(RadioButton)findViewById(R.id.radioButton2);
		woman=(RadioButton)findViewById(R.id.radioButton1);
		ch=(RadioButton)findViewById(R.id.radioButton4);
		wa=(RadioButton)findViewById(R.id.radioButton3);
		mth=(RadioButton)findViewById(R.id.radioButton5);
		pu=(RadioButton)findViewById(R.id.radioButton6);
		positions=(RadioGroup)findViewById(R.id.where);
		
		get=new Reference(this,"personalinfo");
		
		back.setOnClickListener(end);
		myradiogroup.setOnCheckedChangeListener(changeradio);
		positions.setOnCheckedChangeListener(posi);
		int a=get.get_int("pos");
		int b=get.get_int("sex");
		switch(a){
		case 0:
			ch.setChecked(true);
			break;
		case 1:
			wa.setChecked(true);
			break;
		case 2:
			mth.setChecked(true);
			break;
		case 3:
			pu.setChecked(true);
			break;
		default:
		}
		switch(b){
		case 0:
			woman.setChecked(true);
			break;
		case 1:
			man.setChecked(true);
		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pos, menu);
		return true;
	}

	private RadioGroup.OnCheckedChangeListener changeradio=new RadioGroup.OnCheckedChangeListener()
	{
	public void onCheckedChanged(RadioGroup group, int checkedId)
	{
	//透過id來辨認不同的radiobutton
	switch(checkedId)
	{
	case R.id.radioButton2://man
	get.save_int("sex", 1);
	break;
	case R.id.radioButton1://woman
	get.save_int("sex", 0);
	break;
	default:
	}
	//Toast.makeText(PosActivity.this,String.valueOf(get.get_int("sex")),Toast.LENGTH_LONG).show();
    
	}

	};
	
	private RadioGroup.OnCheckedChangeListener posi=new RadioGroup.OnCheckedChangeListener()
	{
	public void onCheckedChanged(RadioGroup group, int checkedId)
	{
	//透過id來辨認不同的radiobutton
	switch(checkedId)
	{
	case R.id.radioButton3://waist
	get.save_int("pos", 1);
	break;
	case R.id.radioButton4://chest
	get.save_int("pos", 0);
	break;
	case R.id.radioButton5://thigh
		get.save_int("pos", 2);
		break;
	case R.id.radioButton6://purse
		get.save_int("pos", 3);
		break;
	default:
	}
	//Toast.makeText(PosActivity.this,String.valueOf(get.get_int("pos")),Toast.LENGTH_LONG).show();
	}

	};
	
	private Button.OnClickListener end = new Button.OnClickListener() {
		public void onClick(View arg0) {
			if(get.first_time()){
			    get.set_first();
			    Intent intent = new Intent(PosActivity.this, ContactActivity.class);
		        startActivity(intent);
			    }
	        finish();
		 }
	};
}
