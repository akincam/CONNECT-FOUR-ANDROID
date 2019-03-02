package com.example.akin.connectfour;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@RequiresApi(api = Build.VERSION_CODES.M)
public class GameScreenActivity extends ConnectFour {
    private int sizeEmpty,width;
    private GridLayout board;
    private boolean check;
    private int poscell;
    private int moveCount=-1;
    private TextView timeText;
    private int [] allMoves;
    private CountDownTimer timer;
    private long totalTime=30000;
    private boolean timeReset;
    private int time1;
    private int x,size;
    private char[] letter1 = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();                   //fullscreen yapmak için kullanıldı
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //fullscreen yapmak için kullanıldı
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);


        String row1 = getIntent().getExtras().getString("row"); //StartOption Class ında kullanıcının girdiği edittexteki board sayısını alır
        row = Integer.parseInt(row1); //integer a cast edildi ve board number alındı
        column = row;
        size=row;
        timeReset=getIntent().getExtras().getBoolean("bool");  //StartOption Class ında kullanıcının girdiği edittexteki time ı alır
        allMoves = new int[column * column * 2]; //hamle geri alabilmek için yapılan hamlelerin satır ve sütun locationları kaydedildi
        if(timeReset!=false) { //eğer kullanıcı time kapattıysa timeReset false olarak işaretlenir.
            String t1 = getIntent().getExtras().getString("time");
            time1 = Integer.parseInt(t1);
        }

        if(timeReset==false) {//eğer time off ise time ı gösteren textview invisible yapılır
            timeText=findViewById(R.id.textView2);
            timeText.setVisibility(View.INVISIBLE);
        }
        else //Tıme off değilse kullanıcının girdiği zaman alınır program çalışmasına bağlı olarak oluşan aksamalara karşılık +1 saniye ayrılır ve o eklenir
            totalTime=time1*1000+1000;

        Character gameS = getIntent().getExtras().getChar("pORc");//oyun tipi StartOption sınıfından alınır
        pvpOrpvc = gameS;

        createGamePage();//Oyunun oynanacağı sayfa oluşturulur
        createGameView(); //Oyunun Board u oluşturulur
        playViewGame(); //Oyunun oynandığı yer

        //Undo hamlesini yapmak istediğimiz button üzerine resim eklenir.
        Drawable dr = getResources().getDrawable(R.drawable.turnback);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        Display display = getWindowManager().getDefaultDisplay();

        Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap,display.getWidth() ,display.getHeight(), true));
        ((Button) findViewById(R.id.Undo)).setBackground(d);

        GameScreenActivity.super.playGame();        //Super Class olan ConnectFour da oyun alanının oluşturulup baslanması sağlanır
        ((Button) findViewById(R.id.e1)).setOnClickListener(new View.OnClickListener() {//Exit Butonu oyun oynanırken ana ekrana dönmeyi sağlar
            @Override
            public void onClick(View view) {
                Intent gamestop = new Intent(GameScreenActivity.this, StartScreen.class);
                startActivity(gamestop);
                finishAffinity();
            }
        });

        if(timeReset==true)//Eğer kullanıcı timeoff yapmadıysa her kullanıcı için süre başlar
            startTimer();

        ((Button) findViewById(R.id.Undo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Eğer Undo pvp modda olacaksa oyun oynanırken tutulan geri hamle alınır symbol değiştirilir ve yeniden süre baslar
                    kullamıcı time ı açık bıraktığında
                 */
                if (moveCount > 0&&pvpOrpvc=='p') {
                    gameBoard[allMoves[moveCount - 1]][allMoves[moveCount]].setCell('*');
                    moveCount -= 2;

                    if (symbol == 'X')
                        symbol = 'O';
                    else
                        symbol = 'X';
                    addCircle(symbol);
                    showSymbol();
                    if(timeReset==true) {
                        resetTimer();
                        startTimer();
                    }
                }
                /*
                PvC modunda iki hamle birden geri alınır ve zaman mevcutsa yeniden baslatılır
                 */
                else if (moveCount > 0&&pvpOrpvc=='c') {
                    gameBoard[allMoves[moveCount - 1]][allMoves[moveCount]].setCell('*');
                    moveCount -= 2;
                    gameBoard[allMoves[moveCount - 1]][allMoves[moveCount]].setCell('*');
                    moveCount -= 2;
                    symbol = 'X';
                    addCircle(symbol);
                    showSymbol();
                    if(timeReset==true) {
                        resetTimer();
                        startTimer();
                    }
                }

            }
        });

    }

    /**
     * Burada kullanıcının girdiği zaman oluşturulan CountDownTimer timer ile başlatılır
     * Her onTick te oyun alanı dolmadığı ve kazananın olmadığı durumda yani gameOver true ise
     * zaman texTView2 vasıtasıyla kullanıcıya gösterilir
     */
    private void startTimer() {
        timer = new CountDownTimer(totalTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                totalTime = millisUntilFinished;
                timeText=findViewById(R.id.textView2);
                if(gameover!=false)
                    timeText.setText("  "+millisUntilFinished / 1000);
                timeText.setTextSize(35);
            }

            /**
             * Eğer süre bittiğinde hamle yapılmamıssa random bir hamle yapılır undo için bu hamle kaydedilir
             * Symbol değiştirilir ve zaman yeniden baslatılır eğer mevcut ise.
             */
            @Override
            public void onFinish() {
               if(symbol=='X') {
                   int cnt=0;
                   for(int i=row-1;i>=0;--i){
                       for(int j=0;j<column;++j){
                           if(gameBoard[i][j].getCell()=='*'){
                               if(cnt==0) {
                                   gameBoard[i][j].setCell('X');
                                   moveCount++;
                                   allMoves[moveCount] = i;
                                   moveCount++;
                                   allMoves[moveCount] = j;
                               }
                               cnt=1;
                               break;
                           }
                       }
                   }
                   addCircle(symbol);
                   gameOverGame();
                    symbol = 'O';
                   resetTimer();
                   startTimer();
                   showSymbol();

                }
                else {
                   int cnt=0;
                   for(int i=row-1;i>=0;--i){
                       for(int j=0;j<column;++j){
                           if(gameBoard[i][j].getCell()=='*'){
                               if(cnt==0) {
                                   gameBoard[i][j].setCell('O');
                                   moveCount++;
                                   allMoves[moveCount] = i;
                                   moveCount++;
                                   allMoves[moveCount] = j;
                               }
                               cnt=1;
                               break;
                           }
                       }
                   }
                   addCircle(symbol);
                   gameOverGame();
                    symbol = 'X';

                   resetTimer();
                   startTimer();
                   showSymbol();
                }
            }
        }.start();

    }

    /**
     * Burada eğer kullanıcı zamanından önce hamle yaptıysa
     * Zaman yeniden baslatılmak için durdurulur
     */
    private void resetTimer() {
        timer.cancel();
        timer=null;
        System.gc();
        totalTime = time1*1000+1000;
    }

    /**
     * Burada Kullanıcının board size ına göre Oyunalanını olusturur.
     * Ekranın genişliğini alır ve bunu oyun row yani oyun alanına böler
     * 10 a kadar secildi cünkü cell sayısı arttıkca daha küçük bi alan olusuyordu.
     * Daha sonra row ve column a göre gridlayout bölünür gameBoard oluşturmak için
     * Daha Sonra iki for içinde( çünkü satır ve sütun carpımı kadar cell olmalı) bir ImageView olusturulur
     * Drawable bir objeye eklenmek istenen resim eklenir
     * Daha sonra bitmap eklenip yukarıda ayarlanan sizeEmpty ye göre resimlerin boyutu ayarlanır
     * Image View arka planı belirlenir ve gridlayout üzerine addView ile eklenir
     */
    public void createGameView(){
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        if(row<=10)
            sizeEmpty=width/row;
        else
            sizeEmpty = width / 10;

        board= findViewById(R.id.gridLayout);
        board.setRowCount(row);
        board.setColumnCount(column);

        for(int i=0;i<row;i++) {
            for (int j = 0; j < column; j++) {
                ImageView Circle =new ImageView(this);
                Drawable dr = getResources().getDrawable(R.drawable.cell_frame);
                Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
                Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap,sizeEmpty ,sizeEmpty, true));
                Circle.setBackground(d);
                board.addView(Circle);

            }
        }
    }

    /**
     * Oyun alanı oluşturulur
     * Drawable bir obje alınır bu bitmap e eklenir Display kullanılarak alınan ekran boyutuna göre resim ayarlanır ve eklenir.
     */
    private  void createGamePage(){
        Drawable dr1 = getResources().getDrawable(R.drawable.gamemen1);
        Bitmap bitmap1 = ((BitmapDrawable) dr1).getBitmap();
        Display display1 = getWindowManager().getDefaultDisplay();

        Drawable d2 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap1,display1.getWidth() ,display1.getHeight(), true));
        RelativeLayout start = (RelativeLayout) findViewById(R.id.gameS);
        start.setBackground(d2);
    }

    /**
     * Bu Ekranda bulunan Boyutu ayarlanmıs üstte bulunan gridLayout Üzerine oyun sırasını göstermek için Symbollere göre ayarlanmıs imageler eklenir.
     * Kullanıcı her hamle yaptığında ya da zaman dolduğunda resim yeniden değişir ve kullanıcı uyarılır.
     */
    private void showSymbol(){
        if(symbol=='X'){
            Drawable image = getResources().getDrawable(R.drawable.yesil);
            Bitmap bitmap1 = ((BitmapDrawable) image).getBitmap();
            Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap1,70 ,70, true));
            (( GridLayout) findViewById(R.id.imageB)).setBackground(d);
        }
        else{
            Drawable image = getResources().getDrawable(R.drawable.orange);
            Bitmap bitmap1 = ((BitmapDrawable) image).getBitmap();
            Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap1,70 ,70, true));
            (( GridLayout) findViewById(R.id.imageB)).setBackground(d);
        }
    }

    /**
     * Burada kullanıcı hamle yaptığında board üzerindeki her şey temizlenir removeAllViews() ile
     * Daha sonra symbollere göre atanan resimler symbollere göre eklenir
     * @param sy
     */
    private void addCircle(char sy)
    {
        board.removeAllViews();
        for(int i=0;i<row;i++) {
            for (int j = 0; j < column; j++) {
                ImageView Circle =new ImageView(this);
                Drawable dr = getResources().getDrawable(R.drawable.cell_frame);
                Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
                Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap,sizeEmpty ,sizeEmpty, true));
                Circle.setBackground(d);
                board.addView(Circle);
                if(gameBoard[i][j].getCell()=='X') {
                    Drawable dr1 = getResources().getDrawable(R.drawable.yesil);
                    Bitmap bitmap1 = ((BitmapDrawable) dr1).getBitmap();
                    Drawable d1 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap1, sizeEmpty, sizeEmpty, true));
                    Circle.setBackground(d1);
                }
                if(gameBoard[i][j].getCell()=='O') {
                    Drawable dr1 = getResources().getDrawable(R.drawable.orange);
                    Bitmap bitmap1 = ((BitmapDrawable) dr1).getBitmap();
                    Drawable d1 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap1, sizeEmpty, sizeEmpty, true));
                    Circle.setBackground(d1);
                }



            }
        }
    }

    /**
     * Oyunun Oynandığı yer.
     * setOnTouhc listener ve setOnclick Listener ı birlikte kullandım çünkü scroll yapıldığında touch da hamle de yapılıyordu.
     * setOnTouchListener ile kullanıcının dokunduğu locasyon alınıyor.
     * Daha sonra setOnClickListener a gönderiliyor ve hamleler yapılıyor oyunun bitip bitmediği oyun alanının dolup dolmadığı
     * burada kontrol ediliyor.
     */
    @SuppressLint("ClickableViewAccessibility")
    public void playViewGame() {

        board.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                x= (int) motionEvent.getX();
                return false;
            }
        });

        showSymbol();
        board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSymbol();

                check = false;
                int control=0;
                for(int i=0;sizeEmpty*i<x;i++)//burada hangi aralıktaki cell e dokunmus o data alınıyor
                    poscell=i;
                if (pvpOrpvc == 'p' && symbol == 'X') {//Oyun modu
                    if (setPosition(letter1[poscell])) {
                        check = play(letter1[poscell]);
                        for(int i=0;i<row;i++)//Burada kullanıcının oynadığı hamle kaydediliyor
                            if(gameBoard[i][letter1[poscell]-'A'].getCell()!='*'){
                                moveCount++;
                                allMoves[moveCount]=i;
                                moveCount++;
                                allMoves[moveCount]=letter1[poscell]-'A';
                                break;
                            }
                        addCircle(symbol);//Eklenen hamle ekranda gösteriliyot
                        control = 0;
                        for (int i = 0; i < row; ++i) {//oyun alanının dolu olupolmadığını kontrol eder
                            for (int j = 0; j < column; ++j)
                                if (gameBoard[i][j].getCell() != '*') {
                                    ++control;
                                }
                        }
                        if(row*row==control){
                            gameOverGame();
                        }
                        showWinner('X');//Kazananı daha sonra göstermek için kontrol ediliyor
                        showWinner('O');//
                        symbol = 'O';
                        if(timeReset==true) {//Eğer time açıksa hamle yaptığı anda sembol değişip zaman başlatılıyor
                            resetTimer();
                            startTimer();
                        }
                        showSymbol();
                        if(check) {//Eğer oyun bittiyse ConnectFourClass nın play fonk chek e true atıyor ve kazanan hamleler gösteriliyor
                            new CountDownTimer(7000,1000){
                                boolean c1=false;
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    board.setClickable(false);
                                    if(c1==false){
                                        c1=true;
                                        addCircle(symbol);
                                    }
                                    else{
                                        c1=false;
                                        displayWinner();
                                    }
                                }

                                @Override
                                public void onFinish() {//Ana ekrana dönüş sağlanıyor
                                    Intent gamestop = new Intent(GameScreenActivity.this, StartScreen.class);
                                    startActivity(gamestop);
                                    finishAffinity();

                                }
                            }.start();
                        }
                        control = 0;
                        for (int j = 0; j < row; ++j) {//oyun alanının dolu olupolmadığını kontrol eder
                            for (int i = 0; i < column; ++i)
                                if (gameBoard[j][i].getCell() != '*') {
                                    ++control;
                                }
                        }
                        if(row*row==control){
                            gameOverGame();
                        }
                    }
                } else if (pvpOrpvc == 'p' && symbol == 'O') {//kullanıcıya karşı oynanmak istendiğinde diğer kullanıcı için
                    if (setPosition(letter1[poscell])) {
                        check = play(letter1[poscell]);
                        for(int i=0;i<row;i++)
                            if(gameBoard[i][letter1[poscell]-'A'].getCell()!='*'){
                                moveCount++;
                                allMoves[moveCount]=i;
                                moveCount++;
                                allMoves[moveCount]=letter1[poscell]-'A';
                                break;
                            }
                        addCircle(symbol);
                        control = 0;
                        for (int j = 0; j < row; ++j) {//oyun alanının dolu olupolmadığını kontrol eder
                            for (int i = 0; i < column; ++i)
                                if (gameBoard[j][i].getCell() != '*') {
                                    ++control;
                                }
                        }
                        if(row*row==control){
                            gameOverGame();

                        }
                        showWinner('X');
                        showWinner('O');
                        symbol = 'X';
                        if(timeReset==true) {
                            resetTimer();
                            startTimer();
                        }
                        showSymbol();
                        if(check) {
                            new CountDownTimer(7000,1000){
                                boolean c1=false;
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    board.setClickable(false);
                                    if(c1==false){
                                        c1=true;
                                        addCircle(symbol);
                                    }
                                    else{
                                        c1=false;
                                        displayWinner();
                                    }

                                }

                                @Override
                                public void onFinish() {
                                    Intent gamestop = new Intent(GameScreenActivity.this, StartScreen.class);
                                    startActivity(gamestop);
                                    finishAffinity();

                                }
                            }.start();
                        }
                        control = 0;
                        for (int j = 0; j < row; ++j) {//oyun alanının dolu olupolmadığını kontrol eder
                            for (int i = 0; i < column; ++i)
                                if (gameBoard[j][i].getCell() != '*') {
                                    ++control;
                                }
                        }
                        if(row*row==control){
                            gameOverGame();
                        }
                    }
                } else if (pvpOrpvc == 'c') {//bilgisayara karşı oynamak istediğinde kullanıcı her hamle yaptığında bilgisayarda hamle yapar
                    if (setPosition(letter1[poscell])) {
                        symbol = 'X';
                        check = play(letter1[poscell]);
                        for(int i=0;i<row;i++)//kullanıcının hamlesi kaydediliyor
                            if(gameBoard[i][letter1[poscell]-'A'].getCell()!='*'){
                                moveCount++;
                                allMoves[moveCount]=i;
                                moveCount++;
                                allMoves[moveCount]=letter1[poscell]-'A';
                                break;
                            }
                        addCircle(symbol);
                        control = 0;
                        for (int j = 0; j < row; ++j) {//oyun alanının dolu olupolmadığını kontrol eder
                            for (int i = 0; i < column; ++i)
                                if (gameBoard[j][i].getCell() != '*') {
                                    ++control;
                                }
                        }
                        if(row*row==control){
                            gameOverGame();

                        }
                        showWinner('X');
                        showWinner('O');
                        if(check) {
                            new CountDownTimer(7000,1000){
                                boolean c1=false;
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    board.setClickable(false);

                                    if(c1==false){
                                        c1=true;
                                        addCircle(symbol);
                                    }
                                    else{
                                        c1=false;
                                        displayWinner();
                                    }
                                }

                                @Override
                                public void onFinish() {
                                    Intent gamestop = new Intent(GameScreenActivity.this, StartScreen.class);
                                    startActivity(gamestop);
                                    finishAffinity();

                                }
                            }.start();
                        }
                        control = 0;
                        for (int j = 0; j < row; ++j) {//oyun alanının dolu olupolmadığını kontrol eder
                            for (int i = 0; i < column; ++i)
                                if (gameBoard[j][i].getCell() != '*') {
                                    ++control;
                                }
                        }
                        if(row*row==control){
                            gameOverGame();

                        }
                        //Oyunun bitip bitmediği kontrol ediliyor
                        symbol = 'O';
                        if(row*row!=control)
                            check = play();
                        addCircle(symbol);
                        control = 0;
                        for (int j = 0; j < row; ++j) {//oyun alanının dolu olupolmadığını kontrol eder
                            for (int i = 0; i < column; ++i)
                                if (gameBoard[j][i].getCell() != '*') {
                                    ++control;
                                }
                        }
                        if(row*row==control){
                            gameOverGame();

                        }
                        showWinner('X');
                        showWinner('O');
                        if(row*row!=control) {
                            moveCount++;
                            allMoves[moveCount] = (comploc - comploc % 10) / 10;
                            moveCount++;
                            allMoves[moveCount] = comploc % 10;
                        }
                        symbol='X';
                        if(timeReset==true) {
                            resetTimer();
                            startTimer();
                        }
                        control = 0;
                        for (int j = 0; j < row; ++j) {//oyun alanının dolu olupolmadığını kontrol eder
                            for (int i = 0; i < column; ++i)
                                if (gameBoard[j][i].getCell() != '*') {
                                    ++control;
                                }
                        }
                        if(row*row==control){
                            gameOverGame();
                        }
                        if(check) {
                            new CountDownTimer(7000,1000){
                                boolean c1=false;
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    board.setClickable(false);
                                    if(c1==false){
                                        c1=true;
                                        addCircle(symbol);
                                    }
                                    else{
                                        c1=false;
                                        displayWinner();
                                    }
                                }

                                @Override
                                public void onFinish() {
                                    Intent gamestop = new Intent(GameScreenActivity.this, StartScreen.class);
                                    startActivity(gamestop);
                                    finishAffinity();

                                }
                            }.start();
                        }

                    }
                }
            }
        });

    }

    /**
     * Burada oyunu kazanan symboller K ile değiştirilir daha sonra kullanıcıya göstermek için
     * @param s
     */
    private void showWinner(char s) {
        for (int i = 0; i < size; ++i) {
            for (int j = size - 1; j >= 0; --j) {
                if (j - 3 >= 0 && gameBoard[j][i].getCell() == s && gameBoard[j - 1][i].getCell() == s &&
                        gameBoard[j - 2][i].getCell() == s && gameBoard[j - 3][i].getCell() == s) {
                    gameBoard[j][i].setCell('K');
                    gameBoard[j - 1][i].setCell('K');
                    gameBoard[j - 2][i].setCell('K');
                    gameBoard[j - 3][i].setCell('K');
                    break;
                }
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; ++j) {
                if (i + 3 < size &&j + 3 < size&& gameBoard[i][j].getCell() == s &&
                        gameBoard[i + 1][j + 1].getCell() == s && gameBoard[i + 2][j + 2].getCell() == s &&
                        gameBoard[i + 3][j + 3].getCell() == s) {
                    gameBoard[i][j].setCell('K');
                    gameBoard[i + 1][j + 1].setCell('K');
                    gameBoard[i + 2][j + 2].setCell('K');
                    gameBoard[i + 3][j + 3].setCell('K');
                    break;
                }
            }
        }
        for (int i = size - 1; i >= 0; --i) {
            for (int j = 0; j < size; ++j) {
                if (i - 3 >= 0 && j + 3 < size && gameBoard[i][j].getCell() == s && gameBoard[i - 1][j + 1].getCell() == s &&
                        gameBoard[i - 2][j + 2].getCell() == s && gameBoard[i - 3][j + 3].getCell() == s) {
                    gameBoard[i][j].setCell('K');
                    gameBoard[i - 1][j + 1].setCell('K');
                    gameBoard[i - 2][j + 2].setCell('K');
                    gameBoard[i - 3][j + 3].setCell('K');
                    break;
                }
            }
        }
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (j + 3 < size && gameBoard[i][j].getCell() == s && gameBoard[i][j + 1].getCell() == s &&
                        gameBoard[i][j + 2].getCell() == s && gameBoard[i][j + 3].getCell() == s) {
                    gameBoard[i][j].setCell('K');
                    gameBoard[i][j + 1].setCell('K');
                    gameBoard[i][j + 2].setCell('K');
                    gameBoard[i][j + 3].setCell('K');
                    break;
                }
            }
        }
    }

    /**
     * Burada animasyon için oluşturulmus bir method
     *
     */
    private void displayWinner(){
        board.removeAllViews();
        ImageView Circle;
        for(int i=0;i<size;i++) {
            for (int j = 0; j < size; j++) {
                Circle =new ImageView(this);
                Drawable dr = getResources().getDrawable(R.drawable.cell_frame);
                Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
                Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap,sizeEmpty ,sizeEmpty, true));
                Circle.setBackground(d);
                board.addView(Circle);
                Drawable dr11;
                    if(gameBoard[i][j].getCell()=='K') {

                        if(symbol=='X'){
                            dr11 = getResources().getDrawable(R.drawable.orange);
                            Bitmap bitmap11 = ((BitmapDrawable) dr11).getBitmap();
                            Drawable d12 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap11, sizeEmpty, sizeEmpty, true));
                            Circle.setBackground(d12);
                        }

                        else if(symbol=='O') {
                            dr11 = getResources().getDrawable(R.drawable.yesil);
                            Bitmap bitmap11 = ((BitmapDrawable) dr11).getBitmap();
                            Drawable d12 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap11, sizeEmpty, sizeEmpty, true));
                            Circle.setBackground(d12);
                        }
                    }
                if(gameBoard[i][j].getCell()=='X') {
                    dr11 = getResources().getDrawable(R.drawable.yesil);
                    Bitmap bitmap11 = ((BitmapDrawable) dr11).getBitmap();
                    Drawable d12 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap11, sizeEmpty, sizeEmpty, true));
                    Circle.setBackground(d12);
                }
                if(gameBoard[i][j].getCell()=='O') {
                    dr11 = getResources().getDrawable(R.drawable.orange);
                    Bitmap bitmap11 = ((BitmapDrawable) dr11).getBitmap();
                    Drawable d12 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap11, sizeEmpty, sizeEmpty, true));
                    Circle.setBackground(d12);
                }

            }
        }
        ((Button)findViewById(R.id.e1)).setClickable(false);
        ((Button)findViewById(R.id.Undo)).setClickable(false);
       // ((GridLayout)findViewById(R.id.imageB)).setVisibility(View.GONE);
        //((TextView)findViewById(R.id.textView2)).setVisibility(View.GONE);
    }

    /**
     * Oyun bittiğinde TextView ikiye oyunun bittğini yazıp ana ekrana dönüs sağlanıyor
     */
    private void gameOverGame(){
        if(gameover==false){

            new CountDownTimer(6000,1000){
                boolean refresh=false;
                @Override
                public void onTick(long millisUntilFinished) {
                    ((Button)findViewById(R.id.e1)).setVisibility(View.GONE);
                    ((Button)findViewById(R.id.Undo)).setVisibility(View.GONE);
                    ((GridLayout)findViewById(R.id.imageB)).setVisibility(View.GONE);
                    ((TextView)findViewById(R.id.textView2)).setVisibility(View.VISIBLE);
                    ((TextView)findViewById(R.id.textView2)).getLayoutParams().height=180;
                    Display display = getWindowManager().getDefaultDisplay();
                    ((TextView)findViewById(R.id.textView2)).getLayoutParams().width=display.getWidth();
                    ((TextView)findViewById(R.id.textView2)).setBackgroundColor(Color.WHITE);
                    ((TextView)findViewById(R.id.textView2)).setTextColor(Color.BLACK);
                    if(refresh==false) {
                        ((TextView) findViewById(R.id.textView2)).setText("   ------GameBoard Full------ ");
                        refresh=true;
                    }
                    else {
                        refresh=false;
                        ((TextView) findViewById(R.id.textView2)).setText("");
                    }


                }

                @Override
                public void onFinish() {
                    Intent gamestop = new Intent(GameScreenActivity.this, StartScreen.class);
                    startActivity(gamestop);
                    finishAffinity();

                }
            }.start();
        }
    }

}






