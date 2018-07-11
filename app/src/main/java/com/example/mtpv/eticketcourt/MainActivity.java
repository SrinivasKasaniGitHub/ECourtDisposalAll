package com.example.mtpv.eticketcourt;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.mtpv.eticketcourt.service.DBHelper;
import com.example.mtpv.eticketcourt.service.ServiceHelper;
import com.example.mtpv.eticketcourt.service.PidSecEncrypt;
import com.wang.avi.AVLoadingIndicatorView;

public class MainActivity extends Activity implements LocationListener {

    AppCompatImageView ip_Settings;
    AppCompatTextView compny_Name;
    AppCompatButton btn_login;
    final int SPLASH_DIALOG = 0;
    AppCompatEditText et_pid;
    AppCompatEditText et_pid_pwd;
    AVLoadingIndicatorView progIndicator;
    DBHelper db;
    final int PROGRESS_DIALOG = 1;
    public static String[] arr_logindetails;
    LocationManager m_locationlistner;
    android.location.Location location;
    public static double latitude = 0.0;
    public static double longitude = 0.0;
    public static String IMEI = "";
    public static String user_id = "";
    public static String user_pwd = "";
    public static String e_user_id = null, sim_No = null;
    public static String e_user_tmp = "";
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60;

    SharedPreferences preference;
    SharedPreferences.Editor editor;
    String services_url = "";
    String ftps_url = "";
    public static String URL = "";
    public String url_to_fix = "/services/MobileEticketServiceImpl?wsdl";
    public static String appVersion;
    private static final int REQUEST_PERMISSIONS = 20;
    public SparseIntArray mErrorString;
    private static final String[] requiredPermissions = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INSTALL_SHORTCUT,
            Manifest.permission.CAMERA
    };

    TextView textView2;

    public static String psName, cadreName, pidName, unitCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadUiComponents();

        mErrorString = new SparseIntArray();

        if (Build.VERSION.SDK_INT > 22 && !hasPermissions(requiredPermissions)) {
            MainActivity.this.requestAppPermissions(new
                            String[]{
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.INTERNET,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.INSTALL_SHORTCUT, Manifest.permission.CAMERA}, R.string.permissions
                    , REQUEST_PERMISSIONS);
        }

        appVersion = getResources().getString(R.string.app_version);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    userLogin();
                } else {
                    toast("Please check your Internet Connection!");
                }
            }
        });

        ip_Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_ipSettings = new Intent(getApplicationContext(), IPSettings.class);
                startActivity(intent_ipSettings);
            }
        });

    }

    private void loadUiComponents() {
        textView2 = (TextView) findViewById(R.id.textView2);
        compny_Name = (AppCompatTextView) findViewById(R.id.CompanyName);
        ip_Settings = (AppCompatImageView) findViewById(R.id.ip_settings);
        btn_login = (AppCompatButton) findViewById(R.id.btnlog);
        et_pid = (AppCompatEditText) findViewById(R.id.edtTxt_pid);
        et_pid_pwd = (AppCompatEditText) findViewById(R.id.edtTxt_pwd);
        progIndicator = (AVLoadingIndicatorView) findViewById(R.id.progIndicator);
        Animation marquee = AnimationUtils.loadAnimation(this, R.anim.marquee);
        compny_Name.startAnimation(marquee);
    }

    public void startAnim() {
        progIndicator.smoothToShow();
    }

    public void stopAnim() {
        progIndicator.smoothToHide();
    }

    public void requestAppPermissions(final String[] requestedPermissions,
                                      final int stringId, final int requestCode) {
        mErrorString.put(requestCode, stringId);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale) {
                Snackbar.make(findViewById(android.R.id.content), stringId,
                        Snackbar.LENGTH_INDEFINITE).setAction("GRANT",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(MainActivity.this, requestedPermissions, requestCode);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
            }
        } else {
            onPermissionsGranted(requestCode);
        }
    }

    public void onPermissionsGranted(final int requestCode) {
        //Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();
    }

    public boolean hasPermissions(String... permissions) {
        for (String permission : permissions)
            if (PackageManager.PERMISSION_GRANTED != checkCallingOrSelfPermission(permission)) {
                return false;
            }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            onPermissionsGranted(requestCode);
        } else {
            Snackbar.make(findViewById(android.R.id.content), mErrorString.get(requestCode),
                    Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            startActivity(intent);
                        }
                    }).show();
        }
    }

    public void userLogin() {
        String pidcode = et_pid.getText().toString();
        String password = et_pid_pwd.getText().toString();

        ContentValues values = new ContentValues();
        values.put("PIDCODE", pidcode);
        values.put("PASSWORD", password);
        values.put("PIDNAME", pidcode);
        values.put("PS_CODE", "");
        values.put("PS_NAME", "");
        values.put("CADRE_CODE", "");
        values.put("CADRE_NAME", "");
        values.put("UNIT_CODE", "");
        values.put("UNIT_NAME", "");
        SQLiteDatabase db = openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
        db.execSQL(DBHelper.CREATE_USER_TABLE);
        db.execSQL("delete from " + DBHelper.USER_TABLE);
        db.insert(DBHelper.USER_TABLE, null, values);

        System.out.println("*********************OFFICER TABLE Insertion Successfully **********************");

        if (et_pid.getText().toString().trim().equals("")) {
            et_pid.setError("Enter PID");
            et_pid.requestFocus();
        } else if (et_pid_pwd.getText().toString().trim().equals("")) {
            et_pid_pwd.setError("Enter password");
            et_pid_pwd.requestFocus();
        } else {
            if ((!services_url.equals("url1")) && (!ftps_url.equals("url2"))) {
                if (isOnline()) {
                    user_id = "" + et_pid.getText().toString().trim();
                    user_pwd = "" + et_pid_pwd.getText().toString().trim();

                    getLocation();

                    try {
                        e_user_id = PidSecEncrypt.encryptmd5(user_id);
                        e_user_tmp = PidSecEncrypt.encryptmd5(user_pwd);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    toast("Please check your network connection !");
                }

                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                assert locationManager != null;
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    new Async_task_login().execute();
                } else {
                    //showGPSDisabledAlertToUser();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("GPS is Disabled in your Device\nKindly Enable it !")
                            .setCancelable(false)
                            .setPositiveButton("Goto Settings", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent callGPSSettingIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(callGPSSettingIntent);
                                }
                            });
                    alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = alertDialogBuilder.create();
                    alert.show();
                }
            }
        }
    }

    public Boolean isOnline() {
        ConnectivityManager conManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert conManager != null;
        NetworkInfo nwInfo = conManager.getActiveNetworkInfo();
        return nwInfo != null && nwInfo.isConnected();
    }

    @SuppressLint("StaticFieldLeak")
    private class Async_task_login extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String[] version_split = appVersion.split("-");
            ServiceHelper.authenticateLogin("" + user_id, "" + e_user_tmp, "" + IMEI,
                    "" + sim_No, "" + latitude, "" + longitude,
                    "" + version_split[1], "eCourt");
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            startAnim();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            stopAnim();

            if (ServiceHelper.Opdata_Chalana != null) {
                MainActivity.arr_logindetails = ServiceHelper.Opdata_Chalana.split(":");
                switch (ServiceHelper.Opdata_Chalana.trim()) {
                    case "1":
                        toast("Invalid Login ID");
                        break;
                    case "2":
                        toast("Invalid password");
                        break;
                    case "3":
                        toast("Unauthorized device");
                        break;
                    case "4":
                        toast("Error, Please contact e-Challan team at 040-27852721");
                        break;
                    case "5":
                        toast("You have exceeded number of attempts with wrong password,\n Please contact e-Challan team at 040-27852721 ");
                        break;
                    case "0":
                        toast("Please contact e-Challan team at 040-27852721");
                        break;
                    default:
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editors = sharedPreferences.edit();
                        String pidCode = "" + arr_logindetails[0];
                        pidName = "" + arr_logindetails[1];
                        String psCd = "" + arr_logindetails[2];
                        psName = "" + arr_logindetails[3];
                        String cadre_code = "" + arr_logindetails[4];
                        cadreName = "" + arr_logindetails[5];
                        String pass_word = "" + user_pwd;
                        String off_phone_no = "" + arr_logindetails[6];
                        String current_version = "" + arr_logindetails[7];
                        String rta_data_flg = "" + arr_logindetails[8];
                        String dl_data_flg = "" + arr_logindetails[9];
                        String aadhaar_data_flg = "" + arr_logindetails[10];
                        String otp_no_flg = "" + arr_logindetails[11];
                        String cashless_flg = "" + arr_logindetails[12];
                        String mobileNo_flg = "" + arr_logindetails[13];
                        editors.putString("PID_CODE", pidCode);
                        editors.putString("PID_NAME", pidName);
                        editors.putString("PS_CODE", psCd);
                        editors.putString("PS_NAME", psName);
                        editors.putString("CADRE_CODE", cadre_code);
                        editors.putString("CADRE_NAME", cadreName);
                        editors.putString("PASS_WORD", pass_word);
                        editors.putString("OFF_PHONE_NO", off_phone_no);
                        editors.putString("CURRENT_VERSION", current_version);
                        editors.putString("RTA_DATA_FLAG", rta_data_flg);
                        editors.putString("DL_DATA_FLAG", dl_data_flg);
                        editors.putString("AADHAAR_DATA_FLAG", aadhaar_data_flg);
                        editors.putString("OTP_NO_FLAG", otp_no_flg);
                        editors.putString("CASHLESS_FLAG", cashless_flg);
                        editors.putString("MOBILE_NO_FLAG", mobileNo_flg);
                        editors.apply();
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        finish();
                        break;
                }
            } else {
                toast("Login Failed!");
            }
        }
    }

    @SuppressLint("HardwareIds")
    public void getLocation() {

        try {
            m_locationlistner = (LocationManager) this.getSystemService(LOCATION_SERVICE);
            assert m_locationlistner != null;
            isGPSEnabled = m_locationlistner.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = m_locationlistner.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                latitude = 0.0;
                longitude = 0.0;
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        m_locationlistner.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Network", "Network");
                        if (m_locationlistner != null) {
                            location = m_locationlistner.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

                                long time = location.getTime();
                                Date date = new Date(time);

                                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf =
                                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String gps_Date = sdf.format(date);
                                System.out.println(gps_Date);
                                if (gps_Date == null) {
                                    startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                                }
                                Log.i("location_Date", "" + gps_Date);
                            } else {
                                latitude = 0.0;
                                longitude = 0.0;
                            }
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        m_locationlistner.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");

                        if (m_locationlistner != null) {
                            location = m_locationlistner.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

                                long time = location.getTime();
                                Date date = new Date(time);

                                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf =
                                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String gps_Date = sdf.format(date);
                                System.out.println(gps_Date);
                                if (gps_Date == null) {
                                    startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                                }
                                Log.i("gps_Date", "" + gps_Date);
                            } else {
                                latitude = 0.0;
                                longitude = 0.0;
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            //showToast("Please check the GPS Location !");
        }

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = getDeviceID(telephonyManager);
        assert telephonyManager != null;
        if (telephonyManager.getSimState() != TelephonyManager.SIM_STATE_ABSENT) {
            sim_No = "" + telephonyManager.getSimSerialNumber();
            Log.d("", "" + Build.DEVICE);
        } else {
            sim_No = "";
        }
    }

    String getDeviceID(TelephonyManager phonyManager) {
        String id = phonyManager.getDeviceId();
        if (id == null) {
            id = "not available";
        }
        int phoneType = phonyManager.getPhoneType();
        switch (phoneType) {
            case TelephonyManager.PHONE_TYPE_NONE:
                return id;
            case TelephonyManager.PHONE_TYPE_GSM:
                return id;
            case TelephonyManager.PHONE_TYPE_CDMA:
                return id;
            default:
                return "UNKNOWN:ID=" + id;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case SPLASH_DIALOG:
                Dialog dg_splash = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dg_splash.setCancelable(false);
                dg_splash.setContentView(R.layout.splash);
                return dg_splash;
            case PROGRESS_DIALOG:
                ProgressDialog pd = ProgressDialog.show(this, "", "", true);
                pd.setContentView(R.layout.splash);
                pd.setCancelable(false);
                return pd;
            default:
                break;

        }
        return super.onCreateDialog(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        preference = getSharedPreferences("preferences", MODE_PRIVATE);
        services_url = preference.getString("serviceurl", "url1");
        ftps_url = preference.getString("ftpurl", "url2");
        if (!services_url.equals("urls1")) {
            URL = "" + services_url + "" + url_to_fix;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        preference = getSharedPreferences("preferences", MODE_PRIVATE);
        services_url = preference.getString("serviceurl", "url1");
        ftps_url = preference.getString("ftpurl", "url2");
    }

    public void toast(String msg) {
        Toast.makeText(this, "" + msg, Toast.LENGTH_LONG).show();
    }
}
