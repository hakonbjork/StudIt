package studit.json;

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

public class CourseItemDeserializer extends JsonDeserializer<CourseItem> {

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

  CourseItem deserialize(JsonNode jsonNode) {
    if (jsonNode instanceof ObjectNode) {
      ObjectNode objectNode = (ObjectNode) jsonNode;
      CourseItem item = new CourseItem();

      JsonNode fagkodeNode = objectNode.get("fagkode");
      if (fagkodeNode instanceof TextNode) {
        item.setFagkode(((TextNode) fagkodeNode).asText());
      }

      JsonNode nameNode = objectNode.get("navn");
      if (nameNode instanceof TextNode) {
        item.setFagnavn(((TextNode) nameNode).asText());
      }

      JsonNode infoNode = objectNode.get("informasjon");
      if (infoNode instanceof TextNode) {
        item.setInformasjon(((TextNode) infoNode).asText());
      }

      JsonNode pensumNode = objectNode.get("pensumlitteratur");
      if (pensumNode instanceof TextNode) {
        item.setInformasjon(((TextNode) pensumNode).asText());
      }

      JsonNode anbefaltNode = objectNode.get("anbefaltLitteratur");
      if (anbefaltNode instanceof TextNode) {
        item.setInformasjon(((TextNode) anbefaltNode).asText());
      }

      JsonNode tipsNode = objectNode.get("tips");
      if (tipsNode instanceof TextNode) {
        item.setTips(((TextNode) tipsNode).asText());
      }

      JsonNode eksamensNode = objectNode.get("eksamensdato");
      if (eksamensNode instanceof TextNode) {
        item.setTips(((TextNode) eksamensNode).asText());
      }

      JsonNode vurderingsNode = objectNode.get("vurderingsform");
      if (vurderingsNode instanceof TextNode) {
        item.setTips(((TextNode) vurderingsNode).asText());
      }

      JsonNode hjelpemidlerNode = objectNode.get("hjelpemidler");
      if (hjelpemidlerNode instanceof TextNode) {
        item.setTips(((TextNode) hjelpemidlerNode).asText());
      }

      JsonNode vurderingerNode = objectNode.get("vurderinger");
      List<Integer> vurderinger = new ArrayList<>();
      if (vurderingerNode instanceof ArrayNode) {
        for (JsonNode vurdering : vurderingerNode) {
          vurderinger.add(vurdering.asInt());
        }
        item.setVurderinger(vurderinger);
      }
      
      item.setAverageVurdering();

      return item;

    }

    return null;
  }
}
