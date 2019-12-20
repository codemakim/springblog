package kr.jhkim.springblog.controller;

import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kr.jhkim.springblog.domain.UploadFile;
import kr.jhkim.springblog.service.ImageService;
import kr.jhkim.springblog.utils.MediaUtils;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping(value = "/image")
@AllArgsConstructor
public class ImageController {

  private ImageService imageService;

  /**
   * 업로드 되어있는 모든 파일 정보 목록을 model에 담아 페이지를 반환하는 컨트롤러입니다.
   * 
   * @param model
   * @return
   */
  @GetMapping(value = "")
  public String listUploadFile(Model model) {
    model.addAttribute("files", imageService.loadAll().collect(Collectors.toList()));
    return "/list";
  }

  /**
   * 업로드한 이미지를 테이블과 디스크에 저장하고 다운로드(조회) 경로를 응답객체에 담아 반환하는 컨트롤러입니다.
   * 
   * @param file
   * @return
   */
  @PostMapping(value = "")
  public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) {
    try {
      UploadFile uploadFile = imageService.store(file);
      return ResponseEntity.ok().body("/image/" + uploadFile.getId());
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * 이미지를 조회하기 위한 컨트롤러입니다.
   * 
   * @param id
   * @return
   */
  @GetMapping(value = "{id}")
  public ResponseEntity<?> serveFile(@PathVariable(value = "id") Long id) {
    try {
      UploadFile uploadedFile = imageService.load(id);
      HttpHeaders headers = new HttpHeaders();

      String fileName = uploadedFile.getFileName();
      headers.add(HttpHeaders.CONTENT_DISPOSITION,
          "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");

      if (MediaUtils.containsImageMediaType(uploadedFile.getContentType())) {
        headers.setContentType(MediaType.valueOf(uploadedFile.getContentType()));
      } else {
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
      }

      Resource resource = imageService.loadAsResource(uploadedFile.getSaveFileName());
      return ResponseEntity.ok().headers(headers).body(resource);

    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * 
   * @return
   */
  @ResponseBody
  @GetMapping(value = "delete/{id}")
  public ResponseEntity<?> deleteImage(@PathVariable(value = "id") Long id) {
    UploadFile targetFile = imageService.load(id);
    try {
      if (imageService.deleteResource(targetFile.getFilePath())) {
        imageService.deleteFIleFromTable(id);
        return ResponseEntity.ok().body(targetFile);
      } else {
        return ResponseEntity.badRequest().build();
      }
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().build();
    }
  }

}