package mn.btgt.safetyinst.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mn.btgt.safetyinst.R;
import mn.btgt.safetyinst.database.SettingsTable;
import mn.btgt.safetyinst.model.Settings;
import mn.btgt.safetyinst.utils.EscPosPrinter;
import mn.btgt.safetyinst.utils.SAFCONSTANT;

public class SettingsActivity extends AppCompatActivity {

    private static final int PICKFILE_REQUEST_CODE = 950;
    private static final int PICKFILE_REQUEST_CODE_KITKAT = 951;
    private SettingsTable settingsTable;
    private SharedPreferences sharedPrefs;
    private String fontSelect;
    private int noatType;
    private int nhatType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        AppCompatButton saveBtn = findViewById(R.id.saveSettings);
        AppCompatButton testBtn = findViewById(R.id.printFontTest);
        AppCompatButton fileBtn = findViewById(R.id.printFile);

        EditText cp = (EditText) findViewById(R.id.txtFont);
        settingsTable = new SettingsTable(getApplicationContext());
        sharedPrefs = getSharedPreferences(SAFCONSTANT.SHARED_PREF_NAME, MODE_PRIVATE);
        try {
            String cpv = String.valueOf(settingsTable.select(SAFCONSTANT.SETTINGS_KEY_PRINTER_CODEPAGE));
            cp.setText(cpv.length() > 0 ? cpv : "6");
            fontSelect = String.valueOf(settingsTable.select(SAFCONSTANT.SETTINGS_KEY_PRINTER_FONT));

            if (fontSelect.equals("ASCII")) {
                RadioButton ac = (RadioButton) findViewById(R.id.fontASCII);
                ac.setChecked(true);
            }else{
                RadioButton lt = (RadioButton) findViewById(R.id.fontLATIN);
                lt.setChecked(true);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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
        fileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printFile();
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
                    fontSelect = "LATIN";
                    Toast.makeText(getApplicationContext(), "font: Mongol hel",
                            Toast.LENGTH_SHORT).show();
                } else if(checkedId == R.id.fontASCII) {
                    fontSelect = "ASCII";
                    Toast.makeText(getApplicationContext(), "font: Молгол хэл",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
    public void saveSettings(){
        EditText cp = (EditText) findViewById(R.id.txtFont);
        String codePage = String.valueOf(cp.getText());
        if (codePage.length() >0) {
            List<Settings> SList = new ArrayList<Settings>();;
            SList.add(new Settings(SAFCONSTANT.SETTINGS_KEY_PRINTER_CODEPAGE, codePage));
            SList.add(new Settings(SAFCONSTANT.SETTINGS_KEY_PRINTER_FONT, fontSelect));
            SList.add(new Settings(SAFCONSTANT.SETTINGS_KEY_TAX_NOAT_TYPE, String.valueOf(noatType)));
            SList.add(new Settings(SAFCONSTANT.SETTINGS_KEY_TAX_NHAT_TYPE, String.valueOf(nhatType)));

            SAFCONSTANT.codePage = Integer.valueOf(String.valueOf(cp.getText()));
            SAFCONSTANT.printer_font = fontSelect;
            settingsTable.insertList(SList);
            finish();
        }else{
            Toast.makeText(this.getApplicationContext(),R.string.error_occurred,Toast.LENGTH_SHORT);
        }
    }
    public void printFontTest(){
        EditText cp = (EditText) findViewById(R.id.txtFont);
        String codePage = String.valueOf(cp.getText());
        if (codePage.length() >0 ) {
            SAFCONSTANT.company_name  = sharedPrefs.getString("", "Company Name");
            SAFCONSTANT.padaan_head = sharedPrefs.getString(SAFCONSTANT.PREF_HEAD, "");
            SAFCONSTANT.padaan_foot = sharedPrefs.getString(SAFCONSTANT.PREF_FOOT, "");
            SAFCONSTANT.sendData(EscPosPrinter.getTestData80(fontSelect,Integer.valueOf(codePage),this ));
        }else{
            Toast.makeText(this.getApplicationContext(),R.string.error_occurred,Toast.LENGTH_SHORT);

        }

    }
    private String selectedImagePath;

    public void printFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), PICKFILE_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PICKFILE_REQUEST_CODE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                Toast.makeText(this,"f:"+selectedImagePath,Toast.LENGTH_SHORT).show();
                int photox = SAFCONSTANT.printPhoto(selectedImagePath);
                switch (photox){
                    case 10 : Toast.makeText(this,"Зургын өргөн 400px ээс их байж болохгүй.", Toast.LENGTH_SHORT).show(); break;
                    case 11 : Toast.makeText(this,"Зургын өндөр 800px ээс их байж болохгүй.", Toast.LENGTH_SHORT).show(); break;
                    case 1 : Toast.makeText(this,"Зургыг хэмжээ хэвийн байна", Toast.LENGTH_SHORT).show(); break;
                }
            }
        }
    }

    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();
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
