package models.field_visit;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.DB;
import org.sql2o.Connection;

import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 11/23/2015.
 */
public class PlantStageEnd {
    @JsonProperty("farmer_field_visit_id")
    public int farmerFieldVisitId;

    @JsonProperty("plant_stage_type")
    public String plantStageType;

    @JsonProperty("end_date")
    public Date endDate;

    public static List<PlantStageEnd> select (String plantStageType, int farmerFieldVisitId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT farmer_field_visit_id AS farmerFieldVisitId, plant_stage_type AS plantStageType, end_date AS endDate " +
                "FROM plant_stage_end WHERE " +
                    "farmer_field_visit_id = :farmerFieldVisitId AND " +
                    "(:plantStageType IS NULL OR lower(plant_stage_type) = lower(:plantStageType))";
            return con.createQuery(sql)
                    .addParameter("farmerFieldVisitId", farmerFieldVisitId)
                    .addParameter("plantStageType", plantStageType)
                    .executeAndFetch(PlantStageEnd.class);
        }
    }

    public static int insert (PlantStageEnd plantStageEnd) {
        try (Connection con = DB.sql2o.open()) {
            String sql = "INSERT INTO plant_stage_end VALUES (:farmerFieldVisitId, :plantStageType, :endDate)";
            return con.createQuery(sql)
                    .addParameter("farmerFieldVisitId", plantStageEnd.farmerFieldVisitId)
                    .addParameter("plantStageType", plantStageEnd.plantStageType.toLowerCase())
                    .addParameter("endDate", plantStageEnd.endDate)
                    .executeUpdate().getResult();
        }
    }

    public static int update (PlantStageEnd plantStageEnd) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                    "UPDATE plant_stage_end SET end_date = :endDate WHERE " +
                            "farmer_field_visit_id = :farmerFieldVisitId AND plant_stage_type = :plantStageType";
            return con.createQuery(sql)
                    .addParameter("farmerFieldVisitId", plantStageEnd.farmerFieldVisitId)
                    .addParameter("plantStageType", plantStageEnd.plantStageType)
                    .addParameter("endDate", plantStageEnd.endDate)
                    .executeUpdate().getResult();
        }
    }

    public static int delete (PlantStageEnd plantStageEnd) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                    "DELETE FROM plant_stage_end WHERE farmer_field_visit_id = :farmerFieldVisitId AND " +
                            "plant_stage_type = :plantStageType";
            return con.createQuery(sql)
                    .addParameter("farmerFieldVisitId", plantStageEnd.farmerFieldVisitId)
                    .addParameter("plantStageType", plantStageEnd.plantStageType)
                    .executeUpdate().getResult();
        }
    }
}
