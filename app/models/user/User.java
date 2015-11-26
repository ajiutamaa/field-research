package models.user;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.DB;
import org.sql2o.Connection;
import play.Logger;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * Created by lenovo on 26/11/2015.
 */
public class User {
    @JsonProperty("user_id")
    public int userId;

    @JsonProperty("username")
    public String username;

    @JsonIgnore
    public String password;

    @JsonIgnore
    public String token;

    @JsonIgnore
    public Date tokenExp;

    public static User selectByUserId (int userId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                    "SELECT id AS userId, username, password, token, token_exp AS tokenExp FROM ci_agri_user " +
                            "WHERE id = :userId";
            return con.createQuery(sql)
                    .addParameter("userId", userId)
                    .executeAndFetchFirst(User.class);
        }
    }

    public static User selectByUsernamePassword (String username, String password) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT id AS userId, username, password, token, token_exp AS tokenExp FROM ci_agri_user " +
                "WHERE username = :username AND password = :password";
            return con.createQuery(sql)
                    .addParameter("username", username)
                    .addParameter("password", password)
                    .executeAndFetchFirst(User.class);
        }
    }

    public static int insert (String username, String password) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "INSERT INTO ci_agri_user(username, password, created_at, updated_at) " +
                "VALUES (:username, :password, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
            return con.createQuery(sql, true)
                    .addParameter("username", username)
                    .addParameter("password", password)
                    .executeUpdate()
                    .getKey(Integer.class);
        }
    }

    public static int update (int userId, String username, String password, String newToken, Date newTokenExp) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "UPDATE ci_agri_user SET username = :username, password = :password, token = :newToken, token_exp = :newTokenExp, " +
                "updated_at = CURRENT_TIMESTAMP WHERE id = :userId";
            return con.createQuery(sql)
                    .addParameter("userId", userId)
                    .addParameter("username", username)
                    .addParameter("password", password)
                    .addParameter("newToken", newToken)
                    .addParameter("newTokenExp", newTokenExp)
                    .executeUpdate()
                    .getResult();
        }
    }

    public String generateToken () {
        Config config = ConfigFactory.load();
        token = UUID.randomUUID().toString();
        tokenExp = new Date(System.currentTimeMillis() + (config.getInt("app.tokenexp") * 1000));
        // update user token together with its expiration time
        update(this.userId, this.username, this.password, token, tokenExp);
        return token;
    }

    public int updateTokenExp () {
        Config config = ConfigFactory.load();
        tokenExp = new Date(System.currentTimeMillis() + (config.getInt("app.tokenexp") * 1000));
        // update user token expiration time
        return update(this.userId, this.username, this.password, this.token, tokenExp);
    }

    public int deleteToken () {
        token = null;
        tokenExp = null;
        return update(this.userId, this.username, this.password, token, tokenExp);
    }
}
