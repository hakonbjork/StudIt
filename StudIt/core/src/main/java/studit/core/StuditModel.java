package studit.core;

import studit.core.mainpage.CourseList;

public class StuditModel {

  private CourseList courseList = new CourseList();

  /**
   * Get the active courseList.
   * @return CourseList object containing our list of Courses
   */
  public CourseList getCourseList() {
    return courseList;
  }

  public void setCourseList(CourseList courseList) {
    this.courseList = courseList;
  }
  
}