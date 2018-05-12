package com.photogallery.photogallerywebservice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExifData implements Serializable {

    @JsonInclude()
    @Transient
    private String make;
    @JsonInclude()
    @Transient
    private String model;
    @JsonInclude()
    @Transient
    private String orientation;
    @JsonInclude()
    @Transient
    private String date;
    @JsonInclude()
    @Transient
    private String width;
    @JsonInclude()
    @Transient
    private String height;
    @JsonInclude()
    @Transient
    private String exposureTime;
    @JsonInclude()
    @Transient
    private String fNumber;
    @JsonInclude()
    @Transient
    private String iso;
    @JsonInclude()
    @Transient
    private String shutterSpeed;
    @JsonInclude()
    @Transient
    private String brightness;
    @JsonInclude()
    @Transient
    private String exposureBias;
    @JsonInclude()
    @Transient
    private String flash;
    @JsonInclude()
    @Transient
    private String focalLength;
    @JsonInclude()
    @Transient
    private String colorSpace;
    @JsonInclude()
    @Transient
    private int fileSize;

    public void initialize(){
        this.make = Params.EMPTY_VALUE;
        this.model = Params.EMPTY_VALUE;
        this.orientation = Params.EMPTY_VALUE;
        this.date = Params.EMPTY_VALUE;
        this.width = Params.EMPTY_VALUE;
        this.height = Params.EMPTY_VALUE;
        this.exposureTime = Params.EMPTY_VALUE;
        this.fNumber = Params.EMPTY_VALUE;
        this.iso = Params.EMPTY_VALUE;
        this.shutterSpeed = Params.EMPTY_VALUE;
        this.brightness = Params.EMPTY_VALUE;
        this.exposureBias = Params.EMPTY_VALUE;
        this.flash = Params.EMPTY_VALUE;
        this.focalLength = Params.EMPTY_VALUE;
        this.colorSpace = Params.EMPTY_VALUE;
        this.fileSize = Params.ZERO_VALUE;
    }

}
