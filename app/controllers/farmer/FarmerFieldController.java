package controllers.farmer;

import com.fasterxml.jackson.databind.JsonNode;
import helpers.InputValidator;
import models.farmer.Farmer;
import models.farmer.FarmerField;
import play.mvc.Controller;
import play.mvc.Result;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static play.libs.Json.toJson;

/**
 * Created by lenovo on 11/5/2015.
 */
public class FarmerFieldController extends Controller {
    private static final String[] FIELD_INSERT_FARMER_FIELD = {"farmer_id", "desa_id", "area", "ownership_status", "is_irrigated"};
    private static final String[] FIELD_FARMER = {"field_id"};

    public static Result findFarmerField (int farmerId) {
        Map<String, Object> result = new HashMap<>();
        try {
            return ok(toJson(FarmerField.select(farmerId)));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError (toJson(result));
        }
    }

    public static Result insertFarmerField () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, FIELD_INSERT_FARMER_FIELD);

            FarmerField farmerField = new FarmerField();
            farmerField.farmerId = jsonNode.get("farmer_id").asInt();
            farmerField.desaId = jsonNode.get("desa_id").asInt();
            farmerField.fieldArea = jsonNode.get("area").asDouble();
            farmerField.ownershipStatus = jsonNode.get("ownership_status").asText();
            farmerField.isIrrigated = jsonNode.get("is_irrigated").asBoolean();
            farmerField.notes = jsonNode.has("notes")? jsonNode.get("notes").asText() : null;

            farmerField.fieldId = FarmerField.insert(farmerField);
            result.put("message", "farmer field inserted");
            result.put("id", farmerField.fieldId);
            return ok(toJson(result));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result updateFarmerField () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, FIELD_FARMER);

            int fieldId = jsonNode.get("field_id").asInt();
            FarmerField farmerField = FarmerField.selectOne(fieldId);

            if (farmerField == null) {
                result.put("message", "farmer field does not exist");
                return badRequest(toJson(result));
            }

            farmerField.desaId = (jsonNode.has("desa_id"))? jsonNode.get("desa_id").asInt() : farmerField.desaId;
            farmerField.fieldArea = (jsonNode.has("area"))? jsonNode.get("area").asDouble() : farmerField.fieldArea;
            farmerField.ownershipStatus = (jsonNode.has("ownership_status"))? jsonNode.get("ownership_status").asText() : farmerField.ownershipStatus;
            farmerField.isIrrigated = (jsonNode.has("is_irrigated"))? jsonNode.get("is_irrigated").asBoolean() : farmerField.isIrrigated;
            farmerField.notes = (jsonNode.has("notes"))? jsonNode.get("notes").asText() : farmerField.notes;

            if (FarmerField.update(farmerField) < 1) {
                result.put("message", "error occured during updating");
                return internalServerError(toJson(result));
            } else {
                result.put("message", "farmer " + farmerField.farmerId + " updated");
                return ok(toJson(result));
            }
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result deleteFarmerField () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, FIELD_FARMER);

            int fieldId = jsonNode.get("field_id").asInt();

            if (FarmerField.delete(fieldId) < 1) {
                result.put("message", "error occured during deleting");
                return internalServerError(toJson(result));
            } else {
                result.put("message", "farmer field " + fieldId + " deleted");
                return ok(toJson(result));
            }
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError(toJson(result));
        }
    }
}
