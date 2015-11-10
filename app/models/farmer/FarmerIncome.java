package models.farmer;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.DB;
import org.sql2o.Connection;

/**
 * Created by lenovo on 11/9/2015.
 */
public class FarmerIncome {
    @JsonProperty("farmer_id")
    public int farmerId;

    @JsonProperty("source")
    public String source;

    @JsonProperty("amount")
    public double amount;

    public static FarmerIncome selectOne (int farmerId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT farmer_id AS farmerId, source, amount " +
                "FROM farmer_income WHERE farmer_id = :farmerId";
            return con.createQuery(sql).addParameter("farmerId", farmerId).executeAndFetchFirst(FarmerIncome.class);
        }
    }

    public static int insert (FarmerIncome farmerIncome) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "INSERT INTO farmer_income (farmer_id, source, amount) " +
                "VALUES (:farmerId, :source, :amount)";
            return con.createQuery(sql)
                    .addParameter("farmerId", farmerIncome.farmerId)
                    .addParameter("source", farmerIncome.source)
                    .addParameter("amount", farmerIncome.amount)
                    .executeUpdate()
                    .getResult();
        }
    }

    public static int update (FarmerIncome farmerIncome) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "UPDATE farmer_income SET source = :source, amount = :amount " +
                "updated_at = CURRENT_TIMESTAMP WHERE farmer_id = :farmerId";
            return con.createQuery(sql)
                    .addParameter("farmerId", farmerIncome.farmerId)
                    .addParameter("source", farmerIncome.source)
                    .addParameter("amount", farmerIncome.amount)
                    .executeUpdate()
                    .getResult();
        }
    }

    public static int delete (int farmerId) {
        try (Connection con = DB.sql2o.open()) {
            String sql = "DELETE FROM farmer_income WHERE farmer_id = :farmerId";
            return con.createQuery(sql).addParameter("farmerId", farmerId).executeUpdate().getResult();
        }
    }
}
