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
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author User
 */
public class Title extends JPanel implements KeyListener{

    private static final long serialVersionUID = 1L;
    private BufferedImage instructions;
    private WindowGame window;
    private BufferedImage[] playButton = new BufferedImage[2];
    private Timer timer;
    
    public Title(WindowGame window){
        //instructions = ImageLoader.loadImage("");
        timer = new Timer(1000/60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        
        timer.start();
        this.window = window;
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        g.setColor(Color.black);
        
        g.fillRect(0, 0, WindowGame.WIDTH, WindowGame.HEIGHT);
        
        //g.drawImage(instructions, WindowGame.WIDTH/2 - instructions.getWidth()/2,
                //30 - instructions.getHeight()/2 + 150, null);
        
        g.setColor(Color.white);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
        g.drawString("Press Space to Play!", 50, WindowGame.HEIGHT/2+100);
    }
    
    
    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getKeyChar() == KeyEvent.VK_SPACE){
            window.startTetris();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
    
}
