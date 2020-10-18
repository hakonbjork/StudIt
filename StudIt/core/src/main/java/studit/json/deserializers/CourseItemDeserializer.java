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
import java.util.ArrayList;
import java.util.List;
import studit.core.mainpage.CourseItem;
import studit.core.mainpage.Discussion;

public class CourseItemDeserializer extends JsonDeserializer<CourseItem> {

  private DiscussionDeserializer discussionDeserializer = new DiscussionDeserializer();

  /**
   * Deserialiezes a CourseItem.
   *
   */
  @Override
  public CourseItem deserialize(JsonParser parser, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    TreeNode treeNode = parser.getCodec().readTree(parser);
    return deserialize((JsonNode) treeNode);
  }

  public CourseItem deserialize(JsonNode jsonNode) {
    if (jsonNode instanceof ObjectNode) {
      ObjectNode objectNode = (ObjectNode) jsonNode;
      CourseItem item = new CourseItem();

      JsonNode fagkodeNode = objectNode.get("fagkode");
      if (fagkodeNode instanceof TextNode) {
        item.setFagkode(((TextNode) fagkodeNode).asText());
      }

      JsonNode nameNode = objectNode.get("fagnavn");
      if (nameNode instanceof TextNode) {
        item.setFagnavn(((TextNode) nameNode).asText());
      }

      JsonNode infoNode = objectNode.get("informasjon");
      if (infoNode instanceof TextNode) {
        item.setInformasjon(((TextNode) infoNode).asText());
      }

      JsonNode pensumNode = objectNode.get("pensumlitteratur");
      if (pensumNode instanceof TextNode) {
        item.setPensumlitteratur(((TextNode) pensumNode).asText());
      }

      JsonNode anbefaltNode = objectNode.get("anbefaltLitteratur");
      if (anbefaltNode instanceof TextNode) {
        item.setAnbefaltLitteratur(((TextNode) anbefaltNode).asText());
      }

      JsonNode tipsNode = objectNode.get("tips");
      if (tipsNode instanceof TextNode) {
        item.setTips(((TextNode) tipsNode).asText());
      }

      JsonNode eksamensNode = objectNode.get("eksamensdato");
      if (eksamensNode instanceof TextNode) {
        item.setEksamensdato(((TextNode) eksamensNode).asText());
      }

      JsonNode vurderingsNode = objectNode.get("vurderingsform");
      if (vurderingsNode instanceof TextNode) {
        item.setVurderingsform(((TextNode) vurderingsNode).asText());
      }

      JsonNode hjelpemidlerNode = objectNode.get("hjelpemidler");
      if (hjelpemidlerNode instanceof TextNode) {
        item.setHjelpemidler(((TextNode) hjelpemidlerNode).asText());
      }

      JsonNode vurderingerNode = objectNode.get("vurderinger");
      List<Integer> vurderinger = new ArrayList<>();
      if (vurderingerNode instanceof ArrayNode) {
        for (JsonNode vurdering : vurderingerNode) {
          vurderinger.add(vurdering.asInt());
        }
        item.setVurderinger(vurderinger);
      }

      JsonNode discussionNode = objectNode.get("diskusjon");
      Discussion discussion = discussionDeserializer.deserialize(discussionNode);
      item.setDiskusjon(discussion);

      item.setAverageVurdering();

      return item;

    }

    return null;
  }
}
