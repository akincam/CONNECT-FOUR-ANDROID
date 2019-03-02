package com.example.akin.connectfour;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.content.Intent;
import android.widget.LinearLayout;

/**
 * Bu activityde vertical linear layout üzerine bir backround eklendi.
 * Start ve Exit buttonları eklendi ve intentler kullanılarak option activityle geçilmesi sağlandı
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class StartScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        createStartScreen();



        Drawable dr = getResources().getDrawable(R.drawable.s1);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        Display display = getWindowManager().getDefaultDisplay();

        Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap,display.getWidth() ,display.getHeight(), true));
        ((Button) findViewById(R.id.startButton)).setBackground(d);

        Drawable dr1 = getResources().getDrawable(R.drawable.s2);
        Bitmap bitmap1 = ((BitmapDrawable) dr1).getBitmap();
        Display display1 = getWindowManager().getDefaultDisplay();

        Drawable d1 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap1,display1.getWidth() ,display1.getHeight(), true));
        ((Button) findViewById(R.id.exitButton)).setBackground(d1);



        /**
         * start_scren de oluşturulan button üzerine intent ile diğer activiteye geçilmesi sağlandı
         */
        ((Button) findViewById(R.id.startButton)).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                Intent start_button = new Intent (StartScreen.this,StartOptions.class);
                startActivity(start_button);
                finishAffinity();
            }
        });
        /**
         * start_scren de oluşturulan button ile oyundan çıkılması sağlandı
         */
        ((Button) findViewById(R.id.exitButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(1);

            }
        });

    }

    /**
     * Xml üzerinden eklenen resimlerde çıkan hatalar nedeniyle drawable bir obje üzerine backroun eklendi
     * Telefonun ekran uzunluk ve genişliğinine göre bitmap objesiyle resim ana ekrana uygun olarak boyutlandırılıp
     * linear layout üzerine eklendi
     */
    public void createStartScreen(){
        Drawable dr = getResources().getDrawable(R.drawable.start1);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        Display display = getWindowManager().getDefaultDisplay();

        Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap,display.getWidth() ,display.getHeight(), true));
        LinearLayout start = (LinearLayout) findViewById(R.id.startS);
        start.setBackground(d);
    }
}
