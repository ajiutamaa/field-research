package models.master;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.DB;
import org.sql2o.Connection;

import java.util.List;

/**
 * Created by 1199 on 3/31/2016.
 */
public class Season {
    @JsonProperty("season_id")
    public int seasonId;
    @JsonProperty("season_code")
    public String seasonCode;
    @JsonProperty("previous_season_id")
    public int prevSeasonId;
    @JsonProperty("previous_season_code")
    public String prevSeasonCode;

    public static List<Season> select () {
        try (Connection con = DB.sql2o.open()) {
            String sql = "SELECT s.season_id AS seasonId, s.season_code AS seasonCode, " +
                    "prev.season_id AS prevSeasonId, prev.season_code AS prevSeasonCode " +
                "FROM master_season s " +
                    "LEFT OUTER JOIN master_season prev ON s.previous_season = prev.season_id " +
                "ORDER BY s.previous_season DESC NULLS LAST";
            return con.createQuery(sql).executeAndFetch(Season.class);
        }
    }
}
