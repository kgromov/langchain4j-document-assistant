package org.kgromov.web;

import lombok.RequiredArgsConstructor;
import org.kgromov.assistant.EmbeddingsService;
import org.kgromov.storage.StorageService;
import org.kgromov.storage.StorageFileNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

// TODO: rewrite to vaadin file upload - https://vaadin.com/docs/v14/ds/components/upload
@Controller
@RequestMapping("/documents")
@RequiredArgsConstructor
public class FileUploadController {
    private final StorageService storageService;
    private final EmbeddingsService embeddingsService;

    @PostMapping(value = "/", headers = {"content-type=multipart/*"})
    public String upload(@RequestParam("file") MultipartFile file,
                         RedirectAttributes redirectAttributes) {
        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                file.getOriginalFilename() + " successfully uploaded");
        return "redirect:/";
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) {
        model.addAttribute("files", storageService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(
                                FileUploadController.class,
                                "serveFile",
                                path.getFileName().toString()).build().toUri().toString()
                )
                .collect(Collectors.toList()));
        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        embeddingsService.processDocument(file);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}
