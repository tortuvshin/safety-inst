package mn.btgt.safetyinst.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import mn.btgt.safetyinst.R;
import mn.btgt.safetyinst.database.repo.SettingsRepo;
import mn.btgt.safetyinst.database.model.Settings;
import mn.btgt.safetyinst.utils.EscPosPrinter;
import mn.btgt.safetyinst.utils.SAFCONSTANT;
public class SettingsActivity extends AppCompatActivity {

    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private SettingsRepo settingsRepo;
    private String fontEncode;
    private String fontSize;
    private EditText fontSizeEditText;
    @SuppressLint("StaticFieldLeak")
    public static ToggleButton togglePrinter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Тохиргоо");
        }

        fontSizeEditText = findViewById(R.id.txtFont);
        AppCompatButton saveBtn = findViewById(R.id.saveSettings);
        AppCompatButton testBtn = findViewById(R.id.printFontTest);

        togglePrinter= findViewById(R.id.toggleButtonPrinter);
        settingsRepo = new SettingsRepo();

        try {
            fontSize = String.valueOf(settingsRepo.select(SAFCONSTANT.SETTINGS_PRINTER_FONT_SIZE));
            fontSizeEditText.setText(fontSize != null ? fontSize : "23");
            fontEncode = String.valueOf(settingsRepo.select(SAFCONSTANT.SETTINGS_PRINTER_FONT_ENCODE));

            if (fontEncode.equals("ASCII")) {
                RadioButton ac = findViewById(R.id.fontASCII);
                ac.setChecked(true);
            } else {
                RadioButton lt = findViewById(R.id.fontLATIN);
                lt.setChecked(true);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        List<Settings> list = settingsRepo.selectAll();
        Logger.d(list.toString());
        rgOnchanged();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSettings();
            }
        });
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printFontTest();
            }
        });
        togglePrinter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (togglePrinter.isChecked()) {
                    SAFCONSTANT.findBT(SettingsActivity.this);
                    togglePrinter.toggle();
                } else {
                    SAFCONSTANT.closeBT();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SAFCONSTANT.checkPrinter()){
            togglePrinter.setChecked(true);
        }else{
            togglePrinter.setChecked(false);
        }
    }

    private void rgOnchanged(){
        RadioGroup radioFont = findViewById(R.id.groupFontSelect);
        radioFont.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.fontLATIN) {
                    fontEncode = "LATIN";
                    Toast.makeText(getApplicationContext(), "font: Mongol hel",
                            Toast.LENGTH_SHORT).show();
                } else if(checkedId == R.id.fontASCII) {
                    fontEncode = "ASCII";
                    Toast.makeText(getApplicationContext(), "font: Монгол хэл",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    public void saveSettings(){

        if (fontSizeEditText.getText().length() >0) {
            fontSize = fontSizeEditText.getText().toString();
            List<Settings> SList = new ArrayList<>();
            SList.add(new Settings(SAFCONSTANT.SETTINGS_PRINTER_FONT_SIZE, fontSize));
            SList.add(new Settings(SAFCONSTANT.SETTINGS_PRINTER_FONT_ENCODE, fontEncode));
            settingsRepo.insertList(SList);
            finish();
            Toast.makeText(this.getApplicationContext(), R.string.save_settings_success,Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this.getApplicationContext(), R.string.save_settings_error,Toast.LENGTH_SHORT).show();
        }
    }

    public void printFontTest(){
        if (fontEncode.length() > 0 && fontSize != null) {
            SAFCONSTANT.sendData(
                    EscPosPrinter.getTestData80(
                            String.valueOf(settingsRepo.select(SAFCONSTANT.SETTINGS_PRINTER_FONT_ENCODE)),
                            Integer.valueOf(settingsRepo.select(SAFCONSTANT.SETTINGS_PRINTER_FONT_SIZE)),
                            SettingsActivity.this ));
        }else{
            Toast.makeText(this.getApplicationContext(), R.string.font_encode_error,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
        }else if (resultCode == Activity.RESULT_OK){
            switch (requestCode) {
                case REQUEST_CONNECT_DEVICE_SECURE:
                    // When DeviceListActivity returns with a device to connect
                    String address = data.getExtras().getString("device_address");
                    Log.d("ACTIVRES", "BT onActivityResult address : " + address);
                    SAFCONSTANT.last_printer_address = address;
                    SAFCONSTANT.openBT(SettingsActivity.this,address);
                    break;
            }
        }
    }
}
