package com.example.akin.connectfour;

/**
 * Created by AKIN Ç on 15.02.2018.
 */


import android.support.v7.app.AppCompatActivity;
import android.text.Editable;

import static java.lang.System.exit;

/**
 *
 * @author akncam
 */
public class ConnectFour extends AppCompatActivity {
    //  protected  JFrame window;
    protected int row=3;
    protected int column;
    protected  int time=1000;
    protected char pvpOrpvc='p';
    protected char symbol='X';
    protected char position;
    protected Cell[][] gameBoard;
    public boolean timeOff=false;
    protected int comploc;
    boolean gameover=true;
    protected void displayGame()
    {
        char letter='A';
        System.out.println();
        int i ; int j;
        for(i=0;i<column;++i){
            System.out.printf("%c ",letter);
            ++letter;
        }
        System.out.println();
        for(i=0;i<row;++i){
            for(j=0;j<column;++j)
                System.out.printf("%s \n",gameBoard[i][j].getCell());
        }
    }
    protected boolean playGame()
    {
        gameBoard=new Cell[row][column];
        for(int i=0; i<row; ++i)
            for(int j=0; j<column; ++j)
                gameBoard[i][j]= new Cell();
        for(int i=0; i<row; ++i){
            for(int j=0; j<column; ++j){
                gameBoard[i][j].setCell('*');
                gameBoard[i][j].setRow(i);
                gameBoard[i][j].setColumn(j);
            }
        }
        return true;
    }
    /**
     * bu fonksiyon kullanıcının oynadığı hamleyi ilgili pozisyona ekler
     * @param take_input
     * @return
     */
    protected boolean play(char take_input)
    {
        int control=0;
        int i;
        for(int j=0;j<row;++j){//check whether bord is full
            for(i=0; i<column;++i)
                if(gameBoard[j][i].getCell()!='*'){
                    ++control;
                }
        }
        if(control==column*row-1){//if board is full
            for(i=row-1;i>=0;--i){//move the symbol legal location
                if(gameBoard[i][take_input-'A'].getCell()=='*'){
                    gameBoard[i][take_input-'A'].setCell(symbol);
                    break;
            }
            }
            controlWinner(i,take_input-'A');
            if(controlWinner(i,take_input-'A')==false){
                gameover=false;
                return gameover;
            }
            return true;
        }
        else{//if board has still empty legal location
            for(i=row-1;i>=0;--i){//move the symbol legal location
                if(gameBoard[i][take_input-'A'].getCell()=='*'){
                    gameBoard[i][take_input-'A'].setCell(symbol);
                    break;
                }
            }
            return controlWinner(i,take_input-'A');
        }

    }
    private boolean controlWinner(int _row,int _pos)
    {
        if(check_LF(_row,_pos)>=4){
            return true;
        }
        else if(check_UD(_row,_pos)>=4){
            return true;
        }
        else if(check_LD(_row,_pos)>=4){
            return true;
        }
        else if(check_RD(_row,_pos)>=4){
            return true;
        }
        return false;
    }
    /**
     * kullanıcının hamlesinin legal olup olmadığını kontrol eder
     * @param take_position
     * @return
     */
    protected boolean setPosition(char take_position)
    {
        for(int i=row-1;i>=0&&take_position==1;--i){
            if(take_position==1&&gameBoard[i][take_position-'A'].getCell()=='*'){
                position=take_position;
                return true;
            }
            else if(i==0&&take_position==1&&gameBoard[i][take_position-'A'].getCell()!='*'||gameBoard[i][take_position-'A'].getCell()==' ')
                return false;
        }
        for(int i=row-1;i>=0;--i){
            if(gameBoard[i][take_position-'A'].getCell()=='*'){
                position=take_position;
                return true;
            }
            else if(i==0&&gameBoard[i][take_position-'A'].getCell()!='*'||gameBoard[i][take_position-'A'].getCell()==' ')
                return false;
        }
        return false;
    }
    /**
     * oyunun yukarıdan aşağ bitip bitmedğini kontrol eder
     * @param pos_row
     * @param pos_col
     * @return
     */
    protected int check_UD(int pos_row,int pos_col)
    {
        int counter=0;
        for(int i=pos_row;i<row;++i){
            if(gameBoard[i][pos_col].getCell()==symbol){
                ++counter;
            }
            else
                break;
        }
        return counter;
    }
    /**
     * soldan sağa oyunun bitip bitmediğini kontrol eder
     * @param pos_row
     * @param pos_col
     * @return
     */
    protected int check_LF(int pos_row,int pos_col)
    {

        int counter=0;
        for(int i=pos_col;i>=0;--i){
            if(gameBoard[pos_row][i].getCell()==symbol)
                ++counter;
            else
                break;
        }
        for(int i=pos_col+1;i<column;++i){
            if(gameBoard[pos_row][i].getCell()==symbol)
                ++counter;
            else
                break;
        }
        return counter;

    }
    /**
     * oyunun sol çapraz bitip bitmediğini kontrol eder
     * @param pos_row
     * @param pos_col
     * @return
     */
    protected int check_RD(int pos_row,int pos_col)
    {
        int counter=0;
        for(int i=pos_row, j=pos_col;i>=0&&j<column;--i,++j){
            if(i>=0&&j<column&&gameBoard[i][j].getCell()==symbol)
                ++counter;
            else
                break;
        }
        for(int i=pos_row+1, j=pos_col-1;i<row&&j>=0;++i,--j){
            if(i<row&&j>=0&&gameBoard[i][j].getCell()==symbol)
                ++counter;
            else
                break;
        }
        return counter;

    }
    /**
     * oyunun sağ çapraz olarak bitip bitmedğini kontrol eder
     * @param pos_row
     * @param pos_col
     * @return
     */
    protected int check_LD(int pos_row,int pos_col)
    {
        int counter=0;
        for(int i=pos_row, j=pos_col;i>=0&&j>=0;--i,--j){
            if(i>=0&&j>=0&&gameBoard[i][j].getCell()==symbol)
                ++counter;
            else
                break;
        }
        for(int i=pos_row+1, j=pos_col+1;i<row&&j<column;++i,++j){
            if(i<row&&j<column&&gameBoard[i][j].getCell()==symbol)
                ++counter;
            else
                break;
        }
        return counter;

    }
    /**
     * Cell class ı game board datasını tutar size(row column) ve cell içindeki datayı
     */
    public class Cell
    {
        private int return_row;
        private int return_column;
        private char cell;
        /**
         *Parametre almayan constructor data field ları initialize eder.
         */
        public Cell()
        {
            return_row=0;
            return_column=0;
            cell=0;
        }
        /**
         * parametre olarak aldığı cell fieldini setter ile kontrol edip assignment yapar.
         * @param cell
         */
        public Cell(char cell)
        {
            setCell(cell);
        }
        /**
         * return_row ve column a atama yapar.
         * @param cell_row
         * @param cell_column
         */
        public Cell(int cell_row ,int cell_column)
        {
            setRow(cell_row);
            setColumn(cell_column);
        }

        /**
         *
         * @param cell_row
         */
        private void setRow(int cell_row)
        {
            return_row=cell_row;
        }
        /**
         * setter.
         * @param cell_column
         */
        private void setColumn(int cell_column)
        {
            return_column=cell_column;
        }
        /**
         * setter.
         * @param content_cell
         */
        protected void setCell(char content_cell)
        {
            cell=content_cell;
        }
        /**
         * getter return_row field a erişmek için
         * @return
         */
        public int getRow()
        {
            return return_row;
        }
        /**
         * getter return_column field a erişmek için.
         * @return
         */
        public int getColumn()
        {
            return return_column;
        }
        /**
         * getter.
         * @return
         */
        public char getCell()
        {
            return cell;
        }
    }

    int comp_LF(char sy)
    {
        for(int i=0;i<row;i++){
            for(int j=0;j<column;++j){
                if(j+3<row&&
                        gameBoard[i][j].getCell()==sy&&gameBoard[i][j+1].getCell()==sy&&
                        gameBoard[i][j+2].getCell()==sy&&gameBoard[i][j+3].getCell()=='*')
                    return(j+3);
                if(j-3>=0&&
                        gameBoard[i][j].getCell()==sy&&gameBoard[i][j-1].getCell()==sy&&gameBoard[i][j-2].getCell()==sy&&
                        gameBoard[i][j-3].getCell()=='*')
                    return(j-3);
            }
        }
        return 2017;
    }
    int comp_UD(char sy)
    {
        int counter=0;
        for(int i=0;i<column;++i){
            for(int j=row-1;j>=0;--j){
                if(gameBoard[j][i].getCell()==sy){
                    ++counter;
                    if(counter==3&&j-1>=0&&gameBoard[j-1][i].getCell()=='*'){
                        return i;
                    }
                }
                else
                    counter=0;
            }
        }
        return 2017;
    }
    int comp_LD(char sy)
    {
        for(int i=0;i<row;i++){
            for(int j=0;j<row;++j){
                if(i+2<row&&j+2<row&&gameBoard[i][j].getCell()==sy&&gameBoard[i+1][j+1].getCell()==sy&&gameBoard[i+2][j+2].getCell()==sy){
                    if(i-1>=0&&j-1>=0&&gameBoard[i][j-1].getCell()!='*'&&gameBoard[i-1][j-1].getCell()=='*')
                        return(j-1);
                    if(i+3<row&&j+3<row&&gameBoard[i+3][j+3].getCell()=='*')
                        return j+3;
                }
            }
        }
        return 2017;
    }

    int comp_RD(char sy)
    {
        for(int i=row-1;i>=0;--i){
            for(int j=0;j<column;++j){
                if(i-2>=0&&j+2<column&&gameBoard[i][j].getCell()==sy&&gameBoard[i-1][j+1].getCell()==sy&&
                        gameBoard[i-2][j+2].getCell()==sy){
                    if(j+3<column&&i-3>=0&&gameBoard[i-3][j+3].getCell()=='*')
                        return j+3;
                    else if(j-1>=0&&i+1<row&&gameBoard[i+1][j-1].getCell()=='*')
                        return j-1;
                }
            }
        }
        return 2017;
    }
    int compAttack_LF(char sy)
    {
        int counter=0;
        for(int i=0;i<row;++i){
            for(int j=0;j<column;++j){
                if(gameBoard[i][j].getCell()==sy){
                    ++counter;
                    if(counter==2&&j+1<column&&i+1<row&&gameBoard[i+1][j+1].getCell()!='*'&&gameBoard[i][j+1].getCell()=='*')
                        return j+1;
                    else if(counter==2&&j-2>=0&&i+1<row&&gameBoard[i+1][j-2].getCell()!='*'&gameBoard[i][j-2].getCell()=='*')
                        return j-2;
                }
                else
                    counter=0;
            }
        }
        return 2017;

    }
    int compAttack_UD(char sy)
    {
        int counter=0;
        for(int i=0;i<column;++i){
            for(int j=row-1;j>=0;--j){
                if(gameBoard[j][i].getCell()==sy){
                    ++counter;
                    if(counter==2&&j-1>=0&&gameBoard[j-1][i].getCell()=='*')
                        return i;
                }
                else
                    counter=0;
            }
        }
        return 2017;
    }
    int compAttack_RD(char sy)
    {
        for(int i=row-1;i>=0;--i){
            for(int j=0;j<column;++j){
                if(i-1>=0&&j+1<column&&gameBoard[i][j].getCell()==sy&&gameBoard[i-1][j+1].getCell()==sy){
                    if(j+2<column&&i-2>=0&&gameBoard[i-2][j+2].getCell()=='*')
                        return j+2;
                    else if(j-1>=0&&i+1<row&&gameBoard[i+1][j-1].getCell()=='*')
                        return j-1;
                }
            }
        }
        return 2017;
    }
    int compAttack_LD(char sy)
    {
        for(int i=0;i<row;++i){
            for(int j=0;j<column;++j){
                if(i+1<row&&j+1<column&&gameBoard[i][j].getCell()==sy&&gameBoard[i+1][j+1].getCell()==sy){
                    if(j+2<column&&i+2<row&&gameBoard[i+2][j+2].getCell()=='*')
                        return j+2;
                    else if(j-1>=0&&i-1>=0&&gameBoard[i-1][j-1].getCell()=='*')
                        return j-1;
                }
            }
        }
        return 2017;
    }
    int compB_LF(char sy)
    {
        int counter=0;
        for(int i=0;i<row;++i){
            for(int j=0;j<column;++j){
                if(j+3<column&&i+1<row&&gameBoard[i][j].getCell()==sy&&gameBoard[i][j+1].getCell()==sy&&
                        gameBoard[i][j+3].getCell()==sy&&gameBoard[i][j+2].getCell()=='*'&&gameBoard[i+1][j+2].getCell()!='*')
                    return j+2;
                if(j+3<column&&gameBoard[i][j].getCell()==sy&&gameBoard[i][j+1].getCell()=='*'&&
                        gameBoard[i][j+2].getCell()==sy&&gameBoard[i][j+3].getCell()==sy)
                    return j+1;
            }
        }
        return 2017;
    }
    int compB_LD(char sy)
    {
        for(int i=0;i<row;++i){
            for(int j=0;j<column;++j){
                if(i+3<row&&j+3<column&&gameBoard[i][j].getCell()==sy&&gameBoard[i+1][j+1].getCell()==sy&&
                        gameBoard[i+2][j+2].getCell()=='*'&&gameBoard[i+3][j+2].getCell()!='*'&&gameBoard[i+3][j+3].getCell()==sy)
                    return j+2;
                else if(i+3<row&&j+3<column&&gameBoard[i][j].getCell()==sy&&gameBoard[i+1][j+1].getCell()=='*'&&gameBoard[i+2][j+1].getCell()!='*'&&
                        gameBoard[i+2][j+2].getCell()==sy&&gameBoard[i+3][j+3].getCell()==sy)
                    return j+1;
            }
        }
        return 2017;
    }
    int compB_RD(char sy)
    {
        for(int i=row-1;i>=0;--i){
            for(int j=0;j<column;++j){
                if(i-3>=0&&j+3<column&&gameBoard[i][j].getCell()==sy&&gameBoard[i-1][j+1].getCell()==sy&&
                        gameBoard[i-2][j+2].getCell()=='*'&&gameBoard[i-1][j+2].getCell()!='*'&&gameBoard[i-3][j+3].getCell()==sy)
                    return j+2;
                else if(i-3>=0&&j+3<column&&gameBoard[i][j].getCell()==sy&&gameBoard[i-1][j+1].getCell()=='*'&&gameBoard[i][j+2].getCell()!='*'&&
                        gameBoard[i-2][j+2].getCell()==sy&&gameBoard[i-3][j+3].getCell()==sy)
                    return j+1;
            }
        }
        return 2017;
    }

    int moveComputer(int loc)
    {
        int i;
        for(i=row-1;i>=0&&loc>=0;--i){//inputu ilgili lokasyona yerleştirir
            if(gameBoard[i][loc].getCell()=='*'){
                gameBoard[i][loc].setCell('O');
                comploc=10*i+loc;
                return 10*i+loc;
            }
        }

        return 2017;
    }
    boolean play()
    {
        if(comp_LF('O')!=2017){
            moveComputer(comp_LF('O'));
            return cntrol('O');
        }
        if(comp_UD('O')!=2017){
            moveComputer(comp_UD('O'));
            return cntrol('O');
        }
        if(comp_LD('O')!=2017){
            moveComputer(comp_LD('O'));
            return cntrol('O');
        }
        if(comp_RD('O')!=2017){
            moveComputer(comp_RD('O'));
            return cntrol('O');
        }
        if(compB_LF('O')!=2017){
            moveComputer(compB_LF('O'));
            return cntrol('O');
        }
        if(compB_LD('O')!=2017){
            moveComputer(compB_LD('O'));
            return cntrol('O');
        }
        if(compB_RD('O')!=2017){
            moveComputer(compB_RD('O'));
            return cntrol('O');
        }

        if(comp_LF('X')!=2017){
            moveComputer(comp_LF('X'));
            return false;
        }
        if(comp_UD('X')!=2017){
            moveComputer(comp_UD('X'));
            return false;
        }
        if(comp_LD('X')!=2017){
            moveComputer(comp_LD('X'));
            return false;
        }
        if(comp_RD('X')!=2017){
            moveComputer(comp_RD('X'));
            return false;
        }
        if(compB_LF('X')!=2017){
            moveComputer(compB_LF('X'));
            return false;
        }
        if(compB_LD('X')!=2017){
            moveComputer(compB_LD('X'));
            return false;
        }
        if(compB_RD('X')!=2017){
            moveComputer(compB_RD('X'));
            return false;
        }
        if(compAttack_UD('O')!=2017){
            moveComputer(compAttack_UD('O'));
            return false;
        }
        else if(compAttack_LF('O')!=2017){
            moveComputer(compAttack_LF('O'));
            return false;
        }
        else if(compAttack_RD('O')!=2017){
            moveComputer(compAttack_RD('O'));
            return false;
        }
        else if(compAttack_LD('O')!=2017){
            moveComputer(compAttack_LD('O'));
            return false;
        }
        //to complete 2
        for(int i=0;i<row;++i){
            for(int j=0;j<column;++j){
                if(gameBoard[i][j].getCell()=='O'){
                    if(j+1<column&&i+1<row&&gameBoard[i+1][j+1].getCell()!='*'&&gameBoard[i][j+1].getCell()=='*'){
                        moveComputer(j+1);
                        return false;
                    }
                    else if(j-1>=0&&i+1<row&&gameBoard[i+1][j-1].getCell()!='*'&&gameBoard[i][j-1].getCell()=='*'){
                        moveComputer(j-1);
                        return false;
                    }
                }
            }
        }

        int counter=0;
        for(int i=0;i<column;++i){
            for(int j=0;j<row;++j){
                if(j-1>=0&&gameBoard[j][i].getCell()=='O'&&gameBoard[j-1][i].getCell()=='*'){
                    ++counter;
                    if(counter>=1){
                        moveComputer(i);
                        return false;
                    }
                }
                else
                    counter=0;
            }
        }
        //for first move or other legal moves
        for(int i=row-1;i>=0&&column>=2;--i){
            for(int j=2;j<column;++j){
                if(gameBoard[i][j].getCell()=='*'){
                    moveComputer(j);
                    return false;
                }
            }
        }
        for(int i=row-1;i>=0;--i){
            for(int j=0;j<column;++j){
                if(gameBoard[i][j].getCell()=='*'){
                    moveComputer(j);
                    return false;
                }
            }
        }
        return false;
    }
    private boolean cntrol(char s) {
        int size=row;
        for (int i = 0; i < size; ++i) {
            for (int j = size - 1; j >= 0; --j) {
                if (j - 3 >= 0 &&
                        gameBoard[j][i].getCell() == s && gameBoard[j - 1][i].getCell() == s &&
                        gameBoard[j - 2][i].getCell() == s && gameBoard[j - 3][i].getCell() == s) {
                    return true;
                }
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; ++j) {
                if (i + 3 < size &&j + 3 < size&& gameBoard[i][j].getCell() == s &&
                        gameBoard[i + 1][j + 1].getCell() == s && gameBoard[i + 2][j + 2].getCell() == s &&
                        gameBoard[i + 3][j + 3].getCell() == s) {
                    return true;
                }
            }
        }
        for (int i = size - 1; i >= 0; --i) {
            for (int j = 0; j < size; ++j) {
                if (i - 3 >= 0 && j + 3 < size && gameBoard[i][j].getCell() == s && gameBoard[i - 1][j + 1].getCell() == s &&
                        gameBoard[i - 2][j + 2].getCell() == s && gameBoard[i - 3][j + 3].getCell() == s) {
                    return true;
                }
            }
        }
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (j + 3 < size && gameBoard[i][j].getCell() == s && gameBoard[i][j + 1].getCell() == s &&
                        gameBoard[i][j + 2].getCell() == s && gameBoard[i][j + 3].getCell() == s) {
                    return true;
                }
            }
        }
        return false;
    }

}
