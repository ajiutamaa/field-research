package controllers;

import play.mvc.*;
import static play.libs.Json.toJson;

public class Application extends Controller {
    public static Result hello() {
        return ok(toJson("hello"));
    }
}
