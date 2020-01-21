import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class AmbidextriousDodge implements ActionListener, KeyListener {

    //JFrame/rendering
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    public JFrame frame = new JFrame("AmbidextriousDodge");
    public renderPanel panel;
    public static AmbidextriousDodge game;
    public int HEIGHT = dim.height, WIDTH = dim.width;
    public BufferedImage background, heart, blueheart, gameover, damage;
    //for the text
    public JPanel south = new JPanel();
    public JTextField text = new JTextField(16);

    //important numbers
    public int playerheight = 40, playerwidth = 40, playerMovementSpeed = 7;
    public Timer tim = new Timer(10,this);
    public int ticks = 0, score = 0;
    public boolean right1 = false, left1 = false, up1 = false, down1 = false, hit1 = false;
    public boolean right2 = false, left2 = false, up2 = false, down2 = false, hit2 = false;
    public boolean started = false, impossible = false, HighScorePrinted = false;
    public Random r = new Random();
    public int projectilewidthNHeight = 10, projectileSpeed = 3, spawnInterval = 0;
    public String[] highscores = new String[10];
    public int[] highScoreInts = new int[10];

    //Objects
    public Player thing1 = new Player(WIDTH/2-200,HEIGHT/2 - 150);
    public Player thing2 = new Player(WIDTH/2+200,HEIGHT/2 - 150);
    public LinkedList<Point> projectilestop = new LinkedList<>();
    public LinkedList<Point> projectilesLeft = new LinkedList<>();
    public LinkedList<Point> projectilesRight = new LinkedList<>();
    public LinkedList<Point> projectilesBottom = new LinkedList<>();
//    public Point blueBall = new Point(r.nextInt(WIDTH - 100), r.nextInt(HEIGHT - 100)), redBall = new Point(r.nextInt(WIDTH-100),r.nextInt(HEIGHT-100));


    public AmbidextriousDodge() throws Exception{

        readHighScore();

        south.setBounds(WIDTH/2 - 200,HEIGHT - 200,400,200);
        south.setOpaque(false);
        text.setFont(new Font("Verdana", Font.PLAIN,22));
        text.setBackground(Color.decode("#ebe9e7"));
        text.addKeyListener(this);
        try {
            background = ImageIO.read(getClass().getResource("background.jpg"));
            heart = ImageIO.read(getClass().getResource("heart.png"));
            blueheart = ImageIO.read(getClass().getResource("BlueHeart.png"));
            gameover = ImageIO.read(getClass().getResource("gameover.png"));
            damage = ImageIO.read(getClass().getResource("damage.png"));
        }catch (Exception a) {

        }

        panel = new renderPanel();


        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        panel.setBackground(Color.decode("#f4f4fa"));
        frame.addKeyListener(this);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        panel.setLayout(null);
//        south.add(text,BorderLayout.NORTH);
//        south.setOpaque(false);
        frame.add(panel);
//        south.revalidate();
//        frame.setLayout(null);

        panel.repaint();
        tim.start();

    }

    public static void main(String[] args) throws Exception{
        game = new AmbidextriousDodge();
    }

    public void readHighScore() throws Exception{
            Scanner s = new Scanner(new File("/Users/mitchellsturba/IdeaProjects/AmbidextrousDodge/src/highscore.txt"));
            for (int i = 0; i < 10; i++) {
                if (s.hasNextLine()) {
                    try {
                        highscores[i] = s.next();
                        highScoreInts[i] = s.nextInt();
                        s.nextLine();
                    }catch(Exception n) {
                    }
                }

            }
            s.close();
            System.out.println(highscores[0] + highScoreInts[0]);
            System.out.println(highscores[1] + highScoreInts[1]);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        ticks++;
        panel.repaint();

        if (thing2.dead && thing1.dead && !HighScorePrinted) {

            panel.add(south,BorderLayout.SOUTH);
            south.add(text,BorderLayout.SOUTH);
            frame.revalidate();

            //bubbles sorts the highscores
            for (int o = 0; o < highScoreInts.length - 1; o++) {
                for (int u = o + 1; u < highScoreInts.length; u++) {
                    if (highScoreInts[o] < highScoreInts[u]) {
                        String tmp = highscores[o];
                        highscores[o] = highscores[u];
                        highscores[u] = tmp;

                        int temp = highScoreInts[o];
                        highScoreInts[o] = highScoreInts[u];
                        highScoreInts[u] = temp;
                    }
                }
            }
//            for (int i = 0; i < highscores.length; i++) {
//                if (score > highScoreInts[i]) {
//                    highscores[i] = "You";
//                    highScoreInts[i] = score;
//                }
//            }
            HighScorePrinted = true;
        }

        if (hit1 || hit2 || thing1.dead || thing2.dead) {
            if (ticks % 6 == 0) {
                hit1 = false;
                hit2 = false;

                //is dead?
                if (thing1.health <= 0) {
                    thing1.location.x = -180;
                    thing1.dead = true;

                }
                if (thing2.health <= 0) {
                    thing2.location.x = -180;
                    thing2.dead = true;
                }
            }
        }

        if (ticks % 80 == 0) {
            if (projectileSpeed < 15) projectileSpeed += 1;
            if (spawnInterval > 8) spawnInterval -= 2;
        }

        if (impossible && ticks % 3 == 0) {
            projectileSpeed = 10;
            projectilestop.add(new Point(thing1.location.x,0));
            projectilestop.add(new Point(thing2.location.x,0));
        }

        //movement of players
        if (right1) thing1.location.x += playerMovementSpeed;
        if (left1) thing1.location.x -= playerMovementSpeed;
        if (up1) thing1.location.y -= playerMovementSpeed;
        if (down1) thing1.location.y += playerMovementSpeed;
        if (right2) thing2.location.x += playerMovementSpeed;
        if (left2) thing2.location.x -= playerMovementSpeed;
        if (up2) thing2.location.y -= playerMovementSpeed;
        if (down2) thing2.location.y += playerMovementSpeed;

        //move the projectiles
        if (ticks % 2 == 0) {
            for (Point x : projectilestop) {

                x.y += projectileSpeed;

                //check collision
                if (x.y < HEIGHT) {
                    if (x.x >= thing1.location.x && x.x + projectilewidthNHeight <= thing1.location.x + playerwidth && x.y >= thing1.location.y && x.y + projectilewidthNHeight <= thing1.location.y + playerheight) {
                        x.x += 1000;
                        ticks = 0;
                        hit1 = true;
                        thing1.health--;
                    }
                    if (x.x >= thing2.location.x && x.x + projectilewidthNHeight <= thing2.location.x + playerwidth && x.y >= thing2.location.y && x.y + projectilewidthNHeight <= thing2.location.y + playerheight) {
                        x.x += 1000;
                        ticks = 0;
                        hit2 = true;
                        thing2.health--;
                    }
                }
                if (x.y >= 1020 && x.y <= 1200) {
                    x.y+= 300;
                    score++;
                }
            }
            for (Point x : projectilesLeft){
                x.x += projectileSpeed;
                //check collision
                if (x.x < WIDTH) {
                    if (x.x >= thing1.location.x && x.x + projectilewidthNHeight <= thing1.location.x + playerwidth && x.y >= thing1.location.y && x.y + projectilewidthNHeight <= thing1.location.y + playerheight) {
                        x.x += 1000;
                        ticks = 0;
                        hit1 = true;
                        thing1.health--;
                    }
                    if (x.x >= thing2.location.x && x.x + projectilewidthNHeight <= thing2.location.x + playerwidth && x.y >= thing2.location.y && x.y + projectilewidthNHeight <= thing2.location.y + playerheight) {
                        x.x += 1000;
                        ticks = 0;
                        hit2 = true;
                        thing2.health--;

                    }
                }
                if (x.x >= 1200 && x.x <= 1400) {
                    x.x+= 300;
                    score++;
                }
            }
        }
//        for (Point x : projectilesBottom){
//            x.y -= projectileSpeed;
//            //check collision
//            if (x.y > 0) {
//                if (x.x >= thing1.location.x && x.x + projectilewidthNHeight <= thing1.location.x + playerwidth && x.y >= thing1.location.y && x.y + projectilewidthNHeight <= thing1.location.y + playerheight) {
//                    x.x += 1000;
//                    thing1.health--;
//                }
//                if (x.x >= thing2.location.x && x.x + projectilewidthNHeight <= thing2.location.x + playerwidth && x.y >= thing2.location.y && x.y + projectilewidthNHeight <= thing2.location.y + playerheight) {
//                    x.x += 1000;
//                    thing2.health--;
//                }
//            }
//        }

        //generate new projectile
        if (started && ticks % spawnInterval == 0) {
            projectilestop.add(new Point(r.nextInt(WIDTH - 20) + 20, 0));
            projectilesLeft.add(new Point(0, r.nextInt(HEIGHT - 20) + 20));
//            projectilesBottom.add(new Point(r.nextInt(WIDTH - 20) + 20, HEIGHT));
        }

        //no escape
        if (thing1.location.y <= 0 ) thing1.location.y = 0;
        if (thing1.location.y > frame.getHeight() - playerheight - 25) thing1.location.y = frame.getHeight() - playerheight - 25;
        if (thing2.location.y <= 0 ) thing2.location.y = 0;
        if (thing2.location.y > frame.getHeight() - playerheight - 25) thing2.location.y = frame.getHeight() - playerheight - 25;

        if (thing1.location.x <= 0) thing1.location.x = 0;
        if (thing1.location.x >= frame.getWidth() - playerwidth) thing1.location.x = frame.getWidth() - playerwidth;
        if (thing2.location.x <= 0 && !thing2.dead) thing2.location.x = 0;
        if (thing2.location.x >= frame.getWidth() - playerwidth) thing2.location.x = frame.getWidth() - playerwidth;

        if (thing1.dead) {
            if (ticks % 100 == 0 && spawnInterval > 2) spawnInterval -= 2;
            if (thing2.dead) spawnInterval = 9900000;
        }
//        if (!started && !thing1.dead){
//            if (ticks % 4 == 0) score++;
//        }
        if (thing2.dead) {
            if (ticks % 100 == 0 && spawnInterval > 2) spawnInterval -= 2;
        }
//        if (!thing2.dead && !started){
//            if (ticks% 4 == 0) score++;
//        }


        //check coillision with balls
//        if (redBall.x >= thing1.location.x && redBall.x <= thing1.location.x + playerwidth && redBall.y >= thing1.location.y && redBall.y + 10 <= thing1.location.y + playerheight) {
//            redBall.x = r.nextInt(WIDTH - 100);
//            redBall.y = r.nextInt(HEIGHT - 100);
//            score += 10;
//        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        int i = e.getKeyCode();
        if (!thing1.dead) {
            if (i == KeyEvent.VK_D) {
                right1 = true;
            }
            if (i == KeyEvent.VK_A) {
                left1 = true;
            }
            if (i == KeyEvent.VK_W) {
                up1 = true;
            }
            if (i == KeyEvent.VK_S) {
                down1 = true;
            }
        }
        if (!thing2.dead) {
            if (i == KeyEvent.VK_RIGHT) {
                right2 = true;
            }
            if (i == KeyEvent.VK_LEFT) {
                left2 = true;
            }
            if (i == KeyEvent.VK_UP) {
                up2 = true;
            }
            if (i == KeyEvent.VK_DOWN) {
                down2 = true;
            }
        }
        if (i == KeyEvent.VK_9) {
            impossible = true;
        }

        if (i == KeyEvent.VK_MINUS && spawnInterval != 5) spawnInterval -= 5;
        if (i == KeyEvent.VK_EQUALS) spawnInterval += 5;
        if (i == KeyEvent.VK_0) spawnInterval = 2;
        if (i == KeyEvent.VK_SPACE) {
            spawnInterval = 40;
            started = !started;
        }
        if (i == KeyEvent.VK_R) {
            game = null;
            frame.dispose();
            try {
                game = new AmbidextriousDodge();
            }catch (Exception mm) {
                System.out.println(mm);
            }
        }
        if (i == KeyEvent.VK_Z) {
            thing2.health = 0;
            thing1.health = 0;
            thing1.dead =true;
            thing2.dead = true;
        }

        if (i == KeyEvent.VK_ENTER) {
            String playername = text.getText();
            highscores[3] = playername;
            highScoreInts[3] = score;
            HighScorePrinted = false;
            text.setText("");
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int i = e.getKeyCode();

        if (i == KeyEvent.VK_D) {
            right1 = false;
        }
        if (i == KeyEvent.VK_A) {
            left1 = false;
        }
        if (i == KeyEvent.VK_W) {
            up1 = false;
        }
        if (i == KeyEvent.VK_S) {
            down1 = false;
        }
        if (i == KeyEvent.VK_RIGHT) {
            right2 = false;
        }
        if (i == KeyEvent.VK_LEFT) {
            left2 = false;
        }
        if (i == KeyEvent.VK_UP) {
            up2 = false;
        }
        if (i == KeyEvent.VK_DOWN) {
            down2 = false;
        }
    }
}
