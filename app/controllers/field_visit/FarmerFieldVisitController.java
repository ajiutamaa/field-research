package controllers.field_visit;

import com.fasterxml.jackson.databind.JsonNode;
import helpers.InputValidator;
import models.field_visit.*;
import play.mvc.Controller;
import play.mvc.Result;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static play.libs.Json.toJson;


/**
 * Created by lenovo on 11/5/2015.
 */
public class FarmerFieldVisitController extends Controller {
    private static final String[] INSERT_FARMER_FIELD_VISIT = {"farmer_id"};
    private static final String[] INSERT_PLANT_STAGE_START = {"farmer_field_visit_id", "plant_stage_type", "start_date"};
    private static final String[] INSERT_PLANT_STAGE_END = {"farmer_field_visit_id", "plant_stage_type", "end_date"};
    private static final String[] INSERT_WEEKLY_REPORT = {"farmer_field_visit_id", "observation_date"};
    private static final String[] INSERT_WEEKLY_SAMPLE = {"weekly_visit_id", "height", "number_of_leaf", "leaf_P1", "leaf_P2", "leaf_L"};

    private static final String[] UPDATE_PLANT_STAGE_START = {"farmer_field_visit_id", "plant_stage_type"};
    private static final String[] UPDATE_PLANT_STAGE_END = {"farmer_field_visit_id", "plant_stage_type"};

    public static Result findFarmerFieldVisit () {
        Map<String, Object> result = new HashMap<>();
        try {
            return ok(toJson(FarmerFieldVisit.select()));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError (toJson(result));
        }
    }

    public static Result findFarmerFieldVisitDetail (int farmerFieldVisitId) {
        Map<String, Object> result = new HashMap<>();
        try {
            return ok(toJson(FarmerFieldVisit.selectDetail(farmerFieldVisitId)));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError (toJson(result));
        }
    }

    public static Result findWeeklyVisit (int farmerFieldVisitId) {
        Map<String, Object> result = new HashMap<>();
        try {
            return ok(toJson(WeeklyVisitReport.select(farmerFieldVisitId)));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError (toJson(result));
        }
    }

    public static Result insertFarmerFieldVisit () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, INSERT_FARMER_FIELD_VISIT);

            int farmerId = jsonNode.get("farmer_id").asInt();

            int farmerFieldVisitId = FarmerFieldVisit.insert(farmerId);

            result.put("id", farmerFieldVisitId);
            result.put("message", "farmer field visit inserted");

            return ok(toJson(result));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result insertPlantStageStart () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, INSERT_PLANT_STAGE_START);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");


            PlantStageStart plantStageStart = new PlantStageStart();
            plantStageStart.farmerFieldVisitId = jsonNode.get("farmer_field_visit_id").asInt();
            plantStageStart.plantStageType = jsonNode.get("plant_stage_type").asText();
            plantStageStart.startDate = formatter.parse(jsonNode.get("start_date").asText());

            PlantStageStart.insert(plantStageStart);

            result.put("message", "field stage start " + plantStageStart.plantStageType + " inserted");
            return ok(toJson(result));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result insertPlantStageEnd () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, INSERT_PLANT_STAGE_END);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");


            PlantStageEnd plantStageEnd = new PlantStageEnd();
            plantStageEnd.farmerFieldVisitId = jsonNode.get("farmer_field_visit_id").asInt();
            plantStageEnd.plantStageType = jsonNode.get("plant_stage_type").asText();
            plantStageEnd.endDate = formatter.parse(jsonNode.get("end_date").asText());

            PlantStageEnd.insert(plantStageEnd);

            result.put("message", "field stage end " + plantStageEnd.plantStageType + " inserted");
            return ok(toJson(result));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result insertWeeklyVisitReport () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, INSERT_WEEKLY_REPORT);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");


            WeeklyVisitReport weeklyVisitReport = new WeeklyVisitReport();
            weeklyVisitReport.farmerFieldVisitId = jsonNode.get("farmer_field_visit_id").asInt();
            weeklyVisitReport.observationDate = formatter.parse(jsonNode.get("observation_date").asText());

            WeeklyVisitReport.insert(weeklyVisitReport);

            result.put("message", "weekly visit report inserted");
            return ok(toJson(result));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result insertWeeklyPlantSample () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, INSERT_WEEKLY_SAMPLE);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            WeeklyPlantSample weeklyPlantSample = new WeeklyPlantSample();
            weeklyPlantSample.weeklyVisitId = jsonNode.get("weekly_visit_id").asInt();
            weeklyPlantSample.height = jsonNode.get("height").asDouble();
            weeklyPlantSample.numberOfLeaf = jsonNode.get("number_of_leaf").asInt();
            weeklyPlantSample.pest = (jsonNode.has("pest"))? jsonNode.get("pest").asText() : null;
            weeklyPlantSample.pestScore = (jsonNode.has("pest") && jsonNode.has("pest_score"))? jsonNode.get("pest_score").asDouble() : null;
            weeklyPlantSample.desease = (jsonNode.has("desease"))? jsonNode.get("desease").asText() : null;
            weeklyPlantSample.deseaseScore = (jsonNode.has("desease_score") && jsonNode.has("desease_score"))? jsonNode.get("desease_score").asDouble() : null;
            weeklyPlantSample.leafP1 = jsonNode.get("leaf_P1").asDouble();
            weeklyPlantSample.leafP2 = jsonNode.get("leaf_P2").asDouble();
            weeklyPlantSample.leafL = jsonNode.get("leaf_L").asDouble();

            int weeklyPlantSampleId = WeeklyPlantSample.insert(weeklyPlantSample);

            result.put("id", weeklyPlantSample);
            result.put("message", "weekly plant sample inserted");
            return ok(toJson(result));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result updatePlantStageStart () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, UPDATE_PLANT_STAGE_START);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            int farmerFieldVisitId = jsonNode.get("farmer_field_visit_id").asInt();
            String plantStageType = jsonNode.get("plant_stage_type").asText().toLowerCase();

            List<PlantStageStart> plantStageStarts = PlantStageStart.select(farmerFieldVisitId);
            if (plantStageStarts.isEmpty()) {
                result.put("message", "plant stage start does not exist");
                return badRequest(toJson(result));
            }
            PlantStageStart plantStageStart = plantStageStarts.get(0);
            plantStageStart.startDate = jsonNode.has("start_date")? formatter.parse(jsonNode.get("start_date").asText()) : plantStageStart.startDate;

            if (PlantStageStart.update(plantStageStart) < 1) {
                result.put("message", "error updating");
                return internalServerError(toJson(result));
            }

            result.put("message", "field stage start " + plantStageStart.plantStageType + " updated");
            return ok(toJson(result));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result updatePlantStageEnd () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, UPDATE_PLANT_STAGE_END);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            int farmerFieldVisitId = jsonNode.get("farmer_field_visit_id").asInt();
            String plantStageType = jsonNode.get("plant_stage_type").asText().toLowerCase();

            List<PlantStageEnd> plantStageEnds = PlantStageEnd.select(farmerFieldVisitId);
            if (plantStageEnds.isEmpty()) {
                result.put("message", "plant stage start does not exist");
                return badRequest(toJson(result));
            }
            PlantStageEnd plantStageEnd = plantStageEnds.get(0);
            plantStageEnd.endDate = jsonNode.has("end_date")? formatter.parse(jsonNode.get("end_date").asText()) : plantStageEnd.endDate;

            if (PlantStageEnd.update(plantStageEnd) < 1) {
                result.put("message", "error updating");
                return internalServerError(toJson(result));
            }

            result.put("message", "field stage end " + plantStageEnd.plantStageType + " updated");
            return ok(toJson(result));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }
}
