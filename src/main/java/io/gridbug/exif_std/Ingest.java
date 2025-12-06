package io.gridbug.exif_std;

import java.io.File;
import java.io.IOException;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.kno10.reversegeocode.query.ReverseGeocoder;

import io.gridbug.exif_std.PhotoExifData;

public class Ingest {
    public static void main(String[] args) {
        ClientCache cache;
        Region<String, PhotoExifData> region;

        cache = new ClientCacheFactory().addPoolLocator("10.88.88.123", 10334)
                .setPdxSerializer(new ReflectionBasedAutoSerializer(
                        true,
                        "io.gridbug.exif_std.PhotoExifData"))
                .create();

        region = cache.<String, PhotoExifData>createClientRegionFactory(ClientRegionShortcut.PROXY)
                .create("PhotoExifData");

        if (args.length == 0) {
            System.out.println("Please provide a directory as an argument.");
            return;
        }

        String directoryName = args[0];
        File directory = new File(directoryName);

        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("Directory not found or is not a directory: " + directoryName);
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            System.err.println("Failed to list files in directory: " + directoryName);
            return;
        }

        ReverseGeocoder reverseGeocoder;
        try {
            reverseGeocoder = new ReverseGeocoder(
                    "/home/craig/tanzu/gemfire/apps/exif_gemfire_demo/osm-20151130-0.002-2.bin");
        } catch (IOException e) {
            System.err.println("Error initializing ReverseGeocoder: " + e.getMessage());
            return;
        }

        for (File file : files) {
            if (isJpeg(file)) {
                processFile(file, region, reverseGeocoder);
            }
        }
    }

    private static void processFile(File file, Region<String, PhotoExifData> region, ReverseGeocoder reverseGeocoder) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);

            com.drew.metadata.exif.ExifIFD0Directory exifDirectory = metadata
                    .getFirstDirectoryOfType(com.drew.metadata.exif.ExifIFD0Directory.class);
            com.drew.metadata.exif.GpsDirectory gpsDirectory = metadata
                    .getFirstDirectoryOfType(com.drew.metadata.exif.GpsDirectory.class);

            PhotoExifData exifData = new PhotoExifData();
            exifData.setId(file.getName());

            if (exifDirectory != null) {
                exifData.setMake(exifDirectory.getString(com.drew.metadata.exif.ExifIFD0Directory.TAG_MAKE));
                exifData.setModel(exifDirectory.getString(com.drew.metadata.exif.ExifIFD0Directory.TAG_MODEL));

                // Resolution is often Rational, but we want Double.
                // getDoubleObject handles nulls and conversions.
                exifData.setxResolution(
                        exifDirectory.getDoubleObject(com.drew.metadata.exif.ExifIFD0Directory.TAG_X_RESOLUTION));
                exifData.setyResolution(
                        exifDirectory.getDoubleObject(com.drew.metadata.exif.ExifIFD0Directory.TAG_Y_RESOLUTION));

                exifData.setDateTime(exifDirectory.getString(com.drew.metadata.exif.ExifIFD0Directory.TAG_DATETIME));
            }

            if (gpsDirectory != null) {
                com.drew.lang.GeoLocation geoLocation = gpsDirectory.getGeoLocation();
                if (geoLocation != null) {
                    exifData.setGpsLatitude(geoLocation.getLatitude());
                    exifData.setGpsLongitude(geoLocation.getLongitude());
                    String[] geo = reverseGeocoder.lookup((float) geoLocation.getLongitude(),
                            (float) geoLocation.getLatitude());
                    String rgeo = "";
                    for (int i = 0; i < geo.length; i++) {
                        rgeo += geo[i].split("\t")[0];
                        if (i < geo.length - 1) {
                            rgeo += ", ";
                        }
                    }
                    exifData.setGeoLocation(rgeo);
                }

                exifData.setGpsLatitudeRef(
                        gpsDirectory.getString(com.drew.metadata.exif.GpsDirectory.TAG_LATITUDE_REF));
                exifData.setGpsLongitudeRef(
                        gpsDirectory.getString(com.drew.metadata.exif.GpsDirectory.TAG_LONGITUDE_REF));

                exifData.setGpsAltitude(gpsDirectory.getDoubleObject(com.drew.metadata.exif.GpsDirectory.TAG_ALTITUDE));
                exifData.setGpsAltitudeRef(
                        gpsDirectory.getString(com.drew.metadata.exif.GpsDirectory.TAG_ALTITUDE_REF));
            }

            System.out.println("Processing file: " + file.getName());
            System.out.println("Extracted PhotoExifData:");
            System.out.println(exifData);
            region.put(exifData.getId(), exifData);

        } catch (ImageProcessingException | IOException e) {
            System.err.println("Error reading metadata for file " + file.getName() + ": " + e.getMessage());
        }
    }

    private static boolean isJpeg(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".jpeg");
    }

}
