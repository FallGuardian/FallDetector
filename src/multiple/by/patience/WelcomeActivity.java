package multiple.by.patience;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeActivity extends Activity {
	Button a;
	TextView wel;
	TextView in;
	int state=0;
	private Handler handler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		in=(TextView) findViewById(R.id.textView1);
		wel=(TextView) findViewById(R.id.textView2);
		a=(Button) findViewById(R.id.button1);
		in.setVisibility(View.INVISIBLE);
		wel.setVisibility(View.INVISIBLE);
		a.setVisibility(View.INVISIBLE);
		a.setOnClickListener(infoset);
		
		handler.postDelayed(animation, 800);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}
	private Button.OnClickListener infoset = new Button.OnClickListener() {
	       public void onClick(View arg0) {
	    	   Intent intent = new Intent(WelcomeActivity.this, User_info.class);
		       startActivity(intent);
		       finish();
	      }
	};
	
	private Runnable animation = new Runnable() {
		public void run() {
			switch(state){
			case 0:
				in.setText("W");
				in.setVisibility(View.VISIBLE);
				handler.postDelayed(animation, 100);
				break;
			case 1:
				in.setText("We");
				handler.postDelayed(animation, 100);
				break;
			case 2:
				in.setText("Wel");
				handler.postDelayed(animation, 100);
				break;
			case 3:
				in.setText("Welc");
				handler.postDelayed(animation, 100);
				break;
			case 4:
				in.setText("Welco");
				handler.postDelayed(animation, 100);
				break;
			case 5:
				in.setText("Welcom");
				handler.postDelayed(animation, 100);
				break;
			case 6:
				in.setText("Welcome");
				handler.postDelayed(animation, 100);
				break;
			case 7:
				in.setText("Welcome!");
				handler.postDelayed(animation, 100);
				break;
			case 8:
				in.setText("Welcome!!");
				handler.postDelayed(animation, 100);
				break;
			case 9:
				wel.setText("Y");
				wel.setVisibility(View.VISIBLE);
				handler.postDelayed(animation, 100);
				break;
			case 10:
				wel.setText("Yo");
				handler.postDelayed(animation, 100);
				break;
			case 11:
				wel.setText("You");
				handler.postDelayed(animation, 100);
				break;
			case 12:
				wel.setText("You ");
				handler.postDelayed(animation, 100);
				break;
			case 13:
				wel.setText("You n");
				handler.postDelayed(animation, 100);
				break;
			case 14:
				wel.setText("You ne");
				handler.postDelayed(animation, 100);
				break;
			case 15:
				wel.setText("You nee");
				handler.postDelayed(animation, 100);
				break;
			case 16:
				wel.setText("You need");
				handler.postDelayed(animation, 100);
				break;
			case 17:
				wel.setText("You need ");
				handler.postDelayed(animation, 100);
				break;
			case 18:
				wel.setText("You need t");
				handler.postDelayed(animation, 100);
				break;
			case 19:
				wel.setText("You need to");
				handler.postDelayed(animation, 100);
				break;
			case 20:
				wel.setText("You need to ");
				handler.postDelayed(animation, 100);
				break;
			case 21:
				wel.setText("You need to e");
				handler.postDelayed(animation, 100);
				break;
			case 22:
				wel.setText("You need to en");
				handler.postDelayed(animation, 100);
				break;
			case 23:
				wel.setText("You need to ent");
				handler.postDelayed(animation, 100);
				break;
			case 24:
				wel.setText("You need to ente");
				handler.postDelayed(animation, 100);
				break;
			case 25:
				wel.setText("You need to enter");
				handler.postDelayed(animation, 100);
				break;
			case 26:
				wel.setText("You need to enter ");
				handler.postDelayed(animation, 100);
				break;
			case 27:
				wel.setText("You need to enter\ns");
				handler.postDelayed(animation, 100);
				break;
			case 28:
				wel.setText("You need to enter\nso");
				handler.postDelayed(animation, 100);
				break;
			case 29:
				wel.setText("You need to enter\nsom");
				handler.postDelayed(animation, 100);
				break;
			case 30:
				wel.setText("You need to enter\nsome");
				handler.postDelayed(animation, 100);
				break;
			case 31:
				wel.setText("You need to enter\nsome ");
				handler.postDelayed(animation, 100);
				break;
			case 32:
				wel.setText("You need to enter\nsome i");
				handler.postDelayed(animation, 100);
				break;
			case 33:
				wel.setText("You need to enter\nsome in");
				handler.postDelayed(animation, 100);
				break;
			case 34:
				wel.setText("You need to enter\nsome inf");
				handler.postDelayed(animation, 100);
				break;
			case 35:
				wel.setText("You need to enter\nsome info");
				handler.postDelayed(animation, 100);
				break;
			case 36:
				wel.setText("You need to enter\nsome info ");
				handler.postDelayed(animation, 100);
				break;
			case 37:
				wel.setText("You need to enter\nsome info f");
				handler.postDelayed(animation, 100);
				break;
			case 38:
				wel.setText("You need to enter\nsome info fi");
				handler.postDelayed(animation, 100);
				break;
			case 39:
				wel.setText("You need to enter\nsome info fir");
				handler.postDelayed(animation, 100);
				break;
			case 40:
				wel.setText("You need to enter\nsome info firs");
				handler.postDelayed(animation, 100);
				break;
			case 41:
				wel.setText("You need to enter\nsome info first");
				handler.postDelayed(animation, 100);
				break;
			case 42:
				wel.setText("You need to enter\nsome info first!");
				handler.postDelayed(animation, 100);
				break;
			case 43:
				wel.setText("You need to enter\nsome info first!!");
				handler.postDelayed(animation, 100);
				break;
			case 44:
				a.setVisibility(View.VISIBLE);
				handler.postDelayed(animation, 100);
				break;
				
			default:
				wel.setVisibility(View.VISIBLE);
				a.setVisibility(View.VISIBLE);
				in.setVisibility(View.VISIBLE);
				in.setText("Welcome!!");
				wel.setText("You need to enter\nsome info first!!");
				break;
			}
			state++;
		}	
	};
}
