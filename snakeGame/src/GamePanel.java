import java.awt.*;
import java.awt.event.*;
import java.security.KeyException;
import javax.swing.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener{
    
    /* not changeable values  */
    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGTH = 800;
    static final int UNIT_SIZE = 25; //squares (items size in screen)
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGTH)/UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    
    int bodyParts = 6;
    int applesEaten = 0;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    boolean win = false; /* variable added */
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_HEIGTH,SCREEN_WIDTH));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();     
    }

    public void startGame(){
        
        running = true;
        win = false;
        newApple();
        timer = new Timer(DELAY,this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){ 
        
        if(running){    
            //draw a grid in the panel 
            for(int i=0; i<SCREEN_HEIGTH/UNIT_SIZE; i++){
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGTH);
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            }
            //draw an apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            
            //drawing the body and head
            for(int i=0; i<bodyParts; i++){
                if(i == 0){ 
                    g.setColor(Color.yellow); //head
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else{
                    g.setColor(Color.green); //body
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.white);
            g.setFont(new Font("Calibri",Font.ITALIC,30));
            FontMetrics metrics1 = getFontMetrics(g.getFont());
            g.drawString("SCORE: "+applesEaten, (metrics1.stringWidth("SCORE: "+applesEaten))/2, g.getFont().getSize());
        }
        else if(win && !running){ /* elseif added */
            g.setColor(Color.white);
            g.setFont(new Font("Calibri",Font.BOLD,75));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString("YOU ARE THE BEST", (SCREEN_WIDTH - metrics2.stringWidth("YOU ARE THE BEST"))/2, SCREEN_HEIGTH/2);
        }
        else
            gameOver(g);
    }

    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGTH/UNIT_SIZE))*UNIT_SIZE;
    }

    public void move(){
        for(int i=bodyParts; i>0; i--){
            x[i] = x[i-1]; //shifting coordinates (snake bodyParts)
            y[i] = y[i-1]; //shifting coordinates
        }
        //change the direction of where snake is headed
        switch(direction){
            case 'U': y[0] = y[0] - UNIT_SIZE;
            break;
            case 'D': y[0] = y[0] + UNIT_SIZE;
            break;
            case 'R': x[0] = x[0] + UNIT_SIZE;
            break;
            case 'L': x[0] = x[0] - UNIT_SIZE;
            break;
        }
    }

    public void checkApple(){
        if((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            
            /* limits the total applesEaten for the player */
            if(applesEaten >= 100){ /* if-else added */
                win = true;
                running = false;
            }
            else{
                newApple();
            }
        }
    }

    public void checkCollisions(){
        for(int i=bodyParts; i>0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }
        
        //in case head touches left border
        if(x[0]<0){
            running = false;
        }

        //in case head touches right border
        if(x[0]>SCREEN_WIDTH){
            running = false;
        }

        //in case head touches top border
        if(y[0]<0){
            running = false;
        }
        //in case head touches bottom border
        if(y[0]>SCREEN_HEIGTH){
            running = false;
        }
        if(!running){
            timer.stop();
        }
    }

    public void gameOver(Graphics g){
        //score
        g.setColor(Color.white);
        g.setFont(new Font("Calibri",Font.BOLD,50));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("FINAL SCORE: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("FINAL SCORE: "+applesEaten))/2, g.getFont().getSize());
        //final score
        g.setColor(Color.white);
        g.setFont(new Font("Calibri",Font.BOLD,75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (SCREEN_WIDTH - metrics2.stringWidth("GAME OVER"))/2, SCREEN_HEIGTH/2);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    //inner class
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_A:
                    if(direction != 'R'){
                        direction = 'L';
                    } 
                break;

                case KeyEvent.VK_D:
                    if(direction != 'L'){
                        direction = 'R';
                    } 
                break;

                case KeyEvent.VK_W:
                    if(direction != 'D'){
                        direction = 'U';
                    } 
                break;
                    
                case KeyEvent.VK_S:
                    if(direction != 'U'){
                        direction = 'D';
                    } 
                break;

                case KeyEvent.VK_P:
                    timer.stop();
                break;

                case KeyEvent.VK_C:
                    timer.start();
                break;
            }
        }
    }
}
