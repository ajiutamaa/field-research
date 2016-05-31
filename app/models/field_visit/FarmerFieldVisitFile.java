package models.field_visit;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.DB;
import helpers.fileUtils.StorageUtil;
import org.sql2o.Connection;
import play.Logger;
import play.libs.F;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 07/12/2015.
 */
public class FarmerFieldVisitFile {
    @JsonProperty("id")
    public int id;

    @JsonProperty("weekly_visit_id")
    public int weeklyVisitId;

    @JsonProperty("description")
    public String description;

    @JsonProperty("status")
    public String status;

    @JsonProperty("path")
    public String path;

    @JsonProperty("created_at")
    public Date createdAt;

    @JsonProperty("updated_at")
    public Date updatedAt;

    public static List<FarmerFieldVisitFile> select (int weeklyVisitId, String description) {
        //TODO: only original files considered for the time being
        try (Connection con = DB.sql2o.open()) {
            String descRegex = (description == null) ? "%" : ("%" + description);
            String sql = "SELECT id, weekly_visit_id AS weeklyVisitId, description, status, path, " +
                    "created_at AS createdAt, updated_at AS updatedAt " +
                    "FROM field_visit_file WHERE " +
                    "weekly_visit_id = :weeklyVisitId AND description LIKE :descRegex";
            return con.createQuery(sql)
                    .addParameter("weeklyVisitId", weeklyVisitId)
                    .addParameter("descRegex", descRegex)
                    .executeAndFetch(FarmerFieldVisitFile.class);
        }
    }

    public static String selectZippedFileName (int weeklyVisitId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "SELECT farmer.name || '_' || report.observation_date AS filename " +
                "FROM weekly_visit_report report, farmer_field_visit visit, farmer " +
                "WHERE report.weekly_visit_id = :visitId AND " +
                    "report.farmer_field_visit_id = visit.farmer_field_visit_id AND " +
                    "visit.farmer_id = farmer.id";
            return con.createQuery(sql)
                    .addParameter("visitId", weeklyVisitId)
                    .executeAndFetchFirst(String.class);
        }
    }

    public static String insertOrUpdate (int weeklyVisitId, String desc, File imgFile, String fileName) throws IOException {
        String filePath = StorageUtil.generateFileUrl(String.valueOf(weeklyVisitId), desc);
        Map<String, Object> storingResult = StorageUtil.storeFile(filePath, imgFile, fileName);

        String fileUrl = storingResult.get("path").toString();
        double imageWidth = (int) storingResult.get("width");
        double imageHeight = (int) storingResult.get("height");
        double imageSize = (double) storingResult.get("size");

        int result = update(weeklyVisitId, desc, "original", fileUrl, imageSize, imageWidth, imageHeight);
        if (result < 1) insert(weeklyVisitId, desc, "original", fileUrl, imageSize, imageWidth, imageHeight);

        return fileUrl;
    }

    private static int insert (int weeklyVisitId, String desc, String status , String path,
                               double size, double width, double height) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                "INSERT INTO field_visit_file (weekly_visit_id, description, status, path, kb_size, width, height, " +
                    "created_at, updated_at) VALUES " +
                    "(:weeklyVisitId, :description, :status, :path, :size, :width, :height, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
            return con.createQuery(sql)
                    .addParameter("weeklyVisitId", weeklyVisitId)
                    .addParameter("description", desc)
                    .addParameter("status", status)
                    .addParameter("path", path)
                    .addParameter("size", size)
                    .addParameter("width", width)
                    .addParameter("height", height)
                    .executeUpdate()
                    .getResult();
        }
    }

    private static int update (int weeklyVisitId, String desc, String status , String path,
                               double size, double width, double height) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                    "UPDATE field_visit_file SET path = :path, kb_size = :size, width = :width, height = :height, " +
                            "updated_at = CURRENT_TIMESTAMP WHERE weekly_visit_id = :weeklyVisitId AND " +
                            "description = :description AND status = :status";
            return con.createQuery(sql)
                    .addParameter("weeklyVisitId", weeklyVisitId)
                    .addParameter("description", desc)
                    .addParameter("status", status)
                    .addParameter("path", path)
                    .addParameter("size", size)
                    .addParameter("width", width)
                    .addParameter("height", height)
                    .executeUpdate()
                    .getResult();
        }
    }

    public static int delete (int weeklyVisitId, String description) throws Exception{
        try (Connection con = DB.sql2o.open()) {
            String filePath;
            // delete all farmer files
            if (description == null) {
                filePath = StorageUtil.generateFarmerDirectoryUrl(String.valueOf(weeklyVisitId));
            }
            // delete only specified file
            else {
                filePath = StorageUtil.generateFileUrl(String.valueOf(weeklyVisitId), description);
            }

            Path path = Paths.get(StorageUtil.rootDir + filePath);
            File file = path.toFile();
            if (!file.exists()) {
                throw new IOException("directory does not exist");
            }

            StorageUtil.deleteFile(filePath);

            String descRegex = (description == null) ? "%" : ("%" + description);
            String sql = "DELETE FROM field_visit_file WHERE weekly_visit_id = :weeklyVisitId AND description LIKE :descRegex";
            return con.createQuery(sql)
                    .addParameter("weeklyVisitId", weeklyVisitId)
                    .addParameter("descRegex", descRegex)
                    .executeUpdate()
                    .getResult();
        }
    }
}
