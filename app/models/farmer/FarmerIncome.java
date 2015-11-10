package models.farmer;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.DB;
import org.sql2o.Connection;

import java.util.List;

/**
 * Created by lenovo on 11/9/2015.
 */
public class FarmerIncome {
    @JsonProperty("income_id")
    public int incomeId;

    @JsonProperty("farmer_id")
    public int farmerId;

    @JsonProperty("source")
    public String source;

    @JsonProperty("amount")
    public double amount;

    public static List<FarmerIncome> select (int farmerId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT income_id AS incomeId, farmer_id AS farmerId, source, amount " +
                "FROM farmer_income WHERE farmer_id = :farmerId";
            return con.createQuery(sql).addParameter("farmerId", farmerId).executeAndFetch(FarmerIncome.class);
        }
    }

    public static FarmerIncome selectOne (int incomeId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT income_id AS incomeId, farmer_id AS farmerId, source, amount " +
                "FROM farmer_income WHERE income_id = :incomeId";
            return con.createQuery(sql).addParameter("incomeId", incomeId).executeAndFetchFirst(FarmerIncome.class);
        }
    }

    public static int insert (FarmerIncome farmerIncome) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "INSERT INTO farmer_income (farmer_id, source, amount) " +
                "VALUES (:farmerId, :source, :amount)";
            return con.createQuery(sql, true)
                    .addParameter("farmerId", farmerIncome.farmerId)
                    .addParameter("source", farmerIncome.source)
                    .addParameter("amount", farmerIncome.amount)
                    .executeUpdate()
                    .getKey(Integer.class);
        }
    }

    public static int update (FarmerIncome farmerIncome) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "UPDATE farmer_income SET source = :source, amount = :amount, " +
                "updated_at = CURRENT_TIMESTAMP WHERE income_id = :incomeId";
            return con.createQuery(sql)
                    .addParameter("incomeId", farmerIncome.incomeId)
                    .addParameter("source", farmerIncome.source)
                    .addParameter("amount", farmerIncome.amount)
                    .executeUpdate()
                    .getResult();
        }
    }

    public static int delete (int incomeId) {
        try (Connection con = DB.sql2o.open()) {
            String sql = "DELETE FROM farmer_income WHERE income_id = :incomeId";
            return con.createQuery(sql).addParameter("incomeId", incomeId).executeUpdate().getResult();
        }
    }
}
