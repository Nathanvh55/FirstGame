
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class Main extends JComponent implements KeyListener {

    // Height and Width of our game
    static final int WIDTH = 800;
    static final int HEIGHT = 400;

    // sets the framerate and delay for our game
    // you just need to select an approproate framerate
    long desiredFPS = 60;
    long desiredTime = (1000) / desiredFPS;
    //player import
    BufferedImage fish = ImageHelper.loadImage("pics//Fish.png");
    //background import
    BufferedImage back = ImageHelper.loadImage("pics//background.png");
    //garbage import
    BufferedImage[] garbagepic = new BufferedImage[4];
    //bubbles import
    BufferedImage bubbles = ImageHelper.loadImage("pics//bubbles.png");
    //score variable
    int score = 0;
    int highscore = 0;

    //player variables
    int playerx = 340;
    int playery = 180;
    int playerheight = 40;
    int playerwidth = 70;

    Rectangle player = new Rectangle(340, 180, 70, 40);

    //current resistance/gravity
    int current = 1;
    int swimspeed = 6;
    //player dy dx and speed
    int dx = 0;
    int dy = 1;

    int verticalspeed = 3;

    //swim boolean
    boolean swim = false;
    int[] gy = new int[4];
    int i = 0;

    //garbage
    Rectangle garbage = new Rectangle(800, gy[0], 30, 100);
    Rectangle garbage1 = new Rectangle(1050, gy[1], 30, 100);
    Rectangle garbage2 = new Rectangle(1250, gy[2], 30, 100);
    Rectangle garbage3 = new Rectangle(1500, gy[3], 30, 100);
    //garabge picture variables
    int g0 = 1;
    int g1 = 2;
    int g2 = 3;
    int g3 = 2;

    //boolean
    boolean run = true;
    boolean start = true;
    //bubbles
    boolean bubble = false;

    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)
    @Override
    public void paintComponent(Graphics g) {
        // always clear the screen first!
        g.clearRect(0, 0, WIDTH, HEIGHT);

        // GAME DRAWING GOES HERE 
        //background
        g.drawImage(back, 0, 0, WIDTH, HEIGHT, this);
        
        //player

        g.drawImage(fish, player.x, player.y, player.width + 5, player.height + 5, this);
        //garbage
        g.setColor(Color.black);
        g.drawImage(garbagepic[g0], garbage.x, garbage.y, garbage.width, garbage.height, this);
        g.drawImage(garbagepic[g1], garbage1.x, garbage1.y, garbage1.width, garbage1.height, this);
        g.drawImage(garbagepic[g2], garbage2.x, garbage2.y, garbage2.width, garbage2.height, this);
        g.drawImage(garbagepic[g3], garbage3.x, garbage3.y, garbage3.width, garbage3.height, this);
        //bubbles
        if (bubble) {
            g.drawImage(bubbles, player.x - 30, player.y + 15, 20, 20, this);
        }
        //score
        g.setColor(Color.red);
        g.drawString("Score:", 10, 20);
        g.drawString("" + score, 50, 20);
        //highscore
        g.drawString("High Score:", 10, 40);
        g.drawString("" + highscore, 90, 40);

        //start screen
        //if(start){
        //g.fillRect(0, 0, WIDTH, HEIGHT);
        //}
        // GAME DRAWING ENDS HERE
    }

    // The main game loop
    // In here is where all the logic for my game will go
    public void run() {
        // Used to keep track of time used to draw and update the game
        // This is used to limit the framerate later on
        long startTime;
        long deltaTime;

        // the main game loop section
        // game will end if you set done = false;
        boolean done = false;
        while (!done) {
            // determines when we started so we can keep a framerate
            startTime = System.currentTimeMillis();

            // all your game rules and move is done in here
            // GAME LOGIC STARTS HERE 
           
            //for start menu
            while (start) {
                start();
            }

            basicPhysics();
            basicCollision();
            garbage();
            handleCollision(player, garbage);
            handleCollision(player, garbage1);
            handleCollision(player, garbage2);
            handleCollision(player, garbage3);
            //highscore 
            if (score >= highscore) {
                highscore = score;
            }
            //if hit garbage reset game
            if (run == false) {
                reset();
            }

            // GAME LOGIC ENDS HERE 
            // update the drawing (calls paintComponent)
            repaint();

            // SLOWS DOWN THE GAME BASED ON THE FRAMERATE ABOVE
            // USING SOME SIMPLE MATH
            deltaTime = System.currentTimeMillis() - startTime;
            if (deltaTime > desiredTime) {
                //took too much time, don't wait
            } else {
                try {
                    Thread.sleep(desiredTime - deltaTime);
                } catch (Exception e) {
                };
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // creates a windows to show my game
        JFrame frame = new JFrame("Fish Frenzie");

        // creates an instance of my game
        Main game = new Main();
        // sets the size of my game
        game.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // adds the game to the window
        frame.add(game);

        // sets some options and size of the window automatically
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // shows the window to the user
        frame.setVisible(true);
        frame.addKeyListener(game);
        // starts my game loop
        game.run();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //space pressed
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_SPACE) {
            swim = true;

            bubble = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //space released
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_SPACE) {
            swim = false;

            bubble = false;
        }
    }

    public void basicPhysics() {
        //player up down movement
        player.y += verticalspeed * dy;
        //applying current
        dx -= current;
        //applying swim 
        player.x += dx;
        //if space is pressed
        if (swim) {
            dx = swimspeed;

        }

    }

    public void start() {
        //initilize garbage y array
        for (i = 0; i < 4; i++) {
            gy[i] = 0;
        }
        //initilize garbage array
        garbagepic[1] = ImageHelper.loadImage("pics//garbage1.png");
        garbagepic[2] = ImageHelper.loadImage("pics//garbage2.png");
        garbagepic[3] = ImageHelper.loadImage("pics//garbage3.png");
        //if space is pressed start game
        if (swim) {

            start = false;
        }
    }

    public void basicCollision() {
        //bottom collision
        if (player.y >= HEIGHT - player.height) {
            dy *= -1;

        }
        //top collision
        if (player.y <= 0) {
            dy *= -1;
        }

        //left collision
        if (player.x <= 0) {
            player.x = 0;
        }
        
        //right collision 
        if(player.x+player.width>=WIDTH){
        player.x = WIDTH-player.width;   
        }

    }

    public static int randInt(int min, int max) {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public void garbage() {
        //move garbage to the left
        garbage.x -= 2;
        garbage1.x -= 2;
        garbage2.x -= 2;
        garbage3.x -= 2;
        //if the garbage goes off the screen reset it
        if (garbage.x <= -30) {
            //reset x
            garbage.x = WIDTH + 30;
            //randomize y
            garbage.y = randInt(0, HEIGHT - garbage.height);
            //add 1 to score
            score++;
            //set random picture
            g0 = randInt(1, 3);

        }
        if (garbage1.x <= -30) {
            garbage1.x = WIDTH + 30;
            garbage1.y = randInt(0, HEIGHT - garbage.height);
            score++;
            g1 = randInt(1, 3);
        }
        if (garbage2.x <= -30) {
            garbage2.x = WIDTH + 30;
            garbage2.y = randInt(0, HEIGHT - garbage.height);
            score++;
            g2 = randInt(1, 3);
        }
        if (garbage3.x <= -30) {
            garbage3.x = WIDTH + 30;
            garbage3.y = randInt(0, HEIGHT - garbage.height);
            score++;
            g3 = randInt(1, 3);
        }

    }

    public void handleCollision(Rectangle player, Rectangle block) {
        //for collisions with garbage
        if (player.intersects(block)) {
            //stop the game
            run = false;
        }

        //handle collision
    }

    public void reset() {

        //reset all variables
        garbage.x = 800;
        garbage1.x = 1050;
        garbage2.x = 1250;
        garbage3.x = 1500;
        player.y = 180;
        player.x = 340;

        score = 0;
        run = true;
        start = true;

    }

}
