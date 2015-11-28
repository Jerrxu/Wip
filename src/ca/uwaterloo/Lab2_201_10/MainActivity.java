package ca.uwaterloo.Lab2_201_10;

import android.app.Activity;
import android.app.Fragment;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private float[] xValues = {0,0,0,0,0,0,0};
		private float[] zValues = {0,0,0,0,0,0,0};
		
		private FiniteStateMachine fsm;
		
		private Button playSoundButton;
		private TextView maxView;
		private ListView soundList;
		
		private static MediaPlayer mp;
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);

			 mp = MediaPlayer.create(rootView.getContext(), R.raw.crack2);
			// Layout
			LinearLayout appLayout = (LinearLayout) rootView.findViewById(R.id.fragmentLayout);

			//fsm
			fsm = new FiniteStateMachine();

			//Views
			maxView = (TextView) rootView.findViewById(R.id.maxDisplay);

			// Reset Button
			playSoundButton = (Button) rootView.findViewById(R.id.playSoundButton);
			playSoundButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mp.start();
				}
			});

			// Sensor manager for managing of sensors
			SensorManager sensorManager = (SensorManager) rootView.getContext().getSystemService(SENSOR_SERVICE);

			// Acceleration sensor
			Sensor accelSensor = sensorManager
					.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
			SensorEventListener accelListener = new AccelerometerEventListener();
			sensorManager.registerListener(accelListener, accelSensor,
					SensorManager.SENSOR_DELAY_NORMAL);
			
			
			Spinner spinner = (Spinner) rootView.findViewById(R.id.planets_spinner);
			// Create an ArrayAdapter using the string array and a default spinner layout
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(rootView.getContext(),
			        R.array.planets_array, android.R.layout.simple_spinner_item);
			// Specify the layout to use when the list of choices appears
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// Apply the adapter to the spinner
			spinner.setAdapter(adapter);
			
			return rootView;

		}
		
		public static void playSound()
		{
			mp.start();
		}

		public static float[] addNewRemoveOld(float[] x, float[] newValues){
			for (int i = x.length-1; i >= 1; i--) {
				x[i] = x[i-1];
			}
			x[0] = newValues[0];
			return x;
			
		}

		class AccelerometerEventListener implements SensorEventListener {

			public AccelerometerEventListener() {
			}

			public void onAccuracyChanged(Sensor s, int i) {
			}

			public void onSensorChanged(SensorEvent se){
				fsm.updateState(MainActivity.PlaceholderFragment.addNewRemoveOld(xValues, se.values));
			}

			public float[] lowpass(float[] in, float dt, float RC) {
				float[] out = new float[in.length];
				float a = dt / (dt + RC);
					out[0] = 0;
					//out[0] = in[0];
				for (int i = 1; i < in.length; i++) {
					out[i] = a * in[i] + (1 - a) * out[i - 1];
				}
				return out;
			}
		}
	}
}
