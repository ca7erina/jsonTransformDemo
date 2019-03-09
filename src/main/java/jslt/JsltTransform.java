package jslt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.schibsted.spt.data.jslt.Expression;
import com.schibsted.spt.data.jslt.Parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsltTransform {

  public String convert(InputStream inputStream, Charset charset) throws IOException {
    return CharStreams.toString(new InputStreamReader(inputStream, charset));
  }

  public void entry() throws IOException {
    System.out.println("\nJSLT");

    InputStream specStream= getClass().getClassLoader().getResourceAsStream("jslt/spec2");
    InputStream inputStream= getClass().getClassLoader().getResourceAsStream("json/input.json");

    final JsonNode inputJson = new ObjectMapper().readTree(inputStream);
    final Expression jslt = Parser.compileString(convert((specStream), Charsets.UTF_8));
    final JsonNode output = jslt.apply(inputJson);

    // Input
    System.out.println("Input:");
    System.out.println(inputJson);

    // Output
    System.out.println("Output:");
    System.out.println(output);
  }
}
