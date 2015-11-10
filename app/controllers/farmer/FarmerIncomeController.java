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
    private static final String[] FIELD_FARMER = {"income_id"};

    public static Result findFarmerIncome (int farmerId) {
        Map<String, Object> result = new HashMap<>();
        try {
            return ok(toJson(FarmerIncome.select(farmerId)));
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

            farmerIncome.incomeId = FarmerIncome.insert(farmerIncome);
            result.put("message", "farmer income inserted");
            result.put("id", farmerIncome.incomeId);
            return ok(toJson(result));
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

            int incomeId = jsonNode.get("income_id").asInt();
            FarmerIncome farmerIncome = FarmerIncome.selectOne(incomeId);

            if (farmerIncome == null) {
                result.put("message", "farmer income does not exist");
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

    public static Result deleteFarmerIncome () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, FIELD_FARMER);

            int incomeId = jsonNode.get("income_id").asInt();

            if (FarmerIncome.delete(incomeId) < 1) {
                result.put("message", "error occured during deleting");
                return internalServerError(toJson(result));
            } else {
                result.put("message", "farmer income " + incomeId + " deleted");
                return ok(toJson(result));
            }
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError(toJson(result));
        }
    }
}
