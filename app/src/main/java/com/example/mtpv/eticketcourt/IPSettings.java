package com.example.mtpv.eticketcourt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class IPSettings extends Activity implements OnClickListener {

    EditText et_service_url, et_ftp_url;
    Button btn_back_ip, btn_save;
    RadioGroup rg_live_test;
    RadioButton rbtn_live, rbtn_test;

    SharedPreferences preference;
    SharedPreferences.Editor editor;

    String SERVICE_URL_PREF = "", FTP_URL_PREF = "", SERVICE_TYPE_PREf = "";
    private String local_network_url = "http://125.16.1.70:8080/TSeTicketMobile";
    private String live_service_url = "https://echallan.tspolice.gov.in/TSeTicketMobile";
    public static String ftp_fix = "125.16.1.69";
    public static String open_ftp_fix = "125.16.1.69";
    String service_type = "";

    @SuppressWarnings("deprecation")
    @SuppressLint({"WorldReadableFiles", "CommitPrefEdits"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ip_settings);

        LoadUIComponents();
        preference = getSharedPreferences("preferences", MODE_PRIVATE);
        editor = preference.edit();
        SERVICE_TYPE_PREf = preference.getString("servicetype", "test");
        SERVICE_URL_PREF = preference.getString("serviceurl", "url1");
        FTP_URL_PREF = preference.getString("ftpurl", "url2");

        rbtn_live = (RadioButton) findViewById(R.id.radioButton_live);
        rbtn_test = (RadioButton) findViewById(R.id.radioButton_test);
        rbtn_test.setChecked(true);

        if (SERVICE_TYPE_PREf.equals("live")) {
            rbtn_live.setChecked(true);
            et_service_url.setText("" + live_service_url);
            et_ftp_url.setText("");
            et_ftp_url.setText(open_ftp_fix);
            service_type = "live";
        } else if (SERVICE_TYPE_PREf.equals("test")) {
            et_service_url.setText("" + local_network_url);
            et_ftp_url.setText("");
            et_ftp_url.setText(ftp_fix);
            rbtn_test.setChecked(true);
            service_type = "test";
        }
    }

    private void LoadUIComponents() {
        // TODO Auto-generated method stub
        et_service_url = (AppCompatEditText) findViewById(R.id.edt_service_ipsettings_xml);
        et_ftp_url = (AppCompatEditText) findViewById(R.id.edt_ftpurl_xml);
        btn_save = (AppCompatButton) findViewById(R.id.btnsubmit_ipsettings_xml);
        btn_back_ip = (AppCompatButton) findViewById(R.id.btnback_ipsettings_xml);
        rg_live_test = (RadioGroup) findViewById(R.id.radioGroup_live_test);
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
                        et_service_url.setEnabled(false);
                        break;

                    case R.id.radioButton_test:
                        service_type = "test";
                        et_service_url.setText(local_network_url);
                        et_ftp_url.setText(ftp_fix);
                        et_service_url.setEnabled(true);
                        break;

                    default:
                        break;
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("WorldReadableFiles")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnsubmit_ipsettings_xml:

                if (et_service_url.getText().toString().trim().equals("")) {
                    showError(et_service_url, "Enter Service URL");
                }
               /* else if (et_ftp_url.getText().toString().trim().equals("")) {
                    showError(et_ftp_url, "Enter FTP URL");
                } */
                else {
                    preference = getSharedPreferences("preferences", MODE_PRIVATE);
                    editor = preference.edit();

                    if (preference.contains("serviceurl")) {
                        editor.remove("serviceurl");
                        editor.commit();
                    }


                    if (preference.contains("ftpurl")) {
                        editor.remove("ftpurl");
                        editor.commit();
                    }

                    if (preference.contains("servicetype")) {
                        editor.remove("servicetype");
                        editor.commit();
                    }

                    editor.putString("serviceurl", "" + et_service_url.getText().toString().trim());
                    editor.putString("ftpurl", "" + et_ftp_url.getText().toString().trim());
                    editor.putString("servicetype", "" + service_type);
                    editor.commit();
                    showToast("Successfully Saved!");
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

    @SuppressWarnings("unused")
    private void clearFields() {
        // TODO Auto-generated method stub
        preference.edit().clear().apply();
        et_service_url.setText("");
        et_ftp_url.setText("");

    }

    private void showToast(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View toastView = toast.getView();
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setPadding(20, 0, 20, 0);
        messageTextView.setTextSize(getResources().getDimension(R.dimen._8sdp));

        toastView.setBackgroundResource(R.drawable.toast_background);
        toast.show();
    }

    private void showError(EditText et, String msg) {
        et.setError("" + msg);
    }

}


