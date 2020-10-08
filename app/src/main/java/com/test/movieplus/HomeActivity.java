package com.test.movieplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    FirstFragment firstFragment;
    SecondFragment secondFragment;
    ThirdFragment thirdFragment;
    MenuItem mitem_search;
    MenuItem mitem_reset;
    MenuItem mitem_logout;
    MenuItem mitem_withdrawal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firstFragment = new FirstFragment();
        secondFragment = new SecondFragment();
        thirdFragment = new ThirdFragment();

        loadFragment(firstFragment);

        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Fragment fragment =null;

                switch (menuItem.getItemId()){
                    case R.id.firstFragment:
                        fragment = firstFragment;
//                        getSupportActionBar().setTitle("Home");
                        break;
                    case R.id.secondFragment:
                        fragment = secondFragment;
//                        getSupportActionBar().setTitle("AR");
                        break;
                    case R.id.thirdFragment:
                        fragment = thirdFragment;
//                        getSupportActionBar().setTitle("Liked");
                        break;
                }
                return loadFragment(fragment);
            }
        });


    }

    private boolean loadFragment(Fragment fragment) {

        if(fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overflow, menu);
        mitem_search = menu.findItem(R.id.mitem_search);
        mitem_reset = menu.findItem(R.id.mitem_reset);
        mitem_logout = menu.findItem(R.id.mitem_logout);
        mitem_withdrawal = menu.findItem(R.id.mitem_withdrawal);

//        mitem_search.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Intent i = new Intent(getApplicationContext(),SearchActivity.class);
//                startActivity(i);
//                return true;
//            }
//        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.mitem_search){
            Intent i = new Intent(this,SearchActivity.class);
            startActivity(i);
        }
        if(id == R.id.mitem_reset){
            Intent i = new Intent(this,SearchActivity.class);
            startActivity(i);
        }
        if(id == R.id.mitem_logout){
            return true;
        }
        if(id == R.id.mitem_withdrawal){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}