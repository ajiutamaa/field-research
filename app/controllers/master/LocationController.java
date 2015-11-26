package controllers.master;

import helpers.security.Secured;
import models.master.Location;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.util.HashMap;
import java.util.Map;

import static play.libs.Json.toJson;

/**
 * Created by lenovo on 11/13/2015.
 */

@Security.Authenticated(Secured.class)
public class LocationController extends Controller{
    public static Result findMasterProvinsi () {
        Map<String, Object> result = new HashMap<>();
        try {
            return ok(toJson(Location.selectProvinsi()));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError (toJson(result));
        }
    }

    public static Result findMasterKabupaten (int provinsiId) {
        Map<String, Object> result = new HashMap<>();
        try {
            return ok(toJson(Location.selectKabupaten(provinsiId)));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError (toJson(result));
        }
    }

    public static Result findMasterKecamatan (int kabupatenId) {
        Map<String, Object> result = new HashMap<>();
        try {
            return ok(toJson(Location.selectKecamatan(kabupatenId)));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError (toJson(result));
        }
    }

    public static Result findMasterDesa (int kecamatanId) {
        Map<String, Object> result = new HashMap<>();
        try {
            return ok(toJson(Location.selectDesa(kecamatanId)));
        } catch (Exception e) {
            result.put("message", e.getMessage());
            return internalServerError (toJson(result));
        }
    }
}
