import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/*
Esta clase organiza y dibuja el main Frame, aparte de organizar los botones.
También escucha las interacciones del usuario
*/



public class MainView implements Runnable {

    private static String TITLE = "The Game Of Life";   //Titulo
    private static String GENERATION_PREFIX = "Generation: ";
    private static String PLAYBUTTON_ICON = "\u25B6";   //Icono de play
    private static String STOPBUTTON_ICON = "\u25FC";   //Icono de stop
    private static String RELOADBUTTON_ICON = "\u21BA"; //Icono de recarga
    private static String CLEARBUTTON_ICON = "\u232B";  //Icono para limpiar

    private JFrame mainFrame;
    private JLabel generation;
    private JButton playButton, stopButton, clearButton, reloadButton;
    private JToggleButton drawMode;
    private JCheckBox gridCheck;
    private Container frameContent, southPanel, eastPanel,
            centerPanel, mediaPanel, statusBar;
    private LevelView levelView = new LevelView(new Field());
    private boolean stillRunning = false;


    public MainView() {
        mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setTitle(TITLE);


        //Containers
        frameContent = mainFrame.getContentPane();
        frameContent.setLayout(new BoxLayout(frameContent, BoxLayout.Y_AXIS));
        levelView.setPreferredSize(levelView.getSize());
        southPanel = new Container();
        eastPanel = new Container();
        centerPanel = new Container();
        mediaPanel = new Container();
        statusBar = new Container();


        //Labels, Buttons, etc.
        generation = new JLabel(GENERATION_PREFIX);
        playButton = new JButton(PLAYBUTTON_ICON);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playLevel();
            }
        });

        stopButton = new JButton(STOPBUTTON_ICON);
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopLevel();
            }
        });

        clearButton = new JButton(CLEARBUTTON_ICON);
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearLevel();
            }
        });

        reloadButton = new JButton(RELOADBUTTON_ICON);
        reloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reloadLevel();
            }
        });

        gridCheck = new JCheckBox("Grid");
        gridCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                levelView.flipGrid();
            }
        });

        drawMode = new JToggleButton("Draw", true);
        drawMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                levelView.toggleDrawMode();
            }
        });


        //Building the frame
        eastPanel.setLayout(new GridLayout(8, 2));
        eastPanel.add(drawMode);

        //BoxLayout left to right: add the levelView, a border, and the eastPanel.
        //This is done to insure the fixed size of the elements.
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
        centerPanel.add(levelView);
        centerPanel.add(Box.createRigidArea(new Dimension(5,0)));
        centerPanel.add(eastPanel);



        southPanel.setLayout(new GridLayout(2, 1));

        //Jugar, parar, recargar, limpiar y cuadrículas. Todas las opciones en un panel
        mediaPanel.setLayout(new FlowLayout(FlowLayout.CENTER, -10, 0));
        mediaPanel.add(playButton);
        mediaPanel.add(stopButton);
        mediaPanel.add(reloadButton);
        mediaPanel.add(clearButton);
        mediaPanel.add(gridCheck);

        southPanel.add(mediaPanel);
        southPanel.add(generation);

        frameContent.add(centerPanel);
        frameContent.add(southPanel);


        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);

    }

    public void run() {
        while(true) {

            if(stillRunning) {
                generation.setText(GENERATION_PREFIX + levelView.getGenerationNum());
                levelView.nextGeneration();
            }

            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }

        }
    }

    //Action methods.
    public void playLevel() {
        stillRunning = true;
        if(drawMode.getModel().isSelected())    //Cambia el botón de dibujar entre on y off
            drawMode.doClick();                 //al estar el botón pulsado
        drawMode.getModel().setEnabled(false);  //No es posible dibujar mientras las casillas estén activas
    }                                           

    public void stopLevel() {                   //Lo mismo que arriba, pero al revés.
        stillRunning = false;
        if(!drawMode.getModel().isEnabled()) {
            drawMode.getModel().setEnabled(true);
            drawMode.doClick();
        }
    }

    public void reloadLevel() {
        levelView.reloadLevel();
        generation.setText(GENERATION_PREFIX + levelView.getGenerationNum());
    }

    public void clearLevel() {
        levelView.clearLevel();
        generation.setText(GENERATION_PREFIX + levelView.getGenerationNum());
    }
}