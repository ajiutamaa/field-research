package controllers.user;

import com.fasterxml.jackson.databind.JsonNode;
import helpers.InputValidator;
import helpers.security.Secured;
import models.user.User;
import play.Logger;
import play.mvc.Controller;
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
public class SecurityController extends Controller {
    public static final String AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";
    private static final String[] FIELD_REGISTER_LOGIN = {"username", "password"};

    public static Result register () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, FIELD_REGISTER_LOGIN);

            String username = jsonNode.get("username").asText();
            String password = jsonNode.get("password").asText();
            password = Base64.getEncoder().encodeToString(password.getBytes());

            int userId = User.insert(username, password);

            result.put("id", userId);
            result.put("username", username);

            return ok(toJson(result));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return badRequest(toJson(result));
        }
    }

    public static Result login () {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode jsonNode = request().body().asJson();
            InputValidator inputValidator = new InputValidator();
            inputValidator.checkInputFields(jsonNode, FIELD_REGISTER_LOGIN);

            Date currTime = new Date(System.currentTimeMillis());

            String username = jsonNode.get("username").asText();
            String password = jsonNode.get("password").asText();
            password = Base64.getEncoder().encodeToString(password.getBytes());

            User user = User.selectByUsernamePassword(username, password);

            if (user == null) {
                result.put("message", "invalid username or password");
                return unauthorized(toJson(result));
            } else {
                String authToken = null;
                if (currTime.before(user.tokenExp)) {
                    authToken = user.token;
                    user.updateTokenExp();
                } else {
                    authToken = user.generateToken();
                }

                Map<String, Object> fullToken = new HashMap<String, Object>();
                fullToken.put("user_id", user.userId);
                fullToken.put("token", authToken);

                String base64Token = Base64.getEncoder().encodeToString(toJson(fullToken).toString().getBytes());

                result.put("user_id", user.userId);
                result.put("token", base64Token);
                result.put("username", user.username);

                return ok(toJson(result));
            }

        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError(toJson(result));
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result logout () {
        Map<String, Object> result = new HashMap<>();
        try {
            ((User) Http.Context.current().args.get("user")).deleteToken();
            result.put("message", "successfully logout");
            return ok(toJson(result));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError(toJson(result));
        }
    }
}
