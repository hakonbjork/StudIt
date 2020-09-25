package studit.core.json;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CourseListManager {

    /**
	 * Writes a list of dummy keyword connections to our json database
	 */
	public void writeToDb(String fagkode, String fagnavn, String score, String kommentar) {

		
        Map<String, String[]> output = loadJson("db.json");
        
		output.put(fagkode, new String[]{fagkode, fagnavn, score, kommentar});
		
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(Paths.get("src/main/resources/studit/db/db.json").toFile(), output);
		} catch (IOException e) {
			System.out.println("Error occured while printing json to file");
			e.printStackTrace();
		}

    }
    
    /**
     * Reads Json file on the format Map<String, String[]> and returns the list
     * 
     * @param filename - Filename under resources/studit/db. E.g "test.json"
     * @return Map<String, String[]> containing our values
     */
    public Map<String, String[]> loadJson(String filename) {
        String path = "src/main/resources/studit/db/" + filename;

        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, String[]> keywords = mapper.readValue(Paths.get(path).toFile(),
                    new TypeReference<Map<String, String[]>>() {
                    });
            return keywords;
        } catch (IOException e) {
            System.out.println("Error occured while reading json '" + filename + "'.");
            e.printStackTrace();
        }
        return null;
    }
}