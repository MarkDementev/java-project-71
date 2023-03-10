package hexlet.code;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class Parser {
    public static final String[] FILE_EXTENSIONS = {"json", "yml", "yaml"};
    public static final String DIFFERENT_EXTENSIONS_ERROR = "Files has different filename extensions."
            + "\nEnter paths only with same filename extensions!";
    public static final String UNKNOWN_EXTENSION_ERROR = "There is unknown filename extension.\nCheck input files!";
    public static final String NULL_STRING_TEXT = "null";
    public static final String BOTH_FILES_EMPTY = "Both files are empty!";
    private static final String NO_FILE_EXTENSION_WARNING = "There is no file extension!!!";

    public static void checkFilesExtension(String firstFileExtension, String secondFileExtension) throws Exception {
        if (!firstFileExtension.equals(secondFileExtension)) {
            throw new Exception(DIFFERENT_EXTENSIONS_ERROR);
        }

        if (!firstFileExtension.equals(FILE_EXTENSIONS[0]) && !firstFileExtension.equals(FILE_EXTENSIONS[1])) {
            throw new Exception(UNKNOWN_EXTENSION_ERROR);
        }
    }

    public static Map<String, Object> parseToMap(String filesExtension, String filePath)
            throws NullPointerException, JsonProcessingException {
        return switch (filesExtension) {
            case "json" -> parseFromJSON(filePath);
            case "yml" -> parseFromYAML(filePath);
            default -> throw new NullPointerException(NO_FILE_EXTENSION_WARNING);
        };
    }

    public static void checkIsEmptyFiles(String filesExtension,
                                         Map<String, Object> firstFileParsedMap,
                                         Map<String, Object> secondFileParsedMap) throws Exception {
        if (filesExtension.equals(FILE_EXTENSIONS[0])) {
            if (firstFileParsedMap.size() == 0 && secondFileParsedMap.size() == 0) {
                throw new Exception(BOTH_FILES_EMPTY);
            }
        } else {
            if (firstFileParsedMap == null && secondFileParsedMap == null) {
                throw new Exception(BOTH_FILES_EMPTY);
            }
        }
    }

    private static Map<String, Object> parseFromJSON(String filePath) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jSONMap = mapper.readValue(filePath, new TypeReference<>() { });

        return makeNullsToString(jSONMap);
    }

    private static Map<String, Object> parseFromYAML(String filePath) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Map<String, Object> yAMLMap = mapper.readValue(filePath, new TypeReference<>() { });

        return makeNullsToString(yAMLMap);
    }

    private static Map<String, Object> makeNullsToString(Map<String, Object> map) {
        if (map == null) {
            return null;
        }

        for (Map.Entry<String, Object> element : map.entrySet()) {
            if (element.getValue() == null) {
                element.setValue(NULL_STRING_TEXT);
            }
        }
        return map;
    }
}
