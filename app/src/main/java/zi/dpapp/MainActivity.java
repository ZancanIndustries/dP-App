package zi.dpapp;

import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import zi.dpapp.adapter.AgendaAdapter;
import zi.dpapp.adapter.MainPagerAdapter;
import zi.dpapp.adapter.StreamingAdapter;
import zi.dpapp.adapter.TeamAdapter;
import zi.dpapp.models.Giorni;
import zi.dpapp.models.Streamer;
import zi.dpapp.models.Team;
import zi.dpapp.models.User;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        AgendaAdapter.Agenda, AddAgendaFragment.addAgenda {

        private TabLayout tabLayout;
        private ViewPager viewPager;
        private BottomNavigationView navigationView;
        private ProgressBar progressBarStreaming, progressBarTeam;
        private RecyclerView recyclerViewStreaming, recyclerViewTeam, recyclerViewAgenda;
        private RecyclerView.LayoutManager layoutManager, layoutManager2, layoutManager3;
        private RecyclerView.Adapter adapter, adapter2, adapter3;
        private RelativeLayout layoutHome, layoutStreaming, layoutSplash, layoutTeam, layoutAgenda;
        private String[] arrayStreamer = {"Akumenji", "alyssafernweh", "BetaRonnu", "Federica88Gamer", "HoneyDafne", "k4rma_tv", "Kroatomist", "LunaRisel", "marghenefe", "R3b0rn91", "RykkuChan", "senesi_please", "xHiriel"};
        private ArrayList<Integer> arrayListId = new ArrayList<>();
        private ArrayList<String> arrayListLogo = new ArrayList<>();
        private ArrayList<Boolean> arrayListStream = new ArrayList<>();
        private int arrayStreamerIndex = 0;
        private int arrayStreamIndex = 0;
        private ArrayList<Streamer> arrayListStreamer;
        private TextView randomText, textViewName;
        private LoadingSpinner loadingSpinner;
        private String[] random = {"Accendendo il focolare", "Mettendo a letto i draghetti", "Svegliando Giammaicol", "Avviando lo stream", "Inviando una donation"};
        private ArrayList<Team> arrayListTeam = new ArrayList<>();
        private ArrayList<Giorni> arrayListGiorni = new ArrayList<>();
        private String agendaData, agendaOra, loggedUser, loggedRole, loggedTwitch;
        private FirebaseFirestore firebaseFirestore;
        private FirebaseAuth firebaseAuth;
        private DatabaseReference mDatabase;
        private boolean running;
        private JSONArray jsonArrayEventi = new JSONArray();
        private Toolbar toolbar;
        private NavigationView navigationViewDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        textViewName = (TextView) findViewById(R.id.textViewNome);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationViewDrawer = (NavigationView) findViewById(R.id.nav_view);
        layoutHome = (RelativeLayout) findViewById(R.id.layoutHome);
        layoutStreaming = (RelativeLayout) findViewById(R.id.layoutStreaming);
        layoutTeam = (RelativeLayout) findViewById(R.id.layoutTeam);
        layoutAgenda = (RelativeLayout) findViewById(R.id.layoutAgenda);
        progressBarTeam = (ProgressBar) findViewById(R.id.progressBarTeam);
        recyclerViewTeam = (RecyclerView) findViewById(R.id.recyclerViewTeam);
        layoutSplash = (RelativeLayout) findViewById(R.id.layoutSplash);
        recyclerViewStreaming = (RecyclerView) findViewById(R.id.recyclerViewStreaming);
        recyclerViewAgenda = (RecyclerView) findViewById(R.id.recyclerViewAgenda);
        progressBarStreaming = (ProgressBar) findViewById(R.id.progressBar);
        randomText = (TextView) findViewById(R.id.textViewRandom);
        loadingSpinner = (LoadingSpinner) findViewById(R.id.loadingSpinner);
        randomText.setText(random[new Random().nextInt(random.length)]);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        running = true;
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (running) {
                        Thread.sleep(2000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                randomText.setText(random[new Random().nextInt(random.length)]);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                running = false;
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                updateUI(currentUser);
            }
        }, 7000);

        setSupportActionBar(toolbar);
        setTitle("Home");

        progressBarStreaming.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_cerca:
                        // TODO
                        return true;
                    case R.id.navigation_streaming:
                        layoutStreaming.setVisibility(View.VISIBLE);
                        layoutHome.setVisibility(View.GONE);
                        layoutTeam.setVisibility(View.GONE);
                        layoutAgenda.setVisibility(View.GONE);
                        arrayStreamerIndex = 0;
                        setTitle("Streamers");
                        return true;
                    case R.id.navigation_home:
                        layoutHome.setVisibility(View.VISIBLE);
                        layoutStreaming.setVisibility(View.GONE);
                        layoutTeam.setVisibility(View.GONE);
                        layoutAgenda.setVisibility(View.GONE);
                        setTitle("Home");
                        return true;
                    case R.id.navigation_match:
                        layoutTeam.setVisibility(View.VISIBLE);
                        layoutHome.setVisibility(View.GONE);
                        layoutStreaming.setVisibility(View.GONE);
                        layoutAgenda.setVisibility(View.GONE);
                        setTitle("Teams");
                        setTeam();
                        return true;
                    case R.id.navigation_calendario:
                        layoutStreaming.setVisibility(View.GONE);
                        layoutHome.setVisibility(View.GONE);
                        layoutTeam.setVisibility(View.GONE);
                        layoutAgenda.setVisibility(View.VISIBLE);
                        setTitle("Calendario");
                        setAgenda();
                        return true;
                }
                return false;
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.getMenu().getItem(2).setChecked(true);

        MainPagerAdapter adapter = new MainPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        navigationViewDrawer.setNavigationItemSelectedListener(this);

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {

            DatabaseReference myRef = mDatabase.child("users/" + user.getUid());
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    User utente = dataSnapshot.getValue(User.class);
                    Log.d("nome", utente.getNome());
                    loggedUser = utente.getNome();
                    loggedRole = utente.getRole();
                    loggedTwitch = utente.getTwitchNick();
                    View navHeaderView = navigationViewDrawer.getHeaderView(0);
                    TextView textViewName = (TextView) navHeaderView.findViewById(R.id.textViewNome);
                    textViewName.setText(utente.getNome());
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("Firebase", "Failed to read value.", error.toException());
                }
            });

            layoutSplash.setVisibility(View.GONE);
            layoutHome.setVisibility(View.VISIBLE);
            navigationView.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            setLayoutStreaming();

        } else {

            Intent vIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(vIntent);

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //finishAffinity();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Toast.makeText(this, "sdadasd", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_login) {
            firebaseAuth.signOut();
            Intent vIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(vIntent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setLayoutStreaming() {

        OkHttpStreamer okHttpStreamer = new OkHttpStreamer();
        okHttpStreamer.execute();

    }

    public class OkHttpStreamer extends AsyncTask {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected Object doInBackground(Object[] objects) {

            Request request = new Request.Builder()
                    .url("https://api.twitch.tv/kraken/users?login=" + arrayStreamer[arrayStreamerIndex])
                    .get()
                    .addHeader("Client-ID", "6ynaluryw8ynuoai3ae39ltt55f0i3")
                    .addHeader("Accept", "application/vnd.twitchtv.v5+json")
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Postman-Token", "34f2a35e-aa98-c061-4b3f-1f12f36fff46")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Log.d("okhttp", o.toString());
            try {
                JSONObject jsonObject = new JSONObject(o.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("users");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                String id = jsonObject1.getString("_id");
                arrayListId.add(Integer.parseInt(id));
                String logo = jsonObject1.getString("logo");
                arrayListLogo.add(logo);;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (arrayStreamerIndex < arrayStreamer.length-1) {
                arrayStreamerIndex++;

                OkHttpStreamer okHttpStreamer = new OkHttpStreamer();
                okHttpStreamer.execute();
            } else {
                OkHttpStream okHttpStream = new OkHttpStream();
                okHttpStream.execute();
            }
        }
    }

    public class OkHttpStream extends AsyncTask {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected Object doInBackground(Object[] objects) {

            Request request = new Request.Builder()
                    .url("https://api.twitch.tv/kraken/streams/" + arrayListId.get(arrayStreamIndex))
                    .get()
                    .addHeader("Client-ID", "6ynaluryw8ynuoai3ae39ltt55f0i3")
                    .addHeader("Accept", "application/vnd.twitchtv.v5+json")
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Postman-Token", "34f2a35e-aa98-c061-4b3f-1f12f36fff46")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Log.d("okhttp", o.toString());
            try {
                JSONObject jsonObject = new JSONObject(o.toString());
                String stream = jsonObject.getString("stream");
                if (stream.equals("null")) {
                    arrayListStream.add(false);
                } else {
                    arrayListStream.add(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (arrayStreamIndex < arrayListId.size()-1) {
                arrayStreamIndex++;

                OkHttpStream okHttpStream = new OkHttpStream();
                okHttpStream.execute();
            } else {
                arrayListStreamer = new ArrayList<>();

                for (int i = 0; i < arrayStreamer.length; i++) {
                    arrayListStreamer.add(new Streamer(arrayListId.get(i), arrayStreamer[i], arrayListLogo.get(i), arrayListStream.get(i)));
                }

                layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerViewStreaming.setLayoutManager(layoutManager);
                adapter = new StreamingAdapter(MainActivity.this, arrayListStreamer);
                recyclerViewStreaming.setAdapter(adapter);
                progressBarStreaming.setVisibility(View.GONE);
            }
        }
    }

    public void setAgenda() {
        final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        arrayListGiorni = new ArrayList<>();
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Calendar calQuery = Calendar.getInstance();
        calQuery.add(Calendar.DAY_OF_WEEK, 7);
        Log.d("oggi", cal.getTime().toString());
        Log.d("tra 7 giorni", calQuery.getTime().toString());

        firebaseFirestore.collection("calendario")
                .whereGreaterThan("data", cal.getTime()).whereLessThan("data", calQuery.getTime())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        jsonArrayEventi = new JSONArray();
                        if (e != null) {
                            Log.w("Firestore", "Listen failed.", e);
                            return;
                        }

                        for (DocumentSnapshot doc : value) {
                            if (doc.get("canale") != null) {
                                JSONObject jsonObjectEvento = new JSONObject();
                                try {
                                    jsonObjectEvento.put("canale", doc.getString("canale"));
                                    jsonObjectEvento.put("data", format.format(doc.getDate("data")));
                                    jsonObjectEvento.put("info", doc.getString("info"));
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }

                                jsonArrayEventi.put(jsonObjectEvento);
                                Log.d("array", jsonArrayEventi.toString());
                                Log.d("canale", doc.getString("canale"));
                            }
                        }

                        SimpleDateFormat sdfData = new SimpleDateFormat("dd/MM/yyyy");
                        SimpleDateFormat sdfGiorno = new SimpleDateFormat("EEEE", Locale.ITALIAN);
                        Giorni giorni;
                        for (int i = 0; i < 7; i++) {
                            Log.d("data", sdfData.format(cal.getTime()));
                            Log.d("giorno", sdfGiorno.format(cal.getTime()));

                            if (i == 0) {
                                giorni = new Giorni("Oggi", sdfData.format(cal.getTime()), jsonArrayEventi.toString());
                            } else if (i == 1) {
                                giorni = new Giorni("Domani", sdfData.format(cal.getTime()), jsonArrayEventi.toString());
                            } else {
                                giorni = new Giorni(sdfGiorno.format(cal.getTime()), sdfData.format(cal.getTime()), jsonArrayEventi.toString());
                            }

                            arrayListGiorni.add(giorni);
                            layoutManager3 = new LinearLayoutManager(getApplicationContext());
                            recyclerViewAgenda.setLayoutManager(layoutManager3);
                            cal.add(Calendar.DAY_OF_WEEK, 1);
                            adapter3 = new AgendaAdapter(MainActivity.this, arrayListGiorni, loggedRole);
                            recyclerViewAgenda.setAdapter(adapter3);
                        }
                    }
                });

    }

    @Override
    public void openTimePickerAlert(String data) {

        agendaData = data;
        int mHour, mMinute;
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        agendaOra = hourOfDay + ":" + minute;
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putString("twitch", loggedTwitch);
                        AddAgendaFragment addAgendaDialogFragment = AddAgendaFragment.newInstance(0);
                        addAgendaDialogFragment.setArguments(bundle);
                        addAgendaDialogFragment.show(ft, "");
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();

    }

    @Override
    public void addAgendaEvents(String canale, String info) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String stringData = agendaData + " " + agendaOra;
        Log.d("string", stringData);
        Date dataEvento = null;
        try {
            dataEvento = format.parse(stringData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("data", dataEvento.toString());
        Map<String, Object> data = new HashMap<>();
        data.put("canale", canale);
        data.put("data", dataEvento);
        data.put("info", info);

        firebaseFirestore.collection("calendario")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Firestore", "DocumentSnapshot written with ID: " + documentReference.getId());
                        Toast.makeText(MainActivity.this, "Evento aggiunto!", Toast.LENGTH_SHORT).show();
                        setAgenda();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firestore", "Error adding document", e);
                    }
                });
    }

    public void setTeam() {
        arrayListTeam.add(new Team(0, "Nome", "Call of Duty", R.drawable.logocod));
        arrayListTeam.add(new Team(1, "Nome", "Rainbow Six", R.drawable.logor6s));
        arrayListTeam.add(new Team(2, "Nome", "Gear of Wars", R.drawable.logogow));
        arrayListTeam.add(new Team(3, "Nome", "Fifa 17", R.drawable.logofifa17));
        arrayListTeam.add(new Team(4, "Nome", "League of Legends", R.drawable.logolol));
        arrayListTeam.add(new Team(0, "Nome", "Hearthstone", R.drawable.logohs));
        arrayListTeam.add(new Team(0, "Nome", "CS:GO", R.drawable.logocsgo));
        arrayListTeam.add(new Team(0, "Nome", "Overwatch", R.drawable.logooverwatch));

        layoutManager2 = new LinearLayoutManager(getApplicationContext());
        recyclerViewTeam.setLayoutManager(layoutManager2);
        adapter2 = new TeamAdapter(MainActivity.this, arrayListTeam);
        recyclerViewTeam.setAdapter(adapter2);
        progressBarTeam.setVisibility(View.GONE);
    }
}
