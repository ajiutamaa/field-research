package controllers.farmer;

import com.fasterxml.jackson.databind.JsonNode;
import helpers.InputValidator;
import models.farmer.FarmerField;
import models.farmer.FarmerIncome;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.HashMap;
import java.util.Map;

import static play.libs.Json.toJson;

/**
 * Created by lenovo on 11/5/2015.
 */
public class FarmerIncomeController extends Controller {
    private static final String[] FIELD_INSERT_FARMER_INCOME = {"farmer_id", "source", "amount"};
    private static final String[] FIELD_FARMER = {"farmer_id"};

    public static Result findFarmerIncome (int farmerId) {
        Map<String, Object> result = new HashMap<>();
        try {
            return ok(toJson(FarmerIncome.selectOne(farmerId)));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError (toJson(result));
        }
    }

    public static Result insertFarmerIncome () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, FIELD_INSERT_FARMER_INCOME);

            FarmerIncome farmerIncome = new FarmerIncome();
            farmerIncome.farmerId = jsonNode.get("farmer_id").asInt();
            farmerIncome.source = jsonNode.get("source").asText();
            farmerIncome.amount = jsonNode.get("amount").asDouble();

            if (FarmerIncome.insert(farmerIncome) < 1) {
                result.put("message", "error during insertion");
                return internalServerError(toJson(result));
            } else {
                result.put("message", "detail of farmer " + farmerIncome.farmerId + " updated");
                return ok(toJson(result));
            }
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result updateFarmerIncome () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, FIELD_FARMER);

            int farmerId = jsonNode.get("farmer_id").asInt();
            FarmerIncome farmerIncome = FarmerIncome.selectOne(farmerId);

            if (farmerIncome == null) {
                result.put("message", "farmer field does not exist");
                return badRequest(toJson(result));
            }

            farmerIncome.source = (jsonNode.has("source"))? jsonNode.get("source").asText() : farmerIncome.source;
            farmerIncome.amount = (jsonNode.has("amount"))? jsonNode.get("amount").asDouble() : farmerIncome.amount;


            if (FarmerIncome.update(farmerIncome) < 1) {
                result.put("message", "error occured during updating");
                return internalServerError(toJson(result));
            } else {
                result.put("message", "farmer " + farmerIncome.farmerId + " updated");
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

            if (FarmerIncome.delete(farmerId) < 1) {
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
