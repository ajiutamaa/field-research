import helpers.DB;
import org.sql2o.Sql2o;
import play.Application;
import play.GlobalSettings;
import play.Logger;

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
}
