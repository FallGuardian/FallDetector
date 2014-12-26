package multiple.by.patience;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;
import java.util.ArrayList;

public class Web {
	Reference get,person;
	String bug="ddd";
	Web(Context context){
		get=new Reference(context,"algorversion");
		person=new Reference(context,"personalinfo");
	}
	
	public void motionrecord(
			final float[] accx,final float[] accy,final float[] accz, 
			final float[] gyrox,final float[] gyroy,final float[] gyroz,
			final float[] gyrotime,final float[] time,final int fallen
			){
		
		new Thread(new Runnable() {//transmitting
			public void run() {
				int j;
				try{
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost method = new HttpPost("http://140.114.71.113/fallDetect/recevie/test.php");
	          
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
					for(j=0;j<400;j++){//three axis upload
						nameValuePairs.add(new BasicNameValuePair("x[]", String.valueOf(accx[j])));
						nameValuePairs.add(new BasicNameValuePair("y[]", String.valueOf(accy[j])));
						nameValuePairs.add(new BasicNameValuePair("z[]", String.valueOf(accz[j])));
						nameValuePairs.add(new BasicNameValuePair("gyrox[]", String.valueOf(gyrox[j])));
						nameValuePairs.add(new BasicNameValuePair("gyroy[]", String.valueOf(gyroy[j])));
						nameValuePairs.add(new BasicNameValuePair("gyroz[]", String.valueOf(gyroz[j])));
						nameValuePairs.add(new BasicNameValuePair("gyrotime[]", String.valueOf(gyrotime[j])));
						nameValuePairs.add(new BasicNameValuePair("time[]", String.valueOf(time[j])));
					}
	    
					String q = android.os.Build.BRAND;//manufacturer
					char brand = q.charAt(0);
					int bb=0;
					switch(brand){
					case 'a'://ASUS
						bb=2;
						break;
					case 's'://Samsung
						bb=3;
						break;
					case 'h'://HTC
						bb=1;
						break;
					}
					nameValuePairs.add(new BasicNameValuePair("brand", String.valueOf(bb)));//1HTC 2asus 3samsung
					nameValuePairs.add(new BasicNameValuePair("sex", String.valueOf(person.get_int("sex"))));
					nameValuePairs.add(new BasicNameValuePair("pos", String.valueOf(person.get_int("pos"))));
	    
					int x=(Integer.parseInt(person.get_string("weight")))/10;
					nameValuePairs.add(new BasicNameValuePair("weight", String.valueOf(x)));
		
					x=(Integer.parseInt(person.get_string("height"))-100)/10;
					nameValuePairs.add(new BasicNameValuePair("height", String.valueOf(x)));
		
					Scanner in = new Scanner(person.get_string("birth")).useDelimiter("[^0-9]+");
					x = in.nextInt();
					nameValuePairs.add(new BasicNameValuePair("birth", String.valueOf(x)));
					if(fallen==1){
						nameValuePairs.add(new BasicNameValuePair("fell", "1"));}
					else if(fallen==0){
						nameValuePairs.add(new BasicNameValuePair("fell", "0"));
					}
					else{
						nameValuePairs.add(new BasicNameValuePair("fell", "2"));
					}
					method.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        
					HttpResponse response = httpclient.execute(method);
					HttpEntity entity = response.getEntity();
	          
					if(entity != null){
						Log.i("wow:",EntityUtils.toString(entity));
					}
					else{
						Log.i("God Damn Error: " , "No string.");
					}
					Log.i("Send End", "motionrecord");
				}catch(Exception tt){
					Log.i("Exceptions", "Internet failure");
					tt.printStackTrace();
				}
			}//run
		}).start();//thread
		return;
	}
	
	/*public void askinstall(){
		new Thread(new Runnable() {//transmitting
			public void run() {
				String result="0";
				String inputline;
				try{
					HttpClient httpclient = new DefaultHttpClient();
					HttpGet method = new HttpGet("http://logos.cs.nthu.edu.tw/~shihchan/cellphone/install.php?i="
							+String.valueOf(get.get_float("askinstall")));
       
					HttpResponse response = httpclient.execute(method);
					HttpEntity entity = response.getEntity();
       
					if(entity != null){
						result=EntityUtils.toString(entity);
						InputStream is = new ByteArrayInputStream( result.getBytes() );
						BufferedReader in;
						in = new BufferedReader(new InputStreamReader(is));
     	
						inputline=in.readLine();
						get.save_float("askinstall", Integer.parseInt(inputline));
					}
				}catch(Exception tt){
					Log.d("Exceptions", "Internet failure");
					tt.printStackTrace();
				}
			}//run
		}).start();//thread
    	return;
    }*/
	
	public void update(final int before, final int after, final float accx[],
			final float accy[],final float accz[],final float gyrox[],
			final float gyroy[],final float gyroz[]){
		new Thread(new Runnable() {//transmitting
			public void run() {
				try{
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost method = new HttpPost("http://140.114.71.113/fallDetect/receive/test.php");
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
					int count=0;
					if(before>after){
						for(int j=after;j<401;j++){
							count++;
							nameValuePairs.add(new BasicNameValuePair("accx[]", String.valueOf(accx[j])));
							nameValuePairs.add(new BasicNameValuePair("accy[]", String.valueOf(accy[j])));
							nameValuePairs.add(new BasicNameValuePair("accz[]", String.valueOf(accz[j])));
							nameValuePairs.add(new BasicNameValuePair("gyrox[]", String.valueOf(gyrox[j])));
							nameValuePairs.add(new BasicNameValuePair("gyroy[]", String.valueOf(gyroy[j])));
							nameValuePairs.add(new BasicNameValuePair("gyroz[]", String.valueOf(gyroz[j])));
  	
						}
						for(int j=0;j<=after;j++){
							count++;
							nameValuePairs.add(new BasicNameValuePair("accx[]", String.valueOf(accx[j])));
							nameValuePairs.add(new BasicNameValuePair("accy[]", String.valueOf(accy[j])));
							nameValuePairs.add(new BasicNameValuePair("accz[]", String.valueOf(accz[j])));
							nameValuePairs.add(new BasicNameValuePair("gyrox[]", String.valueOf(gyrox[j])));
							nameValuePairs.add(new BasicNameValuePair("gyroy[]", String.valueOf(gyroy[j])));
							nameValuePairs.add(new BasicNameValuePair("gyroz[]", String.valueOf(gyroz[j])));
						}
					}else{
						for(int j=before;j<=after;j++){
							count++;
							nameValuePairs.add(new BasicNameValuePair("accx[]", String.valueOf(accx[j])));
							nameValuePairs.add(new BasicNameValuePair("accy[]", String.valueOf(accy[j])));
							nameValuePairs.add(new BasicNameValuePair("accz[]", String.valueOf(accz[j])));
							nameValuePairs.add(new BasicNameValuePair("gyrox[]", String.valueOf(gyrox[j])));
							nameValuePairs.add(new BasicNameValuePair("gyroy[]", String.valueOf(gyroy[j])));
							nameValuePairs.add(new BasicNameValuePair("gyroz[]", String.valueOf(gyroz[j])));
						}
					}
      
					nameValuePairs.add(new BasicNameValuePair("after", String.valueOf(count)));
		
					method.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        
					HttpResponse response = httpclient.execute(method);
					HttpEntity entity = response.getEntity();
					if(entity != null){
						Log.i("wow:",EntityUtils.toString(entity));
					}
					else{
						Log.i("God Damn Error: " , "in update");
					}
				}catch(Exception tt){
					Log.i("Exceptions", "internet error");
					
					tt.printStackTrace();
				}
				Log.i("Send End", "in update");
			}//run
		}).start();//thread
		
		return;
	}
	
	public void askversion(){
		new Thread(new Runnable() {//transmitting
			public void run() {
				String result="0";
				String inputline;
	 	    	   
				try{
					HttpClient httpclient = new DefaultHttpClient();
					HttpGet method = new HttpGet("http://logos.cs.nthu.edu.tw/~shihchan/cellphone/update.php?i=2"/*+get.get_string("version")*/);
       
					HttpResponse response = httpclient.execute(method);
					HttpEntity entity = response.getEntity();
       
					if(entity != null){
						result=EntityUtils.toString(entity);
						InputStream is = new ByteArrayInputStream( result.getBytes() );
						BufferedReader in;
						in = new BufferedReader(new InputStreamReader(is));
     	
						inputline=in.readLine();
						get.save_float("version", Float.parseFloat(inputline));
						
						
						inputline=in.readLine();
						get.save_float("TA", Float.parseFloat(inputline));
						
						inputline=in.readLine();
						get.save_float("SVM", Float.parseFloat(inputline));
						
						inputline=in.readLine();
						get.save_float("AV", Float.parseFloat(inputline));
						
						inputline=in.readLine();
						get.save_float("dots", Float.parseFloat(inputline));
						
						inputline=in.readLine();
						get.save_int("SVMact", Integer.parseInt(inputline));
						inputline=in.readLine();
						get.save_int("AVact", Integer.parseInt(inputline));
					}
       
				}catch(Exception tt){
					Log.d("Exceptions", "Internet failure");
					tt.printStackTrace();
				}

			}//run
		}).start();//thread
    	return;
    }
	
}
