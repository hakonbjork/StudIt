package studit.core;

import studit.core.mainpage.CourseList;
import studit.core.users.Users;

public class StuditModel {

  private CourseList courseList = new CourseList();
  private Users users = new Users();

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

  public Users getUsers() {
    return users;
  }

  public void setUsers(Users users) {
    this.users = users;
  }
  
}