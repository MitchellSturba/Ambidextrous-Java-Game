import javax.swing.*;
import java.awt.*;

public class renderPanel extends JPanel {


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        AmbidextriousDodge main = AmbidextriousDodge.game;

        try {
            g.drawImage(main.background,0,0,main.frame.getWidth(),main.frame.getHeight(),null);
            g.setColor(Color.BLACK);
//            g.drawString("Spawn Interval: " + main.spawnInterval + " ticks", 400, 40);
            g.setFont(new Font("Vedana",Font.PLAIN, 24));
            g.drawString("Score: " + main.score, 580, 40);
            g.setColor(Color.WHITE);
            g.drawString("Score: " + main.score, 581, 42);
            g.setColor(Color.BLACK);
            if (main.thing1.health > 0) {
                g.fillOval(main.thing1.location.x - 1,main.thing1.location.y - 1,main.playerwidth + 2,main.playerheight + 2);
            }
            g.fillOval(main.thing2.location.x - 1,main.thing2.location.y - 1,main.playerwidth + 2,main.playerheight + 2);

            for (Point x : main.projectilestop) {
                g.fillOval(x.x,x.y,main.projectilewidthNHeight,main.projectilewidthNHeight);
//                g.setColor(Color.white);
//                g.fillOval(x.x + 1,x.y + 1,main.projectilewidthNHeight,main.projectilewidthNHeight);
//                g.setColor(Color.black);
            }
            for (Point x : main.projectilesLeft) {
                g.fillOval(x.x,x.y,main.projectilewidthNHeight,main.projectilewidthNHeight);
            }
            g.setColor(Color.RED);
            for (int i = 0; i < main.thing1.health; i++) {
                g.drawImage(main.heart,20,20 + (i * 40),35,35,null);
            }
            if (main.thing1.health > 0) {
//                g.fillOval(main.redBall.x,main.redBall.y,10,10);
                g.fillOval(main.thing1.location.x,main.thing1.location.y,main.playerwidth,main.playerheight);
            }
            g.setColor(Color.BLUE);
            for (int i = 0; i < main.thing2.health; i++) {
                g.drawImage(main.blueheart,main.WIDTH - 40,20 + (i * 40),35,35,null);
            }
//            g.fillOval(main.blueBall.x,main.blueBall.y,10,10);
            g.fillOval(main.thing2.location.x,main.thing2.location.y,main.playerwidth,main.playerheight);
            if (main.thing1.health <= 0 && main.thing2.health <= 0) {
                g.setColor(Color.BLACK);
                g.setFont(new Font("Vedana",Font.BOLD, 24));
                g.drawString("HighScores",main.WIDTH/2 - 100,main.HEIGHT/2 - 250);
                g.setFont(new Font("Vedana",Font.PLAIN, 16));
                for (int i = 0; i < main.highscores.length; i++) {
                    if (!main.highscores[i].equals("null")) g.drawString(main.highscores[i] + "\t" + main.highScoreInts[i],main.WIDTH/2 - 100,main.HEIGHT/2 - 200 + i*50);
                }
            }
            if (main.hit1) {
                g.drawImage(main.damage,main.thing1.location.x - 20,main.thing1.location.y - 20, 75,80,null);
            }
            if (main.hit2) {
                g.drawImage(main.damage,main.thing2.location.x - 20,main.thing2.location.y - 20,75,80,null);
            }
        }catch (Exception q) {
            System.out.print(q.toString());
        }

    }
}
