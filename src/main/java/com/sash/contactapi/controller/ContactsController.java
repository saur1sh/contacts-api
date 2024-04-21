package com.sash.contactapi.controller;

import com.sash.contactapi.model.Contact;
import com.sash.contactapi.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.sash.contactapi.constant.Constant.PHOTO_DIRECTORY;
import static org.springframework.util.MimeTypeUtils.IMAGE_JPEG_VALUE;
import static org.springframework.util.MimeTypeUtils.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactsController {
    public final ContactService contactService;

    @GetMapping
    public ResponseEntity<Page<Contact>> getContacts(@RequestParam(value="page", defaultValue = "0") int page,
                                                     @RequestParam(value="size", defaultValue = "10") int size) {
        return ResponseEntity.ok(contactService.getAllContacts(page, size));
    }

    @PostMapping
    public ResponseEntity createContact(@RequestBody Contact contact) {
        return ResponseEntity.created(URI.create("/contact-api/contacts/userID")).body(contactService.createContact(contact));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(contactService.getContact(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteContact(@PathVariable(value = "id") String id) {
        contactService.deleteById(id);
        return ResponseEntity.ok().body("Deleted contact");
    }

    @PutMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("id") String id, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().body(contactService.uploadPhoto(id, file));
    }

    @GetMapping(path = "/image/{filename}", produces = {IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE})
    public byte[] getPhoto(@PathVariable(value = "filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(PHOTO_DIRECTORY + filename));
    }
}
