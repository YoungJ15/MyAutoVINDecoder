package com.peralta.apps.vininformation;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import utils.CheckNetwork;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();
        if(!CheckNetwork.isInternetAvailable(this)) {
            Toast.makeText(this, "Failed to load. Internet connection is required", Toast.LENGTH_SHORT).show();
        }
        else{
            getFragmentManager().beginTransaction().add(R.id.container, new VinFragment()).commit();
        }

    }

    private void setUpToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Vin Information");
        toolbar.inflateMenu(R.menu.menu_main);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.about:
                item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Dialog dialog = new Dialog(MainActivity.this);
                        dialog.setContentView(R.layout.about_dialog);
                        dialog.setTitle(R.string.app_name);
                        dialog.show();
                        Log.d("About option clicked", String.valueOf(item.getItemId()));
                        return true;
                    }
                });
                break;
            case R.id.exit:

                finish();
                Log.d("Exit option clicked", String.valueOf(item.getItemId()));
        }
        return super.onOptionsItemSelected(item);
    }
}
