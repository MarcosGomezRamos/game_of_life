import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.*;

public class LevelView extends JPanel implements MouseMotionListener, MouseListener {

    private final int GRID_SCALING_FACTOR = 8; //Aquí se puede cambiar el tamaño de las células
    private int levelHeight;
    private int levelWidth;
    private Dimension levelSize;
    private Field currentLevel;
    private boolean gridGraphics = false;
    private boolean drawMode = true;
    private boolean mouseIsPressed = false;
    private Point lastChangedByUser;



    public LevelView(Field currentLevel) {

        this.currentLevel = currentLevel;
        levelHeight = this.currentLevel.getHeight();
        levelWidth = this.currentLevel.getWidth();
        setSize(levelWidth * GRID_SCALING_FACTOR,
                levelHeight * GRID_SCALING_FACTOR);
        levelSize = getSize();
        lastChangedByUser = new Point(getSize().width, getSize().height);

        addMouseMotionListener(this);
        addMouseListener(this);

        setBackground(Color.WHITE);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        //Posición inicial del "lápiz" del canvas
        int hPosition = 0;
        int wPosition = 0;

        for(int h = 0; h < levelHeight; h++) {
            for(int w = 0; w < levelWidth; w++) {
                if (currentLevel.getCellStateAt(h, w)) {
                    //Si la célula está viva, entonces la pintará de verde
                    g.setColor(currentLevel.getCellAt(h, w).getMyColor());
                }
                else {
                    g.setColor(Color.WHITE);   //Si está muerta, la pinta en blanco
                }

                //Pinta el cuadrado con el color dado.
                g.fillRect(wPosition,
                        hPosition,
                        wPosition + GRID_SCALING_FACTOR,
                        hPosition + GRID_SCALING_FACTOR);

                //Avanza una posición a la derecha del factor de escala.
                wPosition += GRID_SCALING_FACTOR;
            }
            //Avanza una posición hacia abajo del factor de escala
            hPosition += GRID_SCALING_FACTOR;
            wPosition = 0;  //Devuelve la posición del lápiz a la izquierda.
        }

        if(gridGraphics)    //Dibuja la cuadrícula
            drawGrid(g);

    }

    public void nextGeneration() {
        currentLevel.updateAllCells();
        currentLevel.nextGeneration();
        repaint();
    }

    /*
     Dar la vuelta al estado "cuadrícula"
     */
    public void flipGrid() {
        gridGraphics = !gridGraphics;
        repaint();  //En caso de que el usuario decida activar la cuadrícula cuando está en pausa
    }

    public void toggleDrawMode() { drawMode = !drawMode; }

    public void reloadLevel() {
        currentLevel.setInitialGeneration();
        repaint();
    }

    /*
     Matar a todas las células y repintar todo de blanco
     */
    public void clearLevel () {
        currentLevel.clearGrid();
        repaint();
    }

    
    public int getGenerationNum() { return currentLevel.getGenerationNum(); }

    public void drawCellAt(int y, int x) {
        if(currentLevel.getCellStateAt(y, x)) {
            currentLevel.getCellAt(y, x).kill();
        } else {
            currentLevel.getCellAt(y, x).revive();
        }
    }

    /*
     Dibuja líneas rectas de un punto del canvas al otro considerando el facto de escalada
     */
    private void drawGrid(Graphics g2) {
        g2.setColor(Color.GRAY);

        for(int w = GRID_SCALING_FACTOR; w < getSize().getWidth();
            w += GRID_SCALING_FACTOR) {
            g2.drawLine(w, 0, w, (int)getSize().getHeight()-1);
        }

        for(int h = GRID_SCALING_FACTOR; h < getSize().getHeight();
            h += GRID_SCALING_FACTOR) {

            g2.drawLine(0, h, (int)getSize().getWidth()-1, h);

        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseIsPressed = true;


        if(drawMode) {
            if((e.getX() >= 0 && e.getX() <= getSize().width-1) &&
                    (e.getY() >= 0 && e.getY() <= getSize().height-1)) {
                int x = e.getX()/GRID_SCALING_FACTOR;
                int y = e.getY()/GRID_SCALING_FACTOR;
                Point currentPoint = new Point(x, y);

                if(!currentPoint.equals(lastChangedByUser)) {
                    drawCellAt(y, x);
                    repaint();
                }

                lastChangedByUser = currentPoint;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseIsPressed = false;
        lastChangedByUser = this.getParent().getLocationOnScreen();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(drawMode) {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setCursor(Cursor.getDefaultCursor());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(drawMode) {
            mousePressed(e);
        }
    }


    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
