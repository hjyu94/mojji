package me.hjeong.mojji.util;

import me.hjeong.mojji.domain.Post;
import org.imgscalr.Scalr;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtils {
    private static Map<String, MediaType> mediaMap;
    static { // 생성자가 없기 때문에, FileUtils 를 new 해서 만들어 쓸 게 아니기 때문에 메모리에 올라올 때 static 일 때 처리한다
        mediaMap = new HashMap<>();
        mediaMap.put("JPG", MediaType.IMAGE_JPEG);
        mediaMap.put("JPEG", MediaType.IMAGE_JPEG);
        mediaMap.put("GIF", MediaType.IMAGE_GIF);
        mediaMap.put("PNG", MediaType.IMAGE_PNG);
    }
    public static MediaType getMediaType(String ext) {
        return mediaMap.get(ext.toUpperCase());
    }

    public static String storeFile(MultipartFile file, String uploadPath) throws IOException {
        String fileName = file.getOriginalFilename();
        File target = new File(uploadPath, fileName);
        FileCopyUtils.copy(file.getBytes(), target);
        makeThumbnail(uploadPath, fileName);
        return fileName;
    }

    public static List<String> storeFiles(List<MultipartFile> files, String uploadPath) throws IOException {
        List<String> fileNameList = new ArrayList<>();
        for(MultipartFile file : files) {
            if(file != null && !file.isEmpty() && file.getSize() != 0 && isImageFile(file.getName())) {
                fileNameList.add(storeFile(file, uploadPath));
            }
        }
        return fileNameList;
    }

    public static boolean isImageFile (String fileName) {
        String ext = getFileExtension(fileName); // jpg, png, bmp
        return ext.matches("JPG|JPEG|PNG|GIF");
    }

    public static String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
        // mime 체크할 때 편하게 하도록 대문자로 설정
    }

    // 업로드한 파일을 읽어서 세로 100인 썸네일 이미지를 만들어 저장한다.
    public static String makeThumbnail(String uploadPath, String filename) throws IOException {
        BufferedImage srcImg = ImageIO.read(new File(uploadPath, filename));
        BufferedImage destImg = Scalr.resize(srcImg, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_HEIGHT, 100);
        // width: FIT_TO_HEIGHT, height: 100

        String thumbnailName = "s_" + filename;
        String ext = getFileExtension(filename); // "PNG"
        File newFile = new File(uploadPath, thumbnailName);

        ImageIO.write(destImg, ext, newFile);
        return thumbnailName;
    }

    public static String getPostUploadPath(String uploadPath, Post post) {
        String postUploadPath = uploadPath
                + File.separator + post.getAccount().getNickname()
                + File.separator + post.getId().toString();
        File file = new File(postUploadPath);
        if(!file.exists())
            file.mkdirs();
        return postUploadPath;
    }

}
