import java.util.Random;
import java.util.ArrayList;

/*
 Esta clase tiene todas las células como variables privadas 
 Define todos los funcionamientos de las células
 */

public class Field {

    private int fieldHeight;
    private int fieldWidth;
    private int generationNum;
    private Random rnd = new Random();
    private ArrayList<ArrayList<Cell>> currentStep, nextStep;

    public Field() {
        fieldWidth = 100;
        fieldHeight = (fieldWidth/16)*9;
        generationNum = 0;

        currentStep = new ArrayList<ArrayList<Cell>>(fieldHeight);

        for(int h = 0; h < fieldHeight; h++) {
            currentStep.add(h, new ArrayList<Cell>(fieldWidth));
        }

        populateGrid();
    }

    public Field(int inputHeight, int inputWidth) {
        fieldHeight = inputHeight;
        fieldWidth = inputWidth;
        generationNum = 0;

        currentStep = new ArrayList<ArrayList<Cell>>(fieldHeight);

        for(int h = 0; h < fieldHeight; h++) {
            currentStep.add(h, new ArrayList<Cell>(fieldWidth));
        }

        populateGrid();
    }

    public void setInitialGeneration() {
        clearGrid();

        int numOfAttempts = rnd.nextInt((fieldHeight*fieldWidth)/2);
        for(int i = 0; i < numOfAttempts; i++) {
            if(rnd.nextBoolean()) {
                Cell currentCell = getCellAt(rnd.nextInt(fieldHeight), rnd.nextInt(fieldWidth));
                if(!currentCell.isAlive()) {
                    currentCell.revive();
                }
            }
        }

    }

    //Actualiza las células para que sean conscientes de si sus vecinas viven o no
    public void nextGeneration() {
        generationNum++;

        for (int h = 0; h < currentStep.size(); h++) {
            for (int w = 0; w < currentStep.get(h).size(); w++) {
                getCellAt(h, w).advanceOnce();
            }
        }

    }


    /*
 		Hay que actualizar las células antes de actualizar el mapa
    */
    public void updateAllCells() {
        for (int h = 0; h < currentStep.size(); h++) {
            for (int w = 0; w < currentStep.get(h).size(); w++) {
                getCellAt(h, w).setNumOfNeighbors(countNeighbors(h, w));
            }
        }
    }

    public int getHeight() { return fieldHeight; }

    public int getWidth() { return fieldWidth; }

    public void clearGrid() {
        generationNum = 0;
        for (int h = 0; h < currentStep.size(); h++) {
            for (int w = 0; w < fieldWidth; w++) {
                getCellAt(h, w).kill();

            }
        }
    }

    public Cell getCellAt(int h, int w) {
        return currentStep.get(h).get(w);
    }

    public boolean getCellStateAt(int h, int w) { return getCellAt(h, w).isAlive(); }

    public int getGenerationNum() { return generationNum; }

    
    //"puebla" el estado actual con las células muertas
    private void populateGrid() {
        for (int h = 0; h < currentStep.size(); h++) {
            for (int w = 0; w < fieldWidth; w++) {
                currentStep.get(h).add(w, new Cell());
            }
        }
    }

    /*
     Cuenta el número de células rodeando a una célula
     */
    private int countNeighbors(int height, int width) {
        int neighborCount = 0;

        for(int i = -1; i < 2; i ++) {
            for(int j = -1; j < 2; j++) {
                if(i == 0 && j == 0) {

                }
                else {
                    if ((height + i >= 0 && height + i < fieldHeight) &&
                            (width + j >= 0 && width + j < fieldWidth)) {
                        if (getCellAt(height + i, width + j).isAlive()) {
                            neighborCount++;
                        }
                    }
                }
            }
        }
        return neighborCount;
    }

}
