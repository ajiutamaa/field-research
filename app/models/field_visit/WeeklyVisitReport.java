package models.field_visit;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.DB;
import org.sql2o.Connection;

import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 11/23/2015.
 */
public class WeeklyVisitReport {
    @JsonProperty("weekly_visit_id")
    public int weeklyVisitId;

    @JsonProperty("farmer_field_visit_id")
    public int farmerFieldVisitId;

    @JsonProperty("observation_date")
    public Date observationDate;

    @JsonProperty("plant_sample_list")
    public List<WeeklyPlantSample> listPlantSample;

    @JsonProperty("notes")
    public String notes;

    public static List<WeeklyVisitReport> select (int weeklyVisitId, int farmerFieldVisitId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT weekly_visit_id AS weeklyVisitId, farmer_field_visit_id AS farmerFieldVisitId, observation_date AS observationDate, notes " +
                "FROM weekly_visit_report WHERE " +
                    "(:farmerFieldVisitId = -1 OR farmer_field_visit_id = :farmerFieldVisitId) AND " +
                    "(:weeklyVisitId = -1 OR weekly_visit_id = :weeklyVisitId)";
            List<WeeklyVisitReport> weeklyReports = con.createQuery(sql)
                    .addParameter("farmerFieldVisitId", farmerFieldVisitId)
                    .addParameter("weeklyVisitId", weeklyVisitId)
                    .executeAndFetch(WeeklyVisitReport.class);
            for (WeeklyVisitReport weeklyReport : weeklyReports) {
                weeklyReport.listPlantSample = WeeklyPlantSample.select(-1, weeklyReport.weeklyVisitId);
            }

            return weeklyReports;
        }
    }

    public static int insert (WeeklyVisitReport weeklyVisit) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                    "INSERT INTO weekly_visit_report (farmer_field_visit_id, observation_date, notes) VALUES " +
                            "(:farmerFieldVisitId, :observationDate, :notes)";
            return con.createQuery(sql, true)
                    .addParameter("farmerFieldVisitId", weeklyVisit.farmerFieldVisitId)
                    .addParameter("observationDate", weeklyVisit.observationDate)
                    .addParameter("notes", weeklyVisit.notes)
                    .executeUpdate()
                    .getKey(Integer.class);
        }
    }

    public static int update (WeeklyVisitReport weeklyVisitReport) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                    "UPDATE weekly_visit_report SET observation_date = :observationDate, notes = :notes WHERE weekly_visit_id = :weeklyVisitId";
            return con.createQuery(sql)
                    .addParameter("weeklyVisitId", weeklyVisitReport.weeklyVisitId)
                    .addParameter("observationDate", weeklyVisitReport.observationDate)
                    .addParameter("notes", weeklyVisitReport.notes)
                    .executeUpdate()
                    .getResult();
        }
    }

    public static int delete (int weeklyVisitId) throws Exception {
        try (Connection con = DB.sql2o.open()) {
            // delete file of field visit
            FarmerFieldVisitFile.delete(weeklyVisitId, null);
            String sql =
                    "DELETE FROM weekly_visit_report WHERE weekly_visit_id = :weeklyVisitId";
            return con.createQuery(sql)
                    .addParameter("weeklyVisitId", weeklyVisitId)
                    .executeUpdate()
                    .getResult();
        }
    }
}
