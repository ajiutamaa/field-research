package controllers.farmer;

import com.fasterxml.jackson.databind.JsonNode;
import helpers.InputValidator;
import models.farmer.FarmerOrganizationExperience;
import models.farmer.FarmerWorkshopExperience;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.HashMap;
import java.util.Map;

import static play.libs.Json.toJson;

/**
 * Created by lenovo on 11/5/2015.
 */
public class FarmerOrganizationController extends Controller {
    private static final String[] FIELD_INSERT_FARMER_ORG = {"farmer_id", "type", "name", "position", "year_joined", "description"};
    private static final String[] FIELD_FARMER_ORG = {"organization_id"};

    public static Result findFarmerOrganization (int farmerId) {
        Map<String, Object> result = new HashMap<>();
        try {
            return ok(toJson(FarmerOrganizationExperience.select(farmerId)));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError (toJson(result));
        }
    }

    public static Result insertFarmerOrganization () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, FIELD_INSERT_FARMER_ORG);

            FarmerOrganizationExperience farmerOrganization = new FarmerOrganizationExperience();
            farmerOrganization.farmerId = jsonNode.get("farmer_id").asInt();
            farmerOrganization.type = jsonNode.get("type").asText();
            farmerOrganization.name = jsonNode.get("name").asText();
            farmerOrganization.position = jsonNode.get("position").asText();
            farmerOrganization.yearJoined = jsonNode.get("year_joined").asInt();
            farmerOrganization.description = jsonNode.get("description").asText();

            farmerOrganization.organizationId = FarmerOrganizationExperience.insert(farmerOrganization);
            result.put("message", "farmer organization inserted");
            result.put("id", farmerOrganization.organizationId);
            return ok(toJson(result));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result updateFarmerOrganization () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, FIELD_FARMER_ORG);

            int organizationId = jsonNode.get("organization_id").asInt();
            FarmerOrganizationExperience farmerOrganization = FarmerOrganizationExperience.selectOne(organizationId);

            if (farmerOrganization == null) {
                result.put("message", "farmer organization does not exist");
                return badRequest(toJson(result));
            }

            farmerOrganization.type = jsonNode.get("type").asText();
            farmerOrganization.name = jsonNode.get("name").asText();
            farmerOrganization.position = jsonNode.get("position").asText();
            farmerOrganization.yearJoined = jsonNode.get("year_joined").asInt();
            farmerOrganization.description = jsonNode.get("description").asText();

            farmerOrganization.type = jsonNode.has("type")? jsonNode.get("type").asText() : farmerOrganization.type;
            farmerOrganization.name = jsonNode.has("name")? jsonNode.get("name").asText() : farmerOrganization.name;
            farmerOrganization.position = jsonNode.has("position")? jsonNode.get("position").asText() : farmerOrganization.position;
            farmerOrganization.yearJoined = jsonNode.has("year_joined")? jsonNode.get("year_joined").asInt() : farmerOrganization.yearJoined;
            farmerOrganization.description = jsonNode.has("description")? jsonNode.get("description").asText() : farmerOrganization.description;

            if (FarmerOrganizationExperience.update(farmerOrganization) < 1) {
                result.put("message", "error occured during updating");
                return internalServerError(toJson(result));
            } else {
                result.put("message", "farmer " + farmerOrganization.farmerId + " updated");
                return ok(toJson(result));
            }
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result deleteFarmerOrganization () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, FIELD_FARMER_ORG);

            int organizationId = jsonNode.get("organization_id").asInt();

            if (FarmerOrganizationExperience.delete(organizationId) < 1) {
                result.put("message", "error occured during deleting");
                return internalServerError(toJson(result));
            } else {
                result.put("message", "farmer organization " + organizationId + " deleted");
                return ok(toJson(result));
            }
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError(toJson(result));
        }
    }
}
