package me.hjeong.mojji.util;

import lombok.extern.slf4j.Slf4j;
import me.hjeong.mojji.domain.Post;
import org.imgscalr.Scalr;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
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
            if(file != null && !file.isEmpty() && file.getSize() != 0 && isImageFile(file.getOriginalFilename())) {
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

    // 업로드한 파일을 읽어서 가로 400인 썸네일 이미지를 만들어 저장한다.
    public static String makeThumbnail(String uploadPath, String filename) throws IOException {
        BufferedImage srcImg = ImageIO.read(new File(uploadPath, filename));
        BufferedImage destImg = Scalr.resize(srcImg, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_WIDTH, 400);

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

    public static void deleteFiles(List<String> fileNames, String dirName) {
        for (String fileName : fileNames) {
            deleteFile(fileName, dirName);
        }
    }

    private static void deleteFile(String fileName, String dirName) {
        File file = new File(dirName, fileName);
        if (file.exists())
            file.delete();

        // 썸네일 이미지도 삭제!
        String thumbnailName = "s_" + fileName;
        File thumbnail = new File(dirName, thumbnailName);
        if(thumbnail.exists())
            thumbnail.delete();
    }

    public static void deleteFolder(String folderPath) {
        log.info("call deleteFolder, path: {}", folderPath);

        File folder = new File(folderPath);
        try {
            while(folder.exists()) {
                File[] files = folder.listFiles(); //파일리스트 얻어오기

                Arrays.stream(files).forEach(file -> {
                    if(file.isFile()) {
                        log.info("파일 {} 을 삭제합니다..", file.getName());
                        file.delete();
                    } else {
                        deleteFolder(file.getPath());
                        log.info("{} 폴더가 삭제되었습니다.", file.getPath());
                    }
                });

                folder.delete();
                log.info("{} 폴더가 삭제되었습니다.", folder.getPath());
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
