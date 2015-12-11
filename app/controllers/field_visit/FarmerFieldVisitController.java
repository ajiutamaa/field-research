package controllers.field_visit;

import com.fasterxml.jackson.databind.JsonNode;
import helpers.InputValidator;
import helpers.security.Secured;
import models.field_visit.*;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static play.libs.Json.toJson;
import play.Logger;
import play.mvc.Security;

/**
 * Created by lenovo on 11/5/2015.
 */

@Security.Authenticated(Secured.class)
public class FarmerFieldVisitController extends Controller {
    private static final String[] INSERT_FARMER_FIELD_VISIT = {"farmer_id"};
    private static final String[] INSERT_PLANT_STAGE_START = {"farmer_field_visit_id", "plant_stage_type", "start_date"};
    private static final String[] INSERT_PLANT_STAGE_END = {"farmer_field_visit_id", "plant_stage_type", "end_date"};
    private static final String[] INSERT_WEEKLY_REPORT = {"farmer_field_visit_id", "observation_date"};
    private static final String[] INSERT_WEEKLY_SAMPLE = {"weekly_visit_id", "height", "number_of_leaf"};

    private static final String[] UPDATE_PLANT_STAGE_START = {"farmer_field_visit_id", "plant_stage_type"};
    private static final String[] UPDATE_PLANT_STAGE_END = {"farmer_field_visit_id", "plant_stage_type"};
    private static final String[] UPDATE_WEEKLY_REPORT = {"weekly_visit_id"};
    private static final String[] UPDATE_WEEKLY_SAMPLE = {"weekly_plant_sample_id"};
    private static final String[] UPDATE_FIELD_POLYGON = {"farmer_id", "polygon"};
    private static final String[] POLYGON_FIELD = {"longitude", "latitude"};

    private static final String[] DELETE_FARMER_FIELD_VISIT = {"farmer_id"};

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
            return ok(toJson(WeeklyVisitReport.select(-1, farmerFieldVisitId)));
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
            weeklyVisitReport.notes = jsonNode.has("notes")? jsonNode.get("notes").asText() : null;

            int weeklyVisitReportId = WeeklyVisitReport.insert(weeklyVisitReport);

            result.put("id", weeklyVisitReportId);
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
            weeklyPlantSample.pestScore = (jsonNode.has("pest_score"))? jsonNode.get("pest_score").asDouble() : null;
            weeklyPlantSample.desease = (jsonNode.has("desease"))? jsonNode.get("desease").asText() : null;
            weeklyPlantSample.deseaseScore = (jsonNode.has("desease_score"))? jsonNode.get("desease_score").asDouble() : null;
            weeklyPlantSample.leafP1 = jsonNode.has("leaf_P1")? jsonNode.get("leaf_P1").asDouble() : null;
            weeklyPlantSample.leafP2 = jsonNode.has("leaf_P2")? jsonNode.get("leaf_P2").asDouble() : null;
            weeklyPlantSample.leafL = jsonNode.has("leaf_L")? jsonNode.get("leaf_L").asDouble() : null;

            int weeklyPlantSampleId = WeeklyPlantSample.insert(weeklyPlantSample);

            result.put("id", weeklyPlantSampleId);
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

            List<PlantStageStart> plantStageStarts = PlantStageStart.select(plantStageType, farmerFieldVisitId);
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

            List<PlantStageEnd> plantStageEnds = PlantStageEnd.select(plantStageType, farmerFieldVisitId);
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

    public static Result updateFieldPolygon () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, UPDATE_FIELD_POLYGON);

            int farmerId = jsonNode.get("farmer_id").asInt();

            ArrayList<F.Tuple<Double, Double>> longLatList = new ArrayList();

            for (JsonNode node : jsonNode.get("polygon")) {
                inputValidator.checkInputFields(node, POLYGON_FIELD);
                double longitude = node.get("longitude").asDouble();
                double latitude = node.get("latitude").asDouble();
                longLatList.add(new F.Tuple<>(longitude, latitude));
            }

            if (FarmerFieldVisit.insertPolygon(farmerId, longLatList) < 1) {
                result.put("message", "error updating");
                return internalServerError(toJson(result));
            } else {
                result.put("message", "field polygon updated");
                return ok(toJson(result));
            }
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result updateWeeklyVisitReport () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, UPDATE_WEEKLY_REPORT);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            int weeklyVisitId = jsonNode.get("weekly_visit_id").asInt();

            List<WeeklyVisitReport> reports = WeeklyVisitReport.select(weeklyVisitId, -1);
            if (reports.isEmpty()) {
                result.put("message", "weekly visit report does not exist");
                return badRequest(toJson(result));
            }

            WeeklyVisitReport report = reports.get(0);
            report.observationDate = jsonNode.has("observation_date")? formatter.parse(jsonNode.get("observation_date").asText()) : report.observationDate;

            if (WeeklyVisitReport.update(report) < 1) {
                result.put("message", "error updating");
                return internalServerError(toJson(result));
            }

            result.put("message", "weekly visit report updated");
            return ok(toJson(result));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result updateWeeklyPlantSample () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, UPDATE_WEEKLY_SAMPLE);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            int weeklyPlantSampleId = jsonNode.get("weekly_plant_sample_id").asInt();

            List<WeeklyPlantSample> samples = WeeklyPlantSample.select(weeklyPlantSampleId, -1);
            if (samples.isEmpty()) {
                result.put("message", "weekly plant sample does not exist");
                return badRequest(toJson(result));
            }

            WeeklyPlantSample sample = samples.get(0);
            sample.weeklyVisitId = jsonNode.has("weekly_visit_id")? jsonNode.get("weekly_visit_id").asInt() : sample.weeklyVisitId;
            sample.height = jsonNode.has("height")? jsonNode.get("height").asDouble() : sample.height;
            sample.numberOfLeaf = jsonNode.has("number_of_leaf")? jsonNode.get("number_of_leaf").asInt() : sample.numberOfLeaf;
            sample.pest = (jsonNode.has("pest"))? jsonNode.get("pest").asText() : sample.pest;
            sample.pestScore = jsonNode.has("pest") ? jsonNode.get("pest_score").asDouble() : sample.pestScore;
            sample.desease = (jsonNode.has("desease"))? jsonNode.get("desease").asText() : sample.desease;
            sample.deseaseScore = jsonNode.has("desease")? jsonNode.get("desease_score").asDouble() : sample.deseaseScore;
            sample.leafP1 = jsonNode.has("leaf_P1")? jsonNode.get("leaf_P1").asDouble() : sample.leafP1;
            sample.leafP2 = jsonNode.has("leaf_P2")? jsonNode.get("leaf_P2").asDouble() : sample.leafP2;
            sample.leafL = jsonNode.has("leafL")? jsonNode.get("leaf_L").asDouble() : sample.leafL;

            if (WeeklyPlantSample.update(sample) < 1) {
                result.put("message", "error updating");
                return internalServerError(toJson(result));
            }

            result.put("message", "weekly plant sample updated");
            return ok(toJson(result));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result deleteFarmerFieldVisit () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, DELETE_FARMER_FIELD_VISIT);

            int farmerId = jsonNode.get("farmer_id").asInt();

            if (FarmerFieldVisit.delete(farmerId) < 1) {
                result.put("message", "error during deletion");
                return internalServerError(toJson(result));
            }

            result.put("message", "farmer field visit deleted");

            return ok(toJson(result));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result deletePlantStageStart () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, UPDATE_PLANT_STAGE_START);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            int farmerFieldVisitId = jsonNode.get("farmer_field_visit_id").asInt();
            String plantStageType = jsonNode.get("plant_stage_type").asText().toLowerCase();

            List<PlantStageStart> plantStageStarts = PlantStageStart.select(plantStageType, farmerFieldVisitId);
            if (plantStageStarts.isEmpty()) {
                result.put("message", "plant stage start does not exist");
                return badRequest(toJson(result));
            }
            PlantStageStart plantStageStart = plantStageStarts.get(0);

            if (PlantStageStart.delete(plantStageStart) < 1) {
                result.put("message", "error deleting");
                return internalServerError(toJson(result));
            }

            result.put("message", "field stage start " + plantStageStart.plantStageType + " deleted");
            return ok(toJson(result));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result deletePlantStageEnd () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, UPDATE_PLANT_STAGE_END);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            int farmerFieldVisitId = jsonNode.get("farmer_field_visit_id").asInt();
            String plantStageType = jsonNode.get("plant_stage_type").asText().toLowerCase();

            List<PlantStageEnd> plantStageEnds = PlantStageEnd.select(plantStageType, farmerFieldVisitId);
            if (plantStageEnds.isEmpty()) {
                result.put("message", "plant stage start does not exist");
                return badRequest(toJson(result));
            }
            PlantStageEnd plantStageEnd = plantStageEnds.get(0);

            if (PlantStageEnd.delete(plantStageEnd) < 1) {
                result.put("message", "error deleting");
                return internalServerError(toJson(result));
            }

            result.put("message", "field stage end " + plantStageEnd.plantStageType + " deleted");
            return ok(toJson(result));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result deleteWeeklyVisitReport () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, UPDATE_WEEKLY_REPORT);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            int weeklyVisitId = jsonNode.get("weekly_visit_id").asInt();

            if (WeeklyVisitReport.delete(weeklyVisitId) < 1) {
                result.put("message", "error deleting");
                return internalServerError(toJson(result));
            }

            result.put("message", "weekly visit report deleted");
            return ok(toJson(result));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result deleteWeeklyPlantSample () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, UPDATE_WEEKLY_SAMPLE);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            int weeklyPlantSampleId = jsonNode.get("weekly_plant_sample_id").asInt();

            if (WeeklyPlantSample.delete(weeklyPlantSampleId) < 1) {
                result.put("message", "error deleting");
                return internalServerError(toJson(result));
            }

            result.put("message", "weekly plant sample deleted");
            return ok(toJson(result));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }
}
