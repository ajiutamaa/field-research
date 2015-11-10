package models.farmer;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.DB;
import org.sql2o.Connection;

/**
 * Created by lenovo on 11/9/2015.
 */
public class FarmerWorkshopExperience {
    @JsonProperty("farmer_id")
    public int farmerId;

    @JsonProperty("type")
    public String type;

    @JsonProperty("organizer")
    public String organizer;

    @JsonProperty("year")
    public int year;

    @JsonProperty("description")
    public String description;

    public static FarmerWorkshopExperience selectOne (int farmerId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT farmer_id AS farmerId, type, organizer, year, activity_description " +
                "FROM farmer_workshop_experience WHERE farmer_id = :farmerId";
            return con.createQuery(sql).addParameter("farmerId", farmerId)
                    .executeAndFetchFirst(FarmerWorkshopExperience.class);
        }
    }

    public static int insert (FarmerWorkshopExperience farmerWorkshopExperience) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "INSERT INTO farmer_asset (farmer_id, type, organizer, year, activity_description) " +
                "VALUES (:farmerId, :type, :organizer, :year, :description)";
            return con.createQuery(sql)
                    .addParameter("farmerId", farmerWorkshopExperience.farmerId)
                    .addParameter("type", farmerWorkshopExperience.type)
                    .addParameter("organizer", farmerWorkshopExperience.organizer)
                    .addParameter("year", farmerWorkshopExperience.year)
                    .addParameter("description", farmerWorkshopExperience.description)
                    .executeUpdate()
                    .getResult();
        }
    }

    public static int update (FarmerWorkshopExperience farmerWorkshopExperience) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "UPDATE farmer_asset SET type = :type, organizer = :organizer, year = :year, " +
                "activity_description = :description, updated_at = CURRENT_TIMESTAMP WHERE farmer_id = :farmerId";
            return con.createQuery(sql)
                    .addParameter("farmerId", farmerWorkshopExperience.farmerId)
                    .addParameter("type", farmerWorkshopExperience.type)
                    .addParameter("organizer", farmerWorkshopExperience.organizer)
                    .addParameter("year", farmerWorkshopExperience.year)
                    .addParameter("description", farmerWorkshopExperience.description)
                    .executeUpdate()
                    .getResult();
        }
    }

    public static int delete (int farmerId) {
        try (Connection con = DB.sql2o.open()) {
            String sql = "DELETE FROM farmer_workshop_experience WHERE farmer_id = :farmerId";
            return con.createQuery(sql).addParameter("farmerId", farmerId).executeUpdate().getResult();
        }
    }
}
