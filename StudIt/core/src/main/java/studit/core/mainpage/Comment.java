package studit.core.mainpage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Comment {
  private String brukernavn;
  private String kommentar;
  private String dato;
  private int upvotes;
  private int downvotes;
  private int uniqueID;

  public Comment() {

  }

  public Comment(String brukernavn, String kommentar, int uniqueID) {
    this.brukernavn = brukernavn;
    this.kommentar = kommentar;
    this.dato = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(Calendar.getInstance().getTime());
    this.uniqueID = uniqueID;
    this.upvotes = 0;
    this.downvotes = 0;
  }

  public String getBrukernavn() {
    return brukernavn;
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
   * Sets the unique id for the comment.
   * WARNING: This should only be called from the deserializer class and nothing else.
   */
  public void setUniqueID(int uniqueID) {
    this.uniqueID = uniqueID;
  }

  public int getUniqueID() {
    return uniqueID;
  }

  public void upvote() {
    upvotes++;
  }

  public void downvote() {
    downvotes++;
  }

}