package ioio.examples.hello;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * This is the main activity of the HelloIOIO example application.
 * 
 * It displays a toggle button on the screen, which enables control of the
 * on-board LED. This example shows a very simple usage of the IOIO, by using
 * the {@link IOIOActivity} class. For a more advanced use case, see the
 * HelloIOIOPower example.
 */
public class MainActivity extends Service {
	private boolean isRunning = false;

	/**
	 * Called when the activity is first created. Here we normally initialize
	 * our GUI.
	 */
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.main);
//		button_ = (ToggleButton) findViewById(R.id.button);
		
//		alternate();
//		finish();
		
//	}
	
//	public void alternate(){
//		final Handler handler = new Handler();
//		handler.postDelayed(new Runnable() {
//		    @Override
//		    public void run() {
//		        // Do something after 5s = 5000ms
//		       button_.toggle();
//		    }
//		}, 5000);
//		alternate();
//	}

	/**
	 * This is the thread on which all the IOIO activity happens. It will be run
	 * every time the application is resumed and aborted when it is paused. The
	 * method setup() will be called right after a connection with the IOIO has
	 * been established (which might happen several times!). Then, loop() will
	 * be called repetitively until the IOIO gets disconnected.
	 */
	class Looper extends BaseIOIOLooper implements IOIOLooper {
		/** The on-board LED. */
		private DigitalOutput led_;

		/**
		 * Called every time a connection with IOIO has been established.
		 * Typically used to open pins.
		 * 
		 * @throws ConnectionLostException
		 *             When IOIO connection is lost.
		 * 
		 * @see ioio.lib.util.AbstractIOIOActivity.IOIOThread#setup()
		 */
		@Override
		protected void setup() throws ConnectionLostException {
			led_ = ioio_.openDigitalOutput(46, true);
		}

		/**
		 * Called repetitively while the IOIO is connected.
		 * 
		 * @throws ConnectionLostException
		 *             When IOIO connection is lost.
		 * 
		 * @see ioio.lib.util.AbstractIOIOActivity.IOIOThread#loop()
		 */
		@Override
		public void loop() throws ConnectionLostException {
//			led_.write(!button_.isChecked());
			led_.write(true);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}


	private static final String TAG = null;

	/**
	 * A method to create our IOIO thread.
	 * 
	 * @see ioio.lib.util.AbstractIOIOActivity#createIOIOThread()
	 */
	
	 @Override
	    public int onStartCommand(Intent intent, int flags, int startId) {

	        Log.i(TAG, "Service onStartCommand");
	        Context context = getApplicationContext();
	        CharSequence text = "Door CLOSE!";
	        int duration = Toast.LENGTH_LONG;

	        Toast toast = Toast.makeText(context, text, duration);
	        toast.show();
	        createIOIOLooper() ;
	        return Service.START_STICKY;
	    }
	 
	 @Override
	    public void onDestroy() {
		 Context context = getApplicationContext();
	        CharSequence text = "Door OPEN!";
	        int duration = Toast.LENGTH_SHORT;

	        Toast toast = Toast.makeText(context, text, duration);
	        toast.show();
	

	        Log.i(TAG, "Service onDestroy");
	    }

	
	protected IOIOLooper createIOIOLooper() {
		return new Looper();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}