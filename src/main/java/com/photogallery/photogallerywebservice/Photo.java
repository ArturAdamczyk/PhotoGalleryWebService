package com.photogallery.photogallerywebservice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Photos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Photo implements Serializable {
    @Id
    @JsonInclude()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PHOTO_ID")
    private long id;

    @Column(name="PHOTO_NAME")
    private String name;

    @JsonIgnore()
    @Lob
    @Column(name="PHOTO_IMAGE")
    private byte[] image;

    @JsonInclude()
    @Transient
    private ExifData exifData;

}