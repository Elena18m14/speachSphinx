package sphinx;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.decoder.adaptation.Stats;
import edu.cmu.sphinx.decoder.adaptation.Transform;
import edu.cmu.sphinx.result.WordResult;

import java.io.InputStream;

public class SpeechRecord {
    public static void main(String args[]) throws Exception {
        Configuration configuration = Config.addCconfig();

        StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(
                configuration);
        // TODO only need to be read once (use a function like resert())
        // TODO add test.wav to git
        String wave = "D://test.wav";
        //Simple speech recording
        simple(recognizer, wave);

        // Hard speech recording
        hard(recognizer, wave);

    }

    private static void simple(StreamSpeechRecognizer recognizer, String wave) throws Exception {
        InputStream stream = Audio.inputStreemFromPat(wave);
        recognizer.startRecognition(stream);
        String text = result(recognizer);
        recognizer.stopRecognition();
    }


    private static void hard(StreamSpeechRecognizer recognizer, String wave) throws Exception {
        InputStream stream = Audio.inputStreemFromPat(wave);

        recognizer = stats(recognizer, wave);
        // Decode again with updated transform
        recognizer.startRecognition(stream);
        String text = result(recognizer);
        recognizer.stopRecognition();
    }

    private static StreamSpeechRecognizer stats(StreamSpeechRecognizer recognizer, String wave) throws Exception {
        SpeechResult result;
        InputStream stream = Audio.inputStreemFromPat(wave);
        Stats stats = recognizer.createStats(1);
        recognizer.startRecognition(stream);
        while ((result = recognizer.getResult()) != null) {
            stats.collect(result);
        }
        recognizer.stopRecognition();

        // Transform represents the speech profile
        Transform transform = stats.createTransform();
        recognizer.setTransform(transform);
        return recognizer;
    }

    private static String result(StreamSpeechRecognizer recognizer) {
        SpeechResult result;
        String text = "";
        while ((result = recognizer.getResult()) != null) {
            System.out.format("Hypothesis: %s\n", result.getHypothesis());
            text += result.getHypothesis();
            System.out.println("List of recognized words and their times:");
            for (WordResult r : result.getWords()) {
                System.out.println(r);
            }

            System.out.println("Best 3 hypothesis:");
            for (String s : result.getNbest(3))
                System.out.println(s);
        }
        System.out.println(text);
        return text;
    }
}
