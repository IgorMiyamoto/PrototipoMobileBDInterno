package com.example.a3aetim.Myndie;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.MenuItemCompat;
import android.transition.ChangeBounds;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.a3aetim.Myndie.Classes.ImageDAO;
import com.example.a3aetim.Myndie.Classes.User;
import com.example.a3aetim.Myndie.Connection.AppConfig;
import com.example.a3aetim.Myndie.Connection.AppController;
import com.example.a3aetim.Myndie.Fragments.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.example.a3aetim.Myndie.Splash.PREF_NAME;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    User loggedUser;
    NavigationView navigationView;
    Bitmap img;
    public static final String TAG = AppController.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();
        getWindow().setSharedElementExitTransition(new ChangeBounds());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.frameContentMain, new MarketFragment());
            ft.commit();
        }

        loggedUser = (User) getIntent().getSerializableExtra("LoggedUser");
        //img = BitmapFactory.decodeByteArray(loggedUser.getPicUser(),0,loggedUser.getPicUser().length);
        img = ImageDAO.loadImageFromStorage(loggedUser.getPicUser());
        RoundedBitmapDrawable imgRound = RoundedBitmapDrawableFactory.create(getResources(),img);
        imgRound.setCornerRadius(100);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        ImageView navImgView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imgvNavHeader);
        TextView txtNomeUsu = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txtvNameUserNav);
        TextView txtEmailUsu = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txtEmailUserNav);
        navImgView.setImageDrawable(imgRound);
        txtEmailUsu.setText(loggedUser.getEmailUser());
        txtNomeUsu.setText(loggedUser.getNameUser());
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProf();
            }
        });

      setLoggedUser();
    }

    @Override
    public void onRestart(){
        super.onRestart();
        reiniciar();
    }

    private void openProf(){
        Intent intent = new Intent(this,ProfileActivity.class);
        intent.putExtra("ProfileUser",loggedUser);
        startActivity(intent);
    }

    private void setLoggedUser(){
        SharedPreferences sp = getSharedPreferences(PREF_NAME,0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("EmailLoggedUser",loggedUser.getEmailUser());
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        android.support.v7.widget.SearchView searchView;
        MenuItem menuItem = menu.findItem(R.id.search_menu);
        searchView = (android.support.v7.widget.SearchView)menuItem.getActionView();
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.activity_title_item_search));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id==R.id.filter_apps){
            checkLogin("ll@gmail.com","12345");
        }
        return super.onOptionsItemSelected(item);
    }
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        Toast.makeText(this, "Logging.... ", Toast.LENGTH_SHORT).show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        // Now store the user in SQLite
                        String uid = jObj.getString("Id");

                        JSONObject user = jObj.getJSONObject("User");
                        String username = user.getString("Username");

                        AlertDialog d = new AlertDialog.Builder(MainActivity.this).setMessage(uid + username).show();

                        //Toast.makeText(getApplicationContext(), uid + username, Toast.LENGTH_LONG).show();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {



            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_Market) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frameContentMain, new MarketFragment());
            ft.commit();
        } else if (id == R.id.nav_Avaliation) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frameContentMain, new CommentFragment());
            ft.commit();
        } /*else if (id == R.id.nav_Discussions) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frameContentMain, new DiscussionFragment());
            ft.commit();

        } */else if (id == R.id.nav_Confing) {
            Intent i = new Intent(this,SettingsActivity.class);
            startActivity(i);
           /* FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frameContentMain, new SettingsFragment());
            ft.commit();
*/
        } else if (id == R.id.nav_RateUs) {
            final String appName = "Myndie";
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appName)));
            }

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logOut(View view){
        SharedPreferences sp = getSharedPreferences(PREF_NAME,0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("EmailLoggedUser","");
        editor.commit();
        finish();
    }

    public void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
    }

    public void loadLocale(){
        SharedPreferences sp = getSharedPreferences(PREF_NAME,0);
        String language = sp.getString("lang","");
        setLocale(language);
    }

    public void reiniciar(){
        finish();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }
    }
}
