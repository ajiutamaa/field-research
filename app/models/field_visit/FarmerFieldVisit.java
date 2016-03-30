package models.field_visit;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.DB;
import models.farmer.Farmer;
import models.field_visit.PlantStageEnd;
import models.field_visit.PlantStageStart;
import models.field_visit.WeeklyVisitReport;
import org.sql2o.Connection;
import play.Logger;
import play.libs.F;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
/**
 * Created by lenovo on 11/9/2015.
 */
public class FarmerFieldVisit {
    public class FieldVisit {
        @JsonProperty("season_id")
        public int seasonId;

        @JsonProperty("season_code")
        public String seasonCode;

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

        @JsonProperty("polygon")
        public ArrayList<HashMap<String, Double>> polygon;
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

        @JsonProperty("polygon")
        public ArrayList<HashMap<String, Double>> polygon;
    }

    public class FarmerSimple {
        @JsonProperty("farmer_id")
        public int farmer_id;

        @JsonProperty("farmer_name")
        public String farmer_name;
    }

    public static List<FieldVisit> select (int seasonId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT season.season_id as seasonId, season.season_code AS seasonCode, " +
                    "v.farmer_field_visit_id AS farmerFieldVisitId, v.farmer_id AS farmerId, f.name AS farmerName, " +
                    "ds.id AS desaId, ds.name AS desaName, c.id AS clusterGroupId, c.name AS clusterGroupName, " +
                    "kkv.id AS kkvGroupId, kkv.name AS kkvGroupName " +
                "FROM farmer_field_visit v, farmer f, master_desa ds, master_season season, cluster_group c, farmer_kkv_group kkv " +
                "WHERE (:seasonId = -1 OR v.season_id = :seasonId) AND " +
                    "v.farmer_id = f.id AND " +
                    "v.season_id = season.season_id AND " +
                    "f.desa_id = ds.id AND " +
                    "f.cluster_group_id = c.id AND " +
                    "f.kkv_group_id = kkv.id";
            List<FieldVisit> fieldVisits = con.createQuery(sql)
                    .addParameter("seasonId", seasonId)
                    .executeAndFetch(FieldVisit.class);
            for (FieldVisit fieldVisit : fieldVisits) {
                fieldVisit.polygon = selectFieldPolygon(fieldVisit.farmerId);
            }
            return fieldVisits;
        }
    }

    public static List<FarmerSimple> selectUnassignedFarmer () {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT f.id AS farmer_id, f.name AS farmer_name FROM farmer f WHERE NOT EXISTS (SELECT * FROM farmer_field_visit v WHERE v.farmer_id = f.id) ORDER BY farmer_name";
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

            detail.polygon = selectFieldPolygon(detail.farmerId);

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

    public static ArrayList<HashMap<String, Double>> selectFieldPolygon (int farmerId) {
        try (Connection con = DB.sql2o.open()) {
            String sql = "SELECT ST_AsText(polygon) FROM farmer_field_visit WHERE farmer_id = :farmerId";
            String polygon = con.createQuery(sql).addParameter("farmerId", farmerId).executeScalar(String.class);
            if (polygon == null) return null;
            polygon = polygon.replace("(","").replace(")","").replace("POLYGON","");
            ArrayList<HashMap<String, Double>> longLatList = new ArrayList();
            for (String point : polygon.split(",")) {
                String longitude = (point.split(" "))[0];
                String latitude = (point.split(" "))[1];
                HashMap<String, Double> map = new HashMap<>();
                map.put("longitude", Double.parseDouble(longitude));
                map.put("latitude", Double.parseDouble(latitude));
                longLatList.add(map);
            }
            return longLatList;
        }
    }

    public static int insert (int seasonId, int farmerId) {
        try (Connection con = DB.sql2o.open()) {
            String sql = "INSERT INTO farmer_field_visit (season_id, farmer_id) VALUES (:seasonId, :farmerId)";
            return con.createQuery(sql, true)
                    .addParameter("seasonId", seasonId)
                    .addParameter("farmerId", farmerId)
                    .executeUpdate()
                    .getKey(Integer.class);
        }
    }

    public static int insertPolygon (int seasonId, int farmerId, List<F.Tuple<Double, Double>> longLatList) {
        try (Connection con = DB.sql2o.open()) {
            String sql = "UPDATE farmer_field_visit SET polygon = ST_GeographyFromText('POLYGON((";
            int counter = 0;
            for (F.Tuple<Double, Double> longLat : longLatList) {
                if (counter == 0) {
                    sql = sql + longLat._1 + " " + longLat._2;
                } else {
                    sql = sql + ", " + longLat._1 + " " + longLat._2;
                }
                counter++;
            }
            sql = sql + "))') WHERE season_id = :seasonId AND farmer_id = :farmerId";

            return con.createQuery(sql)
                    .addParameter("seasonId", seasonId)
                    .addParameter("farmerId", farmerId)
                    .executeUpdate()
                    .getResult();
        }
    }

    public static int delete (int seasonId, int farmerId) throws Exception {
        try (Connection con = DB.sql2o.open()) {
            // delete files
            try {
                String sqlGetFarmerId = "SELECT farmer_field_visit_id FROM farmer_field_visit WHERE season_id = :seasonId AND farmer_id = :farmerId";
                int farmerFieldVisitId = con.createQuery(sqlGetFarmerId)
                        .addParameter("seasonId", seasonId)
                        .addParameter("farmerId", farmerId).executeScalar(Integer.class);
                for (WeeklyVisitReport weeklyVisitReport : WeeklyVisitReport.select(-1, farmerFieldVisitId)) {
                    FarmerFieldVisitFile.delete(weeklyVisitReport.weeklyVisitId, null);
                }
            } catch (Exception e) {
                // do nothing
            }

            String sql = "DELETE FROM farmer_field_visit WHERE season_id = :seasonId AND farmer_id = :farmerId";
            return con.createQuery(sql)
                    .addParameter("seasonId", seasonId)
                    .addParameter("farmerId", farmerId).executeUpdate().getResult();
        }
    }
}
