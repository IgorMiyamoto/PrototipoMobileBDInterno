package com.example.a3aetim.prototipomobilebdinterno;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           //   // Pega o FragmentManager
           //   FragmentManager fm = getSupportFragmentManager();
           //   // Abre uma transação e adiciona
           //   FragmentTransaction ft = fm.beginTransaction();
           //   ft.replace(R.id.frameContentMain, new ProfileFragment());
           //   ft.commit();
                openProf();
            }
        });
        // Pega o FragmentManager
        FragmentManager fm = getSupportFragmentManager();
        // Abre uma transação e adiciona
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.frameContentMain, new MarketFragment());
        ft.commit();
    }
    private void openProf(){
        Intent intent = new Intent(this,ProfileActivity.class);
        startActivity(intent);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Search) {

        }
        else if(id == R.id.nav_SearchApp){}
        else if(id == R.id.nav_SearchDev){}
        else if (id == R.id.nav_Market) {
            // Pega o FragmentManager
            FragmentManager fm = getSupportFragmentManager();
            // Abre uma transação e adiciona
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frameContentMain, new MarketFragment());
            ft.commit();
        } else if (id == R.id.nav_Avaliation) {

        } else if (id == R.id.nav_Discussions) {

        } else if (id == R.id.nav_Confing) {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_RateUs) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
