package studit.core.mainpage;

import java.util.HashMap;
import java.util.Map;

public class Discussion {

  private Map<Integer, Comment> comments = new HashMap<>();
  private int prevAssignedID = -1;

  public Map<Integer, Comment> getComments() {
    return comments;
  }

  public void setComments(Map<Integer, Comment> comments) {
    this.comments = comments;
  }

  /**
   * Adds a new comment to the discussion.
   * 
   * @param brukernavn unique username that sendt the comment
   * @param kommentar  written comment
   * @return HashMap key (ID) for the new Comment object
   */
  public int addComment(String brukernavn, String kommentar) {
    prevAssignedID += 1;
    comments.put(prevAssignedID, new Comment(brukernavn, kommentar, prevAssignedID));
    return prevAssignedID;
  }

  /**
   * Upvotes a comment in the discussion.
   * 
   * @param username  username that wishes to upvote
   * @param commentID Unique identifier for the comment (comments key)
   */
  public void upvote(String username, int commentID) {
    comments.get(commentID).upvote(username);
  }

  /**
   * Downvotes a comment in the discussion.
   * 
   * @param username  username that wishes to downvote
   * @param commentID Unique identifier for the comment (comments key)
   */
  public void downvote(String username, int commentID) {
    comments.get(commentID).downvote(username);
  }

  /**
   * Get the requested Comment object.
   * 
   * @param commentID Uniquie identifier for the comment (comments key)
   * @return Comment object if ID is valid, otherwise null
   */
  public Comment getCommentByID(int commentID) {
    return comments.get(commentID);
  }

  /**
   * Removes a comment from the discussion (CANNOT BE UNDONE).
   * 
   * @param commentID Uniquie identifier for the comment (comments key)
   * @return true if element was removed else false
   */
  public boolean removeComment(int commentID) {
    Comment comment = comments.remove(commentID);
    return comment == null ? false : true;
  }

  public int getPrevAssignedID() {
    return prevAssignedID;
  }

  public void setPrevAssignedID(int prevAssignedID) {
    this.prevAssignedID = prevAssignedID;
  }

}