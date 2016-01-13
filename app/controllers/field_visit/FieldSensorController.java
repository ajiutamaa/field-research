package controllers.field_visit;

import models.field_visit.FieldSensorRecord;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static play.libs.Json.toJson;

/**
 * Created by 1199 on 1/5/2016.
 */
public class FieldSensorController extends Controller {
    public static Result uploadSensorData () {
        Map<String, Object> result = new HashMap<>(2);
        try {
            Http.MultipartFormData body = request().body().asMultipartFormData();

            int farmerFieldVisitId = Integer.parseInt(body.asFormUrlEncoded().get("farmer_field_visit_id")[0]);

            Http.MultipartFormData.FilePart filepart = body.getFile("sensor_data");
            File file = filepart.getFile();

            ArrayList<FieldSensorRecord> recordList = new ArrayList<FieldSensorRecord>();
            try(BufferedReader br = new BufferedReader(new FileReader(file))) {
                br.readLine();
                for(String line; (line = br.readLine()) != null; ) {
                    String csLine = line.split(";;")[0];
                    FieldSensorRecord record = FieldSensorRecord.recordStringParser(farmerFieldVisitId, csLine);
                    recordList.add(record);
                }
            }

            FieldSensorRecord.sensorRecordInsert(recordList);

            result.put("message", recordList.size() + " sensor data records successfully inserted");
            return ok(toJson(result));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError(toJson(result));
        }
    }
}
