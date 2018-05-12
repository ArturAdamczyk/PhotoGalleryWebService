package com.photogallery.photogallerywebservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class PhotoController {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private PhotoService photoService;

    @GetMapping("/getPhotos")
    public ResponseEntity<List<Photo>> getPhotos() {
        List<Photo> photosList = photoRepository.findAll();
        for(Photo photo: photosList){
            photo.setExifData(photoService.extractExifData(photo.getImage()));
        }
        return ResponseEntity
                .ok()
                .body(photosList);
    }

    @GetMapping("/getPhoto/{id}")
    public ResponseEntity<Photo> getPhoto(@PathVariable("id") long id) {
        Optional<Photo> photo = photoRepository.findById(id);
        if (photo.isPresent()) {
            photo.get().setExifData(photoService.extractExifData(photo.get().getImage()));
            return ResponseEntity
                    .ok()
                    .body(photo.get());
        }else{
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }


    @PostMapping(value = "/uploadPhoto", consumes = "multipart/form-data")
    public ResponseEntity<Photo> uploadPhoto(@RequestParam("picture") MultipartFile file) {
        try{
            Photo photo = new Photo();
            photo.setName(file.getOriginalFilename());
            photo.setImage(file.getBytes());
            Photo returnedPhoto = photoRepository.getOne(photoRepository.save(photo).getId());
            returnedPhoto.setExifData(photoService.extractExifData(returnedPhoto.getImage()));
            return ResponseEntity
                    .ok()
                    .body(photo);
        }catch(IOException e){
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }

    @PutMapping("/updatePhoto/{id}")
    public ResponseEntity<Photo> updatePhoto(@PathVariable("id") long id, @RequestBody Photo photo) {

        Optional<Photo> photoOptional = photoRepository.findById(id);
        photoOptional.get().setExifData(photo.getExifData());

        if (photoOptional.isPresent()){
            try{
                Photo updatedPhoto = photoRepository.save(photoService.updateExifData(photoOptional.get()));
                updatedPhoto.setExifData(photoService.extractExifData(updatedPhoto.getImage()));
                return ResponseEntity
                        .ok()
                        .body(updatedPhoto);
            }catch(ExifDataException e){
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build();
            }
        }else{
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }


    @RequestMapping(value = "/getImage/{id}", method = RequestMethod.GET,
            produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable long id) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(photoRepository.getOne(id).getImage());
    }

}