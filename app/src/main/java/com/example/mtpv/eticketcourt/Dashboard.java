package com.example.mtpv.eticketcourt;

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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mtpv.eticketcourt.adapter.ImageAdapter;
import com.example.mtpv.eticketcourt.service.DBHelper;
import com.example.mtpv.eticketcourt.service.ServiceHelper;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Dashboard extends Activity implements View.OnClickListener {

    String server = "192.168.11.9";
    int port = 1305;
    String username = "ftpuser";
    String password = "Dk0r$l1qMp6";
    String filename = "Version-1.5.1.apk";

    private static final int BUFFER_SIZE = 4096;
    ProgressBar progress;
    Dialog dialog;
    int downloadedSize = 0;
    int totalSize = 0;
    TextView cur_val;

    TextView tv_login_username, echallan, echallan_reports;

    TextView tv_settings;
    TextView tv_sync;
    ImageButton ibtn_logout;
    ImageView offline_btn, aadhaar, tv_about_version;

    String netwrk_info_txt = "";
    final int EXIT_DIALOG = 0;
    final int ALERT_USER = 1;
    final int PROGRESS_DIALOG = 2;
    final int MASTER_DOWNLOAD = 3;

    String version = null;

    String address_spot;

    DBHelper db;
    Cursor c, cursor_psnames,  c_whlr_details,  cursor_courtnames, cursor_court_disnames;

    public static String[][] whlr_code_name;
    public static ArrayList<String> whlr_name_arr;

    public static String[][] vchleCat_code_name;
    public static ArrayList<String> vchleCat_name_arr_list;

    public static String[][] vchle_MainCat_code_name;
    public static ArrayList<String> vchle_MainCat_name_arr_list;

    public static String[][] ocuptn_code_name;
    public static ArrayList<String> ocuptn_name_arr_list;

    public static String[][] bar_code_name;
    public static ArrayList<String> bar_name_arr_list;

    public static String[][] qlfctn_code_name;
    public static ArrayList<String> qlfctn_name_arr_list;

    public static String modified_url = "";

    public static String UNIT_CODE = "23";
    public static String UNIT_NAME = "Hyderabad";
    public static String VEH_CAT_FIX = "99";
    public static String VEH_MAINCAT_FIX = "99";
    public static String VEH_SUBCAT_FIX = "9999";

    ArrayList<String> ps_codes_fr_names_arr;
    ArrayList<String> ps_names_arr;
    String[][] psname_name_code_arr;

    public static ArrayList<String> court_codes_fr_names_arr;
    public static ArrayList<String> court_names_arr;
    public static ArrayList<String> court_address_arr;
    String[][] court_name_code_arr;

    public static ArrayList<String> court_dis_codes_fr_names_arr;
    public static ArrayList<String> court_dis_names_arr;
    public static String[][] court_dis_name_code_arr;

    ArrayList<String> vio_points_fr_names_arr;
    ArrayList<String> vio_points_names_arr;
    String[][] vio_points_name_code_arr;

    ArrayList<String> occupation_fr_names_arr;
    ArrayList<String> occupation_names_arr;
    String[][] occupation_code_arr;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    String psname_settings = "";
    String pointnameBycode_settings = "";

    public static String rta_details_request_from = "";
    public static String licence_details_request_from = "";

    String[] wheeler_code_arr_spot;
    Cursor c_whlr;

    public static String check_vhleHistory_or_Spot = "";

    Calendar calendar;
    int present_date;
    int present_month;
    int present_year;
    SimpleDateFormat date_format;
    String present_date_toSend = "";
    TelephonyManager telephonyManager;
    String imei_send = "";
    String simid_send = "";

    public static String terminalID, bluetoothName, bluetoothAddress;

    TextView versin_txt;
    TextView compny_Name;
    static String current_version = "Y";
    String unitCode = "23";

    ImageView imageView1;
    TextView tv_officer_name, tv_officer_cadre_name, tv_officer_ps, tv_officer_pid;

    GridView grid;
    private static String[] module_name = {
            "Court/Counselling\nSMS Intimation",
            "Court-Details\nUpdation",
            "Download\nMasters"
    };
    public static int[] module_image = {
            R.drawable.vhcle_history_m,
            R.drawable.generate_ticket_m,
            R.drawable.sync_m
    };

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        loadUiComponents();

        db = new DBHelper(getApplicationContext());

        unitCode = MainActivity.arr_logindetails[0];
        unitCode = unitCode.substring(0, 1);
        switch (unitCode) {
            case "22":
                imageView1.setImageDrawable(getResources().getDrawable(R.drawable.cyb_logo));
                break;
            case "24":
                imageView1.setImageDrawable(getResources().getDrawable(R.drawable.rac_logo));
                break;
            default:
                imageView1.setImageDrawable(getResources().getDrawable(R.drawable.htp_left));
                break;
        }

        db = new DBHelper(getApplicationContext());
        netwrk_info_txt = "" + getResources().getString(R.string.newtork_txt);

        if (MainActivity.arr_logindetails != null && MainActivity.arr_logindetails.length > 0) {
            tv_login_username.setText("Welcome : " + MainActivity.arr_logindetails[1]);
        }

        grid =  findViewById(R.id.grid_view);
        ImageAdapter adapter = new ImageAdapter(getApplicationContext(), module_name, module_image);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                if (position == 0) {
                    courtCounselling();
                } else if (position == 1) {
                    courtCousellingSMSIntimation();
                } else if (position == 2) {
                    showDialog(ALERT_USER);
                }
            }
        });

        SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        current_version = sharedPreference.getString("CURRENT_VERSION", "");
        if (current_version.equals("N")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
            builder.setTitle("E-Court");
            builder.setIcon(R.drawable.logo_hyd);
            builder.setMessage("Please update your application...!");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    new Async_UpdateApk().execute();
                    current_version = "Y";
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        StrictMode.ThreadPolicy polocy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(polocy);

        ibtn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
                builder.setTitle(R.string.app_name);
                builder.setIcon(R.drawable.logo_hyd);
                builder.setMessage("\nAre you sure,\nDo you want to exit?\n");
                builder.setCancelable(false);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    class Async_UpdateApk extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressLint("SdCardPath")
        @Override
        protected String doInBackground(Void... arg0) {
            FTPClient ftpClient = new FTPClient();
            server = IPSettings.ftp_fix;
            try {
                ftpClient.connect(server, port);
                ftpClient.login(username, password);
                ftpClient.enterLocalPassiveMode();
                ftpClient.setBufferSize(1024 * 1024);
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                // ftp://192.168.11.9:99/23/TabAPK/Version-1.5.1.txt
//                File downloadFile1 = new File("/sdcard/Download/ECourt.apk");
                File downloadFile1 = new File("/mnt/sdcard/Download/ECourt.apk");

                // String remoteFile1 = "/23/TabAPK" + "/" + version;
                String remoteFile1 = "Evidences/23/TabAPK" + "/ECourt.apk";
                //String remoteFile1 = "Evidences/"+unitCode+"/TabAPK" + "/ECourt.apk";

                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile1));
                boolean success = ftpClient.retrieveFile(remoteFile1, outputStream);

                FileOutputStream fileOutput = new FileOutputStream(downloadFile1);
                InputStream inputStream = ftpClient.retrieveFileStream(remoteFile1);
                if (inputStream == null || ftpClient.getReplyCode() == 550) {
                    // it means that file doesn't exist.
                    fileOutput.close();
                    outputStream.close();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this,
                                    AlertDialog.THEME_HOLO_LIGHT);
                            builder.setTitle("E-Court");
                            builder.setIcon(R.drawable.logo_hyd);
                            builder.setMessage("Your application is up to date,\nNo need to update");
                            builder.setCancelable(false);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });
                } else {
                    try {
                        Log.i("SUCCess LOG 1::::::::", "***********ENTERED*******");
                        SQLiteDatabase db2 = openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
                        db2.execSQL("DROP TABLE IF EXISTS " + DBHelper.psName_table);
                        db2.execSQL("DROP TABLE IF EXISTS " + DBHelper.wheelercode_table);
                        db2.execSQL(DBHelper.psNamesCreation);
                        db2.execSQL(DBHelper.wheelerCodeCreation);
                        db2.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("CATCH BLOG 1::::::::", "***********ENTERED*******");
                    }
                    totalSize = remoteFile1.length();

                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            showProgress(server);
                            progress.setMax(totalSize);
                        }
                    });

                    // create a buffer...
                    byte[] buffer = new byte[1024];
                    int bufferLength = 0;

                    while ((bufferLength = inputStream.read(buffer)) > 0) {
                        fileOutput.write(buffer, 0, bufferLength);
                        downloadedSize += bufferLength;

                        runOnUiThread(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            public void run() {
                                progress.setProgress(downloadedSize);
                                float per = ((float) downloadedSize / totalSize) * 100;
                                cur_val.setText((int) per / 170000 + "%");
                            }
                        });
                    }

                    fileOutput.close();
                    outputStream.close();

                    if (success) {
                        ftpClient.logout();
                        ftpClient.disconnect();

                        try {
                            Log.i("SUCCess LOG ::::::::", "***********ENTERED*******");
                            db.open();
                            db.execSQL("delete from " + DBHelper.psName_table);
                            db.execSQL("delete from " + DBHelper.wheelercode_table);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("CATCH BLOG ::::::::", "***********ENTERED*******");
                            db.close();
                        }

                        finish();

                       /* Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(
                                Uri.fromFile(new File(
                                        Environment.getExternalStorageDirectory() + "/download/" + "ECourt.apk")),
                                "application/vnd.android.package-archive");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);*/

                        if (Build.VERSION.SDK_INT <= 23) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(new File("/mnt/sdcard/Download/ECourt.apk")),
                                    "application/vnd.android.package-archive");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            Uri apkUri = FileProvider.getUriForFile(Dashboard.this,
                                    BuildConfig.APPLICATION_ID + ".provider", new File("/mnt/sdcard/Download/ECourt.apk"));
                            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                            intent.setData(apkUri);
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(intent);
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // removeDialog(PROGRESS_DIALOG);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

    @SuppressLint("SetTextI18n")
    private void showProgress(String server) {
        dialog = new Dialog(Dashboard.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.myprogressdialog);
        dialog.setTitle("Download Progress");
        dialog.setCancelable(false);

        TextView text = dialog.findViewById(R.id.tv1);
        text.setText("Downloading file ... ");
        cur_val = dialog.findViewById(R.id.cur_pg_tv);
        cur_val.setText("It may Take Few Minutes.....");
        dialog.show();

        progress = dialog.findViewById(R.id.progress_bar);
        progress.setProgress(0);// initially progress is 0
        progress.setMax(100);
        progress.setIndeterminate(true);
        progress.setProgressDrawable(getResources().getDrawable(R.drawable.green_progress));
    }

    @SuppressLint("SetTextI18n")
    private void loadUiComponents() {
        versin_txt = findViewById(R.id.textView2);
        tv_officer_name = findViewById(R.id.officer_Name);
        tv_officer_name.setText(MainActivity.pidName + "(" + MainActivity.cadreName + ")");
        //tv_officer_cadre_name = findViewById(R.id.officer_cadre);
        //tv_officer_cadre_name.setText(MainActivity.cadreName);
        tv_officer_ps = findViewById(R.id.officer_PS);
        tv_officer_ps.setText(MainActivity.psName);
        //tv_officer_pid = findViewById(R.id.tv_officer_pid);
        //tv_officer_pid.setText(MainActivity.user_id);
        compny_Name = findViewById(R.id.CompanyName);
        Animation marquee = AnimationUtils.loadAnimation(this, R.anim.marquee);
        compny_Name.startAnimation(marquee);

        tv_login_username = findViewById(R.id.tv_login_username_dashboard_xml);
        tv_settings = findViewById(R.id.tv_settings_dashboard_xml);
        tv_sync = findViewById(R.id.tv_sync_dashboard_xml);

        aadhaar = findViewById(R.id.aadhaar);
        echallan = findViewById(R.id.echallan);
        echallan_reports = findViewById(R.id.echallan_reports);
        ibtn_logout = findViewById(R.id.imgbtn_logout_dashboard_xml);

        imageView1 = findViewById(R.id.img_logo);

        ibtn_logout = (ImageButton) findViewById(R.id.imgbtn_logout_dashboard_xml);
    }

    public Boolean isOnline() {
        ConnectivityManager conManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert conManager != null;
        NetworkInfo nwInfo = conManager.getActiveNetworkInfo();
        return nwInfo != null;
    }

    @SuppressWarnings({"static-access", "deprecation"})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.tv_generateticket_dashboard_xml:
                check_vhleHistory_or_Spot = "drunkdrive";
                getPreferenceValues();
                preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                editor = preferences.edit();
                address_spot = preferences.getString("btaddress", "btaddr");

                try {
                    db.open();
                    cursor_psnames = DBHelper.db.rawQuery("select * from " + db.psName_table, null);
                    cursor_courtnames = DBHelper.db.rawQuery("select * from " + db.courtName_table, null);
                    cursor_court_disnames = DBHelper.db.rawQuery("select * from " + db.court_disName_table, null);
                    c_whlr_details = DBHelper.db.rawQuery("select * from " + DBHelper.wheelercode_table, null);

                    if ((cursor_psnames.getCount() == 0) && (cursor_courtnames.getCount() == 0) && (cursor_court_disnames.getCount() == 0) && (c_whlr_details.getCount() == 0)) {
                        showToast("Please download master's !");
                    }

                   *//* else if ((psname_settings.equals("psname")) && (pointnameBycode_settings.equals("pointname"))) {
                        showToast("Configure Settings!");
                    }*//*
//                    else if (address_spot.trim() != null && address_spot.trim().length() < 15) {
//                        showToast("Configure BlueTooth Settings!");
//                    }
                    else {
                        if (isOnline()) {
                            startActivity(new Intent(Dashboard.this, DDCloseActiviy.class));
                        } else {
                            showToast("" + netwrk_info_txt);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    c_whlr_details.close();
                    cursor_psnames.close();
                    db.close();
                }
                c_whlr_details.close();
                cursor_psnames.close();
                db.close();
                break;

            case R.id.tv_pending_booked_dashboard_xml:
                check_vhleHistory_or_Spot = "drunkdrive";
                getPreferenceValues();
                preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                editor = preferences.edit();
                address_spot = preferences.getString("btaddress", "btaddr");

                try {
                    db.open();
                    cursor_psnames = DBHelper.db.rawQuery("select * from " + db.psName_table, null);
                    cursor_courtnames = DBHelper.db.rawQuery("select * from " + db.courtName_table, null);
                    cursor_court_disnames = DBHelper.db.rawQuery("select * from " + db.court_disName_table, null);

                    *//* TO GET WHEELER LENGTH *//*
                    c_whlr_details = DBHelper.db.rawQuery("select * from " + DBHelper.wheelercode_table, null);

                    if ((cursor_psnames.getCount() == 0) && (cursor_courtnames.getCount() == 0) && (cursor_court_disnames.getCount() == 0) && (c_whlr_details.getCount() == 0)) {
                        showToast("Please download master's !");
                    }
                   *//* else if ((psname_settings.equals("psname")) && (pointnameBycode_settings.equals("pointname"))) {
                        showToast("Configure Settings!");
                    }
                    else if (address_spot.trim() != null && address_spot.trim().length() < 15) {
                        showToast("Configure BlueTooth Settings!");
                    } *//*
                    else {
                        if (isOnline()) {
                            startActivity(new Intent(Dashboard.this, CourtCaseStatusActivity.class));
                        } else {
                            showToast("" + netwrk_info_txt);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    c_whlr_details.close();
                    cursor_psnames.close();
                    db.close();
                }
                c_whlr_details.close();
                cursor_psnames.close();
                db.close();
                break;*/

            case R.id.tv_settings_dashboard_xml:
                try {
                    db.open();
                    cursor_psnames = DBHelper.db.rawQuery("select * from " + db.psName_table, null);
                    if (cursor_psnames.getCount() == 0) {
                        showToast("Please download master's !");
                    } else {
                        startActivity(new Intent(this, Settings.class));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                cursor_psnames.close();
                db.close();
                break;

            case R.id.imgbtn_logout_dashboard_xml:
                showDialog(EXIT_DIALOG);
                break;

            case R.id.tv_sync_dashboard_xml:
                showDialog(ALERT_USER);
                break;

           /* case R.id.tvduplicateprint_dashboad_xml:
                check_vhleHistory_or_Spot = "";

                getPreferenceValues();

                preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                editor = preferences.edit();
                address_spot = preferences.getString("btaddress", "btaddr");

                try {
                    db.open();
                    cursor_psnames = DBHelper.db.rawQuery("select * from " + db.psName_table, null);

                    cursor_courtnames = DBHelper.db.rawQuery("select * from " + db.courtName_table, null);

                    cursor_court_disnames = DBHelper.db.rawQuery("select * from " + db.court_disName_table, null);

                    *//* TO GET WHEELER LENGTH *//*
                    c_whlr_details = DBHelper.db.rawQuery("select * from " + DBHelper.wheelercode_table, null);

                    if ((cursor_psnames.getCount() == 0) && (cursor_courtnames.getCount() == 0) && (cursor_court_disnames.getCount() == 0) && (c_whlr_details.getCount() == 0)) {
                        showToast("Please download master's !");
                    } else if ((psname_settings.equals("psname")) && (pointnameBycode_settings.equals("pointname"))) {
                        showToast("Configure Settings!");
                    } else if (address_spot.trim() != null && address_spot.trim().length() < 15) {
                        showToast("Configure BlueTooth Settings!");
                    } else {
                        if (isOnline()) {
                            startActivity(new Intent(Dashboard.this, DuplicatePrint.class));
                        } else {
                            showToast("" + netwrk_info_txt);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    c_whlr_details.close();
                    cursor_psnames.close();
                    db.close();
                }
                c_whlr_details.close();
                cursor_psnames.close();
                db.close();
                break;

            case R.id.tv_spot_dashboard_xml:
                rtaFLG = true;
                check_vhleHistory_or_Spot = "spot";
                getPreferenceValues();

                preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                editor = preferences.edit();
                address_spot = preferences.getString("btaddress", "btaddr");

                try {
                    db.open();
                    cursor_psnames = DBHelper.db.rawQuery("select * from " + db.psName_table, null);

                    *//* TO GET WHEELER LENGTH *//*
                    c_whlr_details = DBHelper.db.rawQuery("select * from " + DBHelper.wheelercode_table, null);

                    if ((cursor_psnames.getCount() == 0) && (c_whlr_details.getCount() == 0)) {
                        showToast("Please download master's !");
                    } else if ((psname_settings.equals("psname")) && (pointnameBycode_settings.equals("pointname"))) {
                        showToast("Configure Settings!");
                    } else if (address_spot.trim() != null && address_spot.trim().length() < 15) {
                        showToast("Configure BlueTooth Settings!");
                    } else {
                        if (isOnline()) {
                            startActivity(new Intent(Dashboard.this, SpotChallan.class));
                        } else {
                            showToast("" + netwrk_info_txt);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    c_whlr_details.close();
                    cursor_psnames.close();
                    db.close();
                }
                c_whlr_details.close();
                cursor_psnames.close();
                db.close();
                // ShowToast.showNormalToast(Dashboard.this, "" +
                // under_process_msg);
                break;*/

            case R.id.tv_vhclehistory_dashboard_xml:
                check_vhleHistory_or_Spot = "vehiclehistory";
                getPreferenceValues();

                preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                editor = preferences.edit();
                address_spot = preferences.getString("btaddress", "btaddr");

                try {
                    db.open();
                    cursor_psnames = DBHelper.db.rawQuery("select * from " + db.psName_table, null);

                    /* TO GET WHEELER LENGTH */
                    c_whlr_details = DBHelper.db.rawQuery("select * from " + DBHelper.wheelercode_table, null);
                    // DOWNLOAD MASTERS ALERT
                    if ((cursor_psnames.getCount() == 0) && (c_whlr_details.getCount() == 0)) {
                        showToast("Please download master's !");
                    } else if ((psname_settings.equals("psname")) && (pointnameBycode_settings.equals("pointname"))) {
                        showToast("Configure Settings!");
                    } else if (address_spot.trim() != null && address_spot.trim().length() < 15) {
                        showToast("Configure BlueTooth Settings!");
                    } else {
                        if (isOnline()) {
                            startActivity(new Intent(Dashboard.this, ChargeSheetClose.class));
                        } else {
                            showToast("" + netwrk_info_txt);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    c_whlr_details.close();
                    cursor_psnames.close();
                    db.close();
                }
                c_whlr_details.close();
                cursor_psnames.close();
                db.close();

                // ShowToast.showNormalToast(Dashboard.this, "" +
                // under_process_msg);
                break;

           /* case R.id.tv_towing_dashboard_xml:
                check_vhleHistory_or_Spot = "towing";
                getPreferenceValues();

                preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                editor = preferences.edit();
                address_spot = preferences.getString("btaddress", "btaddr");

                try {
                    db.open();
                    cursor_psnames = DBHelper.db.rawQuery("select * from " + db.psName_table, null);

                    *//* TO GET WHEELER LENGTH *//*
                    c_whlr_details = DBHelper.db.rawQuery("select * from " + DBHelper.wheelercode_table, null);

                    if ((cursor_psnames.getCount() == 0) && (c_whlr_details.getCount() == 0)) {
                        showToast("Please download master's !");
                    } else if ((psname_settings.equals("psname")) && (pointnameBycode_settings.equals("pointname"))) {
                        showToast("Configure Settings!");
                    } else if (address_spot.trim() != null && address_spot.trim().length() < 15) {
                        showToast("Configure BlueTooth Settings!");
                    } else {
                        if (isOnline()) {
                            startActivity(new Intent(Dashboard.this, SpotChallan.class));
                        } else {
                            showToast("" + netwrk_info_txt);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    c_whlr_details.close();
                    cursor_psnames.close();
                    db.close();
                }
                c_whlr_details.close();
                cursor_psnames.close();
                db.close();

                break;

            case R.id.tv_releasedocuments_dashboard_xml:
                check_vhleHistory_or_Spot = "releasedocuments";

                getPreferenceValues();

                preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                editor = preferences.edit();
                address_spot = preferences.getString("btaddress", "btaddr");

                try {
                    db.open();
                    cursor_psnames = DBHelper.db.rawQuery("select * from " + db.psName_table, null);

                    *//* TO GET WHEELER LENGTH *//*
                    c_whlr_details = DBHelper.db.rawQuery("select * from " + DBHelper.wheelercode_table, null);

                    if ((cursor_psnames.getCount() == 0) && (c_whlr_details.getCount() == 0)) {
                        showToast("Please download master's !");
                    } else if ((psname_settings.equals("psname")) && (pointnameBycode_settings.equals("pointname"))) {
                        showToast("Configure Settings!");
                    } else if (address_spot.trim() != null && address_spot.trim().length() < 15) {
                        showToast("Configure BlueTooth Settings!");
                    } else {
                        if (isOnline()) {
                            startActivity(new Intent(Dashboard.this, SpotChallan.class));
                        } else {
                            showToast("" + netwrk_info_txt);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    c_whlr_details.close();
                    cursor_psnames.close();
                    db.close();
                }
                c_whlr_details.close();
                cursor_psnames.close();
                db.close();

                break;

            case R.id.tv_reports_dashboard_xml:
                check_vhleHistory_or_Spot = "reports";

                getPreferenceValues();

                preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                editor = preferences.edit();
                address_spot = preferences.getString("btaddress", "btaddr");

                try {
                    db.open();
                    cursor_psnames = DBHelper.db.rawQuery("select * from " + db.psName_table, null);

                    *//* TO GET WHEELER LENGTH *//*
                    c_whlr_details = DBHelper.db.rawQuery("select * from " + DBHelper.wheelercode_table, null);

                    if ((cursor_psnames.getCount() == 0) && (c_whlr_details.getCount() == 0)) {
                        showToast("Please download master's !");
                    } else if ((psname_settings.equals("psname")) && (pointnameBycode_settings.equals("pointname"))) {
                        showToast("Configure Settings!");
                    } else if (address_spot.trim() != null && address_spot.trim().length() < 15) {
                        showToast("Configure BlueTooth Settings!");
                    } else {
                        if (isOnline()) {
                            startActivity(new Intent(Dashboard.this, DuplicatePrint.class));
                        } else {
                            showToast("" + netwrk_info_txt);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    c_whlr_details.close();
                    cursor_psnames.close();
                    db.close();
                }
                c_whlr_details.close();
                cursor_psnames.close();
                db.close();

                break;

            case R.id.tv_about_version:
                *//*
             * check_vhleHistory_or_Spot = "specialdrive"; startActivity(new
             * Intent(Dashboard.this, SpotChallan.class));
             *//*
                new Async_About_Version().execute();

                break;*/

            case R.id.echallan:
                check_vhleHistory_or_Spot = "echallan";
                startActivity(new Intent(Dashboard.this, E_Challan.class));
                break;

            case R.id.echallan_reports:
                check_vhleHistory_or_Spot = "echallanreports";
                startActivity(new Intent(Dashboard.this, E_Challan_Reports.class));
                break;

            case R.id.aadhaar:
                startActivity(new Intent(Dashboard.this, AadhaarUpdate.class));
                break;
            default:
                break;
        }
    }

    public class Async_About_Version extends AsyncTask<Void, Void, String> {

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected String doInBackground(Void... params) {
            ServiceHelper.getversiondetails(UNIT_CODE, "TAB", version);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // removeDialog(PROGRESS_DIALOG);
            if (!ServiceHelper.version_response.equals("NA")) {
                Intent i = new Intent(getApplicationContext(), ReleaseversionActivity.class);
                startActivity(i);
            } else {
                showToast("No Updates Found !!!");
            }

        }
    }

    @SuppressLint("MissingPermission")
    @SuppressWarnings("unused")
    private void getSimImeiNo() {
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei_send = telephonyManager.getDeviceId();
        // TO GET IMEI NUMBER
        if (telephonyManager.getSimState() != TelephonyManager.SIM_STATE_ABSENT) {
            simid_send = "" + telephonyManager.getSimSerialNumber();
        } else {
            simid_send = "";
        }
    }

    /* TO GET SHARED PREFERENCES VALUES FROM SETTINGS */
    @SuppressWarnings("deprecation")
    @SuppressLint("WorldReadableFiles")
    private void getPreferenceValues() {
        // TODO Auto-generated method stub
        preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        editor = preferences.edit();
        psname_settings = preferences.getString("psname_name", "psname");
        pointnameBycode_settings = preferences.getString("point_name", "pointname");
    }

    private ProgressDialog progressDialog;

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case EXIT_DIALOG:
                TextView title = new TextView(this);
                title.setText("Hyderabad E-Ticket");
                title.setBackgroundColor(Color.BLUE);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.WHITE);
                title.setTextSize(26);
                title.setTypeface(title.getTypeface(), Typeface.BOLD);
                title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
                title.setPadding(20, 0, 20, 0);
                title.setHeight(70);

                String otp_message = "\n Are You Sure,\nDo You Want To Exit? \n";

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Dashboard.this,
                        AlertDialog.THEME_HOLO_LIGHT);
                alertDialogBuilder.setCustomTitle(title);
                alertDialogBuilder.setIcon(R.drawable.dialog_logo);
                alertDialogBuilder.setMessage(otp_message);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        removeDialog(EXIT_DIALOG);

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                alertDialog.getWindow().getAttributes();

                TextView textView = alertDialog.findViewById(android.R.id.message);
                textView.setTextSize(28);
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                textView.setGravity(Gravity.CENTER);

                Button btn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                btn.setTextSize(22);
                btn.setTextColor(Color.WHITE);
                btn.setTypeface(btn.getTypeface(), Typeface.BOLD);
                btn.setBackgroundColor(Color.BLUE);

                Button btn2 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                btn2.setTextSize(22);
                btn2.setTextColor(Color.WHITE);
                btn2.setTypeface(btn2.getTypeface(), Typeface.BOLD);
                btn2.setBackgroundColor(Color.BLUE);
                return alertDialog;

            case ALERT_USER:
                AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
                builder.setTitle("Download Masters");
                builder.setIcon(R.drawable.logo_hyd);
                builder.setMessage("Please check your network connection properly. This process will take several minutes.");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isOnline()) {
                            try {
                                db.open();
                                DBHelper.Delete_ReadingData("" + DBHelper.wheelercode_table);
                                DBHelper.Delete_ReadingData("" + DBHelper.psName_table);
                                DBHelper.Delete_ReadingData("" + DBHelper.courtName_table);
                                DBHelper.Delete_ReadingData("" + DBHelper.court_disName_table);
                                DBHelper.Delete_ReadingData("" + DBHelper.occupation_table);
                            } catch (SQLException e) {
                                e.printStackTrace();
                                db.close();
                            }
                            db.close();

                            Async_wheler_details wheeler_task = new Async_wheler_details();
                            wheeler_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                            Async_getPsDetails ps_task = new Async_getPsDetails();
                            ps_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                            Async_getCourtDetails court_task = new Async_getCourtDetails();
                            court_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                            Async_getCourtDisDetails court_dis_task = new Async_getCourtDisDetails();
                            court_dis_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                            Async_ocuptn ocptn_task = new Async_ocuptn();
                            ocptn_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                            Async_qlfctn qlfcn_task = new Async_qlfctn();
                            qlfcn_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                            Async_BarDetails bar_task = new Async_BarDetails();
                            bar_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                            Async_getViolationPoint_SystemMasterData violation_points_task = new Async_getViolationPoint_SystemMasterData();
                            violation_points_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                            Async_GetTerminalDetails terminal_details_task = new Async_GetTerminalDetails();
                            terminal_details_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showToast("Successfully downloaded master's !");
                                }
                            }, 1000);

                        } else {
                            showToast("" + netwrk_info_txt);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert_Dialog = builder.create();
                alert_Dialog.show();
                return alert_Dialog;

            case PROGRESS_DIALOG:
                progressDialog = new ProgressDialog(Dashboard.this);
                progressDialog.setTitle(R.string.app_name);
                progressDialog.setIcon(R.drawable.logo_hyd);
                progressDialog.setMessage("Please wait...");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.show();
                return progressDialog;

            default:
                break;
        }
        return super.onCreateDialog(id);
    }

    public class Async_getViolationPoint_SystemMasterData extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            ServiceHelper.getViolationPoint_SystemMasterData();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressWarnings({"static-access"})
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            new Async_GetTerminalDetails().execute();

            if (ServiceHelper.violation_points_masters.length > 0) {
                vio_points_name_code_arr = new String[ServiceHelper.violation_points_masters.length][2];
                for (int i = 1; i < ServiceHelper.violation_points_masters.length; i++) {
                    vio_points_name_code_arr[i] = ServiceHelper.violation_points_masters[i].split("\\|");
                }

                try {
                    db.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                SQLiteDatabase db2 = openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
                db2.execSQL("DROP TABLE IF EXISTS " + DBHelper.violationPointsTable);
                db2.execSQL(DBHelper.violatioPointsDetaisCreation);

                vio_points_fr_names_arr = new ArrayList<String>();
                vio_points_names_arr = new ArrayList<String>();
                vio_points_fr_names_arr.clear();
                vio_points_names_arr.clear();

                for (int j = 1; j < vio_points_name_code_arr.length; j++) {
                    vio_points_fr_names_arr.add(vio_points_name_code_arr[j][0]);
                    vio_points_names_arr.add(vio_points_name_code_arr[j][1]);
                    db.insertViolationPointDetails("" + vio_points_name_code_arr[j][0],
                            "" + vio_points_name_code_arr[j][1],
                            "" + vio_points_name_code_arr[j][2]);
                }
                db.close();
                db2.close();
            }
        }
    }

    public class Async_wheler_details extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            ServiceHelper.getWheeler();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressWarnings("static-access")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (ServiceHelper.Opdata_Chalana != null) {

                if ((!ServiceHelper.Opdata_Chalana.equals("0")) && (ServiceHelper.whlr_details_master.length > 0)) {

                    whlr_code_name = new String[ServiceHelper.whlr_details_master.length][2];

                    whlr_name_arr = new ArrayList<String>();
                    whlr_name_arr.clear();

                    try {
                        db.open();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    SQLiteDatabase db2 = openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
                    db2.execSQL("DROP TABLE IF EXISTS " + DBHelper.wheelercode_table);
                    db2.execSQL(DBHelper.wheelerCodeCreation);

                    for (int i = 1; i < ServiceHelper.whlr_details_master.length; i++) {
                        whlr_code_name[i] = (ServiceHelper.whlr_details_master[i].split("@"));
                        whlr_name_arr.add(whlr_code_name[i][1]);
                        db.insertWheelerDetails("" + whlr_code_name[i][0], "" + whlr_code_name[i][1]);
                    }
                    db.close();
                    db2.close();
                    // new Async_getPsDetails().execute();

                } else {
                    showToast("Try again");
                    whlr_code_name = new String[0][0];
                    whlr_name_arr.clear();
                }

            } else {
                showToast("Try again");
                whlr_code_name = new String[0][0];
                whlr_name_arr.clear();
            }
        }
    }

    public class Async_getPsDetails extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            ServiceHelper.getPsNames();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressWarnings("static-access")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (ServiceHelper.Opdata_Chalana != null) {

                if (null != ServiceHelper.psNames_master && ServiceHelper.psNames_master.length > 0) {
                    psname_name_code_arr = new String[ServiceHelper.psNames_master.length][2];
                    for (int i = 1; i < ServiceHelper.psNames_master.length; i++) {
                        psname_name_code_arr[i] = ServiceHelper.psNames_master[i].split("@");
                    }
                    try {
                        db.open();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    SQLiteDatabase db2 = openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
                    db2.execSQL("DROP TABLE IF EXISTS " + DBHelper.psName_table);
                    db2.execSQL(DBHelper.psNamesCreation);

                    ps_codes_fr_names_arr = new ArrayList<String>();
                    ps_names_arr = new ArrayList<String>();
                    ps_codes_fr_names_arr.clear();
                    ps_names_arr.clear();

                    for (int j = 1; j < psname_name_code_arr.length; j++) {
                        ps_codes_fr_names_arr.add(psname_name_code_arr[j][0]);
                        ps_names_arr.add(psname_name_code_arr[j][1]);
                        db.insertPsNameDetails("" + psname_name_code_arr[j][0], "" + psname_name_code_arr[j][1]);
                    }
                    db.close();
                    db2.close();
                } else {
                    showToast("Try Again");
                }

            } else {
                showToast("Try Again");
            }
        }
    }


    public class Async_getCourtDetails extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            ServiceHelper.getCourtNames();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressWarnings("static-access")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (ServiceHelper.Opdata_Chalana != null) {

                if (ServiceHelper.court_details_master.length > 0) {
                    court_name_code_arr = new String[ServiceHelper.court_details_master.length][2];
                    for (int i = 0; i < ServiceHelper.court_details_master.length; i++) {

                        court_name_code_arr[i] = ServiceHelper.court_details_master[i].split("@");
                    }
                    try {
                        db.open();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    SQLiteDatabase db2 = openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
                    db2.execSQL("DROP TABLE IF EXISTS " + DBHelper.courtName_table);
                    db2.execSQL(DBHelper.courtNamesCreation);

                    court_codes_fr_names_arr = new ArrayList<String>();
                    court_names_arr = new ArrayList<String>();
                    court_address_arr = new ArrayList<>();
                    court_codes_fr_names_arr.clear();
                    court_names_arr.clear();
                    court_address_arr.clear();

                    for (int j = 0; j < court_name_code_arr.length; j++) {
                        court_codes_fr_names_arr.add(court_name_code_arr[j][0]);
                        court_names_arr.add(court_name_code_arr[j][1]);
                        court_address_arr.add(court_name_code_arr[j][2]);

                        Log.d("ghf", "" + court_name_code_arr[j][0]);
                        Log.d("fdfgh", "" + court_name_code_arr[j][1]);

                        db.insertCourtNameDetails("" + court_name_code_arr[j][0], "" + court_name_code_arr[j][1], "" + court_name_code_arr[j][2]);
                    }
                    db.close();
                    db2.close();
                } else {
                    showToast("Try Again");
                }
            } else {
                showToast("Try Again");
            }
        }
    }


    public class Async_getCourtDisDetails extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            ServiceHelper.getCourtDisNames();
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressWarnings("static-access")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (ServiceHelper.Opdata_Chalana != null) {

                if (null != ServiceHelper.court_dis_details_master && ServiceHelper.court_dis_details_master.length > 0) {
                    court_dis_name_code_arr = new String[ServiceHelper.court_dis_details_master.length][2];
                    for (int i = 0; i < ServiceHelper.court_dis_details_master.length; i++) {

                        court_dis_name_code_arr[i] = ServiceHelper.court_dis_details_master[i].split("@");
                    }
                    try {
                        db.open();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    SQLiteDatabase db2 = openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
                    db2.execSQL("DROP TABLE IF EXISTS " + DBHelper.court_disName_table);
                    db2.execSQL(DBHelper.court_disNamesCreation);

                    court_dis_codes_fr_names_arr = new ArrayList<String>();
                    court_dis_names_arr = new ArrayList<String>();
                    court_dis_codes_fr_names_arr.clear();
                    court_dis_names_arr.clear();

                    for (int j = 0; j < court_dis_name_code_arr.length; j++) {
                        court_dis_codes_fr_names_arr.add(court_dis_name_code_arr[j][0]);
                        court_dis_names_arr.add(court_dis_name_code_arr[j][1]);

                        Log.d("ghf", "" + court_dis_name_code_arr[j][0]);
                        Log.d("fdfgh", "" + court_dis_name_code_arr[j][1]);

                        db.insertCourtDisNameDetails("" + court_dis_name_code_arr[j][0], "" + court_dis_name_code_arr[j][1]);
                    }
                    db.close();
                    db2.close();
                } else {
                    showToast("Try Again");
                }
            } else {
                showToast("Try Again");
            }
        }
    }


    @SuppressWarnings("unused")
    private class AsyncTask_Occupation extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected String doInBackground(Void... arg0) {
            ServiceHelper.getOccupationdetails();
            return null;
        }

        @SuppressWarnings("static-access")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (ServiceHelper.Opdata_Chalana != null) {
                if (ServiceHelper.occupationlist_master.length > 0) {
                    occupation_code_arr = new String[ServiceHelper.occupationlist_master.length][2];
                    for (int i = 1; i < ServiceHelper.occupationlist_master.length; i++) {
                        occupation_code_arr[i] = ServiceHelper.occupationlist_master[i].split("@");
                    }

                    try {
                        db.open();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    SQLiteDatabase db2 = openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
                    db2.execSQL("DROP TABLE IF EXISTS " + DBHelper.Occupation_table);
                    db2.execSQL(DBHelper.occupationCreation);

                    occupation_fr_names_arr = new ArrayList<String>();
                    occupation_names_arr = new ArrayList<String>();
                    occupation_fr_names_arr.clear();
                    occupation_names_arr.clear();

                    for (int j = 1; j < occupation_code_arr.length; j++) {
                        occupation_fr_names_arr.add(occupation_code_arr[j][0]);
                        occupation_names_arr.add(occupation_code_arr[j][1]);
                        db.insertOccupation("" + occupation_code_arr[j][0], "" + occupation_code_arr[j][1]);
                    }
                    db.close();
                    db2.close();
                }
            } else {
                showToast("Try Again");
            }
        }
    }

    public void getWheelerCodeFromDB() {

        try {
            db.open();
            c_whlr = DBHelper.db.rawQuery("select * from " + DBHelper.wheelercode_table, null);
            if (c_whlr.getCount() == 0) {

            } else {

                wheeler_code_arr_spot = new String[c_whlr.getCount()];

                int count = 0;

                while (c_whlr.moveToNext()) {
                    wheeler_code_arr_spot[count] = c_whlr.getString(1);

                    count++;
                }
            }
        } catch (Exception e) {
            c_whlr.close();
            db.close();
        }
        c_whlr.close();
        db.close();

    }

    public class Async_getViolationDetails extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            ServiceHelper.getAllWheelerViolations();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    public class Async_ocuptn extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            ServiceHelper.getOccupation_Details();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressWarnings("static-access")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (ServiceHelper.Opdata_Chalana != null) {

                if (null != ServiceHelper.occupation_master && ServiceHelper.occupation_master.length > 0) {

                    try {
                        db.open();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    ocuptn_code_name = new String[ServiceHelper.occupation_master.length][2];
                    ocuptn_name_arr_list = new ArrayList<String>();
                    ocuptn_name_arr_list.clear();

                    for (int i = 1; i < ServiceHelper.occupation_master.length; i++) {
                        ocuptn_code_name[i] = (ServiceHelper.occupation_master[i].split("@"));
                        ocuptn_name_arr_list.add(ocuptn_code_name[i][1]);
                        db.insertOccupation("" + ocuptn_code_name[i][0], "" + ocuptn_code_name[i][1]);
                    }
                    db.close();
                }
            } else {
                showToast("Try Again");
            }
        }
    }

    public class Async_BarDetails extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            ServiceHelper.getBar_Details();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressWarnings("static-access")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (ServiceHelper.Opdata_Chalana != null) {

                if (ServiceHelper.bar_master.length > 0) {

                    try {
                        db.open();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    SQLiteDatabase db2 = openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
                    db2.execSQL("DROP TABLE IF EXISTS " + DBHelper.bar_table);
                    db2.execSQL(DBHelper.barTableCreation);

                    bar_code_name = new String[ServiceHelper.bar_master.length][2];
                    bar_name_arr_list = new ArrayList<String>();

                    for (int i = 1; i < ServiceHelper.bar_master.length; i++) {
                        bar_code_name[i] = (ServiceHelper.bar_master[i].split("@"));
                        bar_name_arr_list.add(bar_code_name[i][1]);
                        db.insertBarDetails("" + bar_code_name[i][0], "" + bar_code_name[i][1]);
                    }
                    db.close();
                    db2.close();
                } else {
                    showToast("Try Again");
                }
            } else {
                showToast("Try Again");
            }
        }
    }

    public class Async_GetTerminalDetails extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected String doInBackground(Void... params) {
            ServiceHelper.getTerminalID("" + UNIT_CODE);
            return null;
        }

        @SuppressWarnings({"deprecation", "unused"})
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (ServiceHelper.terminaldetails_resp != null) {
                if (ServiceHelper.terminaldetails_resp != null || ServiceHelper.terminaldetails_resp.length() > 0) {
                    String strJson = ServiceHelper.terminaldetails_resp;
                    SQLiteDatabase db2 = null;

                    try {
                        db.open();
                        DBHelper helper = new DBHelper(getApplicationContext());

                        db2 = openOrCreateDatabase(DBHelper.DATABASE_NAME, MODE_PRIVATE, null);
                        db2.execSQL(DBHelper.termailDetailsCreation);
                        db2.execSQL("delete from " + DBHelper.TERMINAL_DETAILS_TABLE);

                        db.execSQL(DBHelper.CREATE_BT_PRINTER);
                        db.execSQL("delete from " + DBHelper.BT_PRINTER_TABLE);

                        JSONObject jsonRootObject = new JSONObject(strJson);

                        JSONArray jsonArray = jsonRootObject.optJSONArray("TERMINAL_DETAILS");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            terminalID = jsonObject.optString("TERMINAL_ID");
                            bluetoothName = jsonObject.optString("BLUETOOTH_NAME");
                            bluetoothAddress = jsonObject.optString("BLUETOOTH_ADDRESS");

                            ContentValues values2 = new ContentValues();
                            values2.put("TERMINAL_ID", "" + terminalID);
                            values2.put("BT_NAME", "" + bluetoothName);
                            values2.put("BT_ADDRESS", "" + bluetoothAddress);
                            db2.insert(DBHelper.TERMINAL_DETAILS_TABLE.trim(), null, values2);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();

                    } finally {
                        if (db2 != null) {
                            db2.close();
                        }
                    }
                }
            } else {
                showToast("Downloaded masters successfully");
            }
        }
    }

    public class Async_qlfctn extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            ServiceHelper.getQualifications_Details();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressWarnings({"deprecation", "static-access"})
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (ServiceHelper.Opdata_Chalana != null) {

                if (ServiceHelper.qualification_master.length > 0) {
                    try {
                        db.open();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    qlfctn_code_name = new String[ServiceHelper.qualification_master.length][2];
                    qlfctn_name_arr_list = new ArrayList<String>();

                    for (int i = 1; i < ServiceHelper.qualification_master.length; i++) {

                        qlfctn_code_name[i] = (ServiceHelper.qualification_master[i].split("@"));
                        qlfctn_name_arr_list.add(qlfctn_code_name[i][1]);
                        db.insertQualificationDetails("" + qlfctn_code_name[i][0], "" + qlfctn_code_name[i][1]);
                    }
                    db.close();
                }
            } else {
                showToast("Try Again");
            }
        }
    }

    public class Async_vchle_Cat extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            ServiceHelper.getVechileCategory("" + ServiceHelper.VECHILE_CATEGORY);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressWarnings({"deprecation", "static-access"})
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (ServiceHelper.Opdata_Chalana != null) {

                if (ServiceHelper.vchle_cat_master.length > 0) {

                    vchleCat_code_name = new String[0][0];
                    vchleCat_code_name = new String[ServiceHelper.vchle_cat_master.length][2];
                    vchleCat_name_arr_list = new ArrayList<String>();// FOR

                    vchleCat_name_arr_list.clear();

                    try {
                        db.open();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    for (int i = 1; i < ServiceHelper.vchle_cat_master.length; i++) {
                        vchleCat_code_name[i] = (ServiceHelper.vchle_cat_master[i].split("@"));
                        vchleCat_name_arr_list.add(vchleCat_code_name[i][1]);
                        db.insertVehicleCatDetails("" + vchleCat_code_name[i][0], "" + vchleCat_code_name[i][1]);
                    }
                    db.close();
                }
            } else {
                showToast("Try again");
                vchleCat_code_name = new String[0][0];
                vchleCat_name_arr_list.clear();
            }
        }
    }

    public class Async_vchle_mainCat extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            ServiceHelper.getVechileCategory("" + ServiceHelper.VECHILE_MAIN_CAT_METHOD_NAME);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressWarnings({"deprecation", "static-access"})
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            if (ServiceHelper.Opdata_Chalana != null) {

                if (ServiceHelper.vchle_mainCat_master.length > 0) {

                    vchle_MainCat_code_name = new String[0][0];
                    vchle_MainCat_code_name = new String[ServiceHelper.vchle_mainCat_master.length][2];
                    vchle_MainCat_name_arr_list = new ArrayList<String>();// FOR

                    vchle_MainCat_name_arr_list.clear();

                    try {
                        db.open();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    for (int i = 1; i < ServiceHelper.vchle_mainCat_master.length; i++) {
                        vchle_MainCat_code_name[i] = (ServiceHelper.vchle_mainCat_master[i].split("@"));
                        vchle_MainCat_name_arr_list.add(vchle_MainCat_code_name[i][1]);
                        db.insertVehicleMainCatDetails("" + vchle_MainCat_code_name[i][0], "" + vchle_MainCat_code_name[i][1]);
                    }
                    db.close();
                }
            } else {
                showToast("Try again");
                vchle_MainCat_code_name = new String[0][0];
                vchle_MainCat_name_arr_list.clear();
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    protected void getPresentDate() {
        calendar = Calendar.getInstance();
        present_date = calendar.get(Calendar.DAY_OF_MONTH);
        present_month = calendar.get(Calendar.MONTH);
        present_year = calendar.get(Calendar.YEAR);
        date_format = new SimpleDateFormat("dd-MMM-yyyy");
        present_date_toSend = date_format.format(new Date(present_year - 1900, present_month, present_date));
    }

    @SuppressLint({"WorldReadableFiles", "CommitPrefEdits"})
    @Override
    protected void onResume() {
        super.onResume();
        preferences = getSharedPreferences("preference", MODE_PRIVATE);
        editor = preferences.edit();
        psname_settings = preferences.getString("psname_name", "psname");
        pointnameBycode_settings = preferences.getString("point_name", "pointname");
    }

    @SuppressLint("CommitPrefEdits")
    public void courtCounselling() {
        check_vhleHistory_or_Spot = "drunkdrive";
        getPreferenceValues();
        preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        editor = preferences.edit();
        address_spot = preferences.getString("btaddress", "btaddr");
        try {
            db.open();
            cursor_psnames = DBHelper.db.rawQuery("select * from " + DBHelper.psName_table, null);
            cursor_courtnames = DBHelper.db.rawQuery("select * from " + DBHelper.courtName_table, null);
            cursor_court_disnames = DBHelper.db.rawQuery("select * from " + DBHelper.court_disName_table, null);
            c_whlr_details = DBHelper.db.rawQuery("select * from " + DBHelper.wheelercode_table, null);
            if ((cursor_psnames.getCount() == 0) && (cursor_courtnames.getCount() == 0) && (cursor_court_disnames.getCount() == 0) && (c_whlr_details.getCount() == 0)) {
                showToast("Please download master's !");
            } else {
                if (isOnline()) {
                    startActivity(new Intent(Dashboard.this, CourtCaseStatusActivity.class));
                } else {
                    showToast("" + netwrk_info_txt);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            c_whlr_details.close();
            cursor_psnames.close();
            db.close();
        }
        c_whlr_details.close();
        cursor_psnames.close();
        db.close();
    }

    @SuppressLint("CommitPrefEdits")
    public void courtCousellingSMSIntimation() {
        check_vhleHistory_or_Spot = "drunkdrive";
        getPreferenceValues();
        preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        editor = preferences.edit();
        address_spot = preferences.getString("btaddress", "btaddr");
        try {
            db.open();
            cursor_psnames = DBHelper.db.rawQuery("select * from " + DBHelper.psName_table, null);
            cursor_courtnames = DBHelper.db.rawQuery("select * from " + DBHelper.courtName_table, null);
            cursor_court_disnames = DBHelper.db.rawQuery("select * from " + DBHelper.court_disName_table, null);
            c_whlr_details = DBHelper.db.rawQuery("select * from " + DBHelper.wheelercode_table, null);
            if ((cursor_psnames.getCount() == 0)
                    && (cursor_courtnames.getCount() == 0)
                    && (cursor_court_disnames.getCount() == 0)
                    && (c_whlr_details.getCount() == 0)) {
                showToast("Please download master's !");
            } else {
                if (isOnline()) {
                    startActivity(new Intent(Dashboard.this, DDCloseActiviy.class));
                } else {
                    showToast("" + netwrk_info_txt);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            c_whlr_details.close();
            cursor_psnames.close();
            db.close();
        }
        c_whlr_details.close();
        cursor_psnames.close();
        db.close();
    }

    private void showToast(String msg) {
        Toast.makeText(this, "" + msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.drawable.logo_hyd);
        builder.setMessage("Do you want to exit ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}