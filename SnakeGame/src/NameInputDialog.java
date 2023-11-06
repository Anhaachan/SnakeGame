import javax.swing.*;

public class NameInputDialog {

    private static boolean dialogShown = false;

    public static String getPlayerName() {
        if (!dialogShown) {
            String playerName = JOptionPane.showInputDialog("Enter your name:");
            dialogShown = true;
            return playerName;
        } else {
            // Return an empty string if the dialog has already been shown
            return "";
        }
    }
}
