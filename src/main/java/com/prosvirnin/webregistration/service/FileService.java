package com.prosvirnin.webregistration.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

@Service
public class FileService {
    public Blob convertMultipartFileToBlob(MultipartFile file) throws IOException, SQLException {
        byte[] fileContent = file.getBytes();
        return new SerialBlob(fileContent);
    }
}
