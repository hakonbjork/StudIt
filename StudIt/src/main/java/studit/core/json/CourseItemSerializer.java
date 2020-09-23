package studit.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import studit.core.mainpage.CourseItem;

public class CourseItemSerializer extends JsonSerializer<CourseItem> {

  /*
   * format: { "TDT4109": [xxx, xxx, xxx, xxx] }
   */

  @Override
  public void serialize(CourseItem item, JsonGenerator jsonGen, SerializerProvider serializerProvider)
      throws IOException {
    jsonGen.writeStartObject();
    jsonGen.writeFieldName(item.getFagkode());
    jsonGen.writeStartArray();

    jsonGen.writeFieldName(item.getFagkode());
    jsonGen.writeFieldName(item.getFagnavn());
    jsonGen.writeFieldName(item.getScore());
    jsonGen.writeFieldName(item.getKommentar());
    
    jsonGen.writeEndArray();
    jsonGen.writeEndObject();
  }
}
