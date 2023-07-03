/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tetris;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JFrame;

/**
 *
 * @author User
 */
public class WindowGame {
    
    public static final int WIDTH=650, HEIGHT=660;
    
    private Board board;
    private Title title;
    private JFrame window;
    
    public WindowGame(){
        window = new JFrame("Tetris");
        window.setSize(WIDTH, HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        
        board = new Board();
        title = new Title(this);
        
        window.addKeyListener(title);
        
        
        window.add(title);
        
        window.setVisible(true);
    }
    
    public void startTetris(){
        window.removeKeyListener(title);
        window.remove(title);
        window.addMouseMotionListener(board);
        window.addMouseListener(board);
        
        window.addKeyListener(board);
        window.add(board);
        board.startGame();
        window.revalidate();
    }
    
    public static void main(String[] args){
        new WindowGame();
    }
}
