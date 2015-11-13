import helpers.DB;
import org.sql2o.Sql2o;
import play.Application;
import play.GlobalSettings;
import play.Logger;

import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.lang.reflect.Method;
/**
 * Created by lenovo on 11/5/2015.
 */
public class Global extends GlobalSettings {
    @Override
    public void onStart(Application application) {
        super.beforeStart(application);
        Logger.debug("--- app started ---");
        try {
            DB.sql2o = new Sql2o("jdbc:postgresql://192.168.10.188:5433/ci-research", "postgres", "");
            Logger.debug("Sql2o: connection created");
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }
        Logger.debug("--- app ready ---");
    }

    @Override
    public Action onRequest(Http.Request request, Method method) {
        return new ActionWrapper(super.onRequest(request, method));
    }

    /** for CORS */
    private class ActionWrapper extends Action.Simple {

        public ActionWrapper(Action<?> action) {
            this.delegate = action;
        }

        @Override
        public F.Promise<Result> call(Http.Context context) throws Throwable {
            F.Promise<Result> result = this.delegate.call(context);
            Http.Response response = context.response();
            response.setHeader("Access-Control-Allow-Origin", "*");
            return result;
        }
    }
}
