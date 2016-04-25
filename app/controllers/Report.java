package controllers;

import helpers.fileUtils.StorageUtil;
import play.Logger;
import play.mvc.Controller;
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
 * Created by 1199 on 2/10/2016.
 */
public class Report extends Controller {
    private static final String FILEDIR = "/var/www/field-research/report/";
    private static final String TEMP = "/tmp/";

    public static Result generateFarmerReport (int seasonId) {
        Map<String, Object> result = new HashMap<>(1);
        try {
            String scriptFilePath = FILEDIR + "report.py";
            String reportFilePath = TEMP + "farmer_report.xlsx";

            String command = "python " + scriptFilePath + " " + reportFilePath + " " + seasonId;

            Logger.debug(command);

            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();

            Path path = Paths.get(reportFilePath);
            File file = path.toFile();
            file.renameTo(new File("AnalyticReport.xlsx"));
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
}
