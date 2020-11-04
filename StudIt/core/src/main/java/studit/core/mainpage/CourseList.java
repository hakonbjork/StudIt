package studit.core.mainpage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CourseList implements Iterable<CourseItem> {

  private List<CourseItem> items = new ArrayList<>();

  public CourseItem createCourseItem() {
    return new CourseListItem(this);
  }

  /**
   * Adds the provided CourseItem to this CourseList. If the CourseItem is not an
   * instance of CourseListItem, its contents is copied in to a new CourseListItem
   * and that is added instead.
   *
   * @param item the CourseItem to add
   */
  public void addCourseItem(CourseItem item) {
    CourseListItem courseListItem = null;
    if (item instanceof CourseListItem) {
      courseListItem = (CourseListItem) item;
    } else {
      courseListItem = new CourseListItem(this);
      courseListItem.setFagkode(item.getFagkode());
      courseListItem.setFagnavn(item.getFagnavn());
      courseListItem.setInformasjon(item.getInformasjon());
      courseListItem.setPensumlitteratur(item.getPensumlitteratur());
      courseListItem.setAnbefaltLitteratur(item.getAnbefaltLitteratur());
      courseListItem.setTips(item.getTips());
      courseListItem.setEksamensdato(item.getEksamensdato());
      courseListItem.setVurderingsform(item.getVurderingsform());
      courseListItem.setHjelpemidler(item.getHjelpemidler());
      courseListItem.setVurderinger(item.getVurderinger());
      courseListItem.setAverageVurdering();
      courseListItem.setDiskusjon(item.getDiskusjon());
    }
    items.add(courseListItem);
  }

  public void removeCourseItem(CourseItem item) {
    items.remove(item);
  }

  @Override
  public Iterator<CourseItem> iterator() {
    return items.iterator();
  }

  public List<CourseItem> getCourseItems() {
    return items;
  }

  /**
   * Get CourseItem by its fagkode.
   * @param fagkode fagkode of the requested course.
   * @return CourseItem if it exists, otherwise null.
   */
  public CourseItem getCourseByFagkode(String fagkode) {
    for (CourseItem item : items) {
      if (item.getFagkode().equals(fagkode)) {
        return item;
      }
    }
    return null;
  }

  /**
   * Get a list on the form [fagkode, fagnavn].
   * @return List of String arrays where String[0] = fagkode & String[1] = fagnavn.
   */
  public List<String[]> getCourseNameList() {
    List<String[]> courseNameList = new ArrayList<>();
    for (CourseItem item : items) {
      courseNameList.add(new String[] {item.getFagkode(), item.getFagnavn()});
    }
    return courseNameList;
  }

}
