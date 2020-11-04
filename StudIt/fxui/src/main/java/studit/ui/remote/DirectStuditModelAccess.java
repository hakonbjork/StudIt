package studit.ui.remote;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import studit.core.StuditModel;
import studit.core.mainpage.Comment;
import studit.core.mainpage.CourseItem;
import studit.core.mainpage.CourseList;
import studit.core.mainpage.Discussion;
import studit.core.users.User;
import studit.core.users.Users;
import studit.json.StuditPersistence;

public class DirectStuditModelAccess extends RemoteStuditModelAccess {

  private StuditModel studitModel;

  public DirectStuditModelAccess() {
    init();
  }

  public void init() {
    StuditPersistence studitPersistence = new StuditPersistence();

    try (Reader reader = new FileReader("src/test/resources/studit/fxui/defaultdb.json", StandardCharsets.UTF_8)) {
      studitModel = studitPersistence.readStuditModel(reader);
    } catch (IOException e) {
      System.out.println("Couldn't read default studitModel");
    }
  }

  @Override
  public boolean ping() {
    return true;
  }

  @Override
  public StuditModel getStuditModel() throws ApiCallException {
    return studitModel;
  }

  @Override
  public CourseList getCourseList() throws ApiCallException {
    return studitModel.getCourseList();
  }

  @Override
  public Users getUsers() throws ApiCallException {
    return studitModel.getUsers();
  }

  @Override
  public User getUserByID(int id) throws ApiCallException {
    return studitModel.getUsers().getUserByID(id);
  }

  @Override
  public boolean removeUserByID(int id) throws ApiCallException {
    return studitModel.getUsers().removeUser(id);
  }

  @Override
  public String[] addUser(String name, String username, String mail, String password) throws ApiCallException {
    String[] result = studitModel.getUsers().addUser(name, username, mail, password);
    return new String[] { result[2], result[1] };
  }

  @Override
  public boolean authenticateLogin(String username, String password) throws ApiCallException {
    return studitModel.getUsers().authenticateLogin(username, password);
  }

  @Override
  public String[] changePassword(int id, String newPassword) throws ApiCallException {
    String result = studitModel.getUsers().changePassword(id, newPassword);
    return new String[] { result == null ? null : "-1", result == null ? "Success" : result };
  }

  @Override
  public String[] changeUsername(int id, String newUsername) throws ApiCallException {
    String result = studitModel.getUsers().changeUsername(id, newUsername);
    System.out.println(result);
    return new String[] { result == null ? null : "-1", result == null ? "Success" : result };
  }

  @Override
  public String[] changeMail(int id, String newMail) throws ApiCallException {
    String result = studitModel.getUsers().changeMail(id, newMail);
    return new String[] { result == null ? null : "-1", result == null ? "Success" : result };
  }

  @Override
  public CourseItem getCourseByFagkode(String fagkode) throws ApiCallException {
    return studitModel.getCourseList().getCourseByFagkode(fagkode);
  }

  @Override
  public Discussion getDiscussion(String fagkode) throws ApiCallException {
    try {
      return studitModel.getCourseList().getCourseByFagkode(fagkode).getDiskusjon();
    } catch (NullPointerException e) {
      throw new ApiCallException("Course with fagkode '" + fagkode + "' does not exist");
    }
  }

  @Override
  public int addCommentToDiscussion(String fagkode, String username, String comment) throws ApiCallException {
    try {
      return studitModel.getCourseList().getCourseByFagkode(fagkode).getDiskusjon().addComment(username, comment);
    } catch (NullPointerException e) {
      throw new ApiCallException("Course with fagkode '" + fagkode + "' does not exist");
    }
  }

  @Override
  public Comment getCommentById(String fagkode, int id) throws ApiCallException {
    try {
      return studitModel.getCourseList().getCourseByFagkode(fagkode).getDiskusjon().getCommentByID(id);
    } catch (NullPointerException e) {
      throw new ApiCallException("Course with fagkode '" + fagkode + "' does not exist");
    }
  }

  @Override
  public boolean deleteCommentByID(String fagkode, int id) throws ApiCallException {
    try {
      return studitModel.getCourseList().getCourseByFagkode(fagkode).getDiskusjon().removeComment(id);
    } catch (NullPointerException e) {
      throw new ApiCallException("Course with fagkode '" + fagkode + "' does not exist");
    }
  }

  @Override
  public boolean upvoteCommentByID(String fagkode, String username, int id) throws ApiCallException {
    try {
      return studitModel.getCourseList().getCourseByFagkode(fagkode).getDiskusjon().upvote(username, id) == 1 ? true
          : false;
    } catch (NullPointerException e) {
      throw new ApiCallException("Course with fagkode '" + fagkode + "' does not exist");
    }
  }

  @Override
  public boolean downvoteCommentByID(String fagkode, String username, int id) throws ApiCallException {
    try {
      return studitModel.getCourseList().getCourseByFagkode(fagkode).getDiskusjon().downvote(username, id) == 1 ? true
          : false;
    } catch (NullPointerException e) {
      throw new ApiCallException("Course with fagkode '" + fagkode + "' does not exist");
    }
  }

}