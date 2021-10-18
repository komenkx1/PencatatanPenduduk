package com.example.pencatatanpenduduk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pencatatanpenduduk.Helpers.DBHelper;
import com.example.pencatatanpenduduk.model.Penduduk;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PendudukAdapter pendudukAdapter;
    RecyclerView.LayoutManager recylerViewLayoutManager;
    ArrayList<Penduduk> penduduks;
    ArrayList<Penduduk> penduduksCopy;
    EditText search;
    TextView searchNoDatas;
    String newText;
    boolean isSearch = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.listPenduduk);
        recylerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recylerViewLayoutManager);
        penduduks = new ArrayList<Penduduk>();
        searchNoDatas = findViewById(R.id.searchNoData);
        penduduksCopy = new ArrayList<>(penduduks);

        search = findViewById(R.id.searchEditText);

        //get all data sqlite
        Cursor cursor = new DBHelper(this).allData();
        while (cursor.moveToNext()){
            Penduduk obj = new Penduduk(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4),
                    cursor.getString(5), cursor.getString(6),
                    cursor.getString(7), cursor.getString(8),
                    cursor.getString(9), cursor.getString(10));
            penduduks.add(obj);
        }

        pendudukAdapter = new PendudukAdapter(penduduks);
        recyclerView.setAdapter(pendudukAdapter);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

    }

    //search filter
    private  void filter(String newText){
        newText =   newText.toLowerCase();
        ArrayList<Penduduk> newPenduduk = new ArrayList<>();

        for (Penduduk penduduk : penduduks){
            String name = penduduk.getnamaLengkap().toLowerCase();
            if (name.contains(newText)){
                newPenduduk.add(penduduk);
                isSearch = true;
            }else{
                isSearch = false;
                searchNoDatas.setVisibility(View.VISIBLE);
            }
        }

        pendudukAdapter.filteredList(newPenduduk);
        pendudukAdapter.notifyDataSetChanged();
    }


//    @Override
//    protected void onPause() {
//        super.onPause();
//        Toast.makeText(this, "app Terpause", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Toast.makeText(this, "app Ter-resume", Toast.LENGTH_SHORT).show();
//    }

    //floating menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_two);
        View view = MenuItemCompat.getActionView(menuItem);

        CircleImageView profileImage = view.findViewById(R.id.toolbar_profile_image);

        Glide
                .with(this)
                .load("https://images.unsplash.com/photo-1478070531059-3db579c041d5?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=750&q=80")
                .into(profileImage);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Profile Clicked",Toast.LENGTH_SHORT).show();
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add:
                Intent intent = new Intent(this, AddPendudukActivity.class);
                startActivity(intent);
                break;
            case R.id.about:
                new AlertDialog.Builder(this)
                        .setTitle("About")
                        .setMessage("Nama : I Komang Wahyu Hadi Permana \n"+"Nim : 1905551010 \n"+"Judul Aplikasi : Pencatatan Penduduk")
                        .setPositiveButton("Tutup", null)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //ketika menuContext di tekan
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case 101:
                pendudukAdapter.editItem(item.getGroupId());
                Snackbar.make(findViewById(R.id.coordinator),"Edited", Snackbar.LENGTH_SHORT).show();
                return  true;
            case 102:
                //alert Dialog
                new AlertDialog.Builder(this).setMessage("Apakah Anda Yakin Ingin Menghapus Data Ini?")
                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                pendudukAdapter.removeItem(item.getGroupId());
                                Toast.makeText(MainActivity.this, "Data Dihapus", Toast.LENGTH_SHORT).show();
                                if (isSearch){
                                    overridePendingTransition( 0, 0);
                                    MainActivity.this.recreate();
                                    overridePendingTransition( 0, 0);
                                }
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Snackbar.make(findViewById(R.id.coordinator),"Tindakan DIbatalakan", Snackbar.LENGTH_SHORT).show();
                            }
                        }).create().show();
                return  true;
        }

        return super.onContextItemSelected(item);

    }
}