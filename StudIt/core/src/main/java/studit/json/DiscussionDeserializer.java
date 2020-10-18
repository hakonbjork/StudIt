package studit.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import studit.core.mainpage.Comment;
import studit.core.mainpage.Discussion;

public class DiscussionDeserializer extends JsonDeserializer<Discussion> {

  private CommentDeserializer commentDeserializer = new CommentDeserializer();

  /**
   * Deserializes Discussion object
   * 
   * @param parser JsonParser instance
   * @param ctxt   DeserializationContext instance
   */
  @Override
  public Discussion deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    
    TreeNode treeNode = parser.getCodec().readTree(parser);
    return deserialize((JsonNode) treeNode);
    
  }

  public Discussion deserialize(JsonNode jsonNode) {
    if (jsonNode instanceof ObjectNode) {
      ObjectNode objectNode = (ObjectNode) jsonNode;
      Discussion discussion = new Discussion();

      JsonNode IDNode = objectNode.get("prevAssignedID");
      if (IDNode instanceof TextNode) {
        discussion.setPrevAssignedID(((TextNode) IDNode).asInt());
      }

      JsonNode commentsNode = objectNode.get("comments");
      Map<Integer, Comment> comments = new HashMap<>();
      if (commentsNode instanceof ArrayNode) {
        for (JsonNode elementNode : ((ArrayNode) commentsNode)) {
          Comment comment = commentDeserializer.deserialize(elementNode);
          if (comment != null) {
            comments.put(comment.getUniqueID(), comment);
          }
        }
        discussion.setComments(comments);
      }
      return discussion;
    }
    return null;
  }

}