package btgt.mn.safetyinst;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try {
            Thread timerThread = new Thread(){
                public void run(){
                    try{
                        sleep(1000);
                    } catch(InterruptedException e){
                        e.printStackTrace();
                    } finally{
                        Intent intent = new Intent(SplashActivity.this, LoginListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            };
            timerThread.start();
        }catch (Exception e){

        }
    }
}
