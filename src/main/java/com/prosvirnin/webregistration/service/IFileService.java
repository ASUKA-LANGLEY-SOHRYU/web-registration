package com.prosvirnin.webregistration.service;

import com.prosvirnin.webregistration.model.Image;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {

    String saveFile(MultipartFile file);

    boolean deleteFileByName(String filename);

   boolean deleteFileByImage(Image image);

    Image saveImage(MultipartFile file);

    byte[] getResource(String fileName);

    byte[] getImageById(Long id);
}
