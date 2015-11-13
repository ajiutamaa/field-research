package models.farmer;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.DB;
import org.sql2o.Connection;

import java.util.List;

/**
 * Created by lenovo on 11/13/2015.
 */
public class FarmerGroup {
    public class KkvGroup {
        @JsonProperty("kkv_group_id")
        public int id;
        @JsonProperty("kkv_group_name")
        public String name;
    }

    public class ClusterGroup {
        @JsonProperty("cluster_group_id")
        public int id;
        @JsonProperty("cluster_group_name")
        public String name;
    }

    public static List<KkvGroup> selectKkvGroup () {
        try (Connection con = DB.sql2o.open()) {
            String sql = "SELECT * FROM farmer_kkv_group";
            return con.createQuery(sql).executeAndFetch(KkvGroup.class);
        }
    }

    public static List<ClusterGroup> selectClusterGroup () {
        try (Connection con = DB.sql2o.open()) {
            String sql = "SELECT * FROM cluster_group";
            return con.createQuery(sql).executeAndFetch(ClusterGroup.class);
        }
    }
}
