package jolt;

import com.bazaarvoice.jolt.SpecDriven;
import com.bazaarvoice.jolt.Transform;
import com.bazaarvoice.jolt.exception.SpecException;
import com.google.inject.Inject;

import java.util.LinkedHashMap;
import java.util.Map;

public class SensorDataTransform implements SpecDriven, Transform {

  private final Object spec;

  @Inject
  public SensorDataTransform(final Object spec) {
    this.spec = spec;
    if (spec == null) {
      throw new SpecException("SensorDataTransform expected a spec of Map type, got 'null'.");
    }
    if (!(spec instanceof Map)) {
      throw new SpecException(
          "SensorDataTransform expected a spec of Map type, got "
              + spec.getClass().getSimpleName());
    }
  }

  @Override
  public Object transform(Object input) {
    if (!(input instanceof Map)) {
      return input;
    }

    final Map<String, Object> inputMap = (Map<String, Object>) input;
    final Map<String, Object> outputMap = new LinkedHashMap<>();

    for (Map.Entry<String, Object> entry : inputMap.entrySet()) {
      final String colName = entry.getKey();
      final Object value = entry.getValue();
      final Double colValue;
      try {
        colValue = Double.parseDouble(value.toString());
      } catch (NumberFormatException e) {
        outputMap.put(colName, value);
        continue;
      }
      outputMap.put(colName, getOutputValueBySpec(colName, colValue));
    }

    return outputMap;
  }

  private Object getOutputValueBySpec(String colName, double inputColValue) {

    final Map<String, Map<String, Integer>> mySpec = (Map<String, Map<String, Integer>>) spec;
    final Map<String, Integer> rules = mySpec.get(colName);

    if (rules != null) {
      return getValue(rules, inputColValue);
    } else {
      return inputColValue;
    }
  }

  public int getValue(Map<String, Integer> rules, double inputValue) {
    int outputValue = rules.get("default");

    for (Map.Entry<String, Integer> entry : rules.entrySet()) {
      final String key = entry.getKey();

      if (isValidRule(key)) {
        final String operator = key.split(" ")[0];
        final double ruleValue = Double.parseDouble(key.split(" ")[1]);
        final int categoryNumber = entry.getValue();

        switch (operator) {
          case ">":
            if (inputValue > ruleValue) {
              outputValue = categoryNumber;
            }
            break;
          case "<":
            if (inputValue < ruleValue) {
              outputValue = categoryNumber;
            }
            break;
          case "=":
            if (inputValue == ruleValue) {
              outputValue = categoryNumber;
            }
            break;
          case ">=":
            if (inputValue >= ruleValue) {
              outputValue = categoryNumber;
            }
            break;
          case "<=":
            if (inputValue <= ruleValue) {
              outputValue = categoryNumber;
            }
            break;
          default:
            outputValue = rules.get("default");
        }
      }
    }
    return outputValue;
  }

  private boolean isValidRule(String ruleKey) {
    return ruleKey.contains(" ");
  }
}
