package com.prosvirnin.webregistration.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.UUID;

@Service
public class FileService {

    @Value("${upload-file.path}")
    private String path;

    public String saveFile(MultipartFile file) throws IOException {
        if (file == null)
            return null;
        File uploadPath = new File(path);
        if (!uploadPath.exists())
            uploadPath.mkdir();

        String fileName = UUID.randomUUID() + "." + file.getOriginalFilename();
        file.transferTo(new File(path + File.separator + fileName));

        return fileName;
    }

    public Resource getResource(String fileName){
        var filePath = Paths.get(path).resolve(fileName).normalize();
        try {
            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
