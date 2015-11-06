package models.farmer;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.DB;
import org.sql2o.Connection;

import java.util.Date;

/**
 * Created by lenovo on 11/6/2015.
 */
public class FarmerDetail {
    @JsonProperty("farmer_id")
    public int farmerId;

    @JsonProperty("religion")
    public String religion;

    @JsonProperty("name_of_spouse")
    public String nameOfSpouse;

    @JsonProperty("number_of_dependants")
    public Integer numOfDependants;

    @JsonProperty("number_of_children")
    public Integer numOfChildren;

    @JsonProperty("residential_status")
    public String residentialStatus;

    @JsonProperty("date_of_birth")
    public Date dateOfBirth;

    @JsonProperty("ethnic_group")
    public String ethnicGroup;

    @JsonProperty("years_of_corn_farming")
    public Integer yearsOfCornFarming;

    @JsonProperty("is_transmigrated")
    public Boolean isTransmigrated;

    public static FarmerDetail selectOne (int farmerId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT farmer_id AS farmerId, religion, name_of_spouse AS nameOfSpouse, number_of_dependants AS numOfDependants, " +
                "number_of_children AS numOfChildren, residential_status AS residentialStatus, date_of_birth AS dateOfBirth, " +
                "ethnic_group AS ethnicGroup, years_of_corn_farming AS yearsOfCornFarming, is_transmigrated AS isTransmigrated " +
                "FROM farmer_detail WHERE farmer_id = :farmerId";
            return con.createQuery(sql).addParameter("farmerId", farmerId).executeAndFetchFirst(FarmerDetail.class);
        }
    }

    public static int insert (FarmerDetail farmerDetail) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "INSERT INTO farmer_detail (farmer_id, religion, name_of_spouse, number_of_dependants, number_of_children, residential_status, " +
                    "date_of_birth, ethnic_group, years_of_corn_farming, is_transmigrated, updated_at, created_at) " +
                "VALUES (:farmerId, :religion, :nameOfSpouse, :numOfDependants, :numOfChildren, :residentialStatus, :dateOfBirth, " +
                    ":ethnicGroup, :yearsOfCornFarming, :isTransmigrated, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
            return con.createQuery(sql)
                    .addParameter("farmerId", farmerDetail.farmerId)
                    .addParameter("religion", farmerDetail.religion)
                    .addParameter("nameOfSpouse", farmerDetail.nameOfSpouse)
                    .addParameter("numOfDependants", farmerDetail.numOfDependants)
                    .addParameter("numOfChildren", farmerDetail.numOfChildren)
                    .addParameter("residentialStatus", farmerDetail.residentialStatus)
                    .addParameter("dateOfBirth", farmerDetail.dateOfBirth)
                    .addParameter("ethnicGroup", farmerDetail.ethnicGroup)
                    .addParameter("yearsOfCornFarming", farmerDetail.yearsOfCornFarming)
                    .addParameter("isTransmigrated", farmerDetail.isTransmigrated)
                    .executeUpdate()
                    .getResult();
        }
    }

    public static int update (FarmerDetail farmerDetail) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "UPDATE farmer_detail SET religion = :religion, name_of_spouse = :nameOfSpouse, number_of_dependants = :numOfDependants, " +
                "number_of_children = :numOfChildren, residential_status = :residentialStatus, date_of_birth = :dateOfBirth, " +
                "ethnic_group = :ethnicGroup, years_of_corn_farming = :yearsOfCornFarming, is_transmigrated = :isTransmigrated, " +
                "updated_at = CURRENT_TIMESTAMP WHERE farmer_id = :farmerId";
            return con.createQuery(sql)
                    .addParameter("farmerId", farmerDetail.farmerId)
                    .addParameter("religion", farmerDetail.religion)
                    .addParameter("nameOfSpouse", farmerDetail.nameOfSpouse)
                    .addParameter("numOfDependants", farmerDetail.numOfDependants)
                    .addParameter("numOfChildren", farmerDetail.numOfChildren)
                    .addParameter("residentialStatus", farmerDetail.residentialStatus)
                    .addParameter("dateOfBirth", farmerDetail.dateOfBirth)
                    .addParameter("ethnicGroup", farmerDetail.ethnicGroup)
                    .addParameter("yearsOfCornFarming", farmerDetail.yearsOfCornFarming)
                    .addParameter("isTransmigrated", farmerDetail.isTransmigrated)
                    .executeUpdate()
                    .getResult();
        }
    }

    public static int delete (int farmerId) {
        try (Connection con = DB.sql2o.open()) {
            String sql = "DELETE FROM farmer_detail WHERE farmer_id = :farmerId";
            return con.createQuery(sql).addParameter("farmerId", farmerId).executeUpdate().getResult();
        }
    }
}
