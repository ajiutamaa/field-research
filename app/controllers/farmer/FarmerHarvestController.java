package controllers.farmer;

import com.fasterxml.jackson.databind.JsonNode;
import helpers.InputValidator;
import models.farmer.FarmerHarvest;
import models.farmer.FarmerWorkshopExperience;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.HashMap;
import java.util.Map;

import static play.libs.Json.toJson;

/**
 * Created by lenovo on 11/5/2015.
 */
public class FarmerHarvestController extends Controller {
    private static final String[] FIELD_INSERT_FARMER_HARVEST = {"farmer_id", "year", "season", "crop", "field_area", "yield"};
    private static final String[] FIELD_FARMER_HARVEST = {"harvest_id"};

    public static Result findFarmerHarvest (int farmerId) {
        Map<String, Object> result = new HashMap<>();
        try {
            return ok(toJson(FarmerHarvest.select(farmerId)));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError (toJson(result));
        }
    }

    public static Result insertFarmerHarvest () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, FIELD_INSERT_FARMER_HARVEST);

            FarmerHarvest farmerHarvest = new FarmerHarvest();
            farmerHarvest.farmerId = jsonNode.get("farmer_id").asInt();
            farmerHarvest.year = jsonNode.get("year").asInt();
            farmerHarvest.season = jsonNode.get("season").asText();
            farmerHarvest.crop = jsonNode.get("crop").asText();
            farmerHarvest.fieldArea = jsonNode.get("field_area").asDouble();
            farmerHarvest.yield = jsonNode.get("yield").asDouble();

            farmerHarvest.harvestId = FarmerHarvest.insert(farmerHarvest);
            result.put("message", "farmer harvest inserted");
            result.put("id", farmerHarvest.harvestId);
            return ok(toJson(result));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result updateFarmerHarvest () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, FIELD_FARMER_HARVEST);

            int harvestId = jsonNode.get("harvest_id").asInt();
            FarmerHarvest farmerHarvest = FarmerHarvest.selectOne(harvestId);

            if (farmerHarvest == null) {
                result.put("message", "farmer field does not exist");
                return badRequest(toJson(result));
            }

            farmerHarvest.year = jsonNode.has("year")? jsonNode.get("year").asInt() : farmerHarvest.year;
            farmerHarvest.season = jsonNode.has("season")? jsonNode.get("season").asText() : farmerHarvest.season;
            farmerHarvest.crop = jsonNode.has("crop")? jsonNode.get("crop").asText() : farmerHarvest.crop;
            farmerHarvest.fieldArea = jsonNode.has("field_area")? jsonNode.get("field_area").asDouble() : farmerHarvest.fieldArea;
            farmerHarvest.yield = jsonNode.has("yield")? jsonNode.get("yield").asDouble() : farmerHarvest.yield;

            if (FarmerHarvest.update(farmerHarvest) < 1) {
                result.put("message", "error occured during updating");
                return internalServerError(toJson(result));
            } else {
                result.put("message", "farmer " + farmerHarvest.farmerId + " updated");
                return ok(toJson(result));
            }
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result deleteFarmerHarvest () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, FIELD_FARMER_HARVEST);

            int harvestId = jsonNode.get("harvest_id").asInt();

            if (FarmerHarvest.delete(harvestId) < 1) {
                result.put("message", "error occured during deleting");
                return internalServerError(toJson(result));
            } else {
                result.put("message", "farmer harvest " + harvestId + " deleted");
                return ok(toJson(result));
            }
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError(toJson(result));
        }
    }
}
