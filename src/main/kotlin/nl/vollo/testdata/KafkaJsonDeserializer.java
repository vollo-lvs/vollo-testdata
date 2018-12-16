package nl.vollo.testdata;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * Generic JSON deserializer.
 */
public class KafkaJsonDeserializer implements Deserializer<Object> {

  private ObjectMapper objectMapper;
  private Class<Object> type;

  /**
   * Default constructor needed by Kafka
   */
  public KafkaJsonDeserializer() {
  }

  @Override
  public void configure(Map<String, ?> props, boolean isKey) {
      System.out.println(props);
      this.objectMapper = new ObjectMapper();
      this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }

  @Override
  public JsonNode deserialize(String ignored, byte[] bytes) {
    if (bytes == null || bytes.length == 0) {
      return null;
    }

    try {
      return objectMapper.readTree(bytes);
    } catch (Exception e) {
      throw new SerializationException(e);
    }
  }

  @Override
  public void close() {

  }
}