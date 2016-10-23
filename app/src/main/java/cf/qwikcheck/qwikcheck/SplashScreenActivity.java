package cf.qwikcheck.qwikcheck;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import cf.qwikcheck.qwikcheck.Base.QwikCheckBaseActivity;
import cf.qwikcheck.qwikcheck.Helper.SessionHelper;
import cf.qwikcheck.qwikcheck.Utils.ConnectivityUtils;

public class SplashScreenActivity extends QwikCheckBaseActivity {

    private final int DELAY_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (!ConnectivityUtils.isAppConnected(SplashScreenActivity.this)) {
            Toast.makeText(SplashScreenActivity.this, "No Internet. The app will exit now.", Toast.LENGTH_LONG);
        } else {
            Intent intent;
            if (new SessionHelper(SplashScreenActivity.this).isLoggedIn()) {
                intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            } else {
                intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            }
            startActivity(intent);
        }

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        finish();
    }
}
