package studit.restserver;

import java.util.List;

import studit.core.mainpage.CourseItem;

public class DefaultGenerator {

  public void testSerializers() {
      CourseItem testItem = new CourseItem();
      testItem.setFagkode("TMA4140");
      testItem.setFagnavn("bruh");
      testItem.setInformasjon("Info");
      testItem.setPensumlitteratur("bok");
      testItem.setAnbefaltLitteratur("bok2");
      testItem.setTips("Dette var lurt");
      testItem.setEksamensdato("03/12/2002");
      testItem.setVurderingsform("hjemmeEKsamen");
      testItem.setHjelpemidler("alle");
      testItem.setVurderinger(List.of(3, 5, 9, 8));
  }
}