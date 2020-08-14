package com.example.mtpv.eticketcourt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.analogics.thermalAPI.Bluetooth_Printer_3inch_ThermalAPI;
import com.analogics.thermalprinter.AnalogicsThermalPrinter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mtpv.eticketcourt.Pojos.CourtCaseDisposal;
import com.example.mtpv.eticketcourt.service.DBHelper;
import com.example.mtpv.eticketcourt.service.ServiceHelper;
import com.example.mtpv.eticketcourt.util.DateUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import fr.ganfra.materialspinner.MaterialSpinner;

public class DDCloseActiviy extends Activity {
    //9533363306

    TextView compny_Name;
    AppCompatImageView imgView_CourtOrderCopy, imgView_DLCopy, minor_img_CourtCopy, owner_img_CourtCopy;
    ArrayList<String> mArrayListCourtNames = new ArrayList<>();
    ArrayList<String> mArrayListCourtDis = new ArrayList<>();
    HashMap<String, String> paramsCourt = new HashMap<>();
    HashMap<String, String> paramsCourtdis = new HashMap<>();
    ArrayList<HashMap<String, String>> maparrayCourtdis = new ArrayList<>();
    String print_Data;
    Calendar cal;
    int present_year;
    int present_month;
    int present_day;
    SimpleDateFormat format;
    Date date_From;
    String present_date_toSend, date_courtAtnd, date_convFRom, date_convicTo, date_DL_SUS_FROM, date_DL_SUS_TO, date_dd_dl_dob,
            owner_date_convFRom, owner_date_convicTo, ownerConDays;
    EditText et_dp_regno;
    EditText edtTxt_STC_No, edtTxt_FineAmnt, edtTxtConDays, edtTxtRisDays, edtTxt_FirNo, edtTxt_PSName, edtTxt_Remarks;
    EditText owner_edtTxt_STC_NO, owner_edtTxt_FineAmnt, owner_edtTxt_RisingDays, owner_edtTxt_Contct_NO;
    Button btn_dp_date_selection, btn_courtAttenddate, btn_courtConFromdate, btn_courtConTo, btn_courtSoclServceFromdate,
            btn_courtSoclServceTodate, btn_DLSUS_FromDate, btn_DLSUS_ToDate, btn_dateSeltion_DL_Canln, btn_DrExpDate;
    Button owner_btn_CrtAtndDate, owner_btn_courtConFromdate, owner_btn_courtConTo, owner_btn_courtSoclServceFromdate,
            owner_btn_courtSoclServceTodate;
    Button btn_dp_get_onlinedetials;
    Button btn_payment;
    AppCompatButton btn_Dl_dob;
    String btn_strng_dl_DOB;
    final int PRESENT_DATE_PICKER = 1;
    final int PROGRESS_DIALOG = 2;
    final int PRESENT_COURT_ATTEND_DATE = 5, OWNER_COURT_ATTEND_DATE = 25, OWNER_COURT_CONVI_FROM = 26, OWNER_COURT_CONVI_TO = 27,
            OWNER_COURT_SCL_SRC_TO = 28, OWNER_COURT_SCL_SRC_FROM = 29;
    final int PRESENT_COURT_CONVI_FROM = 6;
    final int PRESENT_COURT_CONVI_TO = 7;
    final int PRESENT_COURT_SCL_SRC_FROM = 8;
    final int PRESENT_COURT_SCL_SRC_TO = 9;
    final int PRESENT_DL_SUS_FROM = 16;
    final int PRESENT_DL_SUS_TO = 17;
    final int PRESENT_DD_DL_DOB = 15;
    final int PRESENT_DD_DL_CanCel = 18;
    final int PRINT_DD_DIALOG = 10;
    final int PRINT_DD_DIALOG_EXIT = 11;
    String online_report_status = "";
    private static final int REQUEST_ENABLE_BT = 1;
    AnalogicsThermalPrinter actual_printer = new AnalogicsThermalPrinter();
    BluetoothAdapter bluetoothAdapter;
    @SuppressWarnings("unused")
    private BluetoothAdapter mBluetoothAdapter = null;
    ArrayList<String> print_respose, print_apptype;
    public static String printer_addrss, printer_name;
    int selected_type = -1;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String address = "";
    RelativeLayout dd_lyt;
    RelativeLayout pay_dd_lyt;
    TextView Reg_No, Mobile_No, Offender_Date, Challan_No, Aadhar_No, Dl_No;
    EditText edtTxt_Mob_No, edtTxt_Aadhar_No, edtTxt_DlNo, edtTxt_DL_SUSDAYS;
    ArrayList<String> courtNames;
    MaterialSpinner courtspinner, owner_courtSpinner, owner_courtDisSpinner;
    ArrayList<String> courtDisNames;
    MaterialSpinner courtDisspinner;
    RadioGroup rdoGrp_VehcleRlse, rdoGrp_DLSUSCAN;
    RadioButton rdoBtnYes_VehcleRlse, rdoBtn_DLSUS;
    RadioButton rdoBtnNo_VehcleRlse, rdoBtn_DLCAN, rdoBtnNONE;
    LinearLayout lytConFrom, lytConTo, lytConDays, lytFineAmnt, lytSoclFrom, lytSclSerTo, lytRisingDays, lytSUSDAYS, lytDLCanDate,
            lytDLSusFrom, lytDLSusTo, lytFirNo, lytPSName, lyt_DrvrExpredDate, lytImages;
    LinearLayout owner_Lyt_Pay_details, owner_lytConvtdFrom, owner_lytConvtdTo, owner_lytFineAmnt, owner_lytSoclFrom, owner_lytSclSerTo,
            owner_lytRisingDays, owner_lytImages;
    DBHelper db;
    Cursor c, cursor_courtnames, cursor_court_Disnames, printer_cursor;
    String selectedCourtCode, selectedCourtDisCode, owner_selectedCourtCode, owner_selectedCourtDisCode;
    String vehcleRelse = "Y";
    String dl_SUS = "N";
    String dl_CAN = "N";
    String dl_SUS_Status = "N";
    String dl_SusDays = "";
    String vEHICLE_NUMBER, chall_No, chall_Type, violations, offence_Date, driver_Adhar, driver_Mobile, driver_LCNCE, driver_DL_DOB,
            unit_CODE, pid_CODE, ps_CODE;
    String dayDifference;
    DateUtil dateUtil;
    byte[] byteArray;
    String img_dataCourtCopy, img_dataDLCopy = null, minor_ImgData = null, driver_ImgData = null;
    String selection_Pic_Flag = "";
    String tckt_UPdated_Flag = "N";
    ImageView imageView1;
    TextView tv_officer_name, tv_officer_cadre_name, tv_officer_ps, tv_officer_pid;
    boolean minor_Dvng_Flag = false;
    CourtCaseDisposal caseDisposal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ddclosing);

        imageView1 = (ImageView) findViewById(R.id.img_logo);
        String unitCode = MainActivity.arr_logindetails[0];
        unitCode = unitCode.substring(0, 2);

        tv_officer_name = (TextView) findViewById(R.id.officer_Name);
        tv_officer_name.setText(MainActivity.pidName + "(" + MainActivity.cadreName + ")");
        //tv_officer_cadre_name = findViewById(R.id.officer_cadre);
        //tv_officer_cadre_name.setText(MainActivity.cadreName);
        tv_officer_ps = (TextView) findViewById(R.id.officer_PS);
        tv_officer_ps.setText(MainActivity.psName);
        //tv_officer_pid = findViewById(R.id.tv_officer_pid);
        //tv_officer_pid.setText(MainActivity.user_id);

        db = new DBHelper(getApplicationContext());
        getCourtDisNamesFromDB();
        getCourtNamesFromDB();
        cal = Calendar.getInstance();
        present_year = cal.get(Calendar.YEAR);
        present_month = cal.get(Calendar.MONTH);
        present_day = cal.get(Calendar.DAY_OF_MONTH);
        preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        editor = preferences.edit();
        address = preferences.getString("btaddress", "btaddr");
        lytConFrom = (LinearLayout) findViewById(R.id.lytConvtdFrom);
        lytConTo = (LinearLayout) findViewById(R.id.lytConvtdTo);
        lytConDays = (LinearLayout) findViewById(R.id.lytConvtdDays);
        lytFineAmnt = (LinearLayout) findViewById(R.id.lytFineAmnt);
        lytSoclFrom = (LinearLayout) findViewById(R.id.lytSoclFrom);
        lytSclSerTo = (LinearLayout) findViewById(R.id.lytSclSerTo);
        lytRisingDays = (LinearLayout) findViewById(R.id.lytRisingDays);
        lytSUSDAYS = (LinearLayout) findViewById(R.id.lytSUSDAYS);
        lytDLSusFrom = (LinearLayout) findViewById(R.id.lytDLSusFrom);
        lytDLCanDate = (LinearLayout) findViewById(R.id.lytDLCanDate);
        lytDLSusTo = (LinearLayout) findViewById(R.id.lytDLSusTo);
        lytFirNo = findViewById(R.id.lytFirNo);
        lytPSName = findViewById(R.id.lytPSName);
        lyt_DrvrExpredDate = findViewById(R.id.lyt_DrvrExpredDate);
        lytImages = (LinearLayout) findViewById(R.id.lytImages);
        imgView_CourtOrderCopy = (AppCompatImageView) findViewById(R.id.img_CourtCopy);
        owner_img_CourtCopy = findViewById(R.id.owner_img_CourtCopy);
        minor_img_CourtCopy = findViewById(R.id.minor_img_CourtCopy);
        imgView_DLCopy = (AppCompatImageView) findViewById(R.id.img_DLCopy);
        compny_Name = (TextView) findViewById(R.id.CompanyName);
        Animation marquee = AnimationUtils.loadAnimation(this, R.anim.marquee);
        compny_Name.startAnimation(marquee);
        et_dp_regno = (EditText) findViewById(R.id.edt_regno_dp_xml);
        edtTxt_STC_No = (EditText) findViewById(R.id.edtTxt_STC_NO);
        edtTxt_FineAmnt = (EditText) findViewById(R.id.edtTxt_FineAmnt);
        edtTxtConDays = (EditText) findViewById(R.id.edtTxt_ConDays);
        edtTxtRisDays = (EditText) findViewById(R.id.edtTxt_RisingDays);
        edtTxt_Remarks = (EditText) findViewById(R.id.edtTxt_Remarks);
        edtTxt_DL_SUSDAYS = (EditText) findViewById(R.id.edtTxt_DL_SUSDAYS);
        edtTxt_DL_SUSDAYS.setKeyListener(null);
        edtTxt_FirNo = findViewById(R.id.edtTxt_FirNo);
        edtTxt_PSName = findViewById(R.id.edtTxt_PSName);
        btn_dp_date_selection = (Button) findViewById(R.id.btn_dateselection_dp_xml);
        btn_courtAttenddate = (Button) findViewById(R.id.btn_dateselection_dp_xml1);
        btn_courtConFromdate = (Button) findViewById(R.id.btn_dateselection_dp_xml3);
        btn_courtConTo = (Button) findViewById(R.id.btn_dateselection_dp_xml4);
        btn_courtSoclServceFromdate = (Button) findViewById(R.id.btn_dateselection_dp_xmlssF);
        btn_courtSoclServceTodate = (Button) findViewById(R.id.btn_dateselection_dp_xmlSST);
        btn_dp_get_onlinedetials = (Button) findViewById(R.id.btngetdetails_dp_xml);
        btn_DLSUS_FromDate = (Button) findViewById(R.id.btn_dateSeltion_DL_SusFrom);
        btn_DLSUS_ToDate = (Button) findViewById(R.id.btn_dateSeltion_DL_SusTo);
        btn_dateSeltion_DL_Canln = (Button) findViewById(R.id.btn_dateSeltion_DL_Canln);
        btn_DrExpDate = findViewById(R.id.btn_DrExpDate);
        btn_payment = (Button) findViewById(R.id.btn_payment);
        btn_Dl_dob = (AppCompatButton) findViewById(R.id.btn_Dl_dob);
        dd_lyt = (RelativeLayout) findViewById(R.id.Lyt_DD_Details);
        pay_dd_lyt = (RelativeLayout) findViewById(R.id.Lyt_Pay_details);
        Reg_No = (TextView) findViewById(R.id.Txt_Regno);
        Mobile_No = (TextView) findViewById(R.id.Txt_Mobile_no);
        edtTxt_Mob_No = (EditText) findViewById(R.id.edtTxt_Mobile_no);
        edtTxt_Aadhar_No = (EditText) findViewById(R.id.edtTxt_Adhar_no);
        edtTxt_DlNo = (EditText) findViewById(R.id.edtTxt_Dl_no);
        Offender_Date = (TextView) findViewById(R.id.Txt_Offender_date);
        Challan_No = (TextView) findViewById(R.id.Txt_Chaln_No);
        Aadhar_No = (TextView) findViewById(R.id.txt_Adhar_No);
        Dl_No = (TextView) findViewById(R.id.Txt_DlNo);
        courtspinner = (MaterialSpinner) findViewById(R.id.courtSpinner);
        courtDisspinner = (MaterialSpinner) findViewById(R.id.courtDisSpinner);
        owner_courtSpinner = findViewById(R.id.owner_courtSpinner);
        owner_courtDisSpinner = findViewById(R.id.owner_courtDisSpinner);

        rdoGrp_VehcleRlse = (RadioGroup) findViewById(R.id.rdoGrp_VehcleRlse);
        rdoBtnYes_VehcleRlse = (RadioButton) findViewById(R.id.rdoBtnYes_VehcleRlse);
        rdoBtnNo_VehcleRlse = (RadioButton) findViewById(R.id.rdoBtnNo_VehcleRlse);
        print_respose = new ArrayList<>();
        print_apptype = new ArrayList<>();
        dateUtil = new DateUtil();
        rdoGrp_DLSUSCAN = (RadioGroup) findViewById(R.id.rdoGrpDLSUSCAN);
        rdoBtn_DLSUS = (RadioButton) findViewById(R.id.rdoBtnDLSUS);
        rdoBtn_DLCAN = (RadioButton) findViewById(R.id.rdoBtnDLCAN);
        rdoBtnNONE = (RadioButton) findViewById(R.id.rdoBtnNONE);

        owner_Lyt_Pay_details = findViewById(R.id.owner_Lyt_Pay_details);
        owner_lytConvtdFrom = findViewById(R.id.owner_lytConvtdFrom);
        owner_lytConvtdTo = findViewById(R.id.owner_lytConvtdTo);
        owner_lytFineAmnt = findViewById(R.id.owner_lytFineAmnt);
        owner_lytSoclFrom = findViewById(R.id.owner_lytSoclFrom);
        owner_lytSclSerTo = findViewById(R.id.owner_lytSclSerTo);
        owner_lytRisingDays = findViewById(R.id.owner_lytRisingDays);
        owner_lytImages = findViewById(R.id.owner_lytImages);
        owner_btn_CrtAtndDate = findViewById(R.id.owner_btn_CrtAtndDate);
        owner_edtTxt_STC_NO = findViewById(R.id.owner_edtTxt_STC_NO);
        owner_edtTxt_Contct_NO = findViewById(R.id.owner_edtTxt_Contct_NO);
        owner_edtTxt_FineAmnt = findViewById(R.id.owner_edtTxt_FineAmnt);
        owner_edtTxt_RisingDays = findViewById(R.id.owner_edtTxt_RisingDays);
        owner_btn_courtConFromdate = findViewById(R.id.owner_btn_courtConFromdate);
        owner_btn_courtConTo = findViewById(R.id.owner_btn_courtConTo);
        owner_btn_courtSoclServceFromdate = findViewById(R.id.owner_btn_courtSoclServceFromdate);
        owner_btn_courtSoclServceTodate = findViewById(R.id.owner_btn_courtSoclServceTodate);

        edtTxt_DL_SUSDAYS.setEnabled(false);
        edtTxtConDays.setEnabled(false);


        rdoGrp_VehcleRlse.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rdoBtnYes_VehcleRlse:
                        vehcleRelse = "";
                        vehcleRelse = "Y";
                        break;

                    case R.id.rdoBtnNo_VehcleRlse:
                        vehcleRelse = "";
                        vehcleRelse = "N";
                        break;

                    default:
                        break;
                }
            }
        });

        rdoGrp_DLSUSCAN.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rdoBtnDLSUS:
                        dl_SUS = "";
                        dl_SUS = "Y";
                        dl_CAN = "N";
                        lytSUSDAYS.setVisibility(View.VISIBLE);
                        lytDLSusFrom.setVisibility(View.VISIBLE);
                        lytDLSusTo.setVisibility(View.VISIBLE);
                        lytImages.setVisibility(View.VISIBLE);
                        lytDLCanDate.setVisibility(View.GONE);
                        imgView_CourtOrderCopy.setImageResource(R.drawable.courtorder);
                        imgView_DLCopy.setImageResource(R.drawable.dlcopy);
                        break;

                    case R.id.rdoBtnDLCAN:
                        dl_CAN = "";
                        dl_CAN = "Y";
                        dl_SUS = "N";
                        lytDLCanDate.setVisibility(View.VISIBLE);
                        lytSUSDAYS.setVisibility(View.GONE);
                        lytDLSusFrom.setVisibility(View.GONE);
                        lytDLSusTo.setVisibility(View.GONE);
                        lytImages.setVisibility(View.VISIBLE);
                        imgView_CourtOrderCopy.setImageResource(R.drawable.courtorder);
                        imgView_DLCopy.setImageResource(R.drawable.dlcopy);
                        break;

                    case R.id.rdoBtnNONE:
                        dl_CAN = "";
                        dl_SUS = "";
                        dl_CAN = "N";
                        dl_SUS = "N";
                        lytSUSDAYS.setVisibility(View.GONE);
                        lytDLSusFrom.setVisibility(View.GONE);
                        lytDLSusTo.setVisibility(View.GONE);
                        lytImages.setVisibility(View.GONE);
                        lytDLCanDate.setVisibility(View.GONE);
                        break;

                    default:
                        break;
                }

            }
        });

        courtNames = Dashboard.court_names_arr;
        courtDisNames = Dashboard.court_dis_names_arr;
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, mArrayListCourtNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courtspinner.setAdapter(dataAdapter);

        courtspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String courtDis = courtspinner.getSelectedItem().toString();

                Log.i("court map size : ", "" + paramsCourt.size());
                for (String mapCourtName : paramsCourt.keySet()) {
                    if (courtDis.equals(mapCourtName)) {
                        selectedCourtCode = paramsCourt.get(mapCourtName);
                        //Toast.makeText(getApplicationContext(), "COURT NAME : " + mapCourtName + " COURT CODE : " + selectedCourtCode, Toast.LENGTH_LONG).show();
                        break;
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //owner_courtSpinner

        ArrayAdapter<String> owner_dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, mArrayListCourtNames);
        owner_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        owner_courtSpinner.setAdapter(owner_dataAdapter);

        owner_courtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String courtDis = owner_courtSpinner.getSelectedItem().toString();

                Log.i("court map size : ", "" + paramsCourt.size());
                for (String mapCourtName : paramsCourt.keySet()) {
                    if (courtDis.equals(mapCourtName)) {
                        owner_selectedCourtCode = paramsCourt.get(mapCourtName);
                        //Toast.makeText(getApplicationContext(), "COURT NAME : " + mapCourtName + " COURT CODE : " + selectedCourtCode, Toast.LENGTH_LONG).show();
                        break;
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mArrayListCourtDis);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courtDisspinner.setAdapter(dataAdapter1);
        courtDisspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String courtDis = courtDisspinner.getSelectedItem().toString();
                for (String mapCourtName : paramsCourtdis.keySet()) {
                    if (courtDis.equals(mapCourtName)) {
                        selectedCourtDisCode = paramsCourtdis.get(mapCourtName);
                        break;
                    }
                }
                if ("1".equals(selectedCourtDisCode) || "15".equals(selectedCourtDisCode)) {
                    lytConFrom.setVisibility(View.GONE);
                    lytConTo.setVisibility(View.GONE);
                    lytConDays.setVisibility(View.GONE);
                    lytSoclFrom.setVisibility(View.GONE);
                    lytSclSerTo.setVisibility(View.GONE);
                    lytRisingDays.setVisibility(View.GONE);
                    lytFirNo.setVisibility(View.GONE);
                    lytPSName.setVisibility(View.GONE);
                    lyt_DrvrExpredDate.setVisibility(View.GONE);
                    lytFineAmnt.setVisibility(View.VISIBLE);
                    rdoBtnNONE.setChecked(true);
                    clearData();

                } else if ("5".equals(selectedCourtDisCode)) {
                    lytConFrom.setVisibility(View.GONE);
                    lytConTo.setVisibility(View.GONE);
                    lytSoclFrom.setVisibility(View.GONE);
                    lytSclSerTo.setVisibility(View.GONE);
                    lytRisingDays.setVisibility(View.VISIBLE);
                    lytConDays.setVisibility(View.GONE);
                    lytFirNo.setVisibility(View.GONE);
                    lytPSName.setVisibility(View.GONE);
                    lyt_DrvrExpredDate.setVisibility(View.GONE);
                    lytFineAmnt.setVisibility(View.VISIBLE);
                    rdoBtnNONE.setChecked(true);
                    clearData();

                } else if ("7".equals(selectedCourtDisCode)) {
                    lytConFrom.setVisibility(View.GONE);
                    lytConTo.setVisibility(View.GONE);
                    lytConDays.setVisibility(View.GONE);
                    lytFirNo.setVisibility(View.GONE);
                    lytPSName.setVisibility(View.GONE);
                    lyt_DrvrExpredDate.setVisibility(View.GONE);
                    lytFineAmnt.setVisibility(View.VISIBLE);
                    lytSoclFrom.setVisibility(View.VISIBLE);
                    lytSclSerTo.setVisibility(View.VISIBLE);
                    lytRisingDays.setVisibility(View.VISIBLE);
                    rdoBtnNONE.setChecked(true);
                    clearData();

                } else if ("3".equals(selectedCourtDisCode) || "17".equals(selectedCourtDisCode)) {
                    lytConFrom.setVisibility(View.VISIBLE);
                    lytConTo.setVisibility(View.VISIBLE);
                    lytConDays.setVisibility(View.VISIBLE);
                    lytFineAmnt.setVisibility(View.VISIBLE);
                    lytSoclFrom.setVisibility(View.GONE);
                    lytSclSerTo.setVisibility(View.GONE);
                    lytRisingDays.setVisibility(View.GONE);
                    lytFirNo.setVisibility(View.GONE);
                    lytPSName.setVisibility(View.GONE);
                    lyt_DrvrExpredDate.setVisibility(View.GONE);
                    rdoBtnNONE.setChecked(true);
                    clearData();

                } else if ("2".equals(selectedCourtDisCode) || "16".equals(selectedCourtDisCode)) {  // ONLY IMPRISONMENT
                    lytConFrom.setVisibility(View.VISIBLE);
                    lytConTo.setVisibility(View.VISIBLE);
                    lytConDays.setVisibility(View.VISIBLE);
                    lytFineAmnt.setVisibility(View.GONE);
                    lytSoclFrom.setVisibility(View.GONE);
                    lytSclSerTo.setVisibility(View.GONE);
                    lytRisingDays.setVisibility(View.GONE);
                    lytFirNo.setVisibility(View.GONE);
                    lytPSName.setVisibility(View.GONE);
                    lyt_DrvrExpredDate.setVisibility(View.GONE);
                    rdoBtnNONE.setChecked(true);
                    clearData();

                } else if ("4".equals(selectedCourtDisCode)) {
                    lytConFrom.setVisibility(View.GONE);
                    lytConTo.setVisibility(View.GONE);
                    lytConDays.setVisibility(View.GONE);
                    lytFineAmnt.setVisibility(View.GONE);
                    lytSoclFrom.setVisibility(View.GONE);
                    lytSclSerTo.setVisibility(View.GONE);
                    lytRisingDays.setVisibility(View.VISIBLE);
                    lytFirNo.setVisibility(View.GONE);
                    lytPSName.setVisibility(View.GONE);
                    lyt_DrvrExpredDate.setVisibility(View.GONE);
                    rdoBtnNONE.setChecked(true);
                    clearData();

                } else if ("8".equals(selectedCourtDisCode)) {  //ONLY SOCIAL SERVICE
                    lytConFrom.setVisibility(View.GONE);
                    lytConTo.setVisibility(View.GONE);
                    lytConDays.setVisibility(View.GONE);
                    lytFineAmnt.setVisibility(View.GONE);
                    lytSoclFrom.setVisibility(View.VISIBLE);
                    lytSclSerTo.setVisibility(View.VISIBLE);
                    lytRisingDays.setVisibility(View.GONE);
                    lytFirNo.setVisibility(View.GONE);
                    lytPSName.setVisibility(View.GONE);
                    lyt_DrvrExpredDate.setVisibility(View.GONE);
                    rdoBtnNONE.setChecked(true);
                    clearData();

                } else if ("9".equals(selectedCourtDisCode)) {  //DL SUSPENSION AND FINE
                    lytConFrom.setVisibility(View.GONE);
                    lytConTo.setVisibility(View.GONE);
                    lytConDays.setVisibility(View.GONE);
                    lytFineAmnt.setVisibility(View.VISIBLE);
                    lytSoclFrom.setVisibility(View.GONE);
                    lytSclSerTo.setVisibility(View.GONE);
                    lytRisingDays.setVisibility(View.GONE);
                    rdoBtn_DLSUS.setChecked(true);
                    lytFirNo.setVisibility(View.GONE);
                    lytPSName.setVisibility(View.GONE);
                    lyt_DrvrExpredDate.setVisibility(View.GONE);
                    rdoBtn_DLCAN.setKeyListener(null);
                    rdoBtnNONE.setKeyListener(null);
                    clearData();

                } else if ("10".equals(selectedCourtDisCode)) {  // DL SUSPENSION AND IMPRISONMENT
                    lytConFrom.setVisibility(View.VISIBLE);
                    lytConTo.setVisibility(View.VISIBLE);
                    lytConDays.setVisibility(View.VISIBLE);
                    lytFineAmnt.setVisibility(View.GONE);
                    lytSoclFrom.setVisibility(View.GONE);
                    lytSclSerTo.setVisibility(View.GONE);
                    lytRisingDays.setVisibility(View.GONE);
                    lytFirNo.setVisibility(View.GONE);
                    lytPSName.setVisibility(View.GONE);
                    lyt_DrvrExpredDate.setVisibility(View.GONE);
                    rdoBtn_DLSUS.setChecked(true);
                    rdoBtn_DLCAN.setKeyListener(null);
                    rdoBtnNONE.setKeyListener(null);
                    clearData();

                } else if ("11".equals(selectedCourtDisCode)) {  // DL CANCELLATION AND FINE
                    lytConFrom.setVisibility(View.GONE);
                    lytConTo.setVisibility(View.GONE);
                    lytConDays.setVisibility(View.GONE);
                    lytFineAmnt.setVisibility(View.VISIBLE);
                    lytSoclFrom.setVisibility(View.GONE);
                    lytSclSerTo.setVisibility(View.GONE);
                    lytRisingDays.setVisibility(View.GONE);
                    lytFirNo.setVisibility(View.GONE);
                    lytPSName.setVisibility(View.GONE);
                    lyt_DrvrExpredDate.setVisibility(View.GONE);
                    rdoBtn_DLCAN.setChecked(true);
                    rdoBtn_DLSUS.setKeyListener(null);
                    rdoBtnNONE.setKeyListener(null);
                    clearData();

                } else if ("12".equals(selectedCourtDisCode)) {  // DL CANCELLATION AND IMPRISON

                    lytConFrom.setVisibility(View.VISIBLE);
                    lytConTo.setVisibility(View.VISIBLE);
                    lytConDays.setVisibility(View.VISIBLE);
                    lytFineAmnt.setVisibility(View.GONE);
                    lytSoclFrom.setVisibility(View.GONE);
                    lytSclSerTo.setVisibility(View.GONE);
                    lytRisingDays.setVisibility(View.GONE);
                    lytFirNo.setVisibility(View.GONE);
                    lytPSName.setVisibility(View.GONE);
                    lyt_DrvrExpredDate.setVisibility(View.GONE);
                    rdoBtn_DLCAN.setChecked(true);
                    rdoBtn_DLSUS.setKeyListener(null);
                    rdoBtnNONE.setKeyListener(null);
                    clearData();

                } else if ("13".equals(selectedCourtDisCode)) {
                    lytConFrom.setVisibility(View.GONE);
                    lytConTo.setVisibility(View.GONE);
                    lytConDays.setVisibility(View.GONE);
                    lytSoclFrom.setVisibility(View.VISIBLE);
                    lytSclSerTo.setVisibility(View.VISIBLE);
                    lytRisingDays.setVisibility(View.GONE);
                    lytFirNo.setVisibility(View.GONE);
                    lytPSName.setVisibility(View.GONE);
                    lyt_DrvrExpredDate.setVisibility(View.GONE);
                    lytFineAmnt.setVisibility(View.VISIBLE);
                    rdoBtnNONE.setChecked(true);
                    clearData();
                } else {
                    lytConFrom.setVisibility(View.GONE);
                    lytConTo.setVisibility(View.GONE);
                    lytConDays.setVisibility(View.GONE);
                    lytFineAmnt.setVisibility(View.GONE);
                    lytSoclFrom.setVisibility(View.GONE);
                    lytSclSerTo.setVisibility(View.GONE);
                    lytRisingDays.setVisibility(View.GONE);
                    rdoBtnNONE.setChecked(true);
                    rdoBtn_DLSUS.setKeyListener(null);
                    rdoBtn_DLCAN.setKeyListener(null);
                    clearData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> owner_dataAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mArrayListCourtDis);
        owner_dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        owner_courtDisSpinner.setAdapter(owner_dataAdapter1);
        owner_courtDisSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String courtDis = owner_courtDisSpinner.getSelectedItem().toString();
                for (String mapCourtName : paramsCourtdis.keySet()) {
                    if (courtDis.equals(mapCourtName)) {
                        owner_selectedCourtDisCode = paramsCourtdis.get(mapCourtName);
                        break;
                    }
                }

                if ("1".equals(owner_selectedCourtDisCode) || "15".equals(owner_selectedCourtDisCode)) {
                    clearOwnerData();
                    owner_lytConvtdFrom.setVisibility(View.GONE);
                    owner_lytConvtdTo.setVisibility(View.GONE);
                    owner_lytSoclFrom.setVisibility(View.GONE);
                    owner_lytSclSerTo.setVisibility(View.GONE);
                    owner_lytRisingDays.setVisibility(View.GONE);
                    owner_lytFineAmnt.setVisibility(View.VISIBLE);

                } else if ("5".equals(owner_selectedCourtDisCode)) {

                    owner_lytConvtdFrom.setVisibility(View.GONE);
                    owner_lytConvtdTo.setVisibility(View.GONE);
                    owner_lytSoclFrom.setVisibility(View.GONE);
                    owner_lytSclSerTo.setVisibility(View.GONE);
                    owner_lytRisingDays.setVisibility(View.VISIBLE);
                    owner_lytFineAmnt.setVisibility(View.VISIBLE);
                    clearOwnerData();

                } else if ("7".equals(owner_selectedCourtDisCode)) {

                    owner_lytConvtdFrom.setVisibility(View.GONE);
                    owner_lytConvtdTo.setVisibility(View.GONE);
                    owner_lytSoclFrom.setVisibility(View.VISIBLE);
                    owner_lytSclSerTo.setVisibility(View.VISIBLE);
                    owner_lytRisingDays.setVisibility(View.VISIBLE);
                    owner_lytFineAmnt.setVisibility(View.VISIBLE);
                    clearOwnerData();

                } else if ("3".equals(owner_selectedCourtDisCode) || "17".equals(owner_selectedCourtDisCode)) {

                    owner_lytConvtdFrom.setVisibility(View.VISIBLE);
                    owner_lytConvtdTo.setVisibility(View.VISIBLE);
                    owner_lytSoclFrom.setVisibility(View.GONE);
                    owner_lytSclSerTo.setVisibility(View.GONE);
                    owner_lytRisingDays.setVisibility(View.GONE);
                    owner_lytFineAmnt.setVisibility(View.VISIBLE);
                    clearOwnerData();

                } else if ("2".equals(owner_selectedCourtDisCode) || "16".equals(owner_selectedCourtDisCode)) {  // ONLY IMPRISONMENT

                    owner_lytConvtdFrom.setVisibility(View.VISIBLE);
                    owner_lytConvtdTo.setVisibility(View.VISIBLE);
                    owner_lytSoclFrom.setVisibility(View.GONE);
                    owner_lytSclSerTo.setVisibility(View.GONE);
                    owner_lytRisingDays.setVisibility(View.GONE);
                    owner_lytFineAmnt.setVisibility(View.GONE);

                } else if ("4".equals(owner_selectedCourtDisCode)) {

                    owner_lytConvtdFrom.setVisibility(View.GONE);
                    owner_lytConvtdTo.setVisibility(View.GONE);
                    owner_lytSoclFrom.setVisibility(View.GONE);
                    owner_lytSclSerTo.setVisibility(View.GONE);
                    owner_lytRisingDays.setVisibility(View.VISIBLE);
                    owner_lytFineAmnt.setVisibility(View.GONE);
                    clearOwnerData();

                } else if ("8".equals(owner_selectedCourtDisCode)) {  //ONLY SOCIAL SERVICE

                    owner_lytConvtdFrom.setVisibility(View.GONE);
                    owner_lytConvtdTo.setVisibility(View.GONE);
                    owner_lytSoclFrom.setVisibility(View.VISIBLE);
                    owner_lytSclSerTo.setVisibility(View.VISIBLE);
                    owner_lytRisingDays.setVisibility(View.GONE);
                    owner_lytFineAmnt.setVisibility(View.GONE);
                    clearOwnerData();


                } else if ("9".equals(owner_selectedCourtDisCode)) {  //DL SUSPENSION AND FINE

                    owner_lytConvtdFrom.setVisibility(View.GONE);
                    owner_lytConvtdTo.setVisibility(View.GONE);
                    owner_lytSoclFrom.setVisibility(View.GONE);
                    owner_lytSclSerTo.setVisibility(View.GONE);
                    owner_lytRisingDays.setVisibility(View.GONE);
                    owner_lytFineAmnt.setVisibility(View.GONE);
                    clearOwnerData();

                } else if ("10".equals(owner_selectedCourtDisCode)) {  // DL SUSPENSION AND IMPRISONMENT

                    owner_lytConvtdFrom.setVisibility(View.GONE);
                    owner_lytConvtdTo.setVisibility(View.GONE);
                    owner_lytSoclFrom.setVisibility(View.GONE);
                    owner_lytSclSerTo.setVisibility(View.GONE);
                    owner_lytRisingDays.setVisibility(View.GONE);
                    owner_lytFineAmnt.setVisibility(View.GONE);
                    clearOwnerData();

                } else if ("11".equals(owner_selectedCourtDisCode)) {  // DL CANCELLATION AND FINE

                    owner_lytConvtdFrom.setVisibility(View.GONE);
                    owner_lytConvtdTo.setVisibility(View.GONE);
                    owner_lytSoclFrom.setVisibility(View.GONE);
                    owner_lytSclSerTo.setVisibility(View.GONE);
                    owner_lytRisingDays.setVisibility(View.GONE);
                    owner_lytFineAmnt.setVisibility(View.GONE);
                    clearOwnerData();

                } else if ("12".equals(owner_selectedCourtDisCode)) {  // DL CANCELLATION AND IMPRISON

                    clearOwnerData();
                    owner_lytConvtdFrom.setVisibility(View.GONE);
                    owner_lytConvtdTo.setVisibility(View.GONE);
                    owner_lytSoclFrom.setVisibility(View.GONE);
                    owner_lytSclSerTo.setVisibility(View.GONE);
                    owner_lytRisingDays.setVisibility(View.GONE);
                    owner_lytFineAmnt.setVisibility(View.GONE);

                } else if ("13".equals(owner_selectedCourtDisCode)) {

                    owner_lytConvtdFrom.setVisibility(View.GONE);
                    owner_lytConvtdTo.setVisibility(View.GONE);
                    owner_lytSoclFrom.setVisibility(View.VISIBLE);
                    owner_lytSclSerTo.setVisibility(View.VISIBLE);
                    owner_lytRisingDays.setVisibility(View.GONE);
                    owner_lytFineAmnt.setVisibility(View.VISIBLE);
                    clearOwnerData();
                } else {
                    owner_lytConvtdFrom.setVisibility(View.GONE);
                    owner_lytConvtdTo.setVisibility(View.GONE);
                    owner_lytSoclFrom.setVisibility(View.GONE);
                    owner_lytSclSerTo.setVisibility(View.GONE);
                    owner_lytRisingDays.setVisibility(View.GONE);
                    owner_lytFineAmnt.setVisibility(View.GONE);
                    clearOwnerData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        db = new DBHelper(getApplicationContext());

        btn_dp_date_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(PRESENT_DATE_PICKER);
                // btn_dp_date_selection.setText("" + present_date_toSend.toUpperCase());
            }
        });

        btn_courtAttenddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(PRESENT_COURT_ATTEND_DATE);
            }
        });

        owner_btn_CrtAtndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(OWNER_COURT_ATTEND_DATE);
            }
        });


        btn_courtConFromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(PRESENT_COURT_CONVI_FROM);
                // btn_courtConFromdate.setText("" + present_date_toSend.toUpperCase());
            }
        });

        btn_courtConTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(PRESENT_COURT_CONVI_TO);
                //btn_courtConTo.setText("" + present_date_toSend.toUpperCase());
            }
        });

        owner_btn_courtConFromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(OWNER_COURT_CONVI_FROM);
            }
        });

        owner_btn_courtConTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(OWNER_COURT_CONVI_TO);
                //btn_courtConTo.setText("" + present_date_toSend.toUpperCase());
            }
        });

        btn_courtSoclServceFromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(PRESENT_COURT_SCL_SRC_FROM);
                //btn_courtConTo.setText("" + present_date_toSend.toUpperCase());
            }
        });

        owner_btn_courtSoclServceFromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(OWNER_COURT_SCL_SRC_FROM);
            }
        });
        owner_btn_courtSoclServceTodate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(OWNER_COURT_SCL_SRC_TO);
            }
        });

        btn_courtSoclServceTodate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(PRESENT_COURT_SCL_SRC_TO);
                //btn_courtConTo.setText("" + present_date_toSend.toUpperCase());
            }
        });
        btn_DLSUS_FromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(PRESENT_DL_SUS_FROM);
            }
        });
        btn_DLSUS_ToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(PRESENT_DL_SUS_TO);
            }
        });
        btn_Dl_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(PRESENT_DD_DL_DOB);
            }
        });

        btn_dateSeltion_DL_Canln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(PRESENT_DD_DL_CanCel);
            }
        });

        imgView_CourtOrderCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selection_Pic_Flag = "1";
                selectImage();
            }
        });

        imgView_DLCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selection_Pic_Flag = "2";
                selectImage();
            }
        });

        minor_img_CourtCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selection_Pic_Flag = "3";
                selectImage();
            }
        });

        owner_img_CourtCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selection_Pic_Flag = "4";
                selectImage();
            }
        });


        btn_dp_get_onlinedetials.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (et_dp_regno.getText().toString().trim().equals("")) {
                    et_dp_regno.setError(Html.fromHtml("<font color='black'>Enter Vehicle No</font>"));
                } else if (btn_dp_date_selection.getText().toString().equals("Select Date")) {
                    showToast("Select Date");
                } else {
                    if (isOnline()) {
                        new Async_getDD_details().execute();
                    } else {
                        showToast("Please check your network connection!");
                    }
                }
            }
        });

        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String court_Code = selectedCourtCode;
                String court_Attnd_Date = date_courtAtnd;
                String sTS_No = edtTxt_STC_No.getText().toString();
                String court_Disposal_code = selectedCourtDisCode;
                String fineAmnt = edtTxt_FineAmnt.getText().toString();
                dl_SusDays = edtTxt_DL_SUSDAYS.getText().toString();
                if (dl_SUS.equals("Y") || dl_CAN.equals("Y")) {
                    dl_SUS_Status = "Y";
//                    dl_SUS = dl_SUS + "!" + dl_SusDays;
                } else {
                    dl_SUS_Status = "N";
                }

                String date_ConFrom = date_convFRom;
                String date_ConTo = date_convicTo;
                String con_Days = edtTxtConDays.getText().toString();
                String rising_days = edtTxtRisDays.getText().toString();
                String vhcleRelse = vehcleRelse;
                String remarks = edtTxt_Remarks.getText().toString();
                // String mob_No=edtTxt_Mob_No.getText().toString();
                // String adhar_No=edtTxt_Aadhar_No.getText().toString();
                // String dl_no=edtTxt_DlNo.getText().toString();
                String dl = driver_LCNCE;
                String ml = driver_Mobile;
                String aN = driver_Adhar;
                String dl_dob = driver_DL_DOB;
//                driver_LCNCE=edtTxt_DlNo.getText().toString();
//                driver_Mobile=edtTxt_Mob_No.getText().toString();
//                driver_Adhar=edtTxt_Aadhar_No.getText().toString();
                if (("null").equals(dl) || ("").equals(dl)) {
                    driver_LCNCE = edtTxt_DlNo.getText().toString();
                }
                if (("null").equals(ml) || ("").equals(ml)) {
                    driver_Mobile = edtTxt_Mob_No.getText().toString();
                }
                if (("null").equals(aN) || ("").equals(aN)) {
                    driver_Adhar = edtTxt_Aadhar_No.getText().toString();
                }
                if (("null").equals(dl_dob) || ("").equals(dl_dob)) {
                    driver_DL_DOB = btn_Dl_dob.getText().toString();
                }

                if (court_Code == null) {
                    showToast("Select Court Name");
                } else if (driver_Mobile.trim().equals("")) {
                    edtTxt_Mob_No.setError(Html.fromHtml("<font color='white'>Enter Mobile Number </font>"));
                    edtTxt_Mob_No.requestFocus();
                } else if (driver_Mobile.length() != 10) {
                    edtTxt_Mob_No.setError(Html.fromHtml("<font color='white'>Enter Valid Mobile Number </font>"));
                    edtTxt_Mob_No.requestFocus();
                } else if (dl_SUS_Status.equals("Y") && (!driver_LCNCE.equals("null")) && (driver_LCNCE.length() <= 3)) {
                    edtTxt_DlNo.setError(Html.fromHtml("<font color='white'>Enter valid Dl Number </font>"));
                    edtTxt_DlNo.requestFocus();
                } else if (dl_SUS_Status.equals("Y") && (btn_Dl_dob.getText().toString().equals("Select Date"))) {
                    showToast("Select Dl Date!");
                } else if (dl_SUS.equals("Y") && btn_DLSUS_FromDate.getText().toString().equals("Select Date")) {
                    showToast("Please Select DL Suspention From Date!");
                } else if (dl_SUS.equals("Y") && btn_DLSUS_ToDate.getText().toString().equals("Select Date")) {
                    showToast("Please Select DL Suspention To Date!");
                } else if (dl_SUS.equals("Y") && img_dataCourtCopy == null) {
                    showToast("Please Select/ Capture the Megistrate Copy");
                } else if (dl_SUS.equals("Y") && img_dataDLCopy == null) {
                    showToast("Please Select/Capture DL Copy!");
                } else if (dl_CAN.equals("Y") && btn_dateSeltion_DL_Canln.getText().toString().equals("Select Date")) {
                    showToast("Please Select DL Cancellation Date!");
                } else if (dl_CAN.equals("Y") && img_dataCourtCopy == null) {
                    showToast("Please Select/ Capture the Magistrate Copy");
                } else if (dl_CAN.equals("Y") && img_dataDLCopy == null) {
                    showToast("Please Select/Capture DL Copy!");
                } else if (btn_courtAttenddate.getText().toString().equals("Select Date")) {
                    showToast("Select Court Attended Date");
                } else if (sTS_No.trim().equals("")) {
                    edtTxt_STC_No.setError(Html.fromHtml("<font color='white'>Enter STC No</font>"));
                    edtTxt_STC_No.requestFocus();
                }/* else if (dl_SusDays.trim().equals("") && dl_SUS.equals("Y")) {
                    edtTxt_DL_SUSDAYS.setError(Html.fromHtml("<font color='white'>Enter Suspention Days</font>"));
                    edtTxt_DL_SUSDAYS.requestFocus();
                }*/ else if (court_Disposal_code == null) {
                    showToast("Select court Disposal Name");
                } else if (owner_selectedCourtCode == null && minor_Dvng_Flag) {
                    showToast("Select Owner Court Name");
                } else if (owner_btn_CrtAtndDate.getText().toString().equals("Select Date") && minor_Dvng_Flag) {
                    showToast("Select Owner Court Attended Date");
                } else if (owner_edtTxt_STC_NO.getText().toString().isEmpty() && minor_Dvng_Flag) {
                    owner_edtTxt_STC_NO.setError("Please enter the Owner STC No!");
                    owner_edtTxt_STC_NO.requestFocus();
                } else if (!owner_edtTxt_Contct_NO.getText().toString().isEmpty() && minor_Dvng_Flag && owner_edtTxt_Contct_NO.getText().toString().length() != 10) {
                    owner_edtTxt_Contct_NO.setError("Please enter the Owner Contact No!");
                    owner_edtTxt_Contct_NO.requestFocus();
                } else if (owner_selectedCourtDisCode == null && minor_Dvng_Flag) {
                    showToast("Select Owner Disposal Name");
                } else if (owner_edtTxt_FineAmnt.isShown() && owner_edtTxt_FineAmnt.getText().toString().equals("")) {
                    owner_edtTxt_FineAmnt.setError("Please Enter Owner Fine Amount !");
                    owner_edtTxt_FineAmnt.requestFocus();
                } else if (owner_btn_courtConFromdate.isShown() && owner_btn_courtConFromdate.getText().toString().equals("Select Date")) {
                    showToast("Select Owner Conviction From Date!");
                } else if (owner_btn_courtConTo.isShown() && owner_btn_courtConTo.getText().toString().equals("Select Date")) {
                    showToast("Select Owner Conviction To Date!");
                } else if (owner_btn_courtSoclServceFromdate.isShown() && owner_btn_courtSoclServceFromdate.getText().toString().equals("Select Date")) {
                    showToast("Select Owner Social Service From Date!");
                } else if (owner_btn_courtSoclServceTodate.isShown() && owner_btn_courtSoclServceTodate.getText().toString().equals("Select Date")) {
                    showToast("Select Owner Social Service To Date!");
                } else if (owner_edtTxt_RisingDays.isShown()&& owner_edtTxt_RisingDays.getText().toString().isEmpty()){
                    owner_edtTxt_RisingDays.setError("Please enter the Owner Rising Days!");
                    owner_edtTxt_RisingDays.requestFocus();
                }else if (minor_Dvng_Flag && driver_ImgData == null) {
                    showToast("Please Select/Capture Owner Magistrate Copy!");
                } else if (minor_Dvng_Flag && minor_ImgData == null) {
                    showToast("Please Select/Capture Driver Magistrate Copy!");
                } else if ("3".equals(court_Disposal_code) || "17".equals(court_Disposal_code)) {
                    if (btn_courtConFromdate.getText().toString().equals("Select Date")) {
                        showToast("Please select Court Convicted From date");
                    } else if (fineAmnt.trim().equals("")) {
                        edtTxt_FineAmnt.setError(Html.fromHtml("<font color='black'>Enter Fine Amount </font>"));
                    } else if (btn_courtConTo.getText().toString().equals("Select Date")) {
                        showToast("Please select Court Convicted To date");
                    } else if (con_Days.trim().equals("")) {
                        edtTxtConDays.setError(Html.fromHtml("<font color='black'>Enter Convicted Days </font>"));
                    } else {
                        new Async_getCourtClosingUpdateTicketInfo().execute();
                    }

                } else if (Objects.requireNonNull(court_Disposal_code).equals("1") || court_Disposal_code.equals("11") || court_Disposal_code.equals("9")
                        || court_Disposal_code.equals("15")) {

                    if (fineAmnt.trim().equals("")) {
                        edtTxt_FineAmnt.setError(Html.fromHtml("<font color='white'>Enter Fine Amount </font>"));
                        edtTxt_FineAmnt.requestFocus();
                    } else {
                        new Async_getCourtClosingUpdateTicketInfo().execute();
                    }

                } else if (court_Disposal_code.equals("5")) {

                    if (fineAmnt.trim().equals("")) {
                        edtTxt_FineAmnt.setError(Html.fromHtml("<font color='white'>Enter Fine Amount </font>"));
                    } else if (rising_days.trim().equals("")) {
                        edtTxtRisDays.setError(Html.fromHtml("<font color='white'>Enter Convicted Days </font>"));
                        edtTxtRisDays.requestFocus();
                    } else {
                        if (isOnline()) {
                            new Async_getCourtClosingUpdateTicketInfo().execute();
                        } else {
                            showToast("Please check your network connection");
                        }

                    }

                } else if (court_Disposal_code.equals("7")) {

                    if (btn_courtSoclServceFromdate.getText().toString().equals("Select Date")) {
                        showToast("Please select Court Social Service From date");
                    } else if (fineAmnt.trim().equals("")) {
                        edtTxt_FineAmnt.setError(Html.fromHtml("<font color='white'>Enter Fine Amount </font>"));
                        edtTxt_FineAmnt.requestFocus();
                    } else if (btn_courtSoclServceTodate.getText().toString().equals("Select Date")) {
                        showToast("Please select Court Social Service To date");
                    } else if (rising_days.trim().equals("")) {
                        edtTxtRisDays.setError(Html.fromHtml("<font color='white'>Enter Convicted Days </font>"));
                        edtTxtRisDays.requestFocus();
                    } else {
                        new Async_getCourtClosingUpdateTicketInfo().execute();


                    }

                } else if (court_Disposal_code.equals("13")) {

                    if (btn_courtSoclServceFromdate.getText().toString().equals("Select Date")) {
                        showToast("Please select Court Social Service From date");
                    } else if (fineAmnt.trim().equals("")) {
                        edtTxt_FineAmnt.setError(Html.fromHtml("<font color='white'>Enter Fine Amount </font>"));
                        edtTxt_FineAmnt.requestFocus();
                    } else if (btn_courtSoclServceTodate.getText().toString().equals("Select Date")) {
                        showToast("Please select Court Social Service To date");
                    } else {
                        new Async_getCourtClosingUpdateTicketInfo().execute();

                    }

                } else if (court_Disposal_code.equals("8")) {

                    if (btn_courtSoclServceFromdate.getText().toString().equals("Select Date")) {
                        showToast("Please select Court Social Service From date");
                    } else if (btn_courtSoclServceTodate.getText().toString().equals("Select Date")) {
                        showToast("Please select Court Social Service To date");
                    } else {
                        new Async_getCourtClosingUpdateTicketInfo().execute();

                    }

                } else if (court_Disposal_code.equals("2") || court_Disposal_code.equals("12") || court_Disposal_code.equals("10") || court_Disposal_code.equals("16")) {

                    if (btn_courtConFromdate.getText().toString().equals("Select Date")) {
                        showToast("Please select Court Convicted From date");
                    } else if (btn_courtConTo.getText().toString().equals("Select Date")) {
                        showToast("Please select Court Convicted To date");
                    } else if (con_Days.trim().equals("")) {
                        edtTxtConDays.setError(Html.fromHtml("<font color='white'>Enter Convicted Days </font>"));
                        edtTxtConDays.requestFocus();
                    } else {
                        new Async_getCourtClosingUpdateTicketInfo().execute();
                    }

                } else if (court_Disposal_code.equals("4")) {

                    if (rising_days.trim().equals("")) {
                        edtTxtRisDays.setError(Html.fromHtml("<font color='white'>Enter the Days </font>"));
                        edtTxtRisDays.requestFocus();
                    } else {
                        new Async_getCourtClosingUpdateTicketInfo().execute();
                    }

                } else {
                    new Async_getCourtClosingUpdateTicketInfo().execute();
                }

            }
        });
    }

    public void clearData() {
        edtTxt_FineAmnt.setText("");
        edtTxtConDays.setText("");
        edtTxtRisDays.setText("");
        edtTxt_DL_SUSDAYS.setText("");
        edtTxt_FirNo.setText("");
        edtTxt_PSName.setText("");
        btn_courtConFromdate.setText("Select Date");
        btn_courtConTo.setText("Select Date");
        btn_courtSoclServceFromdate.setText("Select Date");
        btn_courtSoclServceTodate.setText("Select Date");
        btn_DLSUS_FromDate.setText("Select Date");
        btn_DLSUS_ToDate.setText("Select Date");
        btn_DrExpDate.setText("Select Date");
        imgView_CourtOrderCopy.setImageResource(R.drawable.courtorder);
        imgView_DLCopy.setImageResource(R.drawable.dlcopy);
        img_dataCourtCopy = null;
        img_dataDLCopy = null;
    }

    public void clearOwnerData() {
        owner_edtTxt_FineAmnt.setText("");
        owner_edtTxt_RisingDays.setText("");
        owner_btn_courtConFromdate.setText("Select Date");
        owner_btn_courtConTo.setText("Select Date");
        owner_btn_courtSoclServceFromdate.setText("Select Date");
        owner_btn_courtSoclServceTodate.setText("Select Date");
        owner_img_CourtCopy.setImageResource(R.drawable.courtorder);
        minor_img_CourtCopy.setImageResource(R.drawable.courtorder);
        minor_ImgData = null;
        driver_ImgData = null;
    }

    public Boolean isOnline() {
        ConnectivityManager conManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwInfo = conManager.getActiveNetworkInfo();
        return nwInfo != null;
    }

    public void getCourtDisNamesFromDB() {
        try {
            db.open();
            cursor_court_Disnames = DBHelper.db.rawQuery("select * from " + db.court_disName_table, null);
            if (cursor_court_Disnames.moveToFirst()) {
                paramsCourtdis = new HashMap<String, String>();
                while (!cursor_court_Disnames.isAfterLast()) {
                    mArrayListCourtDis.add(cursor_court_Disnames.getString(cursor_court_Disnames.getColumnIndex(db.court_dis_name_settings))); //add the item
                    paramsCourtdis.put(cursor_court_Disnames.getString(cursor_court_Disnames.getColumnIndex(db.court_dis_name_settings)), cursor_court_Disnames.getString(cursor_court_Disnames.getColumnIndex(db.court_dis_code_settings)));
                    cursor_court_Disnames.moveToNext();
                    maparrayCourtdis.add(paramsCourtdis);
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        cursor_court_Disnames.close();
        db.close();
    }

    public void getCourtNamesFromDB() {
        try {
            db.open();
            cursor_courtnames = DBHelper.db.rawQuery("select * from " + db.courtName_table, null);


//            if (cursor_court_Disnames.getCount() == 0) {
//                showToast("Please download master's !");
//            } else {

            if (cursor_courtnames.moveToFirst()) {
                paramsCourt = new HashMap<String, String>();
                while (!cursor_courtnames.isAfterLast()) {
                    mArrayListCourtNames.add(cursor_courtnames.getString(cursor_courtnames.getColumnIndex(db.court_name_settings)));

                    paramsCourt.put(cursor_courtnames.getString(cursor_courtnames.getColumnIndex(db.court_name_settings)), cursor_courtnames.getString(cursor_courtnames.getColumnIndex(db.court_code_settings)));
                    cursor_courtnames.moveToNext();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cursor_courtnames.close();
        db.close();
    }

    @SuppressLint("StaticFieldLeak")
    private class Async_getCourtClosingUpdateTicketInfo extends AsyncTask<Void, Void, String> {
        @SuppressLint("DefaultLocale")
        @SuppressWarnings("unused")
        @Override
        protected String doInBackground(Void... params) {

            try {
                if (btn_Dl_dob.getText().toString().equals("Select Date")) {
                    driver_DL_DOB = "";
                } else {
                    driver_DL_DOB = "";
                    driver_DL_DOB = btn_Dl_dob.getText().toString();
                }

                caseDisposal = new CourtCaseDisposal();
                caseDisposal.setEticketNo(chall_No);
                caseDisposal.setRegnNo(vEHICLE_NUMBER);
                caseDisposal.setPidCD(MainActivity.user_id);
                caseDisposal.setPidName(MainActivity.arr_logindetails[1]);
                caseDisposal.setReleaseDT("");
                caseDisposal.setChallanType(chall_Type);
                caseDisposal.setViolations(violations);
                caseDisposal.setUnitCode(unit_CODE);
                caseDisposal.setPsCode(ps_CODE);
                caseDisposal.setOfficerPid(pid_CODE);
                caseDisposal.setOffenceDate(offence_Date);
                caseDisposal.setVehRelease("Y");
                caseDisposal.setDdRemarks(edtTxt_Remarks.getText().toString().trim());
                caseDisposal.setMinorFlag(minor_Dvng_Flag ? "Y" : "N");

                caseDisposal.setDriver_dlNO(driver_LCNCE.toUpperCase());
                caseDisposal.setDriver_dlDob(driver_DL_DOB);
                caseDisposal.setDriver_aadhaarNO(driver_Adhar);
                caseDisposal.setDriver_mobileNo(driver_Mobile);
                caseDisposal.setDriver_stcNO(edtTxt_STC_No.getText().toString().trim());
                caseDisposal.setDriver_courtCD(selectedCourtCode);
                caseDisposal.setDriver_courtDispCD(selectedCourtDisCode);
                caseDisposal.setDriver_imprisFrom(date_convFRom);
                caseDisposal.setDriver_imprisTo(date_convicTo);
                caseDisposal.setDriver_imprisDays(edtTxtConDays.getText().toString().trim());
                caseDisposal.setDriver_courtFine(edtTxt_FineAmnt.getText().toString().trim());
                caseDisposal.setDriver_risingDetails(edtTxtRisDays.getText().toString().trim());
                caseDisposal.setDriver_coutrAttnDT(date_courtAtnd);
                caseDisposal.setDriver_dlRelease("");
                caseDisposal.setDriver_dlSusp(dl_SUS);
                caseDisposal.setDriver_dlCancel(dl_CAN);
                caseDisposal.setDriver_SuspensionFromDate(date_DL_SUS_FROM);
                caseDisposal.setDriver_SuspensionToDate(date_DL_SUS_TO);
                caseDisposal.setDriver_noOfDaysSuspended(dl_SusDays);
                caseDisposal.setDriver_imgMegistrateCopy(img_dataCourtCopy);
                caseDisposal.setDriver_imgDlCopy(img_dataDLCopy);

                if (minor_Dvng_Flag) {
                    caseDisposal.setOwner_COURT_FINE(owner_edtTxt_FineAmnt.getText().toString().trim());
                    caseDisposal.setOwner_CONVICTION_DETAILS(ownerConDays);
                    caseDisposal.setOwner_CONTACT_NO(owner_edtTxt_Contct_NO.getText().toString().trim());
                    caseDisposal.setOwner_CONVICTION_FROM_DT(owner_date_convFRom);
                    caseDisposal.setOwner_CONVICTION_TO_DT(owner_date_convicTo);
                    caseDisposal.setOwner_COURT_CASE_NO(owner_edtTxt_STC_NO.getText().toString().trim());
                    caseDisposal.setOwner_RISING_DETAILS(owner_edtTxt_RisingDays.getText().toString().trim());
                    caseDisposal.setOwner_COURT_CD(owner_selectedCourtCode);
                    caseDisposal.setOwner_DISPOSAL_CD(owner_selectedCourtDisCode);
                    caseDisposal.setOwner_COURT_PC(pid_CODE);
                    caseDisposal.setOwner_D_OF_PAY(owner_btn_CrtAtndDate.getText().toString());
                    caseDisposal.setOwner_LICENSE_NO("");
                    caseDisposal.setDriver_imgMegistrateCopy(minor_ImgData);
                    caseDisposal.setDriver_imgDlCopy(driver_ImgData);
                }

                Gson gson = new Gson();
                String disposal_Data = gson.toJson(caseDisposal);
                ServiceHelper.getCourtDisposalTicketInfo(disposal_Data);

            } catch (Exception e) {
                e.printStackTrace();
                showToast("Please check network and try again!");
            }
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);
            try {
                if (null != ServiceHelper.Opdata_Chalana && !ServiceHelper.Opdata_Chalana.equals("NA") &&
                        !"0".equals(ServiceHelper.Opdata_Chalana) && !"1".equals(ServiceHelper.Opdata_Chalana)) {
                    sucessFull_DialogMSG(ServiceHelper.Opdata_Chalana);
                    tckt_UPdated_Flag = "Y";
                } else {
                    sucessFull_DialogMSG("Updation Failed \n Please try again");
                    tckt_UPdated_Flag = "N";
                }
            } catch (Exception e) {
                e.printStackTrace();
                sucessFull_DialogMSG("Updation Failed \n Please try again");
                tckt_UPdated_Flag = "N";
            }
        }
    }

    public void sucessFull_DialogMSG(String msg) {
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
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DDCloseActiviy.this, AlertDialog.THEME_HOLO_LIGHT);
        alertDialogBuilder.setCustomTitle(title);
        alertDialogBuilder.setIcon(R.drawable.dialog_logo);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (tckt_UPdated_Flag.equals("Y")) {
                    Intent intent = new Intent(getApplicationContext(), DDCloseActiviy.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    alertDialogBuilder.create().dismiss();
                }
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().getAttributes();
        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        textView.setTextSize(22);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setGravity(Gravity.CENTER);
        Button btn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        btn.setTextSize(22);
        btn.setTextColor(Color.WHITE);
        btn.setTypeface(btn.getTypeface(), Typeface.BOLD);
        btn.setBackgroundColor(Color.BLUE);
    }

    @SuppressLint("StaticFieldLeak")
    private class Async_getDD_details extends AsyncTask<Void, Void, String> {
        @SuppressLint("DefaultLocale")
        @SuppressWarnings("unused")
        @Override
        protected String doInBackground(Void... params) {
            ServiceHelper.getCourtClosingTicketInfo("" + et_dp_regno.getText().toString().trim().toUpperCase(), "" + present_date_toSend);
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
            dd_lyt.setVisibility(View.GONE);
            pay_dd_lyt.setVisibility(View.GONE);
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);

            online_report_status = "";
            if (null != ServiceHelper.Opdata_Chalana && !ServiceHelper.Opdata_Chalana.equals("NA")) {

                try {
                    Log.d("DDClose_Data", "" + ServiceHelper.Opdata_Chalana);
                    JSONObject jsonObject = new JSONObject(ServiceHelper.Opdata_Chalana);
                    JSONArray jsonArray = jsonObject.getJSONArray("Challan Details");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        if (jsonObject1.toString().isEmpty() || jsonObject1.length() == 0) {
                            showToast("No data Found");
                        } else {
                            dd_lyt.setVisibility(View.GONE);
                            pay_dd_lyt.setVisibility(View.VISIBLE);
                            lytConFrom.setVisibility(View.GONE);
                            lytConTo.setVisibility(View.GONE);
                            lytConDays.setVisibility(View.GONE);
                            lytFineAmnt.setVisibility(View.GONE);
                            owner_Lyt_Pay_details.setVisibility(View.GONE);
                            vEHICLE_NUMBER = jsonObject1.getString("VEHICLE NUMBER");
                            chall_No = jsonObject1.getString("CHALLAN NUMBER");
                            chall_Type = jsonObject1.getString("CHALLAN_TYPE");
                            violations = jsonObject1.getString("VIOLATIONS");
                            offence_Date = jsonObject1.getString("OFFENCE DATE");
                            driver_Adhar = jsonObject1.getString("DRIVER AADHAAR");
                            driver_Mobile = jsonObject1.getString("DRIVER MOBILE");
                            driver_LCNCE = jsonObject1.getString("DRIVING LICENSE");
                            driver_DL_DOB = jsonObject1.getString("DRIVER_LICENSE_DOB");
                            unit_CODE = jsonObject1.getString("UNIT_CODE");
                            pid_CODE = jsonObject1.getString("PID_CODE");
                            ps_CODE = jsonObject1.getString("PS_CODE");
                            String offence_codes = jsonObject1.getString("OFFENCE_CODES");
                            minor_Dvng_Flag = offence_codes.contains("30");
                            if (minor_Dvng_Flag) {
                                owner_Lyt_Pay_details.setVisibility(View.VISIBLE);
                            } else {
                                owner_Lyt_Pay_details.setVisibility(View.GONE);
                            }
                            Reg_No.setText(vEHICLE_NUMBER);
                            Challan_No.setText(chall_No);
                            Offender_Date.setText(offence_Date);
                            if (!Objects.equals("null", driver_Mobile)) {
                                edtTxt_Mob_No.setVisibility(View.GONE);
                                Mobile_No.setVisibility(View.VISIBLE);
                                Mobile_No.setText(driver_Mobile);
                            } else {
                                edtTxt_Mob_No.setVisibility(View.VISIBLE);
                                Mobile_No.setVisibility(View.GONE);
                            }
                            if (!Objects.equals("null", driver_Adhar)) {
                                edtTxt_Aadhar_No.setVisibility(View.GONE);
                                Aadhar_No.setVisibility(View.VISIBLE);
                                Aadhar_No.setText(driver_Adhar);
                            } else {
                                edtTxt_Aadhar_No.setVisibility(View.VISIBLE);
                                Aadhar_No.setVisibility(View.GONE);
                            }
                            if (!Objects.equals("null", driver_LCNCE)) {
                                edtTxt_DlNo.setVisibility(View.GONE);
                                Dl_No.setVisibility(View.VISIBLE);
                                Dl_No.setText(driver_LCNCE);

                            } else {
                                edtTxt_DlNo.setVisibility(View.VISIBLE);
                                Dl_No.setVisibility(View.GONE);
                            }

                            if (!Objects.equals("null", driver_DL_DOB) && (!Objects.equals("null", driver_LCNCE))) {
                                btn_Dl_dob.setText(driver_DL_DOB);

                            } else {
                                btn_Dl_dob.setText("Select Date");
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("" + getResources().getString(R.string.no_day_report));
                    dd_lyt.setVisibility(View.GONE);
                    pay_dd_lyt.setVisibility(View.GONE);
                }

            } else {
                online_report_status = "";
                showToast("" + getResources().getString(R.string.no_day_report));
                dd_lyt.setVisibility(View.GONE);
                pay_dd_lyt.setVisibility(View.GONE);
            }
        }
    }

    protected void selectImage() {
        // TODO Auto-generated method stub
        if (selection_Pic_Flag.equals("1")) {
            final CharSequence[] options = {"Open Camera", "Choose from Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(DDCloseActiviy.this);
            builder.setTitle("Add Photo!");
            builder.setItems(options, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int item) {
                   /* FileProvider.getUriForFile(SpotChallan.this,
                            BuildConfig.APPLICATION_ID + ".fileProvider"*/

                    if (options[item].equals("Open Camera")) {
                        if (Build.VERSION.SDK_INT <= 23) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            startActivityForResult(intent, 1);
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(DDCloseActiviy.this,
                                    BuildConfig.APPLICATION_ID + ".provider", f));
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 1);
                        }
                    } else if (options[item].equals("Choose from Gallery")) {
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();

                    }
                }
            });
            builder.show();
        } else if (selection_Pic_Flag.equals("2")) {
            final CharSequence[] options = {"Open Camera", "Choose from Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(DDCloseActiviy.this);
            builder.setTitle("Add Photo!");
            builder.setItems(options, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Open Camera")) {
                        if (Build.VERSION.SDK_INT <= 23) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            startActivityForResult(intent, 1);
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(DDCloseActiviy.this,
                                    BuildConfig.APPLICATION_ID + ".provider", f));
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 1);
                        }
                    } else if (options[item].equals("Choose from Gallery")) {

                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();

                    }
                }
            });
            builder.show();

        } else if (selection_Pic_Flag.equals("3")) {
            final CharSequence[] options = {"Open Camera", "Choose from Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(DDCloseActiviy.this);
            builder.setTitle("Add Photo!");
            builder.setItems(options, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Open Camera")) {
                        if (Build.VERSION.SDK_INT <= 23) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            startActivityForResult(intent, 1);
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(DDCloseActiviy.this,
                                    BuildConfig.APPLICATION_ID + ".provider", f));
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 1);
                        }
                    } else if (options[item].equals("Choose from Gallery")) {

                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();

                    }
                }
            });
            builder.show();

        } else if (selection_Pic_Flag.equals("4")) {
            final CharSequence[] options = {"Open Camera", "Choose from Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(DDCloseActiviy.this);
            builder.setTitle("Add Photo!");
            builder.setItems(options, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Open Camera")) {
                        if (Build.VERSION.SDK_INT <= 23) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            startActivityForResult(intent, 1);
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(DDCloseActiviy.this,
                                    BuildConfig.APPLICATION_ID + ".provider", f));
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 1);
                        }
                    } else if (options[item].equals("Choose from Gallery")) {

                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();

                    }
                }
            });
            builder.show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String picturePath = "";
            if (requestCode == 1) {

                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    String current_date = dateUtil.getTodaysDate();
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);

                    String path = Environment.getExternalStorageDirectory() + File.separator + "COURTAPP"
                            + File.separator + current_date;
                    File camerapath = new File(path);

                    if (!camerapath.exists()) {
                        camerapath.mkdirs();
                    }
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");

                    try {
                        if ("1".equals(selection_Pic_Flag) && bitmap != null) {
                            outFile = new FileOutputStream(file);
                            Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

                            Display d = getWindowManager().getDefaultDisplay();
                            int x = d.getWidth();
                            int y = d.getHeight();
                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(mutableBitmap, y, x, true);
                            Matrix matrix = new Matrix();
                            matrix.postRotate(90);
                            mutableBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                            mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outFile);
                            outFile.flush();
                            outFile.close();
                            new SingleMediaScanner(this, file);
                            imgView_CourtOrderCopy.setRotation(0);
                            imgView_CourtOrderCopy.setImageBitmap(mutableBitmap);
                            // imgView_CourtOrderCopy.setRotation(-90);
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);
                            byteArray = bytes.toByteArray();
                            img_dataCourtCopy = Base64.encodeToString(byteArray, Base64.NO_WRAP);

                        } else if ("2".equals(selection_Pic_Flag) && bitmap != null) {
                            outFile = new FileOutputStream(file);
                            Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                            Display d = getWindowManager().getDefaultDisplay();
                            int x = d.getWidth();
                            int y = d.getHeight();
                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(mutableBitmap, y, x, true);
                            Matrix matrix = new Matrix();
                            matrix.postRotate(90);
                            mutableBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                            mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outFile);
                            outFile.flush();
                            outFile.close();
                            new SingleMediaScanner(this, file);
                            imgView_DLCopy.setRotation(0);
                            imgView_DLCopy.setImageBitmap(mutableBitmap);
                            //imgView_DLCopy.setRotation(-90);
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);
                            byteArray = bytes.toByteArray();
                            img_dataDLCopy = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                        } else if ("3".equals(selection_Pic_Flag) && bitmap != null) {
                            outFile = new FileOutputStream(file);
                            Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                            Display d = getWindowManager().getDefaultDisplay();
                            int x = d.getWidth();
                            int y = d.getHeight();
                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(mutableBitmap, y, x, true);
                            Matrix matrix = new Matrix();
                            matrix.postRotate(90);
                            mutableBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                            mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outFile);
                            outFile.flush();
                            outFile.close();
                            new SingleMediaScanner(this, file);
                            minor_img_CourtCopy.setRotation(0);
                            minor_img_CourtCopy.setImageBitmap(mutableBitmap);
                            //imgView_DLCopy.setRotation(-90);
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);
                            byteArray = bytes.toByteArray();
                            minor_ImgData = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                        } else if ("4".equals(selection_Pic_Flag) && bitmap != null) {
                            outFile = new FileOutputStream(file);
                            Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                            Display d = getWindowManager().getDefaultDisplay();
                            int x = d.getWidth();
                            int y = d.getHeight();
                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(mutableBitmap, y, x, true);
                            Matrix matrix = new Matrix();
                            matrix.postRotate(90);
                            mutableBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                            mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outFile);
                            outFile.flush();
                            outFile.close();
                            new SingleMediaScanner(this, file);
                            owner_img_CourtCopy.setRotation(0);
                            owner_img_CourtCopy.setImageBitmap(mutableBitmap);
                            //imgView_DLCopy.setRotation(-90);
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);
                            byteArray = bytes.toByteArray();
                            driver_ImgData = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                        } else {
                            showToast("Image Cannot be Loaded !");
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        img_dataCourtCopy = "";
                        minor_ImgData = null;


                    } catch (IOException e) {
                        e.printStackTrace();
                        img_dataCourtCopy = "";
                        minor_ImgData = null;


                    } catch (Exception e) {
                        e.printStackTrace();
                        img_dataCourtCopy = "";
                        minor_ImgData = null;

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    img_dataCourtCopy = "";
                    minor_ImgData = null;
                }

            } else if (requestCode == 2) {
                try {
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    if (null != c) {
                        c.moveToFirst();
                        int columnIndex = c.getColumnIndex(filePath[0]);
                        picturePath = c.getString(columnIndex);
                        c.close();
                        Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));

                        if ("1".equals(selection_Pic_Flag) && thumbnail != null) {
                            Bitmap mutableBitmap = thumbnail.copy(Bitmap.Config.ARGB_8888, true);
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            imgView_CourtOrderCopy.setRotation(0);
                            imgView_CourtOrderCopy.setImageBitmap(mutableBitmap);

                            mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);
                            byteArray = bytes.toByteArray();
                            img_dataCourtCopy = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                        } else if ("2".equals(selection_Pic_Flag) && thumbnail != null) {
                            Bitmap mutableBitmap = thumbnail.copy(Bitmap.Config.ARGB_8888, true);
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            imgView_DLCopy.setRotation(0);
                            imgView_DLCopy.setImageBitmap(mutableBitmap);
                            mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);
                            byteArray = bytes.toByteArray();
                            img_dataDLCopy = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                        } else if ("3".equals(selection_Pic_Flag) && thumbnail != null) {
                            Bitmap mutableBitmap = thumbnail.copy(Bitmap.Config.ARGB_8888, true);
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            minor_img_CourtCopy.setRotation(0);
                            minor_img_CourtCopy.setImageBitmap(mutableBitmap);
                            mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);
                            byteArray = bytes.toByteArray();
                            minor_ImgData = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                        } else if ("4".equals(selection_Pic_Flag) && thumbnail != null) {
                            Bitmap mutableBitmap = thumbnail.copy(Bitmap.Config.ARGB_8888, true);
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            owner_img_CourtCopy.setRotation(0);
                            owner_img_CourtCopy.setImageBitmap(mutableBitmap);
                            mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);
                            byteArray = bytes.toByteArray();
                            driver_ImgData = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                        } else {
                            showToast("Image Cannot be Loaded !");
                        }
                    } else {
                        img_dataCourtCopy = "";
                        minor_ImgData = null;
                        driver_ImgData = null;
                        showToast("Image Cannot be Loaded !");
                    }
                } catch (Exception e) {
                    img_dataCourtCopy = "";
                    img_dataDLCopy = "";
                    minor_ImgData = null;
                    driver_ImgData = null;

                }
            }
        }
    }

    private void showToast(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View toastView = toast.getView();
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setPadding(20, 0, 20, 0);
        messageTextView.setTextSize(getResources().getDimension(R.dimen._10sdp));
        toastView.setBackgroundResource(R.drawable.toast_background);
        toast.show();
    }

    public void dateReset() {
        cal = Calendar.getInstance();
        present_year = cal.get(Calendar.YEAR);
        present_month = cal.get(Calendar.MONTH);
        present_day = cal.get(Calendar.DAY_OF_MONTH);
    }

    /* FOR OFFENSE DATE */
    DatePickerDialog.OnDateSetListener md1 = new DatePickerDialog.OnDateSetListener() {

        @SuppressWarnings("deprecation")
        @SuppressLint({"SimpleDateFormat", "DefaultLocale"})

        @Override
        public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
            present_year = selectedYear;
            present_month = monthOfYear;
            present_day = dayOfMonth;
            format = new SimpleDateFormat("dd-MMM-yyyy");
            present_date_toSend = format.format(new Date(present_year - 1900, (present_month), present_day));
            btn_dp_date_selection.setText("" + present_date_toSend.toUpperCase());

        }
    };
    DatePickerDialog.OnDateSetListener md2 = new DatePickerDialog.OnDateSetListener() {
        @SuppressWarnings("deprecation")
        @SuppressLint({"SimpleDateFormat", "DefaultLocale"})
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
            present_year = selectedYear;
            present_month = monthOfYear;
            present_day = dayOfMonth;
            format = new SimpleDateFormat("dd-MMM-yyyy");
            date_courtAtnd = format.format(new Date(present_year - 1900, (present_month), present_day));
            btn_courtAttenddate.setText(date_courtAtnd.toUpperCase());
        }

    };

    DatePickerDialog.OnDateSetListener owner_CrtAtndDt = new DatePickerDialog.OnDateSetListener() {
        @SuppressWarnings("deprecation")
        @SuppressLint({"SimpleDateFormat", "DefaultLocale"})
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
            present_year = selectedYear;
            present_month = monthOfYear;
            present_day = dayOfMonth;
            format = new SimpleDateFormat("dd-MMM-yyyy");
            String date_courtAtnd = format.format(new Date(present_year - 1900, (present_month), present_day));
            owner_btn_CrtAtndDate.setText(date_courtAtnd.toUpperCase());

        }

    };

    DatePickerDialog.OnDateSetListener md3 = new DatePickerDialog.OnDateSetListener() {

        @SuppressWarnings("deprecation")
        @SuppressLint({"SimpleDateFormat", "DefaultLocale"})
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
            present_year = selectedYear;
            present_month = monthOfYear;
            present_day = dayOfMonth;
            format = new SimpleDateFormat("dd-MMM-yyyy");
            date_convFRom = "";
            date_convFRom = format.format(new Date(present_year - 1900, (present_month), present_day));
            btn_courtConFromdate.setText(date_convFRom.toUpperCase());
            btn_courtConTo.setText("Select Date");
            edtTxtConDays.setText("");
            try {
                date_From = null;
                date_From = format.parse(date_convFRom);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    DatePickerDialog.OnDateSetListener md4 = new DatePickerDialog.OnDateSetListener() {

        @SuppressWarnings("deprecation")
        @SuppressLint({"SimpleDateFormat", "DefaultLocale"})
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
            present_year = selectedYear;
            present_month = monthOfYear;
            present_day = dayOfMonth;
            format = new SimpleDateFormat("dd-MMM-yyyy");
            date_convicTo = format.format(new Date(present_year - 1900, (present_month), present_day));
            dayDifference = "";
            Date date1;
            Date date2;
            SimpleDateFormat dates = new SimpleDateFormat("dd-MMM-yyyy");
            //Setting dates
            try {
                date1 = dates.parse(date_convFRom);
                date2 = dates.parse(date_convicTo);
                if (date2.after(date1) || date2.equals(date1)) {
                    btn_courtConTo.setText(date_convicTo.toUpperCase());

                    //Comparing dates
                    long difference = Math.abs(date1.getTime() - date2.getTime());
                    long differenceDates = difference / (24 * 60 * 60 * 1000);

                    //Convert long to String
                    dayDifference = Long.toString(differenceDates);
                    edtTxtConDays.setText(dayDifference);
                } else {
                    showToast("Date should be greater than From_Date ");
                    btn_courtConTo.setText("Select Date");
                    edtTxtConDays.setText("");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    DatePickerDialog.OnDateSetListener ownerConFromDt = new DatePickerDialog.OnDateSetListener() {

        @SuppressWarnings("deprecation")
        @SuppressLint({"SimpleDateFormat", "DefaultLocale"})
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
            present_year = selectedYear;
            present_month = monthOfYear;
            present_day = dayOfMonth;
            format = new SimpleDateFormat("dd-MMM-yyyy");
            owner_date_convFRom = "";
            owner_date_convFRom = format.format(new Date(present_year - 1900, (present_month), present_day));
            owner_btn_courtConFromdate.setText(owner_date_convFRom.toUpperCase());
            owner_btn_courtConTo.setText("Select Date");
            try {
                date_From = null;
                date_From = format.parse(owner_date_convFRom);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    DatePickerDialog.OnDateSetListener ownerConFromTo = new DatePickerDialog.OnDateSetListener() {

        @SuppressWarnings("deprecation")
        @SuppressLint({"SimpleDateFormat", "DefaultLocale"})
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
            present_year = selectedYear;
            present_month = monthOfYear;
            present_day = dayOfMonth;
            format = new SimpleDateFormat("dd-MMM-yyyy");
            owner_date_convicTo = format.format(new Date(present_year - 1900, (present_month), present_day));
            dayDifference = "";
            Date date1;
            Date date2;
            SimpleDateFormat dates = new SimpleDateFormat("dd-MMM-yyyy");
            //Setting dates
            try {
                date1 = dates.parse(owner_date_convFRom);
                date2 = dates.parse(owner_date_convicTo);
                if (date2.after(date1) || date2.equals(date1)) {
                    owner_btn_courtConTo.setText(owner_date_convicTo.toUpperCase());

                    //Comparing dates
                    long difference = Math.abs(date1.getTime() - date2.getTime());
                    long differenceDates = difference / (24 * 60 * 60 * 1000);

                    //Convert long to String
                    ownerConDays = Long.toString(differenceDates);
                } else {
                    showToast("Date should be greater than From_Date ");
                    owner_btn_courtConTo.setText("Select Date");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    };

    DatePickerDialog.OnDateSetListener md_scl_servceFrom = new DatePickerDialog.OnDateSetListener() {

        @SuppressWarnings("deprecation")
        @SuppressLint({"SimpleDateFormat", "DefaultLocale"})
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
            present_year = selectedYear;
            present_month = monthOfYear;
            present_day = dayOfMonth;
            format = new SimpleDateFormat("dd-MMM-yyyy");
            date_convFRom = format.format(new Date(present_year - 1900, (present_month), present_day));
            btn_courtSoclServceFromdate.setText("" + date_convFRom.toUpperCase());

        }
    };

    DatePickerDialog.OnDateSetListener md_scl_servceTo = new DatePickerDialog.OnDateSetListener() {

        @SuppressWarnings("deprecation")
        @SuppressLint({"SimpleDateFormat", "DefaultLocale"})
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
            present_year = selectedYear;
            present_month = monthOfYear;
            present_day = dayOfMonth;
            format = new SimpleDateFormat("dd-MMM-yyyy");
            date_convicTo = format.format(new Date(present_year - 1900, (present_month), present_day));
            dayDifference = "";
            Date date1;
            Date date2;
            SimpleDateFormat dates = new SimpleDateFormat("dd-MMM-yyyy");
            //Setting dates
            try {
                date1 = dates.parse(date_convFRom);
                date2 = dates.parse(date_convicTo);
                if (date2.after(date1) || date2.equals(date1)) {
                    btn_courtSoclServceTodate.setText("" + date_convicTo.toUpperCase());
                    //Comparing dates
                    long difference = Math.abs(date1.getTime() - date2.getTime());
                    long differenceDates = difference / (24 * 60 * 60 * 1000);
                    //Convert long to String
                    dayDifference = Long.toString(differenceDates);
                    edtTxtRisDays.setText(dayDifference);
                } else {
                    showToast("Date should be greater than From_Date ");
                    btn_courtSoclServceTodate.setText("Select Date");
                    edtTxtRisDays.setText("");
                }
            } catch (ParseException e) {
                e.printStackTrace();
                showToast("Please select the dates!");
            }
        }
    };

    DatePickerDialog.OnDateSetListener ownerSSFromDt = new DatePickerDialog.OnDateSetListener() {

        @SuppressWarnings("deprecation")
        @SuppressLint({"SimpleDateFormat", "DefaultLocale"})
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
            present_year = selectedYear;
            present_month = monthOfYear;
            present_day = dayOfMonth;
            format = new SimpleDateFormat("dd-MMM-yyyy");
            owner_date_convFRom = "";
            owner_date_convFRom = format.format(new Date(present_year - 1900, (present_month), present_day));
            owner_btn_courtSoclServceFromdate.setText(owner_date_convFRom.toUpperCase());
            owner_btn_courtSoclServceTodate.setText("Select Date");
        }
    };

    DatePickerDialog.OnDateSetListener ownerSSFromTo = new DatePickerDialog.OnDateSetListener() {

        @SuppressWarnings("deprecation")
        @SuppressLint({"SimpleDateFormat", "DefaultLocale"})
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
            present_year = selectedYear;
            present_month = monthOfYear;
            present_day = dayOfMonth;
            format = new SimpleDateFormat("dd-MMM-yyyy");
            owner_date_convicTo = "";
            owner_date_convicTo = format.format(new Date(present_year - 1900, (present_month), present_day));
            dayDifference = "";
            Date date1;
            Date date2;
            SimpleDateFormat dates = new SimpleDateFormat("dd-MMM-yyyy");
            //Setting dates
            try {
                date1 = dates.parse(owner_date_convFRom);
                date2 = dates.parse(owner_date_convicTo);
                if (date2.after(date1) || date2.equals(date1)) {
                    owner_btn_courtSoclServceTodate.setText(owner_date_convicTo.toUpperCase());
                    //Comparing dates
                    long difference = Math.abs(date1.getTime() - date2.getTime());
                    long differenceDates = difference / (24 * 60 * 60 * 1000);
                    //Convert long to String
                    ownerConDays = Long.toString(differenceDates);
                } else {
                    showToast("Date should be greater than From_Date ");
                    owner_btn_courtSoclServceTodate.setText("Select Date");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    };

    DatePickerDialog.OnDateSetListener md_DL_SUS_FROM = new DatePickerDialog.OnDateSetListener() {

        @SuppressWarnings("deprecation")
        @SuppressLint({"SimpleDateFormat", "DefaultLocale"})
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
            present_year = selectedYear;
            present_month = monthOfYear;
            present_day = dayOfMonth;
            format = new SimpleDateFormat("dd-MMM-yyyy");
            date_DL_SUS_FROM = format.format(new Date(present_year - 1900, (present_month), present_day));
            btn_DLSUS_FromDate.setText("" + date_DL_SUS_FROM.toUpperCase());
        }
    };

    DatePickerDialog.OnDateSetListener md_DL_SUS_TO = new DatePickerDialog.OnDateSetListener() {

        @SuppressWarnings("deprecation")
        @SuppressLint({"SimpleDateFormat", "DefaultLocale"})
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
            present_year = selectedYear;
            present_month = monthOfYear;
            present_day = dayOfMonth;
            format = new SimpleDateFormat("dd-MMM-yyyy");
            date_DL_SUS_TO = format.format(new Date(present_year - 1900, (present_month), present_day));
            //btn_courtSoclServceTodate.setText("" + date_convicTo.toUpperCase());
            dayDifference = "";
            Date date1;
            Date date2;
            SimpleDateFormat dates = new SimpleDateFormat("dd-MMM-yyyy");
            //Setting dates
            try {

                date1 = dates.parse(date_DL_SUS_FROM);
                date2 = dates.parse(date_DL_SUS_TO);
                if (date2.after(date1) || date2.equals(date1)) {

                    btn_DLSUS_ToDate.setText(date_DL_SUS_TO.toUpperCase());
                    //Comparing dates
                    long difference = Math.abs(date1.getTime() - date2.getTime());
                    long differenceDates = difference / (24 * 60 * 60 * 1000);

                    //Convert long to String
                    dayDifference = Long.toString(differenceDates);
                    edtTxt_DL_SUSDAYS.setText(dayDifference);
                } else {
                    showToast("Date should be greater than From_Date ");
                    btn_DLSUS_ToDate.setText("Select Date");
                    edtTxt_DL_SUSDAYS.setText("");
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    DatePickerDialog.OnDateSetListener md1_DD_DL_DOB = new DatePickerDialog.OnDateSetListener() {

        @SuppressWarnings("deprecation")
        @SuppressLint({"SimpleDateFormat", "DefaultLocale"})

        @Override
        public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
            present_year = selectedYear;
            present_month = monthOfYear;
            present_day = dayOfMonth;
            format = new SimpleDateFormat("dd-MMM-yyyy");
            date_dd_dl_dob = format.format(new Date(present_year - 1900, (present_month), present_day));
            try {
                String todaysdate = new DateUtil().getTodaysDate();
                long days = new DateUtil().DaysCalucate(date_dd_dl_dob, todaysdate);
                //Minimum Age should be 16
                if (days > 5824) {
                    btn_Dl_dob.setText(date_dd_dl_dob.toUpperCase());
                } else {
                    showToast("Please select Date of Birth Atleast Person Age Should be Greater Than 16");
                    btn_Dl_dob.setText("Select Date");
                }
            } catch (Exception e) {
                e.printStackTrace();
                btn_Dl_dob.setText("Select Date");
            }
        }
    };

    DatePickerDialog.OnDateSetListener md_DL_CANCEL = new DatePickerDialog.OnDateSetListener() {

        @SuppressWarnings("deprecation")
        @SuppressLint({"SimpleDateFormat", "DefaultLocale"})
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
            present_year = selectedYear;
            present_month = monthOfYear;
            present_day = dayOfMonth;
            format = new SimpleDateFormat("dd-MMM-yyyy");
            date_DL_SUS_FROM = "";
            date_DL_SUS_FROM = format.format(new Date(present_year - 1900, (present_month), present_day));
            btn_dateSeltion_DL_Canln.setText(date_DL_SUS_FROM.toUpperCase());
        }
    };

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case PRESENT_DATE_PICKER:
                dateReset();
                DatePickerDialog dp_offence_date = new DatePickerDialog(this, md1, present_year, present_month,
                        present_day);

                dp_offence_date.getDatePicker().setMaxDate(System.currentTimeMillis());
                return dp_offence_date;
            case PRESENT_COURT_ATTEND_DATE:
                dateReset();
                DatePickerDialog dp_courtAtnd_date = new DatePickerDialog(this, md2, present_year, present_month,
                        present_day);

                dp_courtAtnd_date.getDatePicker().setMaxDate(System.currentTimeMillis());
                return dp_courtAtnd_date;
            case OWNER_COURT_ATTEND_DATE: //owner_CrtAtndDt
                dateReset();
                DatePickerDialog courtAtnd_date = new DatePickerDialog(this, owner_CrtAtndDt, present_year, present_month,
                        present_day);

                courtAtnd_date.getDatePicker().setMaxDate(System.currentTimeMillis());
                return courtAtnd_date;
            case PRESENT_COURT_CONVI_FROM:
                dateReset();
                DatePickerDialog dp_courtConFrom_date = new DatePickerDialog(this, md3, present_year, present_month,
                        present_day);

                //dp_courtConFrom_date.getDatePicker().setMaxDate(System.currentTimeMillis());
                // dp_courtConFrom_date.getDatePicker().setMinDate(Long.parseLong(date_convFRom));
                return dp_courtConFrom_date;
            case PRESENT_COURT_CONVI_TO:
                dateReset();
                DatePickerDialog dp_courtConTo_date = new DatePickerDialog(this, md4, present_year, present_month,
                        present_day);

                //dp_courtConTo_date.getDatePicker().setMaxDate(System.currentTimeMillis());
                return dp_courtConTo_date;

            case OWNER_COURT_CONVI_FROM:
                dateReset();
                DatePickerDialog owner_courtConFrm_date = new DatePickerDialog(this, ownerConFromDt, present_year, present_month,
                        present_day);

                //dp_courtConTo_date.getDatePicker().setMaxDate(System.currentTimeMillis());
                return owner_courtConFrm_date;

            case OWNER_COURT_CONVI_TO:
                dateReset();
                DatePickerDialog owner_courtConTo_date = new DatePickerDialog(this, ownerConFromTo, present_year, present_month,
                        present_day);

                //dp_courtConTo_date.getDatePicker().setMaxDate(System.currentTimeMillis());
                return owner_courtConTo_date;
            case PROGRESS_DIALOG:
                ProgressDialog pd = ProgressDialog.show(this, "", "Please Wait...", true);
                pd.setContentView(R.layout.custom_progress_dialog);
                pd.setCancelable(false);
                return pd;

            case PRESENT_COURT_SCL_SRC_FROM:
                dateReset();
                DatePickerDialog dp_SCL_SRC_FROM = new DatePickerDialog(this, md_scl_servceFrom, present_year, present_month,
                        present_day);

                dp_SCL_SRC_FROM.getDatePicker().setMaxDate(System.currentTimeMillis());
                return dp_SCL_SRC_FROM;

            case PRESENT_COURT_SCL_SRC_TO:
                dateReset();
                DatePickerDialog dp_SCL_SRC_TO = new DatePickerDialog(this, md_scl_servceTo, present_year, present_month,
                        present_day);

                // dp_SCL_SRC_TO.getDatePicker().setMaxDate(System.currentTimeMillis());
                return dp_SCL_SRC_TO;

            case OWNER_COURT_SCL_SRC_FROM:
                dateReset();
                DatePickerDialog owner_SCL_SRC_FROM = new DatePickerDialog(this, ownerSSFromDt, present_year, present_month,
                        present_day);

                owner_SCL_SRC_FROM.getDatePicker().setMaxDate(System.currentTimeMillis());
                return owner_SCL_SRC_FROM;

            case OWNER_COURT_SCL_SRC_TO:
                dateReset();
                DatePickerDialog owner_SCL_SRC_TO = new DatePickerDialog(this, ownerSSFromTo, present_year, present_month,
                        present_day);

                // dp_SCL_SRC_TO.getDatePicker().setMaxDate(System.currentTimeMillis());
                return owner_SCL_SRC_TO;

            case PRESENT_DL_SUS_FROM:
                dateReset();
                DatePickerDialog dp_DL_SUS_FROM = new DatePickerDialog(this, md_DL_SUS_FROM, present_year, present_month,
                        present_day);

                // dp_DL_SUS_FROM.getDatePicker().setMaxDate(System.currentTimeMillis());
                return dp_DL_SUS_FROM;

            case PRESENT_DL_SUS_TO:
                dateReset();
                DatePickerDialog dp_DL_SUS_TO = new DatePickerDialog(this, md_DL_SUS_TO, present_year, present_month,
                        present_day);

                // dp_DL_SUS_TO.getDatePicker().setMaxDate(System.currentTimeMillis());
                return dp_DL_SUS_TO;
            case PRESENT_DD_DL_DOB:
                dateReset();
                DatePickerDialog dp_DD_DL_DOB = new DatePickerDialog(this, md1_DD_DL_DOB, present_year, present_month,
                        present_day);

                dp_DD_DL_DOB.getDatePicker().setMaxDate(System.currentTimeMillis());
                return dp_DD_DL_DOB;

            case PRESENT_DD_DL_CanCel:
                dateReset();
                DatePickerDialog dp_DL_CANCEL = new DatePickerDialog(this, md_DL_CANCEL, present_year, present_month,
                        present_day);

                // dp_DL_SUS_FROM.getDatePicker().setMaxDate(System.currentTimeMillis());
                return dp_DL_CANCEL;

            case PRINT_DD_DIALOG:
                TextView title = new TextView(this);
                title.setText("COURT CLOSING");
                title.setBackgroundColor(Color.BLUE);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.WHITE);
                title.setTextSize(26);
                title.setTypeface(title.getTypeface(), Typeface.BOLD);
                title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
                title.setPadding(20, 0, 20, 0);
                title.setHeight(70);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DDCloseActiviy.this,
                        AlertDialog.THEME_HOLO_LIGHT);
                alertDialogBuilder.setCustomTitle(title);
                alertDialogBuilder.setIcon(R.drawable.dialog_logo);
                alertDialogBuilder.setMessage(print_Data);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("PRINT", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Printing", Toast.LENGTH_LONG).show();

                    }
                });

                alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dd_lyt.setVisibility(View.GONE);
                        pay_dd_lyt.setVisibility(View.GONE);
                        removeDialog(PRINT_DD_DIALOG_EXIT);

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                alertDialog.getWindow().getAttributes();

                TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
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


            default:
                break;
        }
        return super.onCreateDialog(id);
    }

}
