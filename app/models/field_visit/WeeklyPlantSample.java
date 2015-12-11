package models.field_visit;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.DB;
import org.sql2o.Connection;

import java.util.List;

/**
 * Created by lenovo on 11/23/2015.
 */
public class WeeklyPlantSample {
    @JsonProperty("weekly_plant_sample_id")
    public Integer weeklyPlantSampleId;

    @JsonProperty("weekly_visit_id")
    public Integer weeklyVisitId;

    @JsonProperty("height")
    public Double height;

    @JsonProperty("number_of_leaf")
    public Integer numberOfLeaf;

    @JsonProperty("pest")
    public String pest;

    @JsonProperty("pest_score")
    public Double pestScore;

    @JsonProperty("desease")
    public String desease;

    @JsonProperty("desease_score")
    public Double deseaseScore;

    @JsonProperty("leaf_P1")
    public Double leafP1;

    @JsonProperty("leaf_P2")
    public Double leafP2;

    @JsonProperty("leaf_L")
    public Double leafL;

    public static List<WeeklyPlantSample> select (int weeklyPlantSampleId, int weeklyVisitId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT weekly_visit_id AS weeklyVisitId, height, number_of_leaf AS numberOfLeaf, " +
                "pest_name AS pest, pest_score AS pestScore, desease_name AS desease, desease_score AS deseaseScore, " +
                "leaf_P1 AS leafP1, leaf_P2 AS leafP2, leaf_L AS leafL, weekly_plant_sample_id AS weeklyPlantSampleId " +
                "FROM weekly_plant_sample WHERE " +
                    "(:weeklyVisitId = -1 OR weekly_visit_id = :weeklyVisitId) AND " +
                    "(:weeklyPlantSampleId = -1 OR weekly_plant_sample_id = :weeklyPlantSampleId)";
            return con.createQuery(sql)
                    .addParameter("weeklyVisitId", weeklyVisitId)
                    .addParameter("weeklyPlantSampleId", weeklyPlantSampleId)
                    .executeAndFetch(WeeklyPlantSample.class);
        }
    }

    public static int insert (WeeklyPlantSample plantSample) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "INSERT INTO weekly_plant_sample (weekly_visit_id, height, number_of_leaf, pest_name, pest_score, " +
                "desease_name, desease_score, leaf_P1, leaf_P2, leaf_L) " +
                "VALUES (:weeklyVisitId, :height, :numberOfLeaf, :pestName, :pestScore, :deseaseName, :deseaseScore, " +
                ":leafP1, :leafP2, :leafL)";
            return con.createQuery(sql, true)
                    .addParameter("weeklyVisitId", plantSample.weeklyVisitId)
                    .addParameter("height", plantSample.height)
                    .addParameter("numberOfLeaf", plantSample.numberOfLeaf)
                    .addParameter("pestName", plantSample.pest)
                    .addParameter("pestScore", plantSample.pestScore)
                    .addParameter("deseaseName", plantSample.desease)
                    .addParameter("deseaseScore", plantSample.deseaseScore)
                    .addParameter("leafP1", plantSample.leafP1)
                    .addParameter("leafP2", plantSample.leafP2)
                    .addParameter("leafL", plantSample.leafL)
                    .executeUpdate()
                    .getKey(Integer.class);
        }
    }

    public static int update (WeeklyPlantSample plantSample) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                    "UPDATE weekly_plant_sample SET height = :height, number_of_leaf = :numberOfLeaf, pest_name = :pestName, " +
                            "pest_score = :pestScore, desease_name = :deseaseName, desease_score = :deseaseScore, " +
                            "leaf_P1 = :leafP1, leaf_P2 = :leafP2, leaf_L = :leafL " +
                            "WHERE weekly_plant_sample_id = :weeklyPlantSampleId";
            return con.createQuery(sql)
                    .addParameter("weeklyPlantSampleId", plantSample.weeklyPlantSampleId)
                    .addParameter("height", plantSample.height)
                    .addParameter("numberOfLeaf", plantSample.numberOfLeaf)
                    .addParameter("pestName", plantSample.pest)
                    .addParameter("pestScore", plantSample.pestScore)
                    .addParameter("deseaseName", plantSample.desease)
                    .addParameter("deseaseScore", plantSample.deseaseScore)
                    .addParameter("leafP1", plantSample.leafP1)
                    .addParameter("leafP2", plantSample.leafP2)
                    .addParameter("leafL", plantSample.leafL)
                    .executeUpdate()
                    .getResult();
        }
    }

    public static int delete (int weeklyPlantSampleId) {
        try (Connection con = DB.sql2o.open()) {
            String sql = "DELETE FROM weekly_plant_sample WHERE weekly_plant_sample_id = :weeklyPlantSampleId";
            return con.createQuery(sql)
                    .addParameter("weeklyPlantSampleId", weeklyPlantSampleId)
                    .executeUpdate().getResult();
        }
    }
}
