import java.io.IOException;

import jolt.JoltTransform;
import jslt.JsltTransform;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import sawmill.SawmillTransform;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EntryTest {

  @Test
  public void test2Jolt() throws IOException {
    new JoltTransform().entry();
  }

  @Test
  public void test1Sawmill() throws IOException {
    new SawmillTransform().entry();
  }

  @Test
  public void test3Jslt() throws IOException {
    new JsltTransform().entry();
  }
}
