package jolt;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;

public class JoltTransform {

  public void entry() {
    System.out.println("\nJOLT");
    final Chainr chainr = Chainr.fromSpec(JsonUtils.classpathToList("/jolt/spec.json"));
    final Object transformedOutput =
        chainr.transform(JsonUtils.classpathToObject("/json/input.json"));

    // Input
    System.out.println("Input:");
    System.out.println(JsonUtils.toJsonString(JsonUtils.classpathToObject("/json/input.json")));

    // Output
    System.out.println("Output:");
    System.out.println(JsonUtils.toJsonString(transformedOutput));
  }
}
