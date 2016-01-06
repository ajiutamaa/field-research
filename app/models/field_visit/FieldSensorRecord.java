package models.field_visit;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.DB;
import org.sql2o.Connection;
import play.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 1199 on 1/5/2016.
 */
public class FieldSensorRecord {
    public static final int RAINFALL_IDX = 0;
    public static final int HUMIDITY_IDX = 1;
    public static final int TEMPERATURE_IDX = 2;
    public static final int LIGHT_INTENSITY_IDX = 3;
    public static final int LATITUDE_IDX = 4;
    public static final int LONGITUDE_IDX = 5;
    public static final int DATE_IDX = 6;
    public static final int TIME_IDX = 7;

    @JsonProperty("farmer_field_visit_id")
    public int farmerFieldVisitId;
    @JsonProperty("record_timestamp")
    public Date recordTimestamp;
    Double longitude;
    Double latitude;
    Double rainfall;
    Double humidity;
    @JsonProperty("temperature")
    public Double temperature;
    Double light_intensity;

    public static FieldSensorRecord recordStringParser (int farmerFieldVisitId, String line) throws Exception {
        FieldSensorRecord fieldSensorRecord = new FieldSensorRecord();

        String[] columns = line.split(",");
        fieldSensorRecord.farmerFieldVisitId = farmerFieldVisitId;
        fieldSensorRecord.rainfall = isNumeric(columns[RAINFALL_IDX])? Double.parseDouble(columns[RAINFALL_IDX]) : null;
        fieldSensorRecord.humidity = isNumeric(columns[HUMIDITY_IDX])? Double.parseDouble(columns[HUMIDITY_IDX]) : null;
        fieldSensorRecord.temperature = isNumeric(columns[TEMPERATURE_IDX])? Double.parseDouble(columns[TEMPERATURE_IDX]) : null;
        fieldSensorRecord.light_intensity = isNumeric(columns[LIGHT_INTENSITY_IDX])? Double.parseDouble(columns[LIGHT_INTENSITY_IDX]) : null;

        fieldSensorRecord.longitude = isNumeric(columns[LONGITUDE_IDX])? Double.parseDouble(columns[LONGITUDE_IDX]) : null;
        fieldSensorRecord.latitude = isNumeric(columns[LATITUDE_IDX])? Double.parseDouble(columns[LATITUDE_IDX]) : null;

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyyhh:mm:ss");
        fieldSensorRecord.recordTimestamp = formatter.parse(columns[DATE_IDX] + columns[TIME_IDX]);

        return fieldSensorRecord;
    }

    public static int sensorRecordInsert (List<FieldSensorRecord> recordList) {
        try (Connection con = DB.sql2o.beginTransaction()) {
            String sql =
                "INSERT INTO field_sensor_data VALUES (:farmerFieldVisitId, :recordTimestamp, " +
                "ST_GeographyFromText('POINT(:long :lat)'), :rainfall, :humidity, :temperature, :light)";
            for (FieldSensorRecord record : recordList) {
                con.createQuery(sql.replace(":long", String.valueOf(record.longitude)).replace(":lat", String.valueOf(record.latitude)))
                    .addParameter("farmerFieldVisitId", record.farmerFieldVisitId)
                    .addParameter("recordTimestamp", record.recordTimestamp)
                    .addParameter("rainfall", record.rainfall)
                    .addParameter("humidity", record.humidity)
                    .addParameter("temperature", record.temperature)
                    .addParameter("light", record.light_intensity)
                    .executeUpdate();
            }

            con.commit();
            return 1;
        }
    }

    public static boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }
}
