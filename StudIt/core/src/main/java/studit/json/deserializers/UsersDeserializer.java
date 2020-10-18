package studit.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import studit.core.users.User;
import studit.core.users.Users;

public class UsersDeserializer extends JsonDeserializer<Users> {

  private UserDeserializer userDeserializer = new UserDeserializer();

  /**
   * Deserializes Users object.
   * 
   * @param parser JsonParser instance
   * @param ctxt   DeserializationContext instance
   */
  @Override
  public Users deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    TreeNode treeNode = parser.getCodec().readTree(parser);
    return deserialize((JsonNode) treeNode);
  }

  public Users deserialize(JsonNode jsonNode) {
    if (jsonNode instanceof ObjectNode) {
      ObjectNode objectNode = (ObjectNode) jsonNode;
      Users users = new Users();

      JsonNode idNode = objectNode.get("prevAssignedID");
      if (idNode instanceof TextNode) {
        users.setPrevAssignedID(((TextNode) idNode).asInt());
      }

      JsonNode usersNode = objectNode.get("users");
      Map<Integer, User> usersMap = new HashMap<>();
      if (usersNode instanceof ArrayNode) {
        for (JsonNode elementNode : ((ArrayNode) usersNode)) {
          User user = userDeserializer.deserialize(elementNode);
          if (user != null) {
            usersMap.put(user.getUniqueID(), user);
          }
        }
        users.setUsers(usersMap);
      }
      return users;
    }
    return null;
  }

}