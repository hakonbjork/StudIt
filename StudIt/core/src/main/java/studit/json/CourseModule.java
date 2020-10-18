package studit.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import studit.core.mainpage.CourseItem;
import studit.core.mainpage.CourseList;
import studit.json.deserializers.CourseItemDeserializer;
import studit.json.deserializers.CourseListDeserializer;
import studit.json.serializers.CourseItemSerializer;
import studit.json.serializers.CourseListSerializer;


@SuppressWarnings("serial")
public class CourseModule extends SimpleModule {

  // TODO: remove this class (deprecated)

  private static final String NAME = "CourseModule";

  /**
   * Initializes this CourseModule with appropriate serializers and deserializers.
   */
  public CourseModule() {
    super(NAME, Version.unknownVersion());
    addSerializer(CourseItem.class, new CourseItemSerializer());
    addSerializer(CourseList.class, new CourseListSerializer());
    addDeserializer(CourseItem.class, new CourseItemDeserializer());
    addDeserializer(CourseList.class, new CourseListDeserializer());
  }
}
