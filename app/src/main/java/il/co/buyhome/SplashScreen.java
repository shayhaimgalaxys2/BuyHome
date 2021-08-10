package il.co.buyhome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
	 private final static int SPLASH_TIME_OUT = 3000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		new Handler(Looper.getMainLooper()).postDelayed(() -> {
			Intent intent = new Intent(SplashScreen.this, MainActivity.class);
			startActivity(intent);
			finish();
		}, SPLASH_TIME_OUT);
	}
}