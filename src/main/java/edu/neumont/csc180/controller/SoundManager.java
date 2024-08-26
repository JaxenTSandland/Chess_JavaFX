/**
 * @author jsandland
 * @createdOn 8/26/2024 at 12:32 PM
 * @projectName JaxenS_ChessJavaFX
 * @packageName edu.neumont.csc180.controller;
 */
package edu.neumont.csc180.controller;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;


public class SoundManager {
    public enum Sounds {
        PIECE_MOVE,
        BUTTON_CLICK
    }

    public void playSound(Sounds sound) {
        String soundURL = null;

        switch (sound) {
            case PIECE_MOVE:
                soundURL = "src\\main\\resources\\edu\\neumont\\csc180\\sounds\\piece_move.mp3";
                break;
            case BUTTON_CLICK:
                soundURL = "src\\main\\resources\\edu\\neumont\\csc180\\sounds\\button_click.mp3";
                break;
        }

        if (soundURL != null) {
            Media media = new Media(new File(soundURL).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        }
    }
}
