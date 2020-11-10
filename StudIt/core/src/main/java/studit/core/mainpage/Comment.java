package studit.core.mainpage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Comment {
  private String brukernavn;
  private String kommentar;
  private String dato;
  private int upvotes;
  private int downvotes;
  private int uniqueID;
  private List<String> upvoters;
  private List<String> downvoters;

  public Comment() {

  }

  public Comment(String brukernavn, String kommentar, int uniqueID) {
    this.brukernavn = brukernavn;
    this.kommentar = kommentar;
    this.dato = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(Calendar.getInstance().getTime());
    this.uniqueID = uniqueID;
    this.upvotes = 0;
    this.downvotes = 0;
    this.upvoters = new ArrayList<>();
    this.downvoters = new ArrayList<>();

  }

  public String getBrukernavn() {
    return this.brukernavn;
  }

  public void setBrukernavn(String brukernavn) {
    this.brukernavn = brukernavn;
  }

  public String getDato() {
    return dato;
  }

  public void setDato(String dato) {
    this.dato = dato;
  }

  public String getKommentar() {
    return kommentar;
  }

  public void setKommentar(String kommentar) {
    this.kommentar = kommentar;
  }

  public int getUpvotes() {
    return upvotes;
  }

  public void setUpvotes(int upvotes) {
    this.upvotes = upvotes;
  }

  public int getDownvotes() {
    return downvotes;
  }

  public void setDownvotes(int downvotes) {
    this.downvotes = downvotes;
  }

  /**
   * Sets the unique id for the comment. WARNING: This should only be called from
   * the deserializer class and nothing else.
   */
  public void setUniqueID(int uniqueID) {
    this.uniqueID = uniqueID;
  }

  public int getUniqueID() {
    return uniqueID;
  }

  /**
   * The user upvotes the comment. If already upvoted, it is not possible. Max 1 upvote per user.
   * If the user regret upvoting, the upvote can be offset by clicking on the downvote button
   */
  public boolean upvote(String username) {
    if (!upvoters.contains(username)) {

      if(downvoters.contains(username)){
        downvoters.remove(username);
        upvotes++;
        return true;
      }

      upvoters.add(username);
      upvotes++;
      return true;
    }
    return false;
  }

  /**
   * The user downvotes the comment. If already downvoted, it is not possible. Max 1 downvote per user.
   * If the user regret downvoting, the downvote can be offset by clicking on the upvote button
   */
  public boolean downvote(String username) {
    if (!downvoters.contains(username)) {

      if(upvoters.contains(username)){
        upvoters.remove(username);
        upvotes--;
        return true;
      }

      downvoters.add(username);
      upvotes--;
      return true;
    }
    return false;
  }

  public List<String> getUpvoters() {
    return upvoters;
  }

  public void setUpvoters(List<String> upvoters) {
    this.upvoters = upvoters;
  }

  public List<String> getDownvoters() {
    return downvoters;
  }

  public void setDownvoters(List<String> downvoters) {
    this.downvoters = downvoters;
  }

}
