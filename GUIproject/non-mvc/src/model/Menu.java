package model;

import gui.View;
import resources.ImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by minpingyang on 24/08/17.
 */
public class Menu extends JFrame implements MouseListener{
    private MenuCanvas canvas;
    private View view;
    public Menu(View view){
        super("Menu");
//        JPanel panel= new JPanel();
        this.addMouseListener(this);
        canvas = new MenuCanvas();
        canvas.addMouseListener(this);
        setLayout(new BorderLayout());
        this.view=view;
        add(canvas,BorderLayout.CENTER);
        setVisible(true);
        pack();


    }


    @Override
    public void mousePressed(MouseEvent e) {

        Point clickP = e.getPoint();
        if(e.getSource() instanceof MenuCanvas){
            if(canvas.getPlayBtn().contains(clickP)){
                view.getJFrame().setVisible(true);
                this.setVisible(false);

            }else if(canvas.getInfoBtn().contains(clickP)){

            }else if(canvas.getInfoBtn().contains(clickP)){
                System.exit(0);
            }
        }

    }
    @Override
    public void mouseClicked(MouseEvent e) {
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


    public class MenuCanvas extends JPanel{

        private Dimension dimension =getPreferredSize();
        private int width= (int)dimension.getWidth();
        private int height = (int) dimension.getHeight();
        private Rectangle playBtn = new Rectangle(width/3+40, height/4, 120,60);

        public Rectangle getPlayBtn() {
            return playBtn;
        }

        public Rectangle getInfoBtn() {
            return infoBtn;
        }

        public Rectangle getQuitBtn() {
            return quitBtn;
        }

        private Rectangle infoBtn= new Rectangle(width/3+40, height/4+80, 120,60);
        private Rectangle quitBtn= new Rectangle(width/3+40, height/4+160, 120,60);


        @Override
        public void paint(Graphics g){

            g.drawImage(ImageResource.ME1.img,0,0,width,height,null);
            g.setColor(Color.BLACK);
            Graphics2D g2d = (Graphics2D)g;
            g2d.fill(playBtn);
            g2d.fill(infoBtn);
            g2d.fill(quitBtn);
            g.setColor(Color.RED);
            Font font0 =new Font("arial",Font.BOLD,40);
            g.setFont(font0);
            g.drawString("Play",playBtn.x+20,playBtn.y+45);
            g.drawString("Info",infoBtn.x+20,infoBtn.y+45);
            g.drawString("Quit",quitBtn.x+20,quitBtn.y+45);
        }

        @Override
        public Dimension getPreferredSize(){
            return new Dimension(600, 600);
        }

    }

}

