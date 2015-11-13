package models.farmer;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.DB;
import org.sql2o.Connection;

import java.util.List;

/**
 * Created by lenovo on 11/9/2015.
 */
public class FarmerAsset {
    @JsonProperty("asset_id")
    public int assetId;

    @JsonProperty("farmer_id")
    public int farmerId;

    @JsonProperty("type")
    public String type;

    @JsonProperty("name")
    public String name;

    @JsonProperty("ownership_status")
    public String ownershipStatus;

    @JsonProperty("quantity")
    public int quantity;

    public static List<FarmerAsset> select (int farmerId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT asset_id AS assetId, farmer_id AS farmerId, type, name, ownership_status AS ownershipStatus, quantity " +
                "FROM farmer_asset WHERE farmer_id = :farmerId";
            return con.createQuery(sql).addParameter("farmerId", farmerId).executeAndFetch(FarmerAsset.class);
        }
    }

    public static FarmerAsset selectOne (int assetId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT asset_id AS assetId, farmer_id AS farmerId, type, name, ownership_status AS ownershipStatus, quantity " +
                "FROM farmer_asset WHERE asset_id = :assetId";
            return con.createQuery(sql).addParameter("assetId", assetId).executeAndFetchFirst(FarmerAsset.class);
        }
    }

    public static int insert (FarmerAsset farmerAsset) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "INSERT INTO farmer_asset (farmer_id, type, name, ownership_status, quantity, created_at, updated_at) " +
                "VALUES (:farmerId, :type, :name, :ownershipStatus, :quantity, NOW(), NOW())";
            return con.createQuery(sql, true)
                    .addParameter("farmerId", farmerAsset.farmerId)
                    .addParameter("type", farmerAsset.type)
                    .addParameter("name", farmerAsset.name)
                    .addParameter("ownershipStatus", farmerAsset.ownershipStatus)
                    .addParameter("quantity", farmerAsset.quantity)
                    .executeUpdate()
                    .getKey(Integer.class);
        }
    }

    public static int update (FarmerAsset farmerAsset) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "UPDATE farmer_asset SET type = :type, name = :name, ownership_status = :ownershipStatus, " +
                "quantity = :quantity, updated_at = CURRENT_TIMESTAMP WHERE asset_id = :assetId";
            return con.createQuery(sql)
                    .addParameter("assetId", farmerAsset.assetId)
                    .addParameter("type", farmerAsset.type)
                    .addParameter("name", farmerAsset.name)
                    .addParameter("ownershipStatus", farmerAsset.ownershipStatus)
                    .addParameter("quantity", farmerAsset.quantity)
                    .executeUpdate()
                    .getResult();
        }
    }

    public static int delete (int assetId) {
        try (Connection con = DB.sql2o.open()) {
            String sql = "DELETE FROM farmer_asset WHERE asset_id = :assetId";
            return con.createQuery(sql).addParameter("assetId", assetId).executeUpdate().getResult();
        }
    }
}
