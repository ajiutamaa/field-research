package controllers.farmer;

import com.fasterxml.jackson.databind.JsonNode;
import helpers.InputValidator;
import models.farmer.FarmerAsset;
import models.farmer.FarmerIncome;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.HashMap;
import java.util.Map;

import static play.libs.Json.toJson;

/**
 * Created by lenovo on 11/5/2015.
 */
public class FarmerAssetController extends Controller {
    private static final String[] FIELD_INSERT_FARMER_ASSET = {"farmer_id", "type", "name", "ownership_status", "quantity"};
    private static final String[] FIELD_FARMER_ASSET = {"farmer_id"};

    public static Result findFarmerAsset (int farmerId) {
        Map<String, Object> result = new HashMap<>();
        try {
            return ok(toJson(FarmerAsset.selectOne(farmerId)));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError (toJson(result));
        }
    }

    public static Result insertFarmerAsset () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, FIELD_INSERT_FARMER_ASSET);

            FarmerAsset farmerAsset = new FarmerAsset();
            farmerAsset.farmerId = jsonNode.get("farmer_id").asInt();
            farmerAsset.type = jsonNode.get("type").asText();
            farmerAsset.name = jsonNode.get("name").asText();
            farmerAsset.ownershipStatus = jsonNode.get("ownership_status").asText();
            farmerAsset.quantity = jsonNode.get("quantity").asInt();

            if (FarmerAsset.insert(farmerAsset) < 1) {
                result.put("message", "error during insertion");
                return internalServerError(toJson(result));
            } else {
                result.put("message", "detail of farmer " + farmerAsset.farmerId + " updated");
                return ok(toJson(result));
            }
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result updateFarmerAsset () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, FIELD_FARMER_ASSET);

            int farmerId = jsonNode.get("farmer_id").asInt();
            FarmerAsset farmerAsset = FarmerAsset.selectOne(farmerId);

            if (farmerAsset == null) {
                result.put("message", "farmer field does not exist");
                return badRequest(toJson(result));
            }

            farmerAsset.type = jsonNode.has("type")? jsonNode.get("type").asText() : farmerAsset.type;
            farmerAsset.name = jsonNode.has("name")? jsonNode.get("name").asText() : farmerAsset.name;
            farmerAsset.ownershipStatus = jsonNode.has("ownership_status")? jsonNode.get("ownership_status").asText() : farmerAsset.ownershipStatus;
            farmerAsset.quantity = jsonNode.has("quantity")? jsonNode.get("quantity").asInt() : farmerAsset.quantity;

            if (FarmerAsset.update(farmerAsset) < 1) {
                result.put("message", "error occured during updating");
                return internalServerError(toJson(result));
            } else {
                result.put("message", "farmer " + farmerAsset.farmerId + " updated");
                return ok(toJson(result));
            }
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result deleteFarmer () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, FIELD_FARMER_ASSET);

            int farmerId = jsonNode.get("farmer_id").asInt();

            if (FarmerAsset.delete(farmerId) < 1) {
                result.put("message", "error occured during deleting");
                return internalServerError(toJson(result));
            } else {
                result.put("message", "farmer " + farmerId + " deleted");
                return ok(toJson(result));
            }
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError(toJson(result));
        }
    }
}
