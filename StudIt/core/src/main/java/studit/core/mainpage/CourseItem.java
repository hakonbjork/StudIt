package studit.core.mainpage;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class CourseItem {

  private String fagkode;
  private String fagnavn;
  private String informasjon;
  private String pensumlitteratur;
  private String anbefaltLitteratur;
  private String tips;
  private String eksamensdato;
  private String vurderingsform;
  private String hjelpemidler;
  private float averageVurdering;
  private List<Integer> vurderinger;
  private Discussion diskusjon;

  public CourseItem() {
    vurderinger = new ArrayList<>();
  }

  public void setFagnavn(String fagnavn) {
    this.fagnavn = fagnavn;
  }

  public void setFagkode(String fagkode) {
    this.fagkode = fagkode;
  }

  public String getFagnavn() {
    return this.fagnavn;
  }

  public String getFagkode() {
    return this.fagkode;
  }

  public String getInformasjon() {
    return informasjon;
  }

  public void setInformasjon(String informasjon) {
    this.informasjon = informasjon;
  }

  public String getPensumlitteratur() {
    return pensumlitteratur;
  }

  public void setPensumlitteratur(String pensumlitteratur) {
    this.pensumlitteratur = pensumlitteratur;
  }

  public String getAnbefaltLitteratur() {
    return anbefaltLitteratur;
  }

  public void setAnbefaltLitteratur(String anbefaltLitteratur) {
    this.anbefaltLitteratur = anbefaltLitteratur;
  }

  public String getTips() {
    return tips;
  }

  public void setTips(String tips) {
    this.tips = tips;
  }

  public String getEksamensdato() {
    return eksamensdato;
  }

  public void setEksamensdato(String eksamensdato) {
    this.eksamensdato = eksamensdato;
  }

  public String getVurderingsform() {
    return vurderingsform;
  }

  public void setVurderingsform(String vurderingsform) {
    this.vurderingsform = vurderingsform;
  }

  public String getHjelpemidler() {
    return hjelpemidler;
  }

  public void setHjelpemidler(String hjelpemidler) {
    this.hjelpemidler = hjelpemidler;
  }

  public List<Integer> getVurderinger() {
    return vurderinger;
  }

  public void setVurderinger(List<Integer> vurderinger) {
    this.vurderinger = vurderinger;
    setAverageVurdering();
  }

  public void addVurdering(Integer vurdering) {
    vurderinger.add(vurdering);
  }

  /**
   * Calculates the average rating of a course.
   */
  public void setAverageVurdering() {
    OptionalDouble average = vurderinger.stream().mapToInt(i -> i).average();
    
    averageVurdering = average.isPresent() ? (float) average.getAsDouble() : 0;
  }

  public float getAverageVurdering() {
    return averageVurdering;
  }

  public Discussion getDiskusjon() {
    return diskusjon;
  }

  public void setDiskusjon(Discussion diskusjon) {
    this.diskusjon = diskusjon == null ? new Discussion() : diskusjon;
  }


}
