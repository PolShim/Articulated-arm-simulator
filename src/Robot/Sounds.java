package Robot;

import com.sun.source.doctree.ThrowsTree;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


/**
 *
 * Klasa odpowiedzialna za odtwarzanie dzwieku
 */
public class Sounds {


    public static void grajDzwiek(String fileName) {
        try {
            File dzwiek = new File (fileName);
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(dzwiek));
            clip.start();

        }
        catch (Exception e){

        }


    }

}

