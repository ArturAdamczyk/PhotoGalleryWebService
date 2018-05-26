package com.photogallery.photogallerywebservice;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.constants.ExifTagConstants;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.apache.sanselan.formats.tiff.write.TiffOutputSet;
import org.apache.sanselan.formats.jpeg.exifRewrite.ExifRewriter;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.formats.tiff.write.TiffOutputDirectory;
import org.apache.sanselan.formats.tiff.write.TiffOutputField;

import java.io.*;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class PhotoService {
    private final static Logger logger = LogManager.getLogger(PhotoService.class);

    /**
     * Extracts EXIF data from image
     *
     * @param image a byte[] array containing an image with metadata to be extracted
     *
     * @return ExifData
     */
    public ExifData extractExifData(byte[] image){

        ExifData exifData = new ExifData();
        exifData.initialize();
        try{
            File imageFile = new File("file");
            FileUtils.writeByteArrayToFile(imageFile, image);

            TiffImageMetadata exifArray = Optional.of(Sanselan.getMetadata(imageFile))
                    .map(metadata -> ((JpegImageMetadata) metadata).getExif())
                    .orElseThrow(() -> new ImageReadException("Metadata read exception"));

            exifData.setMake(
                    Optional.ofNullable(String.valueOf(exifArray.findField(ExifTagConstants.EXIF_TAG_MAKE).getValue()))
                            .orElse(Params.EMPTY_VALUE));
            exifData.setModel(
                    Optional.ofNullable(String.valueOf(exifArray.findField(ExifTagConstants.EXIF_TAG_MODEL).getValue()))
                            .orElse(Params.EMPTY_VALUE));
            exifData.setOrientation(
                    Optional.ofNullable(String.valueOf(exifArray.findField(ExifTagConstants.EXIF_TAG_ORIENTATION).getValue()))
                            .orElse(Params.EMPTY_VALUE));
            exifData.setDate(
                    Optional.ofNullable(String.valueOf(exifArray.findField(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL).getValue()))
                            .orElse(Params.EMPTY_VALUE));
            exifData.setExposureTime(
                    Optional.ofNullable(String.valueOf(exifArray.findField(ExifTagConstants.EXIF_TAG_EXPOSURE_TIME).getValue()))
                            .orElse(Params.EMPTY_VALUE));
            exifData.setFNumber(
                    Optional.ofNullable(String.valueOf(exifArray.findField(ExifTagConstants.EXIF_TAG_FNUMBER).getValue()))
                            .orElse(Params.EMPTY_VALUE));
            exifData.setIso(
                    Optional.ofNullable(String.valueOf(exifArray.findField(ExifTagConstants.EXIF_TAG_ISO).getValue()))
                            .orElse(Params.EMPTY_VALUE));
            exifData.setShutterSpeed(
                    Optional.ofNullable(String.valueOf(exifArray.findField(ExifTagConstants.EXIF_TAG_SHUTTER_SPEED_VALUE).getValue()))
                            .orElse(Params.EMPTY_VALUE));
            exifData.setBrightness(
                    Optional.ofNullable(String.valueOf(exifArray.findField(ExifTagConstants.EXIF_TAG_BRIGHTNESS_VALUE).getValue()))
                            .orElse(Params.EMPTY_VALUE));
            exifData.setExposureBias(
                    Optional.ofNullable(String.valueOf(exifArray.findField(ExifTagConstants.EXIF_TAG_EXPOSURE_COMPENSATION).getValue()))
                            .orElse(Params.EMPTY_VALUE));
            exifData.setFlash(
                    Optional.ofNullable(String.valueOf(exifArray.findField(ExifTagConstants.EXIF_TAG_FLASH).getValue()))
                            .orElse(Params.EMPTY_VALUE));
            exifData.setFocalLength(
                    Optional.ofNullable(String.valueOf(exifArray.findField(ExifTagConstants.EXIF_TAG_FOCAL_LENGTH).getValue()))
                            .orElse(Params.EMPTY_VALUE));
            exifData.setColorSpace(
                    Optional.ofNullable(String.valueOf(exifArray.findField(ExifTagConstants.EXIF_TAG_COLOR_SPACE).getValue()))
                            .orElse(Params.EMPTY_VALUE));
            exifData.setWidth(
                    Optional.ofNullable(String.valueOf((int) Sanselan.getImageSize(image).getWidth()))
                            .orElse(Params.EMPTY_VALUE));
            exifData.setHeight(
                    Optional.ofNullable(String.valueOf((int) Sanselan.getImageSize(image).getHeight()))
                            .orElse(Params.EMPTY_VALUE));
            exifData.setFileSize((int)imageFile.length());
        }catch(IOException e){
            logger.error(e.getMessage());
        }catch(ImageReadException e){
            logger.error(e.getMessage());
        }finally{
            return exifData;
        }
    }

    /**
     * Updates Photo EXIF metadata
     *
     * @param photo a Photo object containing an image with metadata to be updated
     *
     * @return Photo
     */
    public Photo updateExifData(Photo photo) throws ExifDataException{
        OutputStream os = null;
        try{
            File imageFile = new File("originalFile");
            FileUtils.writeByteArrayToFile(imageFile, photo.getImage());
            File destinationFile = new File ("destinationFile");
            FileUtils.copyFile(imageFile, destinationFile);

            TiffOutputSet outputSet = Optional.of(Sanselan.getMetadata(imageFile))
                    .map(metadata -> ((JpegImageMetadata) metadata).getExif())
                    .orElseThrow(() -> new ImageWriteException("Metadata write exception"))
                    .getOutputSet();

            TiffOutputDirectory exifDir0 = (TiffOutputDirectory) outputSet.getDirectories().get(0);
            TiffOutputDirectory exifDir1 = (TiffOutputDirectory) outputSet.getDirectories().get(1);
            TiffOutputDirectory exifDir2 = (TiffOutputDirectory) outputSet.getDirectories().get(2);
            TiffOutputDirectory exifDir = outputSet.getExifDirectory();
            TiffOutputDirectory rootDir = outputSet.getRootDirectory();

            TiffOutputField exifMakeField = createExifField(photo.getExifData().getMake(), ExifTagConstants.EXIF_TAG_MAKE);
            TiffOutputField exifModelField = createExifField(photo.getExifData().getModel(), ExifTagConstants.EXIF_TAG_MODEL);
            TiffOutputField exifOrientationField = createExifField(photo.getExifData().getOrientation(), ExifTagConstants.EXIF_TAG_ORIENTATION);
            TiffOutputField exifIsoField = createExifField(photo.getExifData().getIso(), ExifTagConstants.EXIF_TAG_ISO);

            addExifField(exifDir0,exifDir1,exifDir2,exifDir,rootDir, ExifTagConstants.EXIF_TAG_MAKE, exifMakeField);
            addExifField(exifDir0,exifDir1,exifDir2,exifDir,rootDir, ExifTagConstants.EXIF_TAG_MODEL, exifModelField);
            addExifField(exifDir0,exifDir1,exifDir2,exifDir,rootDir, ExifTagConstants.EXIF_TAG_ISO, exifIsoField);
            addExifField(exifDir0,exifDir1,exifDir2,exifDir,rootDir, ExifTagConstants.EXIF_TAG_ORIENTATION, exifOrientationField);

            os = new BufferedOutputStream(new FileOutputStream(destinationFile));
            new ExifRewriter().updateExifMetadataLossless(imageFile, os, outputSet);

            photo.setImage(FileUtils.readFileToByteArray(destinationFile));
        }catch(IOException e){
            logger.error(e.getMessage());
            throw new ExifDataException(e.getMessage());
        }catch(ImageReadException e){
            logger.error(e.getMessage());
            throw new ExifDataException(e.getMessage());
        }catch(ImageWriteException e){
            logger.error(e.getMessage());
            throw new ExifDataException(e.getMessage());
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
            return photo;
        }
    }

    /**
     * Creates TiffOutputField object which represents single EXIF metadata field
     *
     * @param exifValue a String containing new Exif field value
     * @param tagInfo a TagInfo object containing info about the created Exif field
     *
     * @return TiffOutputField
     */
    private TiffOutputField createExifField(String exifValue, TagInfo tagInfo){
        byte[] bytesArray = exifValue.getBytes();
        return new TiffOutputField(
                tagInfo,
                ExifTagConstants.FIELD_TYPE_ASCII, bytesArray.length, bytesArray);
    }

    /**
     * Adds TiffOutputField object which represents single EXIF metadata field
     * to image metadata containers, by replacing current field value ( if existing )
     *
     * @param exifDir0 a TiffOutputDirectory represents image metadata container
     * @param exifDir1 a TiffOutputDirectory represents image metadata container
     * @param exifDir2 a TiffOutputDirectory represents image metadata container
     * @param exifDir a TiffOutputDirectory represents image metadata container
     * @param rootDir a TiffOutputDirectory represents image metadata container
     * @param tagInfo a TagInfo object containing info about the updated Exif field
     *
     */
    private void addExifField(TiffOutputDirectory exifDir0, TiffOutputDirectory exifDir1, TiffOutputDirectory exifDir2,
                              TiffOutputDirectory exifDir, TiffOutputDirectory rootDir, TagInfo tagInfo, TiffOutputField exifValue){
        exifDir0.removeField(tagInfo);
        exifDir0.add(exifValue);
        exifDir1.removeField(tagInfo);
        exifDir1.add(exifValue);
        exifDir2.removeField(tagInfo);
        exifDir2.add(exifValue);
        exifDir.removeField(tagInfo);
        exifDir.add(exifValue);
        rootDir.removeField(tagInfo);
        rootDir.add(exifValue);
    }



    /*

    #####################################################################################################################
    ############################## EXTRACT EXIF DATA WITH METADATAEXTRACTOR LIBRARY #####################################


    public ExifData extractExifData(byte[] image){

        ExifData exifData = new ExifData();
        try{
            File imageFile = new File("file");
            FileUtils.writeByteArrayToFile(imageFile, image);
            Metadata metadata = ImageMetadataReader.readMetadata(imageFile);

            ExifIFD0Directory directoryIFD = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            ExifSubIFDDirectory directorySUB = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            FileSystemDirectory directoryFile = metadata.getFirstDirectoryOfType(FileSystemDirectory.class);


            exifData.setMake(
                    Optional.ofNullable(directoryIFD.getString(ExifIFD0Directory.TAG_MAKE))
                            .orElse(EMPTY_VALUE));
            exifData.setModel(
                    Optional.ofNullable(directoryIFD.getString(ExifIFD0Directory.TAG_MODEL))
                            .orElse(EMPTY_VALUE));
            exifData.setOrientation(
                    Optional.ofNullable(directoryIFD.getString(ExifIFD0Directory.TAG_ORIENTATION))
                            .orElse(EMPTY_VALUE));
            exifData.setWidth(
                    Optional.ofNullable(directoryIFD.getString(ExifIFD0Directory.TAG_IMAGE_WIDTH))
                            .orElse(EMPTY_VALUE));
            exifData.setHeight(
                    Optional.ofNullable(directoryIFD.getString(ExifIFD0Directory.TAG_IMAGE_HEIGHT))
                            .orElse(EMPTY_VALUE));

            exifData.setDate(
                    Optional.ofNullable(String.valueOf(directorySUB.getDate(ExifIFD0Directory.TAG_DATETIME_ORIGINAL)))
                            .orElse(EMPTY_VALUE));
            exifData.setExposureTime(
                    Optional.ofNullable(directorySUB.getString(ExifSubIFDDirectory.TAG_EXPOSURE_TIME))
                            .orElse(EMPTY_VALUE));
            exifData.setFNumber(
                    Optional.ofNullable(directorySUB.getString(ExifSubIFDDirectory.TAG_FNUMBER))
                            .orElse(EMPTY_VALUE));
            exifData.setIso(
                    Optional.ofNullable(directorySUB.getString(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT))
                            .orElse(EMPTY_VALUE));
            exifData.setShutterSpeed(
                    Optional.ofNullable(directorySUB.getString(ExifSubIFDDirectory.TAG_SHUTTER_SPEED))
                            .orElse(EMPTY_VALUE));
            exifData.setBrightness(
                    Optional.ofNullable(directorySUB.getString(ExifSubIFDDirectory.TAG_BRIGHTNESS_VALUE))
                            .orElse(EMPTY_VALUE));
            exifData.setExposureBias(
                    Optional.ofNullable(directorySUB.getString(ExifSubIFDDirectory.TAG_EXPOSURE_BIAS))
                            .orElse(EMPTY_VALUE));
            exifData.setFlash(
                    Optional.ofNullable(directorySUB.getString(ExifSubIFDDirectory.TAG_FLASH))
                            .orElse(EMPTY_VALUE));
            exifData.setFocalLength(
                    Optional.ofNullable(directorySUB.getString(ExifSubIFDDirectory.TAG_FOCAL_LENGTH))
                            .orElse(EMPTY_VALUE));
            exifData.setColorSpace(
                    Optional.ofNullable(directorySUB.getString(ExifSubIFDDirectory.TAG_COLOR_SPACE))
                            .orElse(EMPTY_VALUE));

            exifData.setFileSize(
                    Optional.ofNullable(directoryFile.getInteger(FileSystemDirectory.TAG_FILE_SIZE))
                            .orElse(ZERO_VALUE));
        }catch(IOException e){

        }catch(ImageProcessingException e){

        }finally{
            return exifData;
        }
    }
*/

}
