package controllers;

import models.field_visit.WeeklyPlantSample;
import play.mvc.*;
import static play.libs.Json.toJson;

public class Application extends Controller {
    public static Result hello() {
        return ok("Hello");
    }

    public static Result preflight(String all) {
        response().setHeader("Access-Control-Allow-Origin", "*");
        response().setHeader("Allow", "*");
        response().setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
        response().setHeader("Access-Control-Allow-Headers", "Origin, X-AUTH-TOKEN, X-Requested-With, Content-Type, Accept, Referer, User-Agent");
        return ok();
    }
}
