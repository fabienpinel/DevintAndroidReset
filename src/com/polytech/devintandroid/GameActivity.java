package com.polytech.devintandroid;


import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
/**
 * 
 * @author Fabien Pinel
 *
 */
public class GameActivity extends Activity implements SensorEventListener {
	private SensorManager	sensorManager;
	private Sensor			accelerometer;
	// private TextView view_x, view_y, view_z;
	private float			x, y, z;
	private Display			display;
	private Vue				vue;
	private static int		majoration	= 6;
	private LinearLayout	layout		= null;
	private int				car, level;
	boolean					soundReady	= false;
	private Canvas			canvas;
	private int				explosionId;
	private SoundPool		soundPool;
	private boolean			loaded		= false;
	private Vibrator vibreur;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		vibreur = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		//empecher la mise en veille de l'écran comme nous sommes dans l'activity de jeu
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		
		/*
		 * Lecture de fichier son
		 */
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		// Chargement du fichier musique.mp3 qui se trouve sous assets de notre

		soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
		explosionId = soundPool.load(this, R.drawable.bip, 1);

		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				loaded = true;

			}
		});

		
		loadSettings();
		gameActivityInit();
		Log.d("init", "init");

		sensorManager.registerListener(this, accelerometer,
				SensorManager.SENSOR_DELAY_UI);
		display = ((WindowManager) getSystemService(WINDOW_SERVICE))
				.getDefaultDisplay();
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		/*
		 * Fin lecture de fichier son
		 */
		//setContentView(R.);

	}

	public void gameActivityInit() {
		//Initialisation des capteurs pour récupérer les infos
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {

		super.onResume();
	}

	/**
	 * Lorsque l'application s'arrête, il faut arrêter proprement la boucle de
	 * jeu
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	public void onSensorChanged(SensorEvent event) {
		if (event != null && display != null) {
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				switch (display.getRotation()) {
				case Surface.ROTATION_0:
					this.x = event.values[0];
					this.y = event.values[1];
					break;
				case Surface.ROTATION_90:
					this.x = -event.values[1];
					this.y = event.values[0];
					break;
				case Surface.ROTATION_180:
					this.x = -event.values[0];
					this.y = -event.values[1];
					break;
				case Surface.ROTATION_270:
					this.x = event.values[1];
					this.y = -event.values[0];
					break;
				}
				this.z = event.values[2];
				if (x > 0) {
					x *= majoration;
				} else {
					x *= majoration;
				}

			}
			/*
			 view_x.setText("x: " + this.getX()); 
			 view_y.setText("y: " +this.getY()); 
			 view_z.setText("z: " + this.getZ());
			 */
		}

	}

	private void playSound(int resId) {
		if (loaded) {
			soundPool.play(explosionId, (float)0.5, (float)0.5, 0, 0, 1);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d("RunGameActivity", "OnTouchEvent");
	
		if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
		      Log.d("TouchTest", "Touch down");
		    } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
		      Log.d("TouchTest", "Touch up");
		    }
		
		return true;
	}
	

	public void loadSettings() {
		SharedPreferences settings = getSharedPreferences("prefs",
				Context.MODE_PRIVATE);
	// ma_variable = settings.getInt("ma _variable", default_value);
		this.level = settings.getInt("level", OptionsActivity.NORMAL);
	}
}