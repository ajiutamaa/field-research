package controllers.farmer;

import com.fasterxml.jackson.databind.JsonNode;
import helpers.InputValidator;
import helpers.security.Secured;
import models.farmer.FarmerAsset;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.util.HashMap;
import java.util.Map;

import static play.libs.Json.toJson;

/**
 * Created by lenovo on 11/5/2015.
 */

@Security.Authenticated(Secured.class)
public class FarmerAssetController extends Controller {
    private static final String[] FIELD_INSERT_FARMER_ASSET = {"farmer_id", "type", "name", "ownership_status", "quantity"};
    private static final String[] FIELD_FARMER_ASSET = {"asset_id"};

    public static Result findFarmerAsset (int farmerId) {
        Map<String, Object> result = new HashMap<>();
        try {
            return ok(toJson(FarmerAsset.select(farmerId)));
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

            farmerAsset.assetId = FarmerAsset.insert(farmerAsset);
            result.put("message", "farmer asset inserted");
            result.put("id", farmerAsset.assetId);
            return ok(toJson(result));
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

            int assetId = jsonNode.get("asset_id").asInt();
            FarmerAsset farmerAsset = FarmerAsset.selectOne(assetId);

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
                result.put("message", "farmer asset " + farmerAsset.assetId + " updated");
                return ok(toJson(result));
            }
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result deleteFarmerAsset () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, FIELD_FARMER_ASSET);

            int assetId = jsonNode.get("asset_id").asInt();

            if (FarmerAsset.delete(assetId) < 1) {
                result.put("message", "error occured during deleting");
                return internalServerError(toJson(result));
            } else {
                result.put("message", "farmer asset " + assetId + " deleted");
                return ok(toJson(result));
            }
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError(toJson(result));
        }
    }
}
