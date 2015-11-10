package models.farmer;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.DB;
import org.sql2o.Connection;

/**
 * Created by lenovo on 11/9/2015.
 */
public class FarmerField {
    @JsonProperty("farmer_id")
    public int farmerId;

    @JsonProperty("desa_id")
    public int desaId;

    @JsonProperty("field_area")
    public double fieldArea;

    @JsonProperty("ownership_status")
    public String ownershipStatus;

    @JsonProperty("is_irrigated")
    public boolean isIrrigated;

    @JsonProperty("notes")
    public String notes;

    public static FarmerField selectOne (int farmerId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT farmer_id AS farmerId, location_desa_id AS desaId, area AS fieldArea, ownership_status AS ownershipStatus, " +
                "is_irrigated AS isIrrigated, notes " +
                "FROM farmer_field WHERE farmer_id = :farmerId";
            return con.createQuery(sql).addParameter("farmerId", farmerId).executeAndFetchFirst(FarmerField.class);
        }
    }

    public static int insert (FarmerField farmerField) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                    "INSERT INTO farmer_field (farmer_id, location_desa_id, area, ownership_status, is_irrigated, notes) " +
                    "VALUES (:farmerId, :desaId, :area, :ownershipStatus, :isIrrigated, :notes)";
            return con.createQuery(sql)
                    .addParameter("farmerId", farmerField.farmerId)
                    .addParameter("desaId", farmerField.desaId)
                    .addParameter("area", farmerField.fieldArea)
                    .addParameter("ownershipStatus", farmerField.ownershipStatus)
                    .addParameter("isIrrigated", farmerField.isIrrigated)
                    .addParameter("notes", farmerField.notes)
                    .executeUpdate()
                    .getResult();
        }
    }

    public static int update (FarmerField farmerField) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "UPDATE farmer_field SET location_desa_id = :desaId, area = :area, ownership_status = :ownershipStatus, " +
                    "is_irrigated = :isIrrigated, notes = :notes " +
                "updated_at = CURRENT_TIMESTAMP WHERE farmer_id = :farmerId";
            return con.createQuery(sql)
                    .addParameter("farmerId", farmerField.farmerId)
                    .addParameter("desaId", farmerField.desaId)
                    .addParameter("area", farmerField.fieldArea)
                    .addParameter("ownershipStatus", farmerField.ownershipStatus)
                    .addParameter("isIrrigated", farmerField.isIrrigated)
                    .addParameter("notes", farmerField.notes)
                    .executeUpdate()
                    .getResult();
        }
    }

    public static int delete (int farmerId) {
        try (Connection con = DB.sql2o.open()) {
            String sql = "DELETE FROM farmer_field WHERE farmer_id = :farmerId";
            return con.createQuery(sql).addParameter("farmerId", farmerId).executeUpdate().getResult();
        }
    }
}
