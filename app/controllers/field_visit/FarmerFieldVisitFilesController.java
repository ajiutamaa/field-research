package controllers.field_visit;

import com.fasterxml.jackson.databind.JsonNode;
import helpers.InputValidator;
import helpers.fileUtils.StorageUtil;
import models.field_visit.FarmerFieldVisitFile;
import models.field_visit.WeeklyVisitReport;
import play.Logger;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import static play.libs.Json.toJson;
/**
 * Created by lenovo on 07/12/2015.
 */
public class FarmerFieldVisitFilesController extends Controller {
    private static final String[] FIELD_DELETE_FARMER_FILE = {"weekly_visit_id"};

    public static Result findFilesPath (int weeklyVisitId, String description) {
        Map<String, Object> result = new HashMap<>(5);
        try {
            result.put("farmer_id", weeklyVisitId);
            for (FarmerFieldVisitFile file : FarmerFieldVisitFile.select(weeklyVisitId, description)) {
                Map<String, Object> fileInfo = new HashMap<>(5);
                fileInfo.put("file_id", file.id);
                fileInfo.put("path", file.path);
                fileInfo.put("status", file.status);
                fileInfo.put("created_at", file.createdAt);
                fileInfo.put("updated_at", file.updatedAt);
                result.put(file.description, fileInfo);
            }
            return ok(toJson(result));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError(toJson(result));
        }
    }

//    @BodyParser.Of(value = BodyParser.MultipartFormData.class, maxLength = 10 * 1024 * 1024)
    public static Result uploadFarmerFile (int weeklyVisitId, String fileDescription) {
        Map<String, Object> result = new HashMap<>(2);
        try {
            WeeklyVisitReport report = null;
            try {
                report = WeeklyVisitReport.select(weeklyVisitId, -1).get(0);
            } catch (Exception e) {
                throw new Exception("Weekly visit does not exist");
            }

            Http.MultipartFormData body = request().body().asMultipartFormData();
            Http.MultipartFormData.FilePart filepart = body.getFile("image");
            File imgFile = filepart.getFile();
            String fileName = filepart.getFilename();

            String fileUrl = FarmerFieldVisitFile.insertOrUpdate(weeklyVisitId, fileDescription, imgFile, fileName);

            result.put("message", "file uploaded");
            result.put("url", fileUrl);
            return ok(toJson(result));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError(toJson(result));
        }
    }

    public static Result sendFarmerFile (String encodedFarmerId, String encodedFileDescription, String fileName) {
        Map<String, Object> result = new HashMap<>(1);
        try {
            String filePath = StorageUtil.rootDir + "/" + encodedFarmerId + "/" + encodedFileDescription + "/" + fileName;
            Path path = Paths.get(filePath);
            File file = path.toFile();
            if (!file.exists()) {
                throw new IOException("file does not exist");
            }
            response().setContentType(Files.probeContentType(path));
            byte [] fileBinaries = StorageUtil.readFileBinaries(file);
            return ok(fileBinaries);
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError(toJson(result));
        }
    }

    public static Result deleteFarmerFile () {
        Map<String, Object> result = new HashMap<>(1);
        try {
            JsonNode jsonNode = Controller.request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, FIELD_DELETE_FARMER_FILE);

            int farmerId = jsonNode.get("weekly_visit_id").asInt();
            String fileDescription = jsonNode.has("file_description") ? jsonNode.get("file_description").asText() : null;

            int deleted = FarmerFieldVisitFile.delete(farmerId, fileDescription);

            result.put("message", deleted + " farmer files deleted");
            return ok(toJson(result));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError(toJson(result));
        }
    }
}
