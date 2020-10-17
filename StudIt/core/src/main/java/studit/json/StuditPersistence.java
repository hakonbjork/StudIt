package studit.json;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.fasterxml.jackson.databind.ObjectMapper;

import studit.core.StuditModel;

public class StuditPersistence {

  private ObjectMapper mapper;

  public StuditPersistence() {
    mapper = new ObjectMapper();
    mapper.registerModule(new StuditModule());
  }

  public StuditModel readStuditModel(Reader reader) throws IOException {
    return mapper.readValue(reader, StuditModel.class);
  }

  public void writeStuditModel(StuditModel studitModel, Writer writer) throws IOException {
    mapper.writerWithDefaultPrettyPrinter().writeValue(writer, studitModel);
  }

}