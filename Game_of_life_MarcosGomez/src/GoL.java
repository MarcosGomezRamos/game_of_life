/* Clase main. Ejecuta o xogo e todas as demais clases */

public class GoL {

    public GoL() {

    }

    public void runGame() {
        MainView mainView = new MainView();
        mainView.run();
    }

    public static void main(String[] args) {
        GoL init = new GoL();
        init.runGame();
    }
}
