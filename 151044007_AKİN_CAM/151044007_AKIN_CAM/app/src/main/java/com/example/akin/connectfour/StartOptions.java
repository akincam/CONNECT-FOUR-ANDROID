package com.example.akin.connectfour;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

public class StartOptions extends ConnectFour  {

    private EditText row_text;
    private ToggleButton pORc;
    private Character gamestyle='c';
    private  EditText time;
    private int checktime=912;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_options);
        System.gc();
        ((EditText)findViewById(R.id.edittime)).setVisibility(View.INVISIBLE);
        createStartOption();


        ((Button) findViewById(R.id.oyuna_basla)).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                Intent start_game_button = new Intent(StartOptions.this,GameScreenActivity.class);
                if(row>=5&&row<=40&&checktime!=911) {
                    start_game_button = new Intent(getApplicationContext(), GameScreenActivity.class);
                    start_game_button.putExtra("row",row_text.getText().toString());
                    start_game_button.putExtra("pORc",gamestyle);
                    start_game_button.putExtra("time",time.getText().toString());
                    start_game_button.putExtra("bool",timeOff);
                    startActivity(start_game_button);
                }
                else{
                    if(checktime==911)
                        ((TextView) findViewById(R.id.standbytime)).setText("ENTER TİME");
                    if(row<5||row>40)
                        ((TextView) findViewById(R.id.oyun_alani_textView)).setText("  Please 4-40" );
                }


            }
        });
        ((Button) findViewById(R.id.exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(1);
            }
        });
        row_text=findViewById(R.id.oyun_alani);
        row_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String no=row_text.getText().toString();
                if(no.length()!=0)
                    row=Integer.parseInt(no);
                else
                    row=3;
                return false;
            }
        });
        pORc=findViewById(R.id.oyuntipi);
        pORc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    gamestyle='p';
                } else {
                    gamestyle='c';
                }
            }
        });
        time=findViewById(R.id.edittime);
        time.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String no=time.getText().toString();
                if(no.length()!=0)
                    checktime=Integer.parseInt(no);
                return false;
            }
        });
        ((ToggleButton) findViewById(R.id.oyuntipi)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean on = ((ToggleButton) view).isChecked();
                if(on)
                    pvpOrpvc='p';
                else
                    pvpOrpvc='c';
            }
        });


        ((ToggleButton)findViewById(R.id.timeoffon)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean on= ((ToggleButton) view).isChecked();
                if(on) {
                    ((EditText) findViewById(R.id.edittime)).setVisibility(View.VISIBLE);
                    checktime = 911;
                    timeOff=true;
                }
                else {
                    ((EditText) findViewById(R.id.edittime)).setVisibility(View.INVISIBLE);
                    checktime = 912;
                    timeOff=false;
                }

            }
        });


    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void createStartOption(){
        Drawable dr = getResources().getDrawable(R.drawable.options);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        Display display = getWindowManager().getDefaultDisplay();

        Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap,display.getWidth() ,display.getHeight(), true));
        LinearLayout option = (LinearLayout) findViewById(R.id.startOption);
        option.setBackground(d);
    }
}
