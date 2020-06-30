package me.hjeong.mojji.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hjeong.mojji.config.AppProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileUploadService {

    private final AppProperties appProperties;

    public Set<String> restoreList(Set<MultipartFile> files) {
        Set<String> urlList = new HashSet<>();
        for(MultipartFile file : files) {
            if(!file.isEmpty()) {
                urlList.add(this.restore(file));
            }
        }
        return urlList;
    }

    private String restore(MultipartFile file) {
        String url = null;
        try {
            String originFilename = file.getOriginalFilename();
            String extName = originFilename.substring(originFilename.lastIndexOf("."), originFilename.length());
            String saveFileName = genSaveFileName(extName);
            file.transferTo(new File(appProperties.getUploadPath() + "/" + saveFileName));
            url = "/upload/" + saveFileName;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return url;
    }

    private String genSaveFileName(String extName) {
        String fileName = "";

        Calendar calendar = Calendar.getInstance();
        fileName += calendar.get(Calendar.YEAR);
        fileName += calendar.get(Calendar.MONTH);
        fileName += calendar.get(Calendar.DATE);
        fileName += calendar.get(Calendar.HOUR);
        fileName += calendar.get(Calendar.MINUTE);
        fileName += calendar.get(Calendar.SECOND);
        fileName += calendar.get(Calendar.MILLISECOND);
        fileName += extName;

        return fileName;
    }
}
