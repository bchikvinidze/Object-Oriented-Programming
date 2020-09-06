package hw2;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JBrainTetris extends JTetris{

    private JCheckBox brainMode;
    private DefaultBrain brain;
    private int limitHeight;
    private int pieceCnt;
    Brain.Move move;

    /** Creates JBrainTetris class
     */
    public JBrainTetris(int pixels){
        super(pixels);
        brain = new DefaultBrain();
        pieceCnt = 0;
        limitHeight = board.getHeight() - 4;
    }

    /** get piece count */
    public int getPieceCnt(){
        return pieceCnt;
    }

    /** Helper function for brain's X coordinate change/rotation.
        Note that this function can be used to change Piece coordinates
        By external user if need be. I would not have declared this method public
        if I did not have a need for simulating JBrainTetris for test cases.
     */
    public void changePiece(){
        if(pieceCnt < super.count) {
            pieceCnt++;
            move = brain.bestMove(board, currentPiece, limitHeight, null);
        }
        if (move.x < currentX)
            currentX--;
        else if (move.x > currentX)
            currentX++;
        currentPiece = move.piece;
    }

    /** tick method that uses Brain to optimize playing
     */
    @Override
    public void tick(int verb) {
        if (!gameOn) return;
        if (currentPiece != null) {
            board.undo();	// remove the piece from its old position
        }
        if(brainMode.isSelected() && verb == DOWN){
            changePiece();
        }
        computeNewPosition(verb);
        tickHelper(verb);
    }

    /**
     Creates the panel of UI controls -- controls wired
     up to call methods on the JTetris.
     */
    @Override
    public JComponent createControlPanel() {
        JPanel panel = new JPanel();
        controlPanelHelper(panel);
        panel.add(new JLabel("Brain:"));
        brainMode = new JCheckBox("Brain active");
        panel.add(brainMode);
        return panel;
    }

    /**
     Creates a frame with a JTetris.
     */
    public static void main(String[] args) {
        // Set GUI Look And Feel Boilerplate.
        // Do this incantation at the start of main() to tell Swing
        // to use the GUI LookAndFeel of the native platform. It's ok
        // to ignore the exception.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        JTetris brainTetris = new JBrainTetris(16);
        JFrame frame = JTetris.createFrame(brainTetris);
        frame.setVisible(true);
    }
}
