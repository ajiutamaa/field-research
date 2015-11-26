package helpers.security;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.user.SecurityController;
import models.user.User;
import play.Logger;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static play.libs.Json.toJson;
/**
 * Created by lenovo on 26/11/2015.
 */
public class Secured extends Security.Authenticator {
    @Override
    public String getUsername(Http.Context context) {
        try {
            String[] authHeader = context.request().headers().get("X-AUTH-TOKEN");
            if (authHeader == null || authHeader.length != 1 || authHeader[0] == null) {
                return null;
            }

            JsonNode jsonNode = Json.parse(new String(Base64.getDecoder().decode(authHeader[0])));

            int userId = jsonNode.get("user_id").asInt();
            String token = jsonNode.get("token").asText();
            Date currentTime = new Date(System.currentTimeMillis());

            User user = User.selectByUserId(userId);

            if (token.equals(user.token) && currentTime.before(user.tokenExp)) {
                user.updateTokenExp();
                context.args.put("user", user);
                return user.token;
            } else {
                // invalid token
                return null;
            }
        } catch (Exception e) {
            // internal error occured
            return null;
        }
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "user not authorized");

        context.response().setHeader("Access-Control-Allow-Origin", "*");
        return unauthorized(toJson(result));
    }
}
