package com.prosvirnin.webregistration.service.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.prosvirnin.webregistration.model.Image;
import com.prosvirnin.webregistration.repository.ImageRepository;
import com.prosvirnin.webregistration.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class S3Service implements IFileService {
    private final AmazonS3 amazonS3;
    private final String bucketName = "web123";
    private final ImageRepository imageRepository;

    @Autowired
    public S3Service(AmazonS3 amazonS3, ImageRepository imageRepository) {
        this.amazonS3 = amazonS3;
        this.imageRepository = imageRepository;
    }

    private String uploadFile(MultipartFile file){
        String key = UUID.randomUUID() + "." + file.getOriginalFilename();
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());

            amazonS3.putObject(bucketName, key, file.getInputStream(), metadata);

            return key;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    private String getFileUrl(String fileName) {
        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    private S3Object getFile(String fileName) {
        return amazonS3.getObject(bucketName, fileName);
    }

    private void deleteFile(String fileName) {
        try {
            amazonS3.deleteObject(bucketName, fileName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file from S3", e);
        }
    }

    @Override
    public String saveFile(MultipartFile file) {
        return uploadFile(file);
    }

    @Override
    public boolean deleteFileByName(String filename) {
        deleteFile(filename);
        return true;
    }

    @Override
    public boolean deleteFileByImage(Image image) {
        return deleteFileByName(image.getFileName());
    }

    @Override
    public Image saveImage(MultipartFile file) {
        return Image.builder()
            .name(file.getOriginalFilename())
            .type(file.getContentType())
            .fileName(saveFile(file))
            .build();
    }

    @Override
    public byte[] getResource(String fileName) {
        S3Object s3Object = getFile(fileName);
        InputStream inputStream = s3Object.getObjectContent();
        try {
            return inputStream.readAllBytes();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return new byte[0];
    }

    @Override
    public byte[] getImageById(Long id){
        return getResource(imageRepository.findById(id).orElseThrow().getFileName());
    }
}
