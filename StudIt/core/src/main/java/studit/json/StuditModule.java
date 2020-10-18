package studit.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

import studit.core.mainpage.Comment;
import studit.core.mainpage.CourseItem;
import studit.core.mainpage.CourseList;
import studit.core.mainpage.Discussion;

public class StuditModule extends SimpleModule {
  private static final long serialVersionUID = 1L;
  private static final String NAME = "StuditModule";

  public StuditModule() {
    super(NAME, Version.unknownVersion());
    addSerializer(CourseItem.class, new CourseItemSerializer());
    addSerializer(CourseList.class, new CourseListSerializer());
    addDeserializer(CourseItem.class, new CourseItemDeserializer());
    addDeserializer(CourseList.class, new CourseListDeserializer());

    addSerializer(Comment.class, new CommentSerializer());
    addSerializer(Discussion.class, new DiscussionSerializer());
    addDeserializer(Comment.class, new CommentDeserializer());
    addDeserializer(Discussion.class, new DiscussionDeserializer());

  }
}