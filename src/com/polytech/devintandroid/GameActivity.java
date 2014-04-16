package com.polytech.devintandroid;

import com.polytech.devintandroid.R;

import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.WindowManager;
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
	private float			x, y, z;
	private Display			display;
	boolean					soundReady	= false;
	private int				explosionId;
	private SoundPool		soundPool;
	private boolean			loaded		= false;
	private Vibrator		vibreur;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		vibreur = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		loadSettings();
		gameActivityInit();
		Log.d("init", "init");

		/*
		 * Lecture de fichier son
		 */
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		// Chargement du fichier musique.mp3 qui se trouve sous assets de notre

		soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
		// explosionId = soundPool.load(this, R.drawable.bip(ici accès à votre
		// ressource par R.<emplacement>), 1);

		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				loaded = true;

			}
		});
		/*
		 * Fin lecture de fichier son
		 */

		setContentView(R.layout.activity_game);

	}

	public void gameActivityInit() {
		// empecher la mise en veille de l'écran comme nous sommes dans
		// l'activity de jeu
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		// Initialisation des capteurs pour récupérer les infos
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		sensorManager.registerListener(this, accelerometer,
				SensorManager.SENSOR_DELAY_UI);
		display = ((WindowManager) getSystemService(WINDOW_SERVICE))
				.getDefaultDisplay();
		// On fixe l'orientation de l'application à paysage pour le jeu
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

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

	/**
	 * Cette méthode est appelée lorsque le capteur change de valeur, dans cette
	 * méthode on récupère alors les valeur en fonction de l'orientation de
	 * l'appareil
	 */
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
			}
		}

	}

	/**
	 * Simplement appeler cette méthode pour jouer un son avec la ressource en
	 * parametre (R.<dossier>.<element>)
	 * 
	 * @param resId
	 */
	private void playSound(int resId) {
		if (loaded) {
			soundPool.play(explosionId, (float) 0.5, (float) 0.5, 0, 0, 1);
		}
	}

	/**
	 * Le nom de la méthode parle de lui meme, c'est une méthode qui est appelée
	 * au touché de l'écran et on peut différencier UP et DOWN
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d("RunGameActivity", "OnTouchEvent");

		if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
			Log.d("TouchTest", "Touch down");
			// Pour faire vibrer l'appareil au touché par exemple
			// this.vibreur.vibrate(100);
		} else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
			Log.d("TouchTest", "Touch up");
		}

		return true;
	}

	/**
	 * Ici on accède à des valeurs stockée dans l'application, typiquement des
	 * valeurs auquelle on peut accéder peut importe notre emplacement dans
	 * l'application (on les change dans le menu option et on y accède partout)
	 */
	public void loadSettings() {

		SharedPreferences settings = getSharedPreferences("prefs",
				Context.MODE_PRIVATE);
		// ma_variable = settings.getInt("ma _variable", default_value);
	}
}
