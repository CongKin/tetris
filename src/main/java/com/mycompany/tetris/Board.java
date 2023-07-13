/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.Timer;
import packets.Attack;
import packets.BoardPacket;
import packets.SignalPacket;

/**
 *
 * @author User
 */
public class Board extends JPanel implements KeyListener, MouseListener, MouseMotionListener{
    
    public static int STATE_GAME_PLAY=0;
    public static int STATE_GAME_PAUSE=1;
    public static int STATE_GAME_OVER=2;
    public static int STATE_GAME_WIN=3;
    
    private int state = STATE_GAME_PAUSE;
    
    private static int FPS= 60;
    private static int delay = FPS / 1000;

    public static final int BOARD_WIDTH=10;
    public static final int BOARD_HEIGHT=20;
    public static final int BLOCK_SIZE=30;
    private Timer looper;
    private Color[][] board = new Color[BOARD_HEIGHT][BOARD_WIDTH];
    
    private Random random;
    
    private Shape[] shapes = new Shape[7];
    private Color[] colors={Color.decode("#ed1c24"), Color.decode("#ff7f27"), Color.decode("#fff200"),
        Color.decode("#22b14c"), Color.decode("#00a2e8"), Color.decode("#a349a4"), Color.decode("#3f48cc")};
    
    private Shape currentShape;
    private LinkedList<Integer> shapeList;
    private int blockPlaced = 0;
    
    private int holdShape = -1;
    private Boolean isChanged = false;
    
    private Client client;
    
    private LinkedList<newRow> newRowList;
    
    private BoardHandler boardHandler;
    
    public Board(){
        random = new Random();
        
        shapes[0] = new Shape(new int[][]{
            {1,1,1,1} // I Shape
        }, this, colors[0], 0);
        
        shapes[1] = new Shape(new int[][]{
            {1,1,1},
            {0,1,0} // T Shape
        }, this, colors[1],1);
        
        shapes[2] = new Shape(new int[][]{
            {1,1,1},
            {1,0,0} // L Shape
        }, this, colors[2],2);
        
        shapes[3] = new Shape(new int[][]{
            {1,1,1},
            {0,0,1} // J Shape
        }, this, colors[3],3);
        
        shapes[4] = new Shape(new int[][]{
            {0,1,1},
            {1,1,0} // S Shape
        }, this, colors[4],4);
        
        shapes[5] = new Shape(new int[][]{
            {1,1,0},
            {0,1,1} // Z Shape
        }, this, colors[5],5);
        
        shapes[6] = new Shape(new int[][]{
            {1,1},
            {1,1,}
        }, this, colors[0],6);
        
        shapeList = new LinkedList<Integer>();
        newRowList = new LinkedList<newRow>();
        /*
        int[] order = {0,1,2,3,4,5,6};
        for(int i=0;i<7;i++){
            int temp1 = random.nextInt(7);
            int temp2 = random.nextInt(7);
            
            int temp = order[temp1];
            order[temp1] = order[temp2];
            order[temp2] = temp;
        }
        
        for(int i=0;i<7;i++){
            shapeList.add(order[i]);
        }*/
        /*
        int temp = shapeList.removeFirst();
        currentShape = new Shape(shapes[temp].getCoords(), this, shapes[temp].getColor(), temp);
        blockPlaced++;
        */
        
        looper = new Timer(delay, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
                repaint();
                
                if(!newRowList.isEmpty()){
                    if(System.currentTimeMillis() > newRowList.getFirst().getTime()){
                        addNewRow(newRowList.removeFirst().getRow());
                    }
                }
                
                
            }
            
        });
        looper.start();
        
    }
    
    public void setClient(Client client){
        this.client = client;
    }
    
    public class newRow{
        private int row;
        private long time;
        
        public newRow(int row, long time){
            this.row = row;
            this.time = time;
        }
        
        public int getRow(){
            return row;
        }
        
        public long getTime(){
            return time;
        }
    }
    
    private void update(){
        if(state == STATE_GAME_PLAY){
            currentShape.update();
        } 
    }
    
    public void startGame(){
        while(shapeList.isEmpty()){
            System.out.println("waiting game start");
        }
        setCurrentShape();
        state = STATE_GAME_PLAY;
    }
    
    public void setCurrentShape(){
        /*
        if (blockPlaced == 1){
            int[] order = {0,1,2,3,4,5,6};
            for(int i=0;i<7;i++){
                int temp1 = random.nextInt(7);
                int temp2 = random.nextInt(7);

                int temp = order[temp1];
                order[temp1] = order[temp2];
                order[temp2] = temp;
            }

            for(int i=0;i<7;i++){
                shapeList.add(order[i]);
            }
            blockPlaced=0;
            //SignalPacket nextBlockSignal = new SignalPacket("next block", client.getIPAdress().toString());
            //client.sendObject(nextBlockSignal);
        }
        */
        Thread nextBlockThread = new Thread(()->client.writeMessages("Next Block"));
        nextBlockThread.start();
        
        int temp = shapeList.removeFirst();
        currentShape = new Shape(shapes[temp].getCoords(), this, shapes[temp].getColor(), temp);
        blockPlaced++;
        currentShape.reset();
        isChanged = false;
        checkOverGame();
        
        
    }
    
    private void checkOverGame(){
        int[][] coords = currentShape.getCoords();
        for (int row=0; row < coords.length; row++){
            for (int col=0; col < coords[0].length; col++){
                if(coords[row][col]!=0){
                    if(board[row+currentShape.getY()][col+currentShape.getX()]!=null){
                        state = STATE_GAME_OVER;
                        Thread gameOver = new Thread(()->client.writeMessages("Game Over"));
                        gameOver.start();
                    }
                }
            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);  
        // background
        g.setColor(Color.black);
        g.fillRect(0,0,getWidth(),getHeight());
        
        currentShape.render(g);
        
        for(int row=0; row<board.length; row++){
            for(int col=0; col<board[row].length; col++){
                if(board[row][col]!=null){
                    g.setColor(board[row][col]);
                    g.fillRect(col * BLOCK_SIZE, row*BLOCK_SIZE ,BLOCK_SIZE,BLOCK_SIZE);
                }
            }
        }
        
        // draw the board
        g.setColor(Color.lightGray);
        for(int row=0; row<BOARD_HEIGHT+1; row++)
            g.drawLine(0, BLOCK_SIZE*row, BLOCK_SIZE*BOARD_WIDTH, BLOCK_SIZE*row);
        
        for(int col=0; col<BOARD_WIDTH+1; col++)
            g.drawLine(BLOCK_SIZE*col, 0, BLOCK_SIZE*col, BLOCK_SIZE*BOARD_HEIGHT);
        
        if(state == STATE_GAME_OVER){
            g.setColor(Color.white);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 40)); 
            g.drawString("GAME OVER", 25, 300);
        }
             
        if(state == STATE_GAME_PAUSE){
            g.setColor(Color.white);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
            g.drawString("GAME PAUSE", 25, 300);
        }
        
        if(state == STATE_GAME_WIN){
            g.setColor(Color.white);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
            g.drawString("YOU WIN", 25, 300);
        }
        
        // draw the hold item
        g.setColor(Color.white);
        g.drawLine(BLOCK_SIZE*BOARD_WIDTH + 50, BLOCK_SIZE*0 , BLOCK_SIZE*BOARD_WIDTH +50 + BLOCK_SIZE *6, BLOCK_SIZE*0);
        g.drawLine(BLOCK_SIZE*BOARD_WIDTH + 50, BLOCK_SIZE*1 , BLOCK_SIZE*BOARD_WIDTH +50 + BLOCK_SIZE *6, BLOCK_SIZE*1);
        g.drawLine(BLOCK_SIZE*BOARD_WIDTH + 50, BLOCK_SIZE*5 , BLOCK_SIZE*BOARD_WIDTH +50 + BLOCK_SIZE *6, BLOCK_SIZE*5);
        g.drawLine(BLOCK_SIZE*BOARD_WIDTH + 50, BLOCK_SIZE*0 , BLOCK_SIZE*BOARD_WIDTH + 50, BLOCK_SIZE*5);
        g.drawLine(BLOCK_SIZE*BOARD_WIDTH + 50 + BLOCK_SIZE *6, BLOCK_SIZE*0 , BLOCK_SIZE*BOARD_WIDTH +50 + BLOCK_SIZE *6, BLOCK_SIZE*5);
        
        g.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        g.drawString("HOLD", BLOCK_SIZE*BOARD_WIDTH + 60, BLOCK_SIZE*0+22);
        
        if (holdShape >= 0){
            for(int row=0; row<shapes[holdShape].getCoords().length; row++){
                for(int col=0; col<shapes[holdShape].getCoords()[row].length; col++){
                    if(shapes[holdShape].getCoords()[row][col]!=0){
                        g.setColor(shapes[holdShape].getColor());
                        g.fillRect(col * BLOCK_SIZE + BLOCK_SIZE*BOARD_WIDTH + 50 + BLOCK_SIZE, row*BLOCK_SIZE + BLOCK_SIZE*2 ,BLOCK_SIZE,BLOCK_SIZE);

                    }
                }
            }
        }
        
        // draw next item
        g.setColor(Color.white);
        g.drawLine(BLOCK_SIZE*BOARD_WIDTH + 50, BLOCK_SIZE*6 , BLOCK_SIZE*BOARD_WIDTH +50 + BLOCK_SIZE *6, BLOCK_SIZE*6);
        g.drawLine(BLOCK_SIZE*BOARD_WIDTH + 50, BLOCK_SIZE*7 , BLOCK_SIZE*BOARD_WIDTH +50 + BLOCK_SIZE *6, BLOCK_SIZE*7);
        g.drawLine(BLOCK_SIZE*BOARD_WIDTH + 50, BLOCK_SIZE*20 , BLOCK_SIZE*BOARD_WIDTH +50 + BLOCK_SIZE *6, BLOCK_SIZE*20);
        g.drawLine(BLOCK_SIZE*BOARD_WIDTH + 50, BLOCK_SIZE*6 , BLOCK_SIZE*BOARD_WIDTH + 50, BLOCK_SIZE*20);
        g.drawLine(BLOCK_SIZE*BOARD_WIDTH + 50 + BLOCK_SIZE *6, BLOCK_SIZE*6 , BLOCK_SIZE*BOARD_WIDTH +50 + BLOCK_SIZE *6, BLOCK_SIZE*20);
        
        g.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        g.drawString("NEXT", BLOCK_SIZE*BOARD_WIDTH + 60, BLOCK_SIZE*6+22);
        
        for(int i=0;i<4;i++){
            int temp = shapeList.get(i);
            for(int row=0; row<shapes[temp].getCoords().length; row++){
                for(int col=0; col<shapes[temp].getCoords()[row].length; col++){
                    if(shapes[temp].getCoords()[row][col]!=0){
                        g.setColor(shapes[temp].getColor());
                        g.fillRect(col * BLOCK_SIZE + BLOCK_SIZE*BOARD_WIDTH + 50 + BLOCK_SIZE, row*BLOCK_SIZE + BLOCK_SIZE*(8+i*3) ,BLOCK_SIZE,BLOCK_SIZE);

                    }
                }
            }
        }
        
        // draw onwaiting new row
        if(!newRowList.isEmpty()){
            int count = 0;
            g.setColor(Color.red);
            for(newRow temp: newRowList){
                count += temp.getRow();
            }
            g.fillRect(BLOCK_SIZE*BOARD_WIDTH, BLOCK_SIZE*(BOARD_HEIGHT - count), BLOCK_SIZE/2, BLOCK_SIZE*count);
        }
        /*
        Thread thread = new Thread(()->boardHandler.render(g));
        thread.start();*/
    }
    
    
    public Color[][] getBoard(){
        return board;
    }
    
    public void holdShape(){
        if (holdShape < 0){
            holdShape = currentShape.getNumber();
            setCurrentShape();
        } else{
            int temp = currentShape.getNumber();
            currentShape = new Shape(shapes[holdShape].getCoords(), this, shapes[holdShape].getColor(), holdShape);
            holdShape = temp;
            currentShape.reset();
        }
    }
    
    public void addNewRowQueue(int row){
        newRow temp = new newRow(row, System.currentTimeMillis() + 2000);
        newRowList.add(temp);
    }
    
    private void addNewRow(int add){
        for(int row=0; row<board.length - add; row++){
            for(int col=0; col<board[0].length;col++){
                board[row][col] = board[row+add][col];
            }
        }
        int emptyCol = random.nextInt(board[0].length);
        for(int row = board.length-1; row>board.length-add-1; row--){
            for(int col=0; col<board[0].length;col++){
                if(col != emptyCol){
                    board[row][col] = Color.GRAY;
                }else{
                    board[row][col] = null;
                }
            }
        }
    }
    
    public void addShapeList(LinkedList<Integer> shapeList){
        
        this.shapeList.addAll(shapeList);
    }
    
    public void clearRow(int row){
        while(!newRowList.isEmpty()){
            if(newRowList.getFirst().getRow()<=row){
                row -= newRowList.removeFirst().getRow();
            }else{
                 newRowList.getFirst().row -= row;
                 row = 0;
                 break;
            }
        }
        if(row >0){
            Attack attackRow = new Attack(row);
            Thread attack = new Thread(()->client.writeMessages(attackRow));
            attack.start();
            System.out.println("attack " + row + " rows");
        }
    }
    
    public void sendBoard(){
        BoardPacket boardPacket = new BoardPacket(board, currentShape.getCoords(), currentShape.getColor(), currentShape.getX(), currentShape.getY());
        Thread send = new Thread(()->client.writeMessages(boardPacket));
        send.start();
    }
    
    public void setWinGame(){
        state = STATE_GAME_WIN;
    }
    
    public void setBoardHandler(BoardHandler boardHandler){
        this.boardHandler = boardHandler;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_DOWN){
            currentShape.speedUp();
        }else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
            currentShape.moveRight();
        }else if(e.getKeyCode()==KeyEvent.VK_LEFT){
            currentShape.moveLeft();
        }else if(e.getKeyCode()==KeyEvent.VK_UP){
            currentShape.rotateShape();
        }else if(e.getKeyCode()==KeyEvent.VK_SPACE){
            currentShape.placeShape();
        }
        
        // clean the board
        /*
        if(state == STATE_GAME_OVER){
            if(e.getKeyCode()==KeyEvent.VK_SPACE){
                for(int row=0; row<board.length; row++){
                    for(int col=0; col<board[row].length; col++){
                        board[row][col] = null;
                    }
                }
                setCurrentShape();
                state = STATE_GAME_PLAY;
            }
        }*/
        
        
        /*
        if(e.getKeyCode()==KeyEvent.VK_P){
            if(state == STATE_GAME_PLAY){
                state = STATE_GAME_PAUSE;
            }else if (state == STATE_GAME_PAUSE){
                state = STATE_GAME_PLAY;
            }
        }*/
        
        if(isChanged == false){
            if(e.getKeyCode()==KeyEvent.VK_C){
                holdShape();
                isChanged = true;
            }
        }
        /*
        if(e.getKeyCode()==KeyEvent.VK_1){
            addNewRowQueue(1);
        }else if(e.getKeyCode()==KeyEvent.VK_2){
            addNewRowQueue(2);
        }else if(e.getKeyCode()==KeyEvent.VK_3){
            addNewRowQueue(3);
        }*/
        
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_DOWN){
            currentShape.speedDown();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
    }
    
}
