package com.example.pencatatanpenduduk;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pencatatanpenduduk.Helpers.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddPendudukActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "AddPendudukActivity";

    //    private  TextView mDisplayDate;
    private EditText mDisplayDate, nama_lengkap, alamat, nomorTlp;
    private RadioGroup agama;
    private RadioButton radioButton;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private SeekBar seekBar;
    private TextView progress, labelAgama, labelHobi, judulHeader, keterangan;
    private FloatingActionButton btnSUbmit;
    private Dialog dialog;
    private Spinner jenisKelamin;
    private CheckBox berenang, tiduran, rebahan, gaming;
    private CircleImageView imageView;
    private Uri uri;
    private ArrayList<String> hobiResult;
    private String[] arrMonth = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    private DBHelper dbHelper;
    private long id;
    private int year, month, day;
    private Bundle bundle;
    private SimpleDateFormat dateFormat;
    private Calendar cal;
    private Locale localeID = new Locale("in", "ID");
    private NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_add_penduduk);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Back");

        dbHelper = new DBHelper(this);

        mDisplayDate = (EditText) findViewById(R.id.birthday);
        seekBar = findViewById(R.id.seekBar);
        progress = findViewById(R.id.progress);
        jenisKelamin = findViewById(R.id.jenisKelamin);
        imageView = (CircleImageView) findViewById(R.id.image_profile);
        btnSUbmit = (FloatingActionButton) findViewById(R.id.btnSubmit);
        dialog = new Dialog(this);

        labelAgama = findViewById(R.id.labelAgama);
        labelHobi = findViewById(R.id.labelHobi);

        nama_lengkap = findViewById(R.id.nama_lengkap);
        alamat = findViewById(R.id.alamat);
        nomorTlp = findViewById(R.id.telepon);
        agama = (RadioGroup) findViewById(R.id.agama);
        berenang = (CheckBox) findViewById(R.id.berenang);
        rebahan = (CheckBox) findViewById(R.id.rebahan);
        tiduran = (CheckBox) findViewById(R.id.tiduran);
        gaming = (CheckBox) findViewById(R.id.gaming);
        judulHeader = (TextView) findViewById(R.id.judulHeader);
        keterangan = (TextView) findViewById(R.id.keterangan);
        hobiResult = new ArrayList<>();

        //get date now
        cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        //ketika form digunakan sebagai form edit
        if (getIntent().getBundleExtra("userData") != null){
            bundle = getIntent().getBundleExtra("userData");
            id = bundle.getInt("id");
            editForm();

        }

        berenang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (berenang.isChecked())
                    hobiResult.add("berenang");
                else
                    hobiResult.remove("berenang");
            }
        });

        rebahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rebahan.isChecked())
                    hobiResult.add("rebahan");
                else
                    hobiResult.remove("rebahan");
            }
        });

        tiduran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tiduran.isChecked())
                    hobiResult.add("tiduran");
                else
                    hobiResult.remove("tiduran");
            }
        });

        gaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gaming.isChecked())
                    hobiResult.add("gaming");
                else
                    hobiResult.remove("gaming");
            }
        });

        btnSUbmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {
                    storeData();
                }

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(AddPendudukActivity.this);
            }
        });

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(AddPendudukActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //set edittext value
                String date =  day + " " + arrMonth[month] + " " + year;
                mDisplayDate.setText(date);
                Log.d("tetDate",mDisplayDate.toString());
                Log.d("tetDate",nama_lengkap.toString());
            }
        };

        seekBar.setOnSeekBarChangeListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        Toast.makeText(this, "Anda Keluar Dari Halaman Create", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem menuItem3 = menu.findItem(R.id.menu_add);
        menuItem3.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
//                todo: goto back activity from here

                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            case R.id.about:
                new AlertDialog.Builder(this)
                        .setTitle("About")
                        .setMessage("Nama : I Komang Wahyu Hadi Permana \n"+"Nim : 1905551010 \n"+"Judul Aplikasi : Pencatatan Penduduk")
                        .setPositiveButton("Tutup", null)
                        .setIcon(R.drawable.ic_baseline_info_24)
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int value, boolean b) {
// Pemberitahuan Bahwa Nilai Pada Progress Telah Berubah
        progress.setText(String.valueOf(formatRupiah.format((double) value)));

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            Uri imageuri = CropImage.getPickImageResultUri(this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageuri)) {
                uri = imageuri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                        , 0);
            } else {
                startCrop(imageuri);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageView.setImageURI(result.getUri());
                uri = result.getUri();
            }
        }
    }

    private void startCrop(Uri imageuri) {
        CropImage.activity(imageuri).setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(this);
    }


    private void editForm() {
        if (bundle.getString("avatar") != null) {
            Uri avatarUri = Uri.parse(bundle.getString("avatar"));
            imageView.setImageURI(avatarUri);
        }
        judulHeader.setText("Edit Data Diri");
        nama_lengkap.setText(bundle.getString("name"));
        alamat.setText(bundle.getString("alamat"));
        mDisplayDate.setText(bundle.getString("tanggal_lahir"));
        jenisKelamin.setSelection(getIndex(jenisKelamin,bundle.getString("jk")));
        nomorTlp.setText(bundle.getString("nomor_telepon"));
        seekBar.setProgress(Integer.parseInt(bundle.getString("gaji")));
        progress.setText(String.valueOf(formatRupiah.format((double) Integer.parseInt(bundle.getString("gaji")))));
        ((RadioButton)agama.getChildAt(getIndexRadioGroup(agama,bundle.getString("agama")))).setChecked(true);
        String[] hobys = bundle.getStringArray("hobi");
        for (int i =0; i < hobys.length; i++)
        {
            if (berenang.getText().toString().equalsIgnoreCase(hobys[i]))
            {
                berenang.setChecked(true);
                if (berenang.isChecked())
                {
                    hobiResult.add("berenang");
                } else {
                    hobiResult.remove("berenang");
                }
            }

            if (rebahan.getText().toString().equalsIgnoreCase(hobys[i]))
            {
                rebahan.setChecked(true);
                if (rebahan.isChecked())
                {
                    hobiResult.add("rebahan");
                } else {
                    hobiResult.remove("rebahan");
                }
            }

            if (tiduran.getText().toString().equalsIgnoreCase(hobys[i]))
            {
                tiduran.setChecked(true);
                if (tiduran.isChecked())
                {
                    hobiResult.add("tiduran");
                } else {
                    hobiResult.remove("tiduran");
                }
            }

            if (gaming.getText().toString().equalsIgnoreCase(hobys[i]))
            {
                gaming.setChecked(true);
                if (gaming.isChecked())
                {
                    hobiResult.add("gaming");
                } else {
                    hobiResult.remove("gaming");
                }
            }

        }
    }

    private int getIndexRadioGroup(RadioGroup agama, String value) {

        String[] agamas = {
                "Hindu",
                "Islam",
                "Kristen",
                "Budha",
        };

        for (int i =0; i< agama.getChildCount(); i++){

            if (agamas[i].equalsIgnoreCase(value)){

                return  i;
            }
        }
//        ketika value tidak sama
        return 0;
    }
    private void storeData(){
        Intent intent;

        if (getIntent().getBundleExtra("userData") == null)
        {
            intent = new Intent(getApplicationContext(), ProfileActivity.class);

        }else{
            intent = new Intent(getApplicationContext(), MainActivity.class);
        }

        // get selected radio button from radioGroup

        int selectedId = agama.getCheckedRadioButtonId();

        //dateNow
        String dateNow = String.valueOf(day + " " + arrMonth[month] + " " + year);
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : hobiResult)
            stringBuilder.append(s).append(",");
        if (checkValidation()) {
            //get id radio button
            radioButton = (RadioButton) findViewById(selectedId);
            //intent
            intent.putExtra("name", nama_lengkap.getText().toString());
            intent.putExtra("alamat", alamat.getText().toString());
            intent.putExtra("tanggalLahir", mDisplayDate.getText().toString());
            intent.putExtra("nomor_telepon", nomorTlp.getText().toString());
            intent.putExtra("gaji", String.valueOf(seekBar.getProgress()));
            intent.putExtra("agama", radioButton.getText().toString());
            intent.putExtra("hobi", stringBuilder.length() > 0 ? stringBuilder.toString() : "-");
            intent.putExtra("tanggal_tercatat",dateNow );
            intent.putExtra("jenisKelamin", jenisKelamin.getSelectedItem().toString());
            intent.putExtra("isNew", "True");

            //insert db

            String name = nama_lengkap.getText().toString();
            String alamats = alamat.getText().toString();
            String tglLahir = mDisplayDate.getText().toString();
            String noTelp = nomorTlp.getText().toString();
            String gaji = String.valueOf(seekBar.getProgress());
            String agama = radioButton.getText().toString();
            String hobi = stringBuilder.toString();
            String jenisKelamins = jenisKelamin.getSelectedItem().toString();

            ContentValues values = new ContentValues();
            values.put(DBHelper.row_namaLengkap, name);
            values.put(DBHelper.row_alamat, alamats);
            values.put(DBHelper.row_tanggaLahir, tglLahir);
            values.put(DBHelper.row_tanggalTercatat, dateNow);
            values.put(DBHelper.row_nomorTelepon, noTelp);
            values.put(DBHelper.row_gaji, gaji);
            values.put(DBHelper.row_agama, agama);
            values.put(DBHelper.row_hobi, hobi);
            values.put(DBHelper.row_jenisKelamin, jenisKelamins);

            if (uri != null) {
                intent.putExtra("avatar", uri.toString());
                String foto = uri.toString();
                values.put(DBHelper.row_foto, foto);
            }
            if (getIntent().getBundleExtra("userData") == null)
            {
                dbHelper.insertData(values);

            }else{
                dbHelper.updateData(values,id);
                Toast.makeText(AddPendudukActivity.this, "Data Berhasil Di update", Toast.LENGTH_SHORT).show();

            }


        }
        startActivity(intent);
        finish();
    }

    private boolean checkValidation() {
        String name = nama_lengkap.getText().toString().trim();
        String adress = alamat.getText().toString().trim();
        String date = mDisplayDate.getText().toString().trim();
        String tlp = nomorTlp.getText().toString().trim();
        int agamaPil = agama.getCheckedRadioButtonId();

        if (name.length() <= 0) {
            nama_lengkap.requestFocus();
            nama_lengkap.setError("Masukan Nama Lengkap Anda");
            return false;
        } else if (adress.length() <= 0) {
            alamat.requestFocus();
            alamat.setError("Masukan Alamat Anda");
            return false;

        } else if (date.length() <= 0) {
            mDisplayDate.requestFocus();
            mDisplayDate.setError("Masukan Tanggal Lahir Anda");
            return false;

        } else if (tlp.length() <= 0) {
            nomorTlp.requestFocus();
            nomorTlp.setError("Masukan Nomor Telepon Anda");
            return false;

        } else if (agamaPil < 0) {
            labelAgama.setError("Anda Belum Memilih Agama!");
            Toast.makeText(this, "Anda Belum Memilih Agama!", Toast.LENGTH_SHORT).show();
            return false;

        } else {
            labelAgama.setError(null);
            mDisplayDate.setError(null);
            return true;
        }
    }

    //method get position selected spinner
    private int getIndex(Spinner jenisKelamin, String jk) {
        for (int i =0; i<jenisKelamin.getCount(); i++){
            Log.d("test",jenisKelamin.getItemAtPosition(i).toString());
            if (jenisKelamin.getItemAtPosition(i).toString().equalsIgnoreCase(jk)){
                //        ketika value  sama
                return  i;
            }
        }
//        ketika value tidak sama
        return 0;
    }


}