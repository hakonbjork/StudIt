package studit.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import studit.core.mainpage.CourseItem;

public class CourseItemSerializer extends JsonSerializer<CourseItem> {

  /**
   * Serializes a courseitem.
   *
   * @param item the item you want to serialize
   */
  @Override
  public void serialize(CourseItem item, JsonGenerator jsonGen, SerializerProvider serializerProvider)
      throws IOException {

    jsonGen.writeStartObject();

    jsonGen.writeStringField("fagkode", item.getFagkode());
    jsonGen.writeStringField("fagnavn", item.getFagnavn());
    jsonGen.writeStringField("informasjon", item.getInformasjon());
    jsonGen.writeStringField("pensumlitteratur", item.getPensumlitteratur());
    jsonGen.writeStringField("anbefaltLitteratur", item.getAnbefaltLitteratur());
    jsonGen.writeStringField("tips", item.getTips());
    jsonGen.writeStringField("eksamensdato", item.getEksamensdato());
    jsonGen.writeStringField("vurderingsform", item.getVurderingsform());
    jsonGen.writeStringField("hjelpemidler", item.getHjelpemidler());
    jsonGen.writeFieldName("vurderinger");
    jsonGen.writeStartArray();
    for (Integer vurdering : item.getVurderinger()) {
      jsonGen.writeString(String.valueOf(vurdering));
    }
    jsonGen.writeEndArray();
    jsonGen.writeObjectField("diskusjon", item.getDiskusjon());
    jsonGen.writeEndObject();

  }
}
