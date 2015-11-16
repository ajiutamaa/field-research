package models.farmer;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.DB;
import org.sql2o.Connection;

import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 11/5/2015.
 */
public class Farmer {
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

    @JsonProperty("kecamatan_id")
    public int kecamatanId;

    @JsonProperty("kabupaten_id")
    public int kabupatenId;

    @JsonProperty("provinsi_id")
    public int provinsiId;

    @JsonProperty("observation_date")
    public Date observationDate;

    public static List<Farmer> select () {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT f.id AS farmerId, f.name AS farmerName, f.cluster_group_id AS clusterGroupId, f.kkv_group_id AS kkvGroupId, " +
                "f.desa_id AS desaId, f.observation_date AS observationDate, " +
                "cg.name AS clusterGroupName, kkv.name AS kkvGroupName, ds.name AS desaName, " +
                "kc.id AS kecamatanId, kb.id AS kabupatenId, pv.id AS provinsiId " +
                "FROM farmer f, cluster_group cg, farmer_kkv_group kkv, " +
                    "master_desa ds, master_kecamatan kc, master_kabupaten kb, master_provinsi pv " +
                "WHERE f.cluster_group_id = cg.id AND f.kkv_group_id = kkv.id " +
                    "AND f.desa_id = ds.id AND ds.kecamatan_id = kc.id " +
                    "AND kc.kabupaten_id = kb.id " +
                    "AND kb.provinsi_id = pv.id";
            return con.createQuery(sql).executeAndFetch(Farmer.class);
        }
    }

    public static Farmer selectOne (int farmerId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT f.id AS farmerId, f.name AS farmerName, f.cluster_group_id AS clusterGroupId, f.kkv_group_id AS kkvGroupId, " +
                "f.desa_id AS desaId, f.observation_date AS observationDate, " +
                "cg.name AS clusterGroupName, kkv.name AS kkvGroupName, ds.name AS desaName " +
                "FROM farmer f, cluster_group cg, farmer_kkv_group kkv, master_desa ds " +
                "WHERE f.id = :farmerId AND f.cluster_group_id = cg.id AND f.kkv_group_id = kkv.id AND f.desa_id = ds.id";
            return con.createQuery(sql)
                    .addParameter("farmerId", farmerId)
                    .executeAndFetchFirst(Farmer.class);
        }
    }

    public static int insert (Farmer farmer) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "INSERT INTO farmer (name, cluster_group_id, kkv_group_id, desa_id, observation_date, created_at, updated_at) " +
                "VALUES (:farmerName, :clusterGroupId, :kkvGroupId, :desaId, :observationDate, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
            return con.createQuery(sql, true)
                    .addParameter("farmerName", farmer.farmerName)
                    .addParameter("clusterGroupId", farmer.clusterGroupId)
                    .addParameter("kkvGroupId", farmer.kkvGroupId)
                    .addParameter("desaId", farmer.desaId)
                    .addParameter("observationDate", farmer.observationDate)
                    .executeUpdate()
                    .getKey(Integer.class);
        }
    }

    public static int update (Farmer farmer) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "UPDATE farmer SET name = :farmerName, cluster_group_id = :clusterGroupId, kkv_group_id = :kkvGroupId, " +
                "desa_id = :desaId, observation_date = :observationDate, updated_at = CURRENT_TIMESTAMP " +
                "WHERE id = :farmerId";
            return con.createQuery(sql)
                    .addParameter("farmerId", farmer.farmerId)
                    .addParameter("farmerName", farmer.farmerName)
                    .addParameter("clusterGroupId", farmer.clusterGroupId)
                    .addParameter("kkvGroupId", farmer.kkvGroupId)
                    .addParameter("desaId", farmer.desaId)
                    .addParameter("observationDate", farmer.observationDate)
                    .executeUpdate()
                    .getResult();
        }
    }

    public static int delete (int farmerId) {
        try (Connection con = DB.sql2o.open()) {
            String sql = "DELETE FROM farmer WHERE id = :farmerId";
            return con.createQuery(sql).addParameter("farmerId", farmerId).executeUpdate().getResult();
        }
    }
}
