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
import studit.core.mainpage.Comment;

public class CommentDeserializer extends JsonDeserializer<Comment> {

  /**
   * Deserializes Comment object.
   * 
   * @param parser JsonParser instance
   * @param ctxt   DeserializationContext instance
   */
  @Override
  public Comment deserialize(JsonParser parser, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    TreeNode treeNode = parser.getCodec().readTree(parser);
    return deserialize((JsonNode) treeNode);
  }

  public Comment deserialize(JsonNode jsonNode) {
    if (jsonNode instanceof ObjectNode) {
      ObjectNode objectNode = (ObjectNode) jsonNode;
      Comment comment = new Comment();

      JsonNode brukerNode = objectNode.get("brukernavn");
      if (brukerNode instanceof TextNode) {
        comment.setBrukernavn(((TextNode) brukerNode).asText());
      }

      JsonNode kommentarNode = objectNode.get("kommentar");
      if (kommentarNode instanceof TextNode) {
        comment.setKommentar(((TextNode) kommentarNode).asText());
      }

      JsonNode datoNode = objectNode.get("dato");
      if (datoNode instanceof TextNode) {
        comment.setDato(((TextNode) datoNode).asText());
      }

      JsonNode upvotesNode = objectNode.get("upvotes");
      if (upvotesNode instanceof TextNode) {
        comment.setUpvotes(((TextNode) upvotesNode).asInt());
      }

      JsonNode downvotesNode = objectNode.get("downvotes");
      if (downvotesNode instanceof TextNode) {
        comment.setDownvotes(((TextNode) downvotesNode).asInt());
      }

      JsonNode idNode = objectNode.get("uniqueID");
      if (idNode instanceof TextNode) {
        comment.setUniqueID(((TextNode) idNode).asInt());
      }

      return comment;
    }
    return null;
  }

}