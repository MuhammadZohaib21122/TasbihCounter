package com.example.tasbeeh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class WazaifActivity extends AppCompatActivity implements SelectListener {

    Button btSave, btBack;
    EditText etWazaif;
    MediaPlayer mediaPlayer4;
    SharedPreferences sharedPreferences;
    MyAdapter adapter;
    private List<Item> items = new ArrayList<>();
    MyDatabase myDatabase;

    @SuppressLint({"MissingInflatedId", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wazaif);


        try {
            myDatabase = new MyDatabase(this);
//            myDatabase.addContact("الله أكبر");
//            myDatabase.addContact("سُبْحَانَ ٱللَّٰهِ");
//            myDatabase.addContact("ٱلْحَمْدُ لِلَّٰهِ");
//            myDatabase.addContact("أَسْتَغْفِرُ اللّٰهَ");
//            myDatabase.addContact("الله الصَّمَد");
//            myDatabase.addContact("ٱلْوَهَّابُ");
//            myDatabase.addContact("یَاحَفِیْظُ یَاسَلَامُُ");
//            myDatabase.addContact("یَا حَنَانْ یَا مَنَانُُْ");
//            myDatabase.addContact("یَا قَدِیْمُ الْاِحْسَانُُْْ");
//            myDatabase.addContact("يَا حَيُّ يَا قَيُّوْمُ ُ");
//            myDatabase.addContact("سُبْحَانَ اللهِ وَبِحَمْدِهِ");
//            myDatabase.addContact("لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِِ");
//            myDatabase.addContact("الَْلهُمَّ أَجِِرْنَا مِنَ النَّار");
//            myDatabase.addContact("حَسْبُنَا اللهُ وَنِعْمَ الْوَكِيلُِ");
//            myDatabase.addContact("اِنّا لِلّهِ وَاِنّا اِلَيْهِ رَاجِعُون");
//            myDatabase.addContact("لَا إِلَٰهَ إِلَّا ٱللَّٰهُ مُحَمَّدٌ رَسُولُ ٱللَّٰهِ");
//            myDatabase.addContact("أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ");
//            myDatabase.addContact("اللَّهُمَّ كَمَا حَسَّنْتَ خَلْقِي فَحَسِّنْ خُلُقِي");


        } catch (Exception e) {
            Log.e("tester", "" + e.getMessage());
        }


        etWazaif = findViewById(R.id.etWazaif);
        btSave = findViewById(R.id.btSave);
        btBack = findViewById(R.id.btBack);

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WazaifActivity.this,DashboardActivity.class);
                startActivity(intent);
            }
        });


        btSave.setOnClickListener(v -> {
            Toast.makeText(WazaifActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
            String text = etWazaif.getText().toString().trim();

            if (!text.isEmpty()) {

                myDatabase = new MyDatabase(this);
                myDatabase.addContact(text);
                recyclerSetter();
                etWazaif.setText(null);
            } else {
                Toast.makeText(WazaifActivity.this, "Please enter text", Toast.LENGTH_SHORT).show();
            }

        });

        recyclerSetter();

    }


    private void recyclerSetter()
    {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        List<Item> items = new ArrayList<>();
        adapter = new MyAdapter(this, items, this);

        ArrayList<ContactModel> arrContacts = myDatabase.getContact();

        items = new ArrayList<Item>();

        for (int i = 0; i < arrContacts.size(); i++) {
            items.add(new Item(arrContacts.get(i).wazaif, i + 1));
        }


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(this, items, this));
    }
    @Override
    public void onItemClicked(Item item) {

         sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("savedText", item.getWazaif());
        editor.apply();

        Intent intent = new Intent(WazaifActivity.this, DashboardActivity.class);
        this.finish();
        startActivity(intent);

    }

    @Override
    public void onDeleteClicked(Item item) {
        String wazaif = item.getWazaif();
        MyDatabase db = new MyDatabase(this);
        db.DeleteContact(wazaif);
        recyclerSetter();
    }

}

