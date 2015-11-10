package models.farmer;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.DB;
import org.sql2o.Connection;

/**
 * Created by lenovo on 11/9/2015.
 */
public class FarmerOrganizationExperience {
    @JsonProperty("farmer_id")
    public int farmerId;

    @JsonProperty("type")
    public String type;

    @JsonProperty("name")
    public String name;

    @JsonProperty("position")
    public String position;

    @JsonProperty("year_joined")
    public int yearJoined;

    @JsonProperty("description")
    public String description;

    public static FarmerOrganizationExperience selectOne (int farmerId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT farmer_id AS farmerId, type, name, position, year_joined, activity_description " +
                "FROM farmer_organization_experience WHERE farmer_id = :farmerId";
            return con.createQuery(sql).addParameter("farmerId", farmerId)
                    .executeAndFetchFirst(FarmerOrganizationExperience.class);
        }
    }

    public static int insert (FarmerOrganizationExperience farmerWorkshopExperience) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "INSERT INTO farmer_asset (farmer_id, type, name, position, year_joined, activity_description) " +
                "VALUES (:farmerId, :type, :name, :position, :yearJoined, :description)";
            return con.createQuery(sql)
                    .addParameter("farmerId", farmerWorkshopExperience.farmerId)
                    .addParameter("type", farmerWorkshopExperience.type)
                    .addParameter("name", farmerWorkshopExperience.name)
                    .addParameter("position", farmerWorkshopExperience.position)
                    .addParameter("yearJoined", farmerWorkshopExperience.yearJoined)
                    .addParameter("description", farmerWorkshopExperience.description)
                    .executeUpdate()
                    .getResult();
        }
    }

    public static int update (FarmerOrganizationExperience farmerWorkshopExperience) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "UPDATE farmer_asset SET type = :type, name = :name, position = :position, year_joined = :yearJoined, " +
                "activity_description = :description, updated_at = CURRENT_TIMESTAMP WHERE farmer_id = :farmerId";
            return con.createQuery(sql)
                    .addParameter("farmerId", farmerWorkshopExperience.farmerId)
                    .addParameter("type", farmerWorkshopExperience.type)
                    .addParameter("name", farmerWorkshopExperience.name)
                    .addParameter("position", farmerWorkshopExperience.position)
                    .addParameter("yearJoined", farmerWorkshopExperience.yearJoined)
                    .addParameter("description", farmerWorkshopExperience.description)
                    .executeUpdate()
                    .getResult();
        }
    }

    public static int delete (int farmerId) {
        try (Connection con = DB.sql2o.open()) {
            String sql = "DELETE FROM farmer_organization_experience WHERE farmer_id = :farmerId";
            return con.createQuery(sql).addParameter("farmerId", farmerId).executeUpdate().getResult();
        }
    }
}
