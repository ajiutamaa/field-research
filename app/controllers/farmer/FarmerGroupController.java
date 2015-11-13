package controllers.farmer;

import com.fasterxml.jackson.databind.JsonNode;
import helpers.InputValidator;
import models.farmer.FarmerGroup;
import models.farmer.FarmerHarvest;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.HashMap;
import java.util.Map;

import static play.libs.Json.toJson;

/**
 * Created by lenovo on 11/5/2015.
 */
public class FarmerGroupController extends Controller {
    public static Result findKkvGroup () {
        Map<String, Object> result = new HashMap<>();
        try {
            return ok(toJson(FarmerGroup.selectKkvGroup()));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError (toJson(result));
        }
    }
    public static Result findClusterGroup () {
        Map<String, Object> result = new HashMap<>();
        try {
            return ok(toJson(FarmerGroup.selectClusterGroup()));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError (toJson(result));
        }
    }
}
