package models.field_visit;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.DB;
import models.farmer.Farmer;
import models.field_visit.PlantStageEnd;
import models.field_visit.PlantStageStart;
import models.field_visit.WeeklyVisitReport;
import org.sql2o.Connection;
import play.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Date;
/**
 * Created by lenovo on 11/9/2015.
 */
public class FarmerFieldVisit {
    public class FieldVisit {
        @JsonProperty("farmer_field_visit_id")
        public int farmerFieldVisitId;

        @JsonProperty("farmer_id")
        public int farmerId;

        @JsonProperty("farmer_name")
        public String farmerName;

        @JsonProperty("cluster_group_id")
        public int clusterGroupId;

        @JsonProperty("cluster_group_name")
        public String clusterGroupName;

        @JsonProperty("kkv_group_id")
        public int kkvGroupId;

        @JsonProperty("kkv_group_name")
        public String kkvGroupName;

        @JsonProperty("desa_id")
        public int desaId;

        @JsonProperty("desa_name")
        public String desaName;
    }

    public class FieldVisitDetail {
        @JsonProperty("farmer_field_visit_id")
        public int farmerFieldVisitId;

        @JsonProperty("farmer_id")
        public int farmerId;

        @JsonProperty("farmer_name")
        public String farmerName;

        @JsonProperty("cluster_group_id")
        public int clusterGroupId;

        @JsonProperty("cluster_group_name")
        public String clusterGroupName;

        @JsonProperty("kkv_group_id")
        public int kkvGroupId;

        @JsonProperty("kkv_group_name")
        public String kkvGroupName;

        @JsonProperty("desa_id")
        public int desaId;

        @JsonProperty("desa_name")
        public String desaName;

        @JsonProperty("plant_stage_start")
        public HashMap<String, Object> plantStageStart;

        @JsonProperty("plant_stage_end")
        public HashMap<String, Object> plantStageEnd;

        @JsonProperty("weekly_visit_list")
        public List<WeeklyVisitReport> listWeeklyVisit;
    }

    public class FarmerSimple {
        @JsonProperty("farmer_id")
        public int farmer_id;

        @JsonProperty("farmer_name")
        public String farmer_name;
    }

    public static List<FieldVisit> select () {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT v.farmer_field_visit_id AS farmerFieldVisitId, v.farmer_id AS farmerId, f.name AS farmerName, " +
                    "ds.id AS desaId, ds.name AS desaName, c.id AS clusterGroupId, c.name AS clusterGroupName, " +
                    "kkv.id AS kkvGroupId, kkv.name AS kkvGroupName " +
                "FROM farmer_field_visit v, farmer f, master_desa ds, cluster_group c, farmer_kkv_group kkv " +
                "WHERE v.farmer_id = f.id AND " +
                    "f.desa_id = ds.id AND " +
                    "f.cluster_group_id = c.id AND " +
                    "f.kkv_group_id = kkv.id";
            return con.createQuery(sql).executeAndFetch(FieldVisit.class);
        }
    }

    public static List<FarmerSimple> selectUnassignedFarmer () {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT f.id AS farmer_id, f.name AS farmer_name FROM farmer f WHERE NOT EXISTS (SELECT * FROM farmer_field_visit v WHERE v.farmer_id = f.id)";
            return con.createQuery(sql).executeAndFetch(FarmerSimple.class);
        }
    }

    public static FieldVisitDetail selectDetail (int farmerFieldVisitId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT v.farmer_field_visit_id AS farmerFieldVisitId, v.farmer_id AS farmerId, f.name AS farmerName, " +
                "ds.id AS desaId, ds.name AS desaName, c.id AS clusterGroupId, c.name AS clusterGroupName, " +
                "kkv.id AS kkvGroupId, kkv.name AS kkvGroupName " +
                "FROM farmer_field_visit v, farmer f, master_desa ds, cluster_group c, farmer_kkv_group kkv " +
                "WHERE v.farmer_field_visit_id = :farmerFieldVisitId AND v.farmer_id = f.id AND " +
                "f.desa_id = ds.id AND " +
                "f.cluster_group_id = c.id AND " +
                "f.kkv_group_id = kkv.id";
            FieldVisitDetail detail =  con.createQuery(sql)
                                                    .addParameter("farmerFieldVisitId", farmerFieldVisitId)
                                                    .executeAndFetchFirst(FieldVisitDetail.class);

            if (detail == null) return detail;

            detail.plantStageStart = new HashMap<String, Object>();
            detail.plantStageEnd = new HashMap<String, Object>();

            for (PlantStageStart plantStageStart : PlantStageStart.select(null, detail.farmerFieldVisitId)) {
                detail.plantStageStart.put(plantStageStart.plantStageType, plantStageStart.startDate);
            }

            for (PlantStageEnd plantStageEnd : PlantStageEnd.select(null, detail.farmerFieldVisitId)) {
                detail.plantStageEnd.put(plantStageEnd.plantStageType, plantStageEnd.endDate);
            }

            detail.listWeeklyVisit = WeeklyVisitReport.select(-1, detail.farmerFieldVisitId);

            return detail;
        }
    }

    public static int insert (int farmerId) {
        try (Connection con = DB.sql2o.open()) {
            String sql = "INSERT INTO farmer_field_visit (farmer_id) VALUES (:farmerId)";
            return con.createQuery(sql, true)
                    .addParameter("farmerId", farmerId)
                    .executeUpdate()
                    .getKey(Integer.class);
        }
    }

    public static int delete (int farmerId) {
        try (Connection con = DB.sql2o.open()) {
            String sql = "DELETE FROM farmer_field_visit WHERE farmer_id = :farmerId";
            return con.createQuery(sql).addParameter("farmerId", farmerId).executeUpdate().getResult();
        }
    }
}
