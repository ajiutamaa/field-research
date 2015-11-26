package models.field_visit;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.DB;
import org.sql2o.Connection;

import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 11/23/2015.
 */
public class PlantStageStart {
    @JsonProperty("farmer_field_visit_id")
    public int farmerFieldVisitId;

    @JsonProperty("plant_stage_type")
    public String plantStageType;

    @JsonProperty("start_date")
    public Date startDate;

    public static List<PlantStageStart> select (String plantStageType, int farmerFieldVisitId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT farmer_field_visit_id AS farmerFieldVisitId, plant_stage_type AS plantStageType, start_date AS startDate " +
                "FROM plant_stage_start WHERE " +
                    "farmer_field_visit_id = :farmerFieldVisitId AND " +
                    "(:plantStageType IS NULL OR lower(plant_stage_type) = lower(:plantStageType))";
            return con.createQuery(sql)
                    .addParameter("farmerFieldVisitId", farmerFieldVisitId)
                    .addParameter("plantStageType", plantStageType)
                    .executeAndFetch(PlantStageStart.class);
        }
    }

    public static int insert (PlantStageStart plantStageStart) {
        try (Connection con = DB.sql2o.open()) {
            String sql = "INSERT INTO plant_stage_start VALUES (:farmerFieldVisitId, :plantStageType, :startDate)";
            return con.createQuery(sql)
                    .addParameter("farmerFieldVisitId", plantStageStart.farmerFieldVisitId)
                    .addParameter("plantStageType", plantStageStart.plantStageType.toLowerCase())
                    .addParameter("startDate", plantStageStart.startDate)
                    .executeUpdate().getResult();
        }
    }

    public static int update (PlantStageStart plantStageStart) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                    "UPDATE plant_stage_start SET start_date = :startDate WHERE " +
                            "farmer_field_visit_id = :farmerFieldVisitId AND plant_stage_type = :plantStageType";
            return con.createQuery(sql)
                    .addParameter("farmerFieldVisitId", plantStageStart.farmerFieldVisitId)
                    .addParameter("plantStageType", plantStageStart.plantStageType)
                    .addParameter("startDate", plantStageStart.startDate)
                    .executeUpdate().getResult();
        }
    }

    public static int delete (PlantStageStart plantStageStart) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                    "DELETE FROM plant_stage_start WHERE farmer_field_visit_id = :farmerFieldVisitId AND " +
                            "plant_stage_type = :plantStageType";
            return con.createQuery(sql)
                    .addParameter("farmerFieldVisitId", plantStageStart.farmerFieldVisitId)
                    .addParameter("plantStageType", plantStageStart.plantStageType)
                    .executeUpdate().getResult();
        }
    }
}
