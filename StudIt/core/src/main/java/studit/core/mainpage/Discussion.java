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
   * Adds a new comment to the discussion
   * @param brukernavn unique username that sendt the comment
   * @param kommentar written comment
   * @param dato date written
   * @return HashMap key (ID) for the new Comment object
   */
  public int addComment(String brukernavn, String kommentar) {
    prevAssignedID += 1;
    comments.put(prevAssignedID, new Comment(brukernavn, kommentar, prevAssignedID));
    return prevAssignedID;
  }

  /**
   * Upvotes a comment in the discussion
   * @param ID Unique identifier for the comment (comments key)
   */
  public void upvote(int ID) {
    comments.get(ID).upvote();
  }

  /**
   * Downvotes a comment in the discussion
   * @param ID Unique identifier for the comment (comments key)
   */
  public void downvote(int ID) {
    comments.get(ID).downvote();
  }

  /**
   * Get the requested Comment object
   * @param ID Uniquie identifier for the comment (comments key)
   * @return Comment object if ID is valid, otherwise null
   */
  public Comment getCommentByID(int ID) {
    return comments.get(ID);
  }

  /**
   * Removes a comment from the discussion (CANNOT BE UNDONE)
   * @param ID Uniquie identifier for the comment (comments key)
   * @return true if element was removed else false
   */
  public boolean removeComment(int ID) {
    Comment comment = comments.remove(ID); 
    return comment == null ? false : true; 
  }

  public int getPrevAssignedID() {
    return prevAssignedID;
  }

  public void setPrevAssignedID(int prevAssignedID) {
    this.prevAssignedID = prevAssignedID;
  }

}