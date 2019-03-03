package sphinx;

import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.InputStream;

public class Audio {
    public static InputStream inputStreemFromPat(String path) throws Exception{
        File soundFile = new File(path);
        InputStream stream = AudioSystem.getAudioInputStream(soundFile);
        stream.skip(44);
        return stream;
    }
}
