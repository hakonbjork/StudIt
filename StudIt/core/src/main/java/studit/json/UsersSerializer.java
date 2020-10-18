package studit.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import studit.core.users.User;
import studit.core.users.Users;

public class UsersSerializer extends JsonSerializer<Users> {

  /**
   * Serializes a Users object.
   * 
   * @param users       Users object to serialize
   * @param jsonGen     JsonGenerator instance
   * @param serializers SerializerProvider instance
   */
  @Override
  public void serialize(Users users, JsonGenerator jsonGen, SerializerProvider serializers) throws IOException {

    jsonGen.writeStartObject();

    jsonGen.writeStringField("prevAssignedID", String.valueOf(users.getPrevAssignedID()));
    jsonGen.writeArrayFieldStart("users");
    for (User user : users.getUsers().values()) {
      jsonGen.writeObject(user);
    }
    jsonGen.writeEndArray();

    jsonGen.writeEndObject();

  }

}