package com.example.anna.shedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivityNew extends ActionBarActivity implements View.OnClickListener {
    Button btnStudent = null;
    Button btnTeacher = null;
    Button btnSteward = null;
    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_new);

        btnStudent = (Button) findViewById(R.id.btnStudent);
        btnTeacher = (Button) findViewById(R.id.btnTeacher);
        btnSteward = (Button) findViewById(R.id.btnSteward);

        btnStudent.setOnClickListener(this);
        btnSteward.setOnClickListener(this);
        btnTeacher.setOnClickListener(this);
        intent = new Intent(MainActivityNew.this, com.example.anna.shedule.login.LoginActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity_new, menu);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStudent:
                intent.putExtra("TypeUser", R.id.btnStudent);
                startActivity(intent);
                break;
            case R.id.btnTeacher:
                intent.putExtra("TypeUser", R.id.btnTeacher);
                startActivity(intent);
                break;
            case R.id.btnSteward:
                intent.putExtra("TypeUser", R.id.btnSteward);
                startActivity(intent);
                break;
        }
    }
}
