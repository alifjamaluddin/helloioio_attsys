package ioio.examples.hello;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MenuMain extends Activity {
//	IOIOMain ioio = new IOIOMain();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_role);
		Intent intent = new Intent(this, MainActivity.class);
		startService(intent);
	}

	public void workerLogin(View view) {
		Intent intent = new Intent(this, WorkerLoginActivity.class);
		startActivity(intent);
//		Context context = getApplicationContext();
//		CharSequence text = "Door open!";
//		int duration = Toast.LENGTH_SHORT;

//		Toast toast = Toast.makeText(context, text, duration);
//		toast.show();
//		ioio.bukaPintu();
		
	}
	public void visitorLogin(View view) {
		Intent intent = new Intent(this, VisitorLoginActivity.class);
		startActivity(intent);
//		Context context = getApplicationContext();
//		CharSequence text = "Door close!";
//		int duration = Toast.LENGTH_SHORT;
		
//		Intent intent = new Intent(this, MainActivity.class);
//		stopService(intent);

//		Toast toast = Toast.makeText(context, text, duration);
//		toast.show();
//		ioio.tutupPintu();
	}

	
}
