package models.farmer;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.DB;
import org.sql2o.Connection;

import java.util.List;

/**
 * Created by lenovo on 11/9/2015.
 */
public class FarmerHarvest {
    @JsonProperty("harvest_id")
    public int harvestId;

    @JsonProperty("farmer_id")
    public int farmerId;

    @JsonProperty("year")
    public int year;

    @JsonProperty("season")
    public String season;

    @JsonProperty("crop")
    public String crop;

    @JsonProperty("field_area")
    public double fieldArea;

    @JsonProperty("yield")
    public double yield;

    public static List<FarmerHarvest> select (int farmerId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT harvest_id AS harvestId, farmer_id AS farmerId, year, season, crop, field_area AS fieldArea, yield " +
                "FROM farmer_harvest_history WHERE farmer_id = :farmerId";
            return con.createQuery(sql).addParameter("farmerId", farmerId)
                    .executeAndFetch(FarmerHarvest.class);
        }
    }

    public static FarmerHarvest selectOne (int harvestId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                    "SELECT harvest_id AS harvestId, farmer_id AS farmerId, year, season, crop, field_area AS fieldArea, yield " +
                            "FROM farmer_harvest_history WHERE harvest_id = :harvestId";
            return con.createQuery(sql).addParameter("harvestId", harvestId)
                    .executeAndFetchFirst(FarmerHarvest.class);
        }
    }

    public static int insert (FarmerHarvest farmerHarvest) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "INSERT INTO farmer_harvest_history (farmer_id, year, season, crop, field_area, yield, updated_at, created_at) " +
                "VALUES (:farmerId, :year, :season, :crop, :fieldArea, :yield, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
            return con.createQuery(sql, true)
                    .addParameter("farmerId", farmerHarvest.farmerId)
                    .addParameter("year", farmerHarvest.year)
                    .addParameter("season", farmerHarvest.season)
                    .addParameter("crop", farmerHarvest.crop)
                    .addParameter("fieldArea", farmerHarvest.fieldArea)
                    .addParameter("yield", farmerHarvest.yield)
                    .executeUpdate()
                    .getKey(Integer.class);
        }
    }

    public static int update (FarmerHarvest farmerHarvest) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "UPDATE farmer_harvest_history SET year = :year, season = :season, crop = :crop, field_area = :fieldArea, " +
                "yield = :yield, updated_at = CURRENT_TIMESTAMP WHERE harvest_id = :harvestId";
            return con.createQuery(sql)
                    .addParameter("harvestId", farmerHarvest.harvestId)
                    .addParameter("year", farmerHarvest.year)
                    .addParameter("season", farmerHarvest.season)
                    .addParameter("crop", farmerHarvest.crop)
                    .addParameter("fieldArea", farmerHarvest.fieldArea)
                    .addParameter("yield", farmerHarvest.yield)
                    .executeUpdate()
                    .getResult();
        }
    }

    public static int delete (int harvestId) {
        try (Connection con = DB.sql2o.open()) {
            String sql = "DELETE FROM farmer_harvest_history WHERE harvest_id = :harvestId";
            return con.createQuery(sql).addParameter("harvestId", harvestId).executeUpdate().getResult();
        }
    }
}
