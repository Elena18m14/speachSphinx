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

        InputStream stream = Audio.inputStreemFromPat("D://test.wav");

        //Simple speech recording
        simple(recognizer, stream);

        //stats (recognizer ,stream);

        // Hard speech recording
        hard(recognizer, stream);

    }

    private static void simple(StreamSpeechRecognizer recognizer, InputStream stream) {
        SpeechResult result;
        recognizer.startRecognition(stream);
        String text = "";
        while ((result = recognizer.getResult()) != null) {
            //not optimized
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
        recognizer.stopRecognition();
    }

    public static void stats(StreamSpeechRecognizer recognizer, InputStream stream) throws Exception {
        SpeechResult result;
        Stats stats = recognizer.createStats(1);
        recognizer.startRecognition(stream);
        while ((result = recognizer.getResult()) != null) {
            stats.collect(result);
        }
        recognizer.stopRecognition();

        Transform transform = stats.createTransform();
        recognizer.setTransform(transform);
    }

    private static void hard(StreamSpeechRecognizer recognizer, InputStream stream) {
        SpeechResult result;
        recognizer.startRecognition(stream);

        String a = "";
        while ((result = recognizer.getResult()) != null) {
            System.out.format("Hypothesis: %s\n", result.getHypothesis());
            a= a + result.getHypothesis() + "";
        }
        recognizer.stopRecognition();
        System.out.println(a);
    }
}
