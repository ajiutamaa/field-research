package models.farmer;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.DB;
import org.sql2o.Connection;

import java.util.List;

/**
 * Created by lenovo on 11/9/2015.
 */
public class FarmerField {
    @JsonProperty("field_id")
    public int fieldId;

    @JsonProperty("farmer_id")
    public int farmerId;

    @JsonProperty("desa_id")
    public int desaId;

    @JsonProperty("desa_name")
    public String desaName;

    @JsonProperty("kecamatan_id")
    public int kecamatanId;

    @JsonProperty("kabupaten_id")
    public int kabupatenId;

    @JsonProperty("provinsi_id")
    public int provinsiId;

    @JsonProperty("field_area")
    public double fieldArea;

    @JsonProperty("ownership_status")
    public String ownershipStatus;

    @JsonProperty("is_irrigated")
    public boolean isIrrigated;

    @JsonProperty("notes")
    public String notes;

    public static List<FarmerField> select (int farmerId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT ff.field_id AS fieldId, ff.farmer_id AS farmerId, ff.location_desa_id AS desaId, ff.area AS fieldArea, " +
                    "ff.ownership_status AS ownershipStatus, ds.name AS desaName, kc.id AS kecamatanId, " +
                    "kb.id AS kabupatenId, pv.id AS provinsiId, " +
                    "ff.is_irrigated AS isIrrigated, ff.notes " +
                "FROM farmer_field ff, master_desa ds, master_kecamatan kc, master_kabupaten kb, master_provinsi pv " +
                "WHERE " +
                    "ff.farmer_id = :farmerId AND ff.location_desa_id = ds.id AND " +
                    "ds.kecamatan_id = kc.id AND " +
                    "kc.kabupaten_id = kb.id AND " +
                    "kb.provinsi_id = pv.id";
            return con.createQuery(sql).addParameter("farmerId", farmerId).executeAndFetch(FarmerField.class);
        }
    }

    public static FarmerField selectOne (int fieldId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT field_id AS fieldId, farmer_id AS farmerId, location_desa_id AS desaId, area AS fieldArea, ownership_status AS ownershipStatus, " +
                "is_irrigated AS isIrrigated, notes " +
                "FROM farmer_field WHERE field_id = :fieldId";
            return con.createQuery(sql).addParameter("fieldId", fieldId).executeAndFetchFirst(FarmerField.class);
        }
    }

    public static int insert (FarmerField farmerField) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                    "INSERT INTO farmer_field (farmer_id, location_desa_id, area, ownership_status, is_irrigated, notes, created_at, updated_at) " +
                    "VALUES (:farmerId, :desaId, :area, :ownershipStatus, :isIrrigated, :notes, NOW(), NOW())";
            return con.createQuery(sql, true)
                    .addParameter("farmerId", farmerField.farmerId)
                    .addParameter("desaId", farmerField.desaId)
                    .addParameter("area", farmerField.fieldArea)
                    .addParameter("ownershipStatus", farmerField.ownershipStatus)
                    .addParameter("isIrrigated", farmerField.isIrrigated)
                    .addParameter("notes", farmerField.notes)
                    .executeUpdate()
                    .getKey(Integer.class);
        }
    }

    public static int update (FarmerField farmerField) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "UPDATE farmer_field SET location_desa_id = :desaId, area = :area, ownership_status = :ownershipStatus, " +
                "is_irrigated = :isIrrigated, notes = :notes, " +
                "updated_at = CURRENT_TIMESTAMP WHERE field_id = :fieldId";
            return con.createQuery(sql)
                    .addParameter("fieldId", farmerField.fieldId)
                    .addParameter("desaId", farmerField.desaId)
                    .addParameter("area", farmerField.fieldArea)
                    .addParameter("ownershipStatus", farmerField.ownershipStatus)
                    .addParameter("isIrrigated", farmerField.isIrrigated)
                    .addParameter("notes", farmerField.notes)
                    .executeUpdate()
                    .getResult();
        }
    }

    public static int delete (int fieldId) {
        try (Connection con = DB.sql2o.open()) {
            String sql = "DELETE FROM farmer_field WHERE field_id = :fieldId";
            return con.createQuery(sql).addParameter("fieldId", fieldId).executeUpdate().getResult();
        }
    }
}
