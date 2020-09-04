package com.example.mtpv.eticketcourt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mtpv.eticketcourt.service.ServiceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class CounsellingUpdateActivity extends Activity {

    EditText edt_Regno;
    AppCompatTextView txt_CRegNo, txt_CChalnNo, txt_BkdOfficerName, txt_BkdPSName, txt_BkdPntName, txt_COfnceDate,
            txt_DrCName, txt_DrCFName, txt_CAge, txt_DrCConNo;
    AppCompatImageView img_CDrImage, img_CDrFrsImage;
    Button btn_OffenceDate, btngetdetails;
    LinearLayout Lyt_details;
    Calendar cal;
    int present_year;
    int present_month;
    int present_day;
    final int PRESENT_DATE_PICKER = 1, PROGRESS_DIALOG = 2;
    String str_OffenceDt;
    SimpleDateFormat format;
    Date date_From;
    String vEHICLE_NUMBER, chall_No, chall_Type, violations, offence_Date, driver_Adhar, driver_Mobile, driver_LCNCE, driver_DL_DOB,
            unit_CODE, pid_CODE, ps_CODE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_councelling_update);
        cal = Calendar.getInstance();
        present_year = cal.get(Calendar.YEAR);
        present_month = cal.get(Calendar.MONTH);
        present_day = cal.get(Calendar.DAY_OF_MONTH);
        initviews();
        btn_OffenceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(PRESENT_DATE_PICKER);
            }
        });

        btngetdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_Regno.getText().toString().trim().equals("")) {
                    edt_Regno.setError(Html.fromHtml("<font color='black'>Enter Vehicle No</font>"));
                } else if (btn_OffenceDate.getText().toString().equals("Select Date")) {
                    showToast("Select Date");
                } else {
                    if (isOnline()) {
                        new Async_getCounselling_details().execute();
                    } else {
                        showToast("Please check your network connection!");
                    }
                }
            }
        });

    }

    private void initviews() {
        edt_Regno = findViewById(R.id.edt_Regno);
        btn_OffenceDate = findViewById(R.id.btn_OffenceDate);
        txt_CRegNo = findViewById(R.id.txt_CRegNo);
        txt_CChalnNo = findViewById(R.id.txt_CChalnNo);
        txt_BkdOfficerName = findViewById(R.id.txt_BkdOfficerName);
        txt_BkdPSName = findViewById(R.id.txt_BkdPSName);
        txt_BkdPntName = findViewById(R.id.txt_BkdPntName);
        txt_COfnceDate = findViewById(R.id.txt_COfnceDate);
        txt_DrCName = findViewById(R.id.txt_DrCName);
        txt_DrCFName = findViewById(R.id.txt_DrCFName);
        txt_CAge = findViewById(R.id.txt_CAge);
        txt_DrCConNo = findViewById(R.id.txt_DrCConNo);
        img_CDrImage = findViewById(R.id.img_CDrImage);
        img_CDrFrsImage = findViewById(R.id.img_CDrFrsImage);
        btngetdetails = findViewById(R.id.btngetdetails);
        Lyt_details = findViewById(R.id.Lyt_details);
    }

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

            case PROGRESS_DIALOG:
                ProgressDialog pd = ProgressDialog.show(this, "", "Please Wait...", true);
                pd.setContentView(R.layout.custom_progress_dialog);
                pd.setCancelable(false);
                return pd;

            default:
                break;
        }
        return super.onCreateDialog(id);
    }

    /* FOR OFFENSE DATE */
    DatePickerDialog.OnDateSetListener md1 = new DatePickerDialog.OnDateSetListener() {

        @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
            present_year = selectedYear;
            present_month = monthOfYear;
            present_day = dayOfMonth;
            format = new SimpleDateFormat("dd-MMM-yyyy");
            str_OffenceDt = format.format(new Date(present_year - 1900, (present_month), present_day));
            btn_OffenceDate.setText("" + str_OffenceDt.toUpperCase());
        }
    };

    public void dateReset() {
        cal = Calendar.getInstance();
        present_year = cal.get(Calendar.YEAR);
        present_month = cal.get(Calendar.MONTH);
        present_day = cal.get(Calendar.DAY_OF_MONTH);
    }

    @SuppressLint("StaticFieldLeak")
    private class Async_getCounselling_details extends AsyncTask<Void, Void, String> {
        @SuppressLint("DefaultLocale")
        @SuppressWarnings("unused")
        @Override
        protected String doInBackground(Void... params) {
            ServiceHelper.getCourtClosingTicketInfo("" + edt_Regno.getText().toString().trim().toUpperCase(), "" + str_OffenceDt, "0");
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
            Lyt_details.setVisibility(View.GONE);
        }

        @SuppressLint("SetTextI18n")
        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);

            if (null != ServiceHelper.Opdata_Chalana && !ServiceHelper.Opdata_Chalana.equals("NA")) {

                try {
                    Log.d("CounData", "" + ServiceHelper.Opdata_Chalana);
                    JSONObject jsonObject = new JSONObject(ServiceHelper.Opdata_Chalana);
                    JSONArray jsonArray = jsonObject.getJSONArray("Challan Details");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        if (jsonObject1.toString().isEmpty() || jsonObject1.length() == 0) {
                            showToast("No data Found");
                        } else {

                            Lyt_details.setVisibility(View.VISIBLE);

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
                            txt_CRegNo.setText(vEHICLE_NUMBER);
                            txt_CChalnNo.setText(chall_No);
                            txt_COfnceDate.setText(offence_Date);
                            txt_BkdOfficerName.setText(""+jsonObject1.getString("OFFICER NAME"));
                            txt_BkdPSName.setText(""+jsonObject1.getString("TrPS JURIS"));
                            txt_BkdPntName.setText(""+jsonObject1.getString("LOCATION NAME"));
                            txt_DrCName.setText(""+jsonObject1.getString("DRIVER NAME"));
                            txt_DrCFName.setText(""+jsonObject1.getString("DRIVER FNAME"));
                            txt_DrCConNo.setText(""+jsonObject1.getString("DRIVER MOBILE"));


                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("" + getResources().getString(R.string.no_day_report));
                    Lyt_details.setVisibility(View.GONE);
                }

            } else {
                showToast("" + getResources().getString(R.string.no_day_report));
                Lyt_details.setVisibility(View.GONE);
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

    public Boolean isOnline() {
        ConnectivityManager conManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwInfo = conManager.getActiveNetworkInfo();
        return nwInfo != null;
    }

}
