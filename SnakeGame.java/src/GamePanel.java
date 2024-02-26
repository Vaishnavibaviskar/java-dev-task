
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH=600;
    static final int SCREEN_HEIGHT=600;
    static final int UNIT_SIZE=25;
    static final int GAME_UNITS=(SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY=75; //speed of game(snake)
    final int x[]=new int [GAME_UNITS];
    final int y[]=new int [GAME_UNITS];
    int bodyParts=6; //initial body size of snake
    int applesEaten;
    int appleX; //X-coordinate
    int appleY; //Y-coordinate
    char direction='R'; //start direction(R-right,L-left,U-up,D-down)
    boolean running=false;
    Timer timer;
    Random random;
    private JButton playAgainButton;
    GamePanel(){
        playAgainButton = new JButton("Play Again");
        playAgainButton.setFocusable(false);
        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                resetGame();
            }
        });
        setLayout(new BorderLayout());


        add(playAgainButton, BorderLayout.SOUTH);

        playAgainButton.setVisible(false);
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void resetGame(){
        running = true;
        direction = 'R';
        bodyParts = 6;
        applesEaten = 0;
        newApple();
        for (int i = 0; i < bodyParts; i++) {
            x[i] = 0;
            y[i] = 0;
        }
        playAgainButton.setVisible(false); // Hide the button again
        timer.restart(); // Restart the timer
    }


    public void startGame(){
        newApple();
        running=true;
        timer=new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {
        if (running) {
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            g.setColor(Color.RED); //apple colour
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); //apple colour fill

            for (int i = 0; i < bodyParts; i++) { //body of snake
                if (i == 0) {
                    g.setColor(Color.MAGENTA);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {

                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);

                }
            }
            g.setColor(Color.PINK); //score colour
            g.setFont(new Font("Ink Free",Font.BOLD,30));
            FontMetrics metrics=getFontMetrics(g.getFont()); //used to align text in centre of screen
            g.drawString("Score:"+applesEaten,(SCREEN_WIDTH-metrics.stringWidth("Score:"+applesEaten))/2,g.getFont().getSize());
        }
        else{
            gameOver(g);
        }
    }

    public void newApple(){
        appleX= random.nextInt((int)SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        appleY= random.nextInt((int)SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
    }
    public void move(){
        for(int i=bodyParts;i>0;i--){
            x[i]=x[i-1];  //shifting snake by one part
            y[i]=y[i-1];
        }
        switch(direction){
            case 'U':
                y[0]=y[0]-UNIT_SIZE;
                break;
            case 'D':
                y[0]=y[0]+UNIT_SIZE;
                break;
            case 'L':
                x[0]=x[0]-UNIT_SIZE;
                break;
            case 'R':
                x[0]=x[0]+UNIT_SIZE;
                break;

        }
    }
    public void checkFruit(){
        if((x[0]==appleX)&&(y[0]==appleY)){
            bodyParts++; //increase size
            applesEaten++; //increase score
            newApple(); //generate new apple after one is eaten
        }

    }
    public void checkCollisions(){
        // checks if head collides with body
        for(int i=bodyParts;i>0;i--){
            if((x[0]==x[i])&&(y[0]==y[i])){
                running=false;
            }
        }
        //check if head touches left border
        if(x[0]<0){
            running=false;
        }
        //check if head touches right border
        if(x[0]>SCREEN_WIDTH){
            running=false;
        }
        //check if head touches top border
        if(y[0]<0){
            running=false;
        }
        //check if head touches top border
        if(y[0]>SCREEN_HEIGHT){
            running=false;
        }
        if(!running){
            timer.stop();
        }


    }
    public void gameOver(Graphics g){
        //Score
        g.setColor(Color.PINK);
        g.setFont(new Font("Ink Free",Font.BOLD,30));
        FontMetrics metrics1=getFontMetrics(g.getFont()); //used to align text in centre of screen
        g.drawString("Score:"+applesEaten,(SCREEN_WIDTH-metrics1.stringWidth("Score:"+applesEaten))/2,g.getFont().getSize());

        // Game Over Text
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free",Font.BOLD,75));
        FontMetrics metrics=getFontMetrics(g.getFont()); //used to align text in centre of screen
        g.drawString("Game Over",(SCREEN_WIDTH-metrics.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2);

        playAgainButton.setVisible(true);

    }
    public void actionPerformed(ActionEvent e){
        if(running){
            move();
            checkFruit();
            checkCollisions();
        }
        repaint();

    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction!='R'){
                        direction='L'; //so that it won't run into itself
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction!='L'){
                        direction='R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction!='D'){
                        direction='U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction!='U'){
                        direction='D';
                    }
                    break;
            }
        }
    }
}