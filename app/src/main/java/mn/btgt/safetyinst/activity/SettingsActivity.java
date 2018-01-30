package mn.btgt.safetyinst.activity;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import mn.btgt.safetyinst.R;
import mn.btgt.safetyinst.database.SettingsTable;
import mn.btgt.safetyinst.model.Settings;
import mn.btgt.safetyinst.utils.EscPosPrinter;
import mn.btgt.safetyinst.utils.SAFCONSTANT;

public class SettingsActivity extends AppCompatActivity {

    private SettingsTable settingsTable;
    private SharedPreferences sharedPrefs;
    private String fontEncode;
    private String fontSize;
    EditText fontSizeEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        fontSizeEditText = (EditText) findViewById(R.id.txtFont);
        AppCompatButton saveBtn = findViewById(R.id.saveSettings);
        AppCompatButton testBtn = findViewById(R.id.printFontTest);

        settingsTable = new SettingsTable(getApplicationContext());
        sharedPrefs = getSharedPreferences(SAFCONSTANT.SHARED_PREF_NAME, MODE_PRIVATE);
        try {
            fontSize = String.valueOf(settingsTable.select(SAFCONSTANT.SETTINGS_PRINTER_FONT_SIZE));
            fontSizeEditText.setText(fontSize != null ? fontSize : "6");
            fontEncode = String.valueOf(settingsTable.select(SAFCONSTANT.SETTINGS_PRINTER_FONT_ENCODE));

            if (fontEncode.equals("ASCII")) {
                RadioButton ac = (RadioButton) findViewById(R.id.fontASCII);
                ac.setChecked(true);
            } else {
                RadioButton lt = (RadioButton) findViewById(R.id.fontLATIN);
                lt.setChecked(true);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        List<Settings> list = settingsTable.selectAll();
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
    }

    private void rgOnchanged(){
        RadioGroup radioFont = (RadioGroup) findViewById(R.id.groupFontSelect);
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
            List<Settings> SList = new ArrayList<Settings>();
            SList.add(new Settings(SAFCONSTANT.SETTINGS_PRINTER_FONT_SIZE, fontSize));
            SList.add(new Settings(SAFCONSTANT.SETTINGS_PRINTER_FONT_ENCODE, fontEncode));
            settingsTable.insertList(SList);
            finish();
            Toast.makeText(this.getApplicationContext(), R.string.save_settings_success,Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this.getApplicationContext(), R.string.save_settings_error,Toast.LENGTH_SHORT).show();
        }
    }

    public void printFontTest(){
        if (fontEncode.length() > 0 && fontSize != null) {
            SAFCONSTANT.company_name  = sharedPrefs.getString("", "Company Name");
            SAFCONSTANT.padaan_head = sharedPrefs.getString(SAFCONSTANT.PREF_HEAD, "");
            SAFCONSTANT.padaan_foot = sharedPrefs.getString(SAFCONSTANT.PREF_FOOT, "");
            SAFCONSTANT.sendData(
                    EscPosPrinter.getTestData80(
                            String.valueOf(settingsTable.select(SAFCONSTANT.SETTINGS_PRINTER_FONT_ENCODE)),
                            Integer.valueOf(settingsTable.select(SAFCONSTANT.SETTINGS_PRINTER_FONT_SIZE)),
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

}
