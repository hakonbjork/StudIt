package studit.json.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import studit.core.mainpage.Comment;

public class CommentSerializer extends JsonSerializer<Comment> {

  /**
   * Serializes a Comment object.
   * 
   * @param comment     Comment object to serialize
   * @param jsonGen     JsonGenerator instance
   * @param serializers SerializerProvider instance
   */
  @Override
  public void serialize(Comment comment, JsonGenerator jsonGen, SerializerProvider serializers) throws IOException {

    jsonGen.writeStartObject();

    jsonGen.writeStringField("brukernavn", comment.getBrukernavn());
    jsonGen.writeStringField("kommentar", comment.getKommentar());
    jsonGen.writeStringField("dato", comment.getDato());
    jsonGen.writeStringField("upvotes", String.valueOf(comment.getUpvotes()));
    jsonGen.writeStringField("downvotes", String.valueOf(comment.getDownvotes()));
    jsonGen.writeStringField("uniqueID", String.valueOf(comment.getUniqueID()));

    jsonGen.writeArrayFieldStart("upvoters");
    for (String upvoter : comment.getUpvoters()) {
      jsonGen.writeObject(upvoter);
    }
    jsonGen.writeEndArray();

    jsonGen.writeArrayFieldStart("downvoters");
    for (String downvoter : comment.getDownvoters()) {
      jsonGen.writeObject(downvoter);
    }
    jsonGen.writeEndArray();

    jsonGen.writeEndObject();

  }

}