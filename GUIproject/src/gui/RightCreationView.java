package gui;

import gameComponent.Piece;
import model.RightCreation;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by minpingyang on 25/08/17.
 */
public class RightCreationView extends JComponent implements Observer{
    private RightCreation rightCreation;
    private int top = 10;
    private int left = 13;

    public RightCreationView(RightCreation rightCreation){
        this.rightCreation=rightCreation;
        this.rightCreation.addObserver(this);
        this.setFocusable(true);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.PINK);
        g.fillRect(0, 0, getWidth(), getHeight());
        rightCreation.setPieces(rightCreation.getYellowPlayer().getPieces());
        int i=0;
        int outline = 10;
        for(int row = 0; row<6;row++){
            for(int col=0;col<4;col++){
                rightCreation.getPieces().get(i++).drawPiece(g,left+col*(Piece.SIZE_PIECE+outline),top+row*(Piece.SIZE_PIECE+outline),row,col);
                rightCreation.getPiecesPoint().add(new Point(left+col*(Piece.SIZE_PIECE+outline), top+ row*(Piece.SIZE_PIECE+outline)));
            }
        }
    }


    @Override
    public Dimension getPreferredSize(){
        //width -> (5)*50
        //height-> (10+10)*50
        return new Dimension(3*(Piece.SIZE_PIECE),9*(Piece.SIZE_PIECE));
    }



    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }
}
