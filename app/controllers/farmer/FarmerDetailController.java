package controllers.farmer;

import com.fasterxml.jackson.databind.JsonNode;
import helpers.InputValidator;
import helpers.security.Secured;
import models.farmer.FarmerDetail;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static play.libs.Json.toJson;

/**
 * Created by lenovo on 11/5/2015.
 */

@Security.Authenticated(Secured.class)
public class FarmerDetailController extends Controller {
    private static final String[] FIELD_FARMER_DETAIL = {"farmer_id"};

    public static Result findFarmerDetail (int farmerId) {
        Map<String, Object> result = new HashMap<>();
        try {
            return ok(toJson(FarmerDetail.selectOne(farmerId)));
        } catch (Exception e) {
            result.put("message", "farmer detail does not exist");
            return badRequest(toJson(result));
        }
    }

    public static Result insertFarmerDetail () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, FIELD_FARMER_DETAIL);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            FarmerDetail farmerDetail = new FarmerDetail();
            farmerDetail.farmerId = jsonNode.get("farmer_id").asInt();
            farmerDetail.religion = (jsonNode.has("religion"))? jsonNode.get("religion").asText() : null;
            farmerDetail.nameOfSpouse = (jsonNode.has("name_of_spouse"))? jsonNode.get("name_of_spouse").asText() : null;
            farmerDetail.numOfDependants = (jsonNode.has("number_of_dependants"))? jsonNode.get("number_of_dependants").asInt() : null;
            farmerDetail.numOfChildren = (jsonNode.has("number_of_children"))? jsonNode.get("number_of_children").asInt() : null;
            farmerDetail.residentialStatus = (jsonNode.has("residential_status"))? jsonNode.get("residential_status").asText() : null;
            farmerDetail.education = (jsonNode.has("education"))? jsonNode.get("education").asText() : null;
            farmerDetail.placeOfBirth = (jsonNode.has("place_of_birth"))? jsonNode.get("place_of_birth").asText() : null;
            farmerDetail.dateOfBirth = (jsonNode.has("date_of_birth"))? formatter.parse(jsonNode.get("date_of_birth").asText()) : null;
            farmerDetail.ethnicGroup = (jsonNode.has("ethnic_group"))? jsonNode.get("ethnic_group").asText() : null;
            farmerDetail.yearsOfCornFarming = (jsonNode.has("years_of_corn_farming"))? jsonNode.get("years_of_corn_farming").asInt() : null;
            farmerDetail.isTransmigrated = (jsonNode.has("is_transmigrated"))? jsonNode.get("is_transmigrated").asBoolean() : null;

            if (FarmerDetail.insert(farmerDetail) < 1) {
                result.put("message", "error during insertion");
                return internalServerError(toJson(result));
            } else {
                result.put("message", "detail of farmer " + farmerDetail.farmerId + " updated");
                return ok(toJson(result));
            }
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result updateFarmerDetail () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, FIELD_FARMER_DETAIL);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            int farmerId = jsonNode.get("farmer_id").asInt();

            FarmerDetail farmerDetail = FarmerDetail.selectOne(farmerId);

            if (farmerDetail == null) {
                result.put("message", "farmer detail does not exist");
                return internalServerError(toJson(result));
            }

            farmerDetail.religion = (jsonNode.has("religion"))? jsonNode.get("religion").asText() : farmerDetail.religion;
            farmerDetail.nameOfSpouse = (jsonNode.has("name_of_spouse"))? jsonNode.get("name_of_spouse").asText() : farmerDetail.nameOfSpouse;
            farmerDetail.numOfDependants = (jsonNode.has("number_of_dependants"))? jsonNode.get("number_of_dependants").asInt() : farmerDetail.numOfDependants;
            farmerDetail.numOfChildren = (jsonNode.has("number_of_children"))? jsonNode.get("number_of_children").asInt() : farmerDetail.numOfChildren;
            farmerDetail.residentialStatus = (jsonNode.has("residential_status"))? jsonNode.get("residential_status").asText() : farmerDetail.residentialStatus;
            farmerDetail.education = (jsonNode.has("education"))? jsonNode.get("education").asText() : farmerDetail.education;
            farmerDetail.placeOfBirth = (jsonNode.has("place_of_birth"))? jsonNode.get("place_of_birth").asText() : farmerDetail.placeOfBirth;
            farmerDetail.dateOfBirth = (jsonNode.has("date_of_birth"))? formatter.parse(jsonNode.get("date_of_birth").asText()) : farmerDetail.dateOfBirth;
            farmerDetail.ethnicGroup = (jsonNode.has("ethnic_group"))? jsonNode.get("ethnic_group").asText() : farmerDetail.ethnicGroup;
            farmerDetail.yearsOfCornFarming = (jsonNode.has("years_of_corn_farming"))? jsonNode.get("years_of_corn_farming").asInt() : farmerDetail.yearsOfCornFarming;
            farmerDetail.isTransmigrated = (jsonNode.has("is_transmigrated"))? jsonNode.get("is_transmigrated").asBoolean() : farmerDetail.isTransmigrated;

            if (FarmerDetail.update(farmerDetail) < 1) {
                result.put("message", "error during update");
                return internalServerError(toJson(result));
            } else {
                result.put("message", "detail of farmer " + farmerDetail.farmerId + " updated");
                return ok(toJson(result));
            }
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result deleteFarmerDetail () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, FIELD_FARMER_DETAIL);

            int farmerId = jsonNode.get("farmer_id").asInt();

            if (FarmerDetail.delete(farmerId) < 1) {
                result.put("message", "error occured during deletion");
                return internalServerError(toJson(result));
            } else {
                result.put("message", "detail of farmer " + farmerId + " deleted");
                return ok(toJson(result));
            }
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError(toJson(result));
        }
    }
}
