package controllers.farmer;

import com.fasterxml.jackson.databind.JsonNode;
import helpers.InputValidator;
import helpers.security.Secured;
import models.farmer.Farmer;
import models.field_visit.FarmerFieldVisit;
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
public class FarmerController extends Controller {
    private static final String[] FIELD_INSERT_FARMER = {"farmer_name", "cluster_group_id", "kkv_group_id", "desa_id", "observation_date"};
    private static final String[] FIELD_FARMER = {"farmer_id"};

    public static Result findFarmer () {
        Map<String, Object> result = new HashMap<>();
        try {
            return ok(toJson(Farmer.select()));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError (toJson(result));
        }
    }

    public static Result findUnssignedFarmer (int seasonId) {
        Map<String, Object> result = new HashMap<>();
        try {
            return ok(toJson(FarmerFieldVisit.selectUnassignedFarmer(seasonId)));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError (toJson(result));
        }
    }

    public static Result insertFarmer () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, FIELD_INSERT_FARMER);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            Farmer farmer = new Farmer();
            farmer.farmerName = jsonNode.get("farmer_name").asText();
            farmer.clusterGroupId = jsonNode.get("cluster_group_id").asInt();
            farmer.kkvGroupId = jsonNode.get("kkv_group_id").asInt();
            farmer.desaId = jsonNode.get("desa_id").asInt();
            farmer.observationDate = formatter.parse(jsonNode.get("observation_date").asText());

            int farmerId = Farmer.insert(farmer);

            result.put("new_farmer_id", farmerId);
            result.put("message", "new farmer inserted");
            return ok(toJson(result));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result updateFarmer () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, FIELD_FARMER);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            int farmerId = jsonNode.get("farmer_id").asInt();
            Farmer farmer = Farmer.selectOne(farmerId);

            if (farmer == null) {
                result.put("message", "farmer does not exist");
                return badRequest(toJson(result));
            }

            farmer.farmerName = (jsonNode.has("farmer_name"))? jsonNode.get("farmer_name").asText() : farmer.farmerName;
            farmer.clusterGroupId = (jsonNode.has("cluster_group_id"))? jsonNode.get("cluster_group_id").asInt() : farmer.clusterGroupId;
            farmer.kkvGroupId = (jsonNode.has("kkv_group_id"))? jsonNode.get("kkv_group_id").asInt() : farmer.kkvGroupId;
            farmer.desaId = (jsonNode.has("desa_id"))? jsonNode.get("desa_id").asInt() : farmer.desaId;
            farmer.observationDate = (jsonNode.has("observation_date"))? formatter.parse(jsonNode.get("observation_date").asText()): farmer.observationDate;

            if (Farmer.update(farmer) < 1) {
                result.put("message", "error occured during updating");
                return internalServerError(toJson(result));
            } else {
                result.put("message", "farmer " + farmer.farmerId + " updated");
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
            inputValidator.checkInputFields(jsonNode, FIELD_FARMER);

            int farmerId = jsonNode.get("farmer_id").asInt();

            if (Farmer.delete(farmerId) < 1) {
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
