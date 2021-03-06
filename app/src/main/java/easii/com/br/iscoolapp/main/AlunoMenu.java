package easii.com.br.iscoolapp.main;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import easii.com.br.iscoolapp.R;
import easii.com.br.iscoolapp.fragmentos.FeedFragment;
import easii.com.br.iscoolapp.fragmentos.OneFragmentAluno;
import easii.com.br.iscoolapp.fragmentos.PerfilFragmentoAluno;
import easii.com.br.iscoolapp.fragmentos.TwoFragmentAluno;

public class AlunoMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_news));
        navigationView.setNavigationItemSelectedListener(this);
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
        getMenuInflater().inflate(R.menu.aluno_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if (id == R.id.nav_news) {
            transaction.replace(R.id.frameLayout, new FeedFragment());
        } else if (id == R.id.nav_group) {
            transaction.replace(R.id.frameLayout, new OneFragmentAluno());
        } else if (id == R.id.nav_notas) {
            transaction.replace(R.id.frameLayout, new TwoFragmentAluno());
        } else if (id == R.id.nav_profile) {
            transaction.replace(R.id.frameLayout, new PerfilFragmentoAluno());
        } else if (id == R.id.nav_settings) {
            transaction.replace(R.id.frameLayout, new FeedFragment());
        } else if (id == R.id.nav_logout) {
            SharedPreferences settings = this.getSharedPreferences("CONSTANTES", Context.MODE_PRIVATE);
            settings.edit().clear().commit();

            SharedPreferences settings2 = this.getSharedPreferences("androidhive-welcome", Context.MODE_PRIVATE);
            settings.edit().clear().commit();

            Intent i  = new Intent(this,MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
            transaction.addToBackStack(null);
            transaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
