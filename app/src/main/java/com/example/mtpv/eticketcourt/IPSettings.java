package com.example.mtpv.eticketcourt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class IPSettings extends Activity implements OnClickListener {

    AppCompatEditText et_service_url, et_ftp_url;
    AppCompatButton btn_back_ip, btn_save;
    RadioGroup rg_live_test;
    RadioButton rbtn_live, rbtn_test;

    SharedPreferences preference;
    SharedPreferences.Editor editor;

    String SERVICE_URL_PREF = "", FTP_URL_PREF = "", SERVICE_TYPE_PREf = "";

    private String local_network_url = "http://192.168.11.97:8080/TSeTicketMobile";
    private String live_service_url = "https://www.echallan.org/TSeTicketMobile";

    public static String ftp_fix = "125.16.1.69";
    public static String open_ftp_fix = "125.16.1.69";
    String service_type = "";

    @SuppressLint("WorldReadableFiles")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ip_settings);

        loadUiComponents();

        preference = getSharedPreferences("preferences", MODE_PRIVATE);
        SERVICE_TYPE_PREf = preference.getString("servicetype", "test");
        SERVICE_URL_PREF = preference.getString("serviceurl", "url1");
        FTP_URL_PREF = preference.getString("ftpurl", "url2");
        if (SERVICE_TYPE_PREf.equals("live")) {
            rbtn_live.setChecked(true);
            et_service_url.setText(live_service_url);
            et_ftp_url.setText("");
            et_ftp_url.setText(open_ftp_fix);
            service_type = "live";
        } else if (SERVICE_TYPE_PREf.equals("test")) {
            et_service_url.setText(local_network_url);
            et_ftp_url.setText("");
            et_ftp_url.setText(ftp_fix);
            rbtn_test.setChecked(true);
            service_type = "test";
        }
    }

    private void loadUiComponents() {
        rg_live_test =  findViewById(R.id.radioGroup_live_test);
        rbtn_live =  findViewById(R.id.radioButton_live);
        rbtn_test =  findViewById(R.id.radioButton_test);
        rbtn_test.setChecked(true);

        et_service_url =  findViewById(R.id.edt_service_ipsettings_xml);
        et_ftp_url =  findViewById(R.id.edt_ftpurl_xml);
        btn_save =  findViewById(R.id.btnsubmit_ipsettings_xml);
        btn_back_ip =  findViewById(R.id.btnback_ipsettings_xml);

        btn_save.setOnClickListener(this);
        btn_back_ip.setOnClickListener(this);

        rg_live_test.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton_live:
                        service_type = "live";
                        et_service_url.setText(live_service_url);
                        et_ftp_url.setText(open_ftp_fix);
                        break;
                    case R.id.radioButton_test:
                        service_type = "test";
                        et_service_url.setText(local_network_url);
                        et_ftp_url.setText(ftp_fix);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @SuppressLint("WorldReadableFiles")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnsubmit_ipsettings_xml:
                if (et_service_url.getText().toString().trim().equals("")) {
                    et_service_url.setError("Enter Service URL");
                } else if (et_ftp_url.getText().toString().trim().equals("")) {
                    et_ftp_url.setError( "Enter FTP URL");
                } else {
                    preference = getSharedPreferences("preferences", MODE_PRIVATE);
                    editor = preference.edit();
                    if (preference.contains("serviceurl")) {
                        editor.remove("serviceurl");
                        editor.apply();
                    }
                    if (preference.contains("ftpurl")) {
                        editor.remove("ftpurl");
                        editor.apply();
                    }
                    if (preference.contains("servicetype")) {
                        editor.remove("servicetype");
                        editor.apply();
                    }
                    editor.putString("serviceurl", "" + et_service_url.getText().toString().trim());
                    editor.putString("ftpurl", "" + et_ftp_url.getText().toString().trim());
                    editor.putString("servicetype", "" + service_type);
                    editor.apply();
                    toast("Setting saved successfully !");
                    finish();
                }
                break;
            case R.id.btnback_ipsettings_xml:
                startActivity(new Intent(this, MainActivity.class));
                break;
            default:
                break;
        }
    }

    public void toast(String msg) {
        Toast.makeText(this, "" + msg, Toast.LENGTH_LONG).show();
    }
}


