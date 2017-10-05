package btgt.mn.safetyinst;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import java.util.Calendar;

import btgt.mn.safetyinst.database.SettingsTable;
import btgt.mn.safetyinst.database.UserTable;
import btgt.mn.safetyinst.entity.Settings;
import btgt.mn.safetyinst.entity.User;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        UserTable userTable = new UserTable(this);
        userTable.add(new User("1", "Цогтгэрэл", "Програм хөгжүүлэгч", 99999999, mngr.getDeviceId(), "toroo.byamba@gmail.com", "admin", "", Calendar.getInstance().getTime().toString()));
        userTable.add(new User("2", "Ганцоож", "Вэб дизайнер", 99999999, mngr.getDeviceId(), "toroo.byamba@gmail.com", "admin", "", Calendar.getInstance().getTime().toString()));
        userTable.add(new User("3", "Бат-Эрдэнэ", "Мобайл апп хөгжүүлэгч", 99999999, mngr.getDeviceId(), "toroo.byamba@gmail.com", "admin", "", Calendar.getInstance().getTime().toString()));
        userTable.add(new User("4", "Ганцоож", "Програм хөгжүүлэгч", 99999999, mngr.getDeviceId(), "toroo.byamba@gmail.com", "admin", "", Calendar.getInstance().getTime().toString()));
        userTable.add(new User("5", "Төртүвшин", "Вэб хөгжүүлэгч", 99999999, mngr.getDeviceId(), "toroo.byamba@gmail.com", "admin", "", Calendar.getInstance().getTime().toString()));
        userTable.add(new User("6", "Энхбаяр", "Вэб хөгжүүлэгч", 99999999, mngr.getDeviceId(), "toroo.byamba@gmail.com", "admin", "", Calendar.getInstance().getTime().toString()));
        userTable.add(new User("7", "Цэнд-Аюуш", "Вэб хөгжүүлэгч", 99999999, mngr.getDeviceId(), "toroo.byamba@gmail.com", "admin", "", Calendar.getInstance().getTime().toString()));
        userTable.add(new User("8", "Анхаа", "Вэб хөгжүүлэгч", 99999999, mngr.getDeviceId(), "toroo.byamba@gmail.com", "admin", "", Calendar.getInstance().getTime().toString()));
        userTable.add(new User("9", "Цэнгүүн", "Вэб хөгжүүлэгч", 99999999, mngr.getDeviceId(), "toroo.byamba@gmail.com", "admin", "", Calendar.getInstance().getTime().toString()));
        userTable.add(new User("10", "Тэргүүн", "Вэб хөгжүүлэгч", 99999999, mngr.getDeviceId(), "toroo.byamba@gmail.com", "admin", "", Calendar.getInstance().getTime().toString()));
        SettingsTable settingsTable = new SettingsTable(this);

        settingsTable.add(new Settings("BTGT LLC", "Software Development", mngr.getDeviceId(), mngr.getDeviceSoftwareVersion(), "1"));
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
