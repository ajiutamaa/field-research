package helpers.fileUtils;

import org.apache.commons.io.FileUtils;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 07/12/2015.
 */
public class StorageUtil {
    public static final String rootDir = "/Field-Visit-Files";

    public static String generateFileUrl(String farmerId, String description) {
        String encodedId = StorageSecurityUtil.encodeToString(farmerId);
        String encodedDesc = StorageSecurityUtil.encodeToString(description);
        String genUrl = "/"+encodedId+"/"+encodedDesc;
        return genUrl;
    }

    public static String generateFarmerDirectoryUrl (String farmerId) {
        String encodedId = StorageSecurityUtil.encodeToString(farmerId);
        String genUrl = "/"+encodedId;
        return genUrl;
    }

    public static Map<String, Object> storeFile (String path, File image, String name) throws IOException {
        Map<String, Object> result = new HashMap<>(4);
        String timestamp = String.valueOf(new Date().getTime());
        String randFileName = StorageSecurityUtil.encodeToString(path)
                .substring(path.length() - 5, path.length()) +
                timestamp;
        int startExt = name.lastIndexOf(".");
        String extension = name.substring(startExt, name.length()).toLowerCase();

        if (!extension.equals(".jpg") && !extension.equals(".jpeg") && !extension.equals(".png")) {
            throw new IOException("Invalid file file format. Only .jpg, .jpeg, or .png accepted");
        }

        // make sure any existing directory removed
        String dirName = rootDir + path;
        FileUtils.deleteDirectory(new File(dirName));

        File newFile = new File(rootDir + path, randFileName+extension);
        BufferedImage bufferedImage = ImageIO.read(image);
        result.put("height", bufferedImage.getHeight());
        result.put("width", bufferedImage.getWidth());
        result.put("size", (image.length() / (1024.0)));

        if ((double) result.get("size") > 1000) {
            throw new IOException("File is larger than 1 MB");
        }

        FileUtils.moveFile(image, newFile);

        result.put("path", "/farmer/fieldWeeklyVisitReportFiles" + path + "/" + randFileName+extension);
        return result;
    }

    public static void deleteFile (String path) throws IOException{
        String dirName = rootDir + path;
        FileUtils.deleteDirectory(new File(dirName));
    }

    public static byte[] readFileBinaries(File file) throws IOException{
        byte [] buffer = new byte [1024];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        FileInputStream inputStream = new FileInputStream(file);

        for (int readNum; (readNum = inputStream.read(buffer)) != -1;) {
            outputStream.write(buffer, 0, readNum);
        }

        inputStream.close();

        return outputStream.toByteArray();
    }
}
