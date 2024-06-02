package com.prosvirnin.webregistration.service;

import com.prosvirnin.webregistration.model.Image;
import com.prosvirnin.webregistration.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService implements IFileService{

    @Value("${upload-file.path}")
    private String path;

    private final ImageRepository imageRepository;

    @Autowired
    public FileService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public String saveFile(MultipartFile file) {
        if (file == null)
            return null;
        File uploadPath = new File(path);
        if (!uploadPath.exists())
            uploadPath.mkdir();
        String fileName = UUID.randomUUID() + "." + file.getOriginalFilename();
        try {
            file.transferTo(new File(path + File.separator + fileName));
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        return fileName;
    }

    public boolean deleteFileByName(String filename){
        Path location = Paths.get(path).resolve(filename).normalize().toAbsolutePath();
        try {
        if (Files.exists(location))
            Files.delete(location);
        else
            return false;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean deleteFileByImage(Image image){
        return deleteFileByName(image.getFileName());
    }

    public Image saveImage(MultipartFile file) {
        try {
        return Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .fileName(saveFile(file))
                .build();
        } catch (Exception e){
            return null;
        }
    }

    public byte[] getResource(String fileName){
        var filePath = Paths.get(path).resolve(fileName).normalize();
        try {
            return new UrlResource(filePath.toUri()).getContentAsByteArray();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public byte[] getImageById(Long id){
        return getResource(imageRepository.findById(id).orElseThrow().getFileName());
    }
}
