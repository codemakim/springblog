package kr.jhkim.springblog.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.jhkim.springblog.domain.UploadFile;
import kr.jhkim.springblog.repository.FileRepository;
import kr.jhkim.springblog.utils.UploadFileUtils;

@Service
public class ImageService {

  private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

  // 배포
  // private final Path rootLocation =
  // Paths.get("/home/ubuntu/app/springblog/uploads/");
  // 개발
  private final Path rootLocation = Paths.get("c:/springblog.uploads");

  @Autowired
  FileRepository fileRepository;

  public Stream<Long> loadAll() {
    List<UploadFile> files = fileRepository.findAll();
    return files.stream().map(file -> file.getId());
  }

  public UploadFile load(Long fileId) {
    return fileRepository.findOneById(fileId);
  }

  public void deleteFIleFromTable(Long id) {
    fileRepository.deleteById(id);
  }

  /**
   * 파일명을 이용해 파일 정보를 찾아내 반환합니다.
   * 
   * @param fileName
   * @return
   * @throws Exception
   */
  public Resource loadAsResource(String fileName) throws Exception {
    try {
      if (fileName.toCharArray()[0] == '/') {
        fileName = fileName.substring(1);
      }

      Path file = loadPath(fileName);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new Exception("Could not read file: " + fileName);
      }
    } catch (Exception e) {
      throw new Exception("Could not read file: " + fileName);
    }
  }

  /**
   * 파일 이름을 전달받아 해당 파일을 삭제하하는 메소드입니다.
   * 
   * @param fileName
   * @return
   * @throws Exception
   */
  public boolean deleteResource(String filePath) throws Exception {
    try {
      File file = new File(filePath);
      return file.delete();

    } catch (Exception e) {
      throw new Exception("Could not delete file: id " + filePath);
    }
  }

  /**
   * 파일 이름을 전달받아 해당 파일의 저장 위치를 반환합니다.
   * 
   * @param fileName
   * @return
   */
  private Path loadPath(String fileName) {
    return rootLocation.resolve(fileName);
  }

  public UploadFile store(MultipartFile file) throws Exception {
    try {
      if (file.isEmpty()) {
        throw new Exception("Failed to store empty file " + file.getOriginalFilename());
      }

      String saveFileName = UploadFileUtils.fileSave(rootLocation.toString(), file);

      if (saveFileName.toCharArray()[0] == '/') {
        saveFileName = saveFileName.substring(1);
      }

      Resource resource = loadAsResource(saveFileName);

      UploadFile saveFile = UploadFile.builder().saveFileName(saveFileName).fileName(file.getOriginalFilename())
          .contentType(file.getContentType())
          .filePath(rootLocation.toString().replace(File.separatorChar, '/') + File.separator + saveFileName)
          .size(resource.contentLength()).regDate(new Date()).build();
      saveFile = fileRepository.save(saveFile);

      return saveFile;
    } catch (IOException e) {
      throw new Exception("Failed to store file " + file.getOriginalFilename(), e);
    }
  }
}