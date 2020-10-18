package studit.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import studit.core.mainpage.Comment;
import studit.core.mainpage.Discussion;

public class DiscussionSerializer extends JsonSerializer<Discussion> {

  /**
   * Serializes a Discussion object
   * 
   * @param discussion  Discussion object to serialize
   * @param jsonGen     JsonGenerator instance
   * @param serializers SerializerProvider instance
   */
  @Override
  public void serialize(Discussion discussion, JsonGenerator jsonGen, SerializerProvider serializer)
      throws IOException {
    jsonGen.writeStartObject();
    jsonGen.writeStringField("prevAssignedID", String.valueOf(discussion.getPrevAssignedID()));
    jsonGen.writeArrayFieldStart("comments");
    for (Comment comment : discussion.getComments().values()) {
      jsonGen.writeObject(comment);
    }
    jsonGen.writeEndArray();
    jsonGen.writeEndObject();
  }

}