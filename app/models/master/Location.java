package models.master;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.DB;
import org.sql2o.Connection;

import java.util.List;

/**
 * Created by lenovo on 11/13/2015.
 */
public class Location {
    public class MasterProvinsi {
        @JsonProperty("provinsi_id")
        public int provinsiId;
        @JsonProperty("provinsi_name")
        public String provinsiName;
        @JsonProperty("provinsi_code")
        public String provinsiCode;
    }

    public class MasterKabupaten {
        @JsonProperty("kabupaten_id")
        public int kabupatenId;
        @JsonProperty("provinsi_id")
        public int provinsiId;
        @JsonProperty("kabupaten_name")
        public String kabupatenName;
        @JsonProperty("kabupaten_code")
        public String kabupatenCode;
    }

    public class MasterKecamatan {
        @JsonProperty("kecamatan_id")
        public int kecamatanId;
        @JsonProperty("kabupaten_id")
        public int kabupatenId;
        @JsonProperty("kecamatan_name")
        public String kecamatanName;
        @JsonProperty("kecamatan_code")
        public String kecamatanCode;
    }

    public class MasterDesa {
        @JsonProperty("desa_id")
        public int desaId;
        @JsonProperty("kecamatan_id")
        public int kecamatanId;
        @JsonProperty("desa_name")
        public String desaName;
        @JsonProperty("desa_code")
        public String desaCode;
    }

    public static List<MasterProvinsi> selectProvinsi () {
        try (Connection con = DB.sql2o.open()) {
            String sql = "SELECT id AS provinsiId, name AS provinsiName, code AS provinsiCode FROM master_provinsi";
            return con.createQuery(sql).executeAndFetch(MasterProvinsi.class);
        }
    }

    public static List<MasterKabupaten> selectKabupaten (int provinsiId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT id AS kabupatenId, name AS kabupatenName, code AS kabupatenCode, provinsi_id AS provinsiId " +
                "FROM master_kabupaten WHERE provinsi_id = :provinsiId";
            return con.createQuery(sql)
                    .addParameter("provinsiId", provinsiId)
                    .executeAndFetch(MasterKabupaten.class);
        }
    }

    public static List<MasterKecamatan> selectKecamatan (int kabupatenId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT id AS kecamatanId, name AS kecamatanName, code AS kecamatanCode, kabupaten_id AS kabupatenId " +
                "FROM master_kecamatan WHERE kabupaten_id = :kabupatenId";
            return con.createQuery(sql)
                    .addParameter("kabupatenId", kabupatenId)
                    .executeAndFetch(MasterKecamatan.class);
        }
    }

    public static List<MasterDesa> selectDesa (int kecamatanId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT id AS desaId, name AS desaName, code AS desaCode, kecamatan_id AS kecamatanId " +
                "FROM master_desa WHERE kecamatan_id = :kecamatanId";
            return con.createQuery(sql)
                    .addParameter("kecamatanId", kecamatanId)
                    .executeAndFetch(MasterDesa.class);
        }
    }
}
