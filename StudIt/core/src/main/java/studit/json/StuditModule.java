package studit.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import studit.core.mainpage.Comment;
import studit.core.mainpage.CourseItem;
import studit.core.mainpage.CourseList;
import studit.core.mainpage.Discussion;
import studit.core.users.User;
import studit.core.users.Users;
import studit.json.deserializers.CommentDeserializer;
import studit.json.deserializers.CourseItemDeserializer;
import studit.json.deserializers.CourseListDeserializer;
import studit.json.deserializers.DiscussionDeserializer;
import studit.json.deserializers.UserDeserializer;
import studit.json.deserializers.UsersDeserializer;
import studit.json.serializers.CommentSerializer;
import studit.json.serializers.CourseItemSerializer;
import studit.json.serializers.CourseListSerializer;
import studit.json.serializers.DiscussionSerializer;
import studit.json.serializers.UserSerializer;
import studit.json.serializers.UsersSerializer;


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

    addSerializer(User.class, new UserSerializer());
    addSerializer(Users.class, new UsersSerializer());
    addDeserializer(User.class, new UserDeserializer());
    addDeserializer(Users.class, new UsersDeserializer());
  }
}