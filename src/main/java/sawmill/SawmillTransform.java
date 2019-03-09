package sawmill;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.logz.sawmill.Doc;
import io.logz.sawmill.ExecutionResult;
import io.logz.sawmill.Pipeline;
import io.logz.sawmill.PipelineExecutor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class SawmillTransform {

  private static String readFile(File file) throws IOException {
    final BufferedReader reader = new BufferedReader(new FileReader(file));
    String line;
    final StringBuilder stringBuilder = new StringBuilder();
    final String ls = System.getProperty("line.separator");

    try {
      while ((line = reader.readLine()) != null) {
        stringBuilder.append(line);
        stringBuilder.append(ls);
      }

      return stringBuilder.toString();
    } finally {
      reader.close();
    }
  }

  public void entry() throws IOException {
    System.out.println("\nSAWMILL");
    final File file = new File(getClass().getClassLoader().getResource("sawmill/steps").getFile());
    final File input =
        new File(getClass().getClassLoader().getResource("json/input.json").getFile());

    final Pipeline pipeline = new Pipeline.Factory().create(readFile(file));

    final Doc doc =
        new Doc(new ObjectMapper().convertValue(new ObjectMapper().readTree(input), Map.class));
    final ExecutionResult executionResult = new PipelineExecutor().execute(pipeline, doc);

    // Input
    System.out.println("Input:");
    System.out.println(new ObjectMapper().readTree(input));

    // Output
    if (executionResult.isSucceeded()) {
      System.out.println("Output:");
      System.out.println(new ObjectMapper().convertValue(doc.getSource(), JsonNode.class));
    }
  }
}
