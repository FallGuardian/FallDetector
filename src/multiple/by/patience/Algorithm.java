package multiple.by.patience;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import android.content.Context;


public class Algorithm {
	int service;
	int many;
	int i;
	int k;
	int checktime;
	int frequency;
	int ptracc;
	int ptraccold;
	int ptrgyro;
	int ptrgyroold;
	float[] gyrox;
	float[] gyroy;
	float[] gyroz;
	float[] accx;
	float[] accy;
	float[] accz;
	float[] time;
	float[] gyrotime;
	public int wait=0;
	
	float limitTA=-40;
	public float limitSVM=(float) 22.3;
	float limitAV=(float) 17.9;
	int SVMrange;
	int TArange;
	
	
    float TA[]=new float[402];//many+2
    int SVMcount;
    int TAcount[]=new int[20];
    int Avcount[]=new int[20];
    int SVMact;
    int AVact;
	
	int version;
	
	public Algorithm(Context context,int which){
		update(context);
		
		switch(which){
		case 0:
			many=Acconly.many;
			accx=Acconly.accx;
			accy=Acconly.accy;
			accz=Acconly.accz;
			gyrox=Acconly.gyrox;
			gyroy=Acconly.gyrox;
			gyroz=Acconly.gyrox;
			time=Acconly.time;
			gyrotime=Acconly.gyrotime;
			SVMact=1;
			AVact=1;
		break;
		case 1:
			many=Acconly.many;
			accx=Acconly.accx;
			accy=Acconly.accy;
			accz=Acconly.accz;
			time=Acconly.time;
			SVMact=1;
			AVact=0;
		break;
		default:
			
		break;
		}
		service=which;
		ptracc=0;
		ptraccold=0;
		ptrgyro=0;
		ptrgyroold=0;
		i=0;
		k=0;
	}
	
	public void update(Context context){
		Reference get=new Reference(context,"algorversion");
		limitTA=get.get_float("TA");
		limitSVM=get.get_float("SVM");
		limitAV=get.get_float("AV");
		SVMact=get.get_int("SVMact");
	    AVact=get.get_int("AVact");
		return;
	}
	
	public boolean calculate(){
		
			return calculateFULL();
	}
	
	public void SVMfunc(int h){
		int now=h%many;
  		Acconly.SVM[now] = (float)Math.sqrt(accx[now]*accx[now]+accy[now]*accy[now]+accz[now]*accz[now]);
		
		return;
	}
	
	private boolean SVMjudge(boolean activate){
		boolean signal=true;
		if(!activate)return true;
		
		for(int i=0;i<400;i++){
			if(limitSVM<Acconly.SVM[i]){
				SVMcount++;
			}
		}
		
		if(1<SVMcount){
			wait++;
		}
		else{
			wait=0;
		}
		
		if(wait>1){
			signal=true;
		}
		else{
			signal=false;
		}
		return signal;
	}
	
	public void AVfunc(int h){
		int now=k%many;
  		int old=(k+many-1)%many;
  		double delta_time_gyro = (gyrotime[now]-gyrotime[old]) / 1000;
   		double delta_y = delta_time_gyro * (gyroy[now]+gyroy[old])/2;
   		double delta_z = delta_time_gyro * (gyroz[now]+gyroz[old])/2;
   		Acconly.Av[now] = (float) Math.abs(accx[now]*sin(delta_z)+accy[now]*sin(delta_y)-accz[now]*cos(delta_y)*cos(delta_z));
   		
		return;
	}
	
	private boolean AVjudge(boolean activate){
		boolean signal=true;
		if(!activate)return true;
		
		for(int i=0;i<400;i++){
			if(limitAV<Acconly.Av[i]){
				signal=true;
				break;
			}
		}
		
		return signal;
	}
	
	private boolean calculateFULL(){
		
		boolean a = false;
	    boolean yellowlight=false;
	    boolean temp;
	    if(SVMact==1)temp=true;
	    else temp=false;
	    a=SVMjudge(temp);//bucketcustom(limitSVM);
	    
	    if(AVact==1)temp=true;
	    else temp=false;
		yellowlight=AVjudge(temp);//condition(a);
	    
		return yellowlight&a;
	}
	
	public int get_count(int u){
		return SVMcount;
	}
	
	
	public boolean bucketcustom(float bound){
		SVMcount=0;
		boolean avcount=false;
		for(int i=0;i<400;i++){
				if(bound<Acconly.SVM[i]){
					SVMcount++;
				}
				if(limitAV<Acconly.Av[i]){
					avcount=true;
				}
				if(limitTA<TA[i]){
					avcount=true;
				}
		}
		if(bound==limitSVM){return avcount;}
		else{
			boolean b=false;
			if(SVMcount>0)b=true;
			return b;
			}
	}
	
	
}
