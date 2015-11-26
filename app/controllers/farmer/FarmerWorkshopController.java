package controllers.farmer;

import com.fasterxml.jackson.databind.JsonNode;
import helpers.InputValidator;
import helpers.security.Secured;
import models.farmer.FarmerWorkshopExperience;
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
public class FarmerWorkshopController extends Controller {
    private static final String[] FIELD_INSERT_FARMER_WORKSHOP = {"farmer_id", "type", "organizer", "year", "description"};
    private static final String[] FIELD_FARMER_WORKSHOP = {"workshop_id"};

    public static Result findFarmerWorkshop (int farmerId) {
        Map<String, Object> result = new HashMap<>();
        try {
            return ok(toJson(FarmerWorkshopExperience.select(farmerId)));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError (toJson(result));
        }
    }

    public static Result insertFarmerWorkshop () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, FIELD_INSERT_FARMER_WORKSHOP);

            FarmerWorkshopExperience farmerWorkshop = new FarmerWorkshopExperience();
            farmerWorkshop.farmerId = jsonNode.get("farmer_id").asInt();
            farmerWorkshop.type = jsonNode.get("type").asText();
            farmerWorkshop.organizer = jsonNode.get("organizer").asText();
            farmerWorkshop.year = jsonNode.get("year").asInt();
            farmerWorkshop.description = jsonNode.get("description").asText();

            farmerWorkshop.workshopId = FarmerWorkshopExperience.insert(farmerWorkshop);
            result.put("message", "farmer workshop inserted");
            result.put("id", farmerWorkshop.workshopId);
            return ok(toJson(result));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result updateFarmerWorkshop () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, FIELD_FARMER_WORKSHOP);

            int workshopId = jsonNode.get("workshop_id").asInt();
            FarmerWorkshopExperience farmerWorkshop = FarmerWorkshopExperience.selectOne(workshopId);

            if (farmerWorkshop == null) {
                result.put("message", "farmer field does not exist");
                return badRequest(toJson(result));
            }

            farmerWorkshop.type = jsonNode.has("type")? jsonNode.get("type").asText() : farmerWorkshop.type;
            farmerWorkshop.organizer = jsonNode.has("organizer")? jsonNode.get("organizer").asText() : farmerWorkshop.organizer;
            farmerWorkshop.year = jsonNode.has("year")? jsonNode.get("year").asInt() : farmerWorkshop.year;
            farmerWorkshop.description = jsonNode.has("description")? jsonNode.get("description").asText() : farmerWorkshop.description;

            if (FarmerWorkshopExperience.update(farmerWorkshop) < 1) {
                result.put("message", "error occured during updating");
                return internalServerError(toJson(result));
            } else {
                result.put("message", "farmer " + farmerWorkshop.farmerId + " updated");
                return ok(toJson(result));
            }
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result deleteFarmerWorkshop () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, FIELD_FARMER_WORKSHOP);

            int workshopId = jsonNode.get("workshop_id").asInt();

            if (FarmerWorkshopExperience.delete(workshopId) < 1) {
                result.put("message", "error occured during deleting");
                return internalServerError(toJson(result));
            } else {
                result.put("message", "farmer workshop " + workshopId + " deleted");
                return ok(toJson(result));
            }
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError(toJson(result));
        }
    }
}
