package studit.json.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import studit.core.users.User;

public class UserSerializer extends JsonSerializer<User> {

  /**
   * Serializes a User object.
   * 
   * @param user        User object to serialize
   * @param jsonGen     JsonGenerator instance
   * @param serializers SerializerProvider instance
   */
  @Override
  public void serialize(User user, JsonGenerator jsonGen, SerializerProvider serializers) throws IOException {

    jsonGen.writeStartObject();

    jsonGen.writeStringField("name", user.getName());
    jsonGen.writeStringField("username", user.getUsername());
    jsonGen.writeStringField("mail", user.getMail());
    jsonGen.writeStringField("password", user.getPassword());
    jsonGen.writeStringField("uniqueID", String.valueOf(user.getUniqueID()));

    jsonGen.writeEndObject();

  }

}