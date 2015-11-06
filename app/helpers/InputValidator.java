package helpers;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;

/**
 * Created by lenovo on 9/21/2015.
 */
public class InputValidator {
    public boolean checkInputFields(JsonNode jsonNode, String [] fieldNameList) throws Exception {
        ArrayList<String> errorFields = new ArrayList<String>(fieldNameList.length);
        for (String fieldName: fieldNameList) {
            if (!jsonNode.has(fieldName)) {
                errorFields.add(fieldName);
            }
        }
        if (errorFields.size() > 0) {
            throw new FieldNotFoundException(errorFields.toString());
        }
        return true;
    }

    public class FieldNotFoundException extends Exception {
        String fieldName;

        public FieldNotFoundException(String fieldName) {
            super();
            this.fieldName = fieldName;
        }

        @Override
        public String getMessage() {
            return "Following fields: "+fieldName+" not found";
        }
    }
}
