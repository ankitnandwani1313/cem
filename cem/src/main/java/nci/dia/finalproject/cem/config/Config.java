package nci.dia.finalproject.cem.config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Config {

	public String readconfigParameters(String jsonFileLocation) {
		JSONParser parser = new JSONParser();
		String tradePath;
		String cpiPath;
		String outputPath;
		String elecgenPath;
		String cordir;
		String localcordir;
		String finalString = "";
		try {
			Object obj = parser.parse(new FileReader(jsonFileLocation));
			JSONObject jsonObject = (JSONObject) obj;
			JSONObject fileInformation = (JSONObject) jsonObject.get("fileInformation");
			tradePath = (String) fileInformation.get("trade");
			cpiPath = (String) fileInformation.get("cpi");
			elecgenPath = (String) fileInformation.get("elecgen");
			outputPath = (String) fileInformation.get("outputLocation");
			cordir = (String) fileInformation.get("cor-dir");
			localcordir = (String) fileInformation.get("local-cor-dir");

			finalString = tradePath + "," + cpiPath + "," + elecgenPath + "," + outputPath + "," + cordir
					+ "," + localcordir;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return finalString;
	}

}
