package multiple.by.patience;

import java.util.Calendar;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class User_info extends Activity {//set name, birthday, height, weight
	EditText inputname;
	Button inputbirth;
	EditText inputheight;
	EditText inputweight;
	private Handler handler = new Handler();
	Button next;
	Reference get;
	private int pYear;
    private int pMonth;
    private int pDay;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		get=new Reference(this,"personalinfo");
		next = (Button) findViewById(R.id.button1);
		inputname = (EditText) findViewById(R.id.editText1);
		inputbirth = (Button) findViewById(R.id.button2);
		inputheight = (EditText) findViewById(R.id.editText3);
		inputweight = (EditText) findViewById(R.id.editText4);
		next.setOnClickListener(store);
		inputbirth.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
			public void onClick(View v) {
                showDialog(0);
            }
        });
		
		final Calendar cal = Calendar.getInstance();
        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);
 
        /** Display the current date in the TextView */
        updateDisplay();
		
		handler.postDelayed(report, 300);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_info, menu);
		return true;
	}

	private Button.OnClickListener store = new Button.OnClickListener() {
		public void onClick(View arg0) {
			String name;
			String birth;
			String height;
			String weight;
			
			name=inputname.getText().toString();
			birth=inputbirth.getText().toString();
			height=inputheight.getText().toString();
			weight=inputweight.getText().toString();
			if(name.matches("")||height.matches("")||weight.matches("")){//not empty
				Toast.makeText(getApplicationContext(), "You didn't fill all of them!", Toast.LENGTH_SHORT).show();
			}
			else{
		    get.save_string("name", name);
		    get.save_string("birth", birth);
		    get.save_string("height", height);
		    get.save_string("weight", weight);
		    if(get.first_time()){
		    Intent intent = new Intent(User_info.this, PosActivity.class);
	        startActivity(intent);
		    }
	        finish();
	        }
		 }
	};
	
	private Runnable report = new Runnable() {
        public void run() {
        	if(get.first_time()){
    			inputname.setText("¤p©ú");
    			inputbirth.setText("2014/2/17");
    			inputheight.setText("180");
    			inputweight.setText("60");
    			next.setText("next");
    		}else{
    			String n=" ";
    			n=get.get_string("name");
    			inputname.setText(n);
    			
    			n=get.get_string("birth");
    			inputbirth.setText(n);
    			
    			n=get.get_string("height");
    			inputheight.setText(n);
    			n=get.get_string("weight");
    			inputweight.setText(n);
    		 	
    			next.setText("back");
    		}
        }
    };
    
    private DatePickerDialog.OnDateSetListener pDateSetListener =
            new DatePickerDialog.OnDateSetListener() {//choose date interface
 
                public void onDateSet(DatePicker view, int year, 
                                      int monthOfYear, int dayOfMonth) {
                    pYear = year;
                    pMonth = monthOfYear;
                    pDay = dayOfMonth;
                    updateDisplay();
                }
            };
            
            private void updateDisplay() {
                inputbirth.setText(// Month is 0 based so add 1
                    new StringBuilder().append(pYear).append("/")
                            .append(pMonth + 1).append("/")
                            .append(pDay).append(" "));
            }
            
            protected Dialog onCreateDialog(int id) {
                    return new DatePickerDialog(this, 
                                pDateSetListener,
                                pYear, pMonth, pDay);
            }
	
}
