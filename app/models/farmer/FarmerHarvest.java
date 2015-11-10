package models.farmer;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.DB;
import org.sql2o.Connection;

/**
 * Created by lenovo on 11/9/2015.
 */
public class FarmerHarvest {
    @JsonProperty("farmer_id")
    public int farmerId;

    @JsonProperty("year")
    public int year;

    @JsonProperty("season")
    public String season;

    @JsonProperty("crop")
    public String crop;

    @JsonProperty("yield_area")
    public double yieldArea;

    @JsonProperty("yield")
    public double yield;

    public static FarmerHarvest selectOne (int farmerId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT farmer_id AS farmerId, year, season, crop, yield_area, yield " +
                "FROM farmer_harvest_history WHERE farmer_id = :farmerId";
            return con.createQuery(sql).addParameter("farmerId", farmerId)
                    .executeAndFetchFirst(FarmerHarvest.class);
        }
    }

    public static int insert (FarmerHarvest farmerHarvest) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "INSERT INTO farmer_asset (farmer_id, year, season, crop, yield_area, yield) " +
                "VALUES (:farmerId, :year, :season, :crop, :yieldArea, :yield)";
            return con.createQuery(sql)
                    .addParameter("farmerId", farmerHarvest.farmerId)
                    .addParameter("year", farmerHarvest.year)
                    .addParameter("season", farmerHarvest.season)
                    .addParameter("crop", farmerHarvest.crop)
                    .addParameter("yieldArea", farmerHarvest.yieldArea)
                    .addParameter("yield", farmerHarvest.yield)
                    .executeUpdate()
                    .getResult();
        }
    }

    public static int update (FarmerHarvest farmerHarvest) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "UPDATE farmer_asset SET year = :year, season = :season, crop = :crop, yield_area = :yieldArea, " +
                "yield = :yield, updated_at = CURRENT_TIMESTAMP WHERE farmer_id = :farmerId";
            return con.createQuery(sql)
                    .addParameter("farmerId", farmerHarvest.farmerId)
                    .addParameter("year", farmerHarvest.year)
                    .addParameter("season", farmerHarvest.season)
                    .addParameter("crop", farmerHarvest.crop)
                    .addParameter("yieldArea", farmerHarvest.yieldArea)
                    .addParameter("yield", farmerHarvest.yield)
                    .executeUpdate()
                    .getResult();
        }
    }

    public static int delete (int farmerId) {
        try (Connection con = DB.sql2o.open()) {
            String sql = "DELETE FROM farmer_harvest WHERE farmer_id = :farmerId";
            return con.createQuery(sql).addParameter("farmerId", farmerId).executeUpdate().getResult();
        }
    }
}
