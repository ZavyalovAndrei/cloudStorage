package com.cloudStorage.controller;

import com.cloudStorage.entity.FileToCloud;
import com.cloudStorage.exeptions.StorageException;
import com.cloudStorage.service.FilesService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
@Validated
public class FilesController {
    private static final String FILE = "/file";
    private static final String LIST = "/list";

    private final FilesService filesService;

    public FilesController(FilesService filesService) {
        this.filesService = filesService;
    }

    @PostMapping(value = FILE, produces = MediaType.APPLICATION_JSON_VALUE, consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadFile(@RequestHeader("auth-token") String authToken,
                                        @Valid @RequestParam String filename,
                                        @RequestBody MultipartFile file) throws IOException, StorageException {
        filesService.uploadFile(authToken, filename, file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = FILE, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> downloadFile(@RequestHeader("auth-token") String authToken,
                                               @Valid @RequestParam String filename) throws StorageException {
        FileToCloud file = filesService.downloadFile(authToken, filename);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(file.getData());
    }

    @GetMapping(LIST)
    public List<FileToCloud> getFiles(@RequestHeader("auth-token") String authToken, @RequestParam("limit") int limit)
            throws StorageException {
        return filesService.getFiles(authToken, limit);
    }

    @DeleteMapping(FILE)
    public ResponseEntity<?> deleteFile(@RequestHeader("auth-token") String token,
                                        @RequestParam("filename") String fileName) throws StorageException {
        filesService.deleteFile(token, fileName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = FILE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editFile(@RequestHeader("auth-token") String authToken,
                                      @Valid @RequestParam String filename,
                                      @RequestBody Map<String, String> bodyParams) throws StorageException {
        filesService.renameFile(authToken, filename, bodyParams.get("filename"));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}