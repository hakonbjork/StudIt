package studit.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.io.IOException;
import studit.core.users.User;

public class UserDeserializer extends JsonDeserializer<User> {

  /**
   * Deserializes Comment object.
   * 
   * @param parser JsonParser instance
   * @param ctxt   DeserializationContext instance
   */
  @Override
  public User deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    TreeNode treeNode = parser.getCodec().readTree(parser);
    return deserialize((JsonNode) treeNode);
  }

  public User deserialize(JsonNode jsonNode) {
    if (jsonNode instanceof ObjectNode) {
      ObjectNode objectNode = (ObjectNode) jsonNode;
      User user = new User();

      JsonNode nameNode = objectNode.get("name");
      if (nameNode instanceof TextNode) {
        user.setName(((TextNode) nameNode).asText());
      }

      JsonNode usernameNode = objectNode.get("username");
      if (usernameNode instanceof TextNode) {
        user.setUsername(((TextNode) usernameNode).asText());
      }

      JsonNode mailNode = objectNode.get("mail");
      if (mailNode instanceof TextNode) {
        user.setMail(((TextNode) mailNode).asText());
      }

      JsonNode passwordNode = objectNode.get("mail");
      if (passwordNode instanceof TextNode) {
        user.setPassword(((TextNode) passwordNode).asText());
      }

      JsonNode idNode = objectNode.get("uniqueID");
      if (idNode instanceof TextNode) {
        user.setUniqueID(((TextNode) idNode).asInt());
      }

      return user;
    }

    return null;
  }

}