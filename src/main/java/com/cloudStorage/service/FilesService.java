package com.cloudStorage.service;

import com.cloudStorage.entity.FileToCloud;
import com.cloudStorage.exeptions.StorageException;
import com.cloudStorage.repository.CloudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FilesService {
    private final CloudRepository cloudRepository;

    @Autowired
    public FilesService(CloudRepository cloudRepository) {
        this.cloudRepository = cloudRepository;
    }

    public FileToCloud downloadFile(String authToken, String fileName) throws StorageException {
        return cloudRepository.downloadFile(authToken, fileName).orElseThrow(() ->
                new StorageException("Error download file " + fileName));
    }

    public List<FileToCloud> getFiles(String authToken, int limit) throws StorageException {
        return cloudRepository.getFiles(authToken, limit).orElseThrow(() ->
                new StorageException("Error getting file list"));
    }

    public void uploadFile(String authToken, String fileName, MultipartFile file) throws IOException, StorageException {
        FileToCloud cloudFile = new FileToCloud(fileName, file.getContentType(), file.getBytes(), file.getSize());
        cloudRepository.uploadFile(cloudFile, authToken).orElseThrow(() ->
                new StorageException("Couldn't save the file " + fileName));
    }

    public void deleteFile(String authToken, String fileName) throws StorageException {
        cloudRepository.deleteFile(authToken, fileName);
    }

    public void renameFile(String authToken, String fileName, String newFileName) throws StorageException {
        cloudRepository.renameFile(authToken, fileName, newFileName).orElseThrow(() ->
                new StorageException("Error edit file " + fileName));
    }
}