package com.me.core.service.uncompress;

import com.me.core.service.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.rauschig.jarchivelib.ArchiverFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

@Slf4j
public class UncompressBlacklist {

    /**
     * Method for uncompressing .tar.gz or .zip files
     * @param archive path to the archive
     * @param destination path for the uncompressed contents
     */
    private static void tarGZorZip(String archive, String destination) throws IOException {
        File fromArchive = new File (archive);
        File toFile = new File (destination);
        org.rauschig.jarchivelib.Archiver archiver =
                ArchiverFactory.createArchiver(fromArchive);
        archiver.extract(fromArchive, toFile);
    }

    /**
     * Method for uncompressing .gz files
     * @param fromArchive path to the archive
     * @param toFile path for the uncompressed contents
     */
    private static void GZip(String fromArchive, String toFile) throws IOException {
        String filename = toFile + '/' + FilenameUtils.getBaseName(fromArchive);
        File d1 = new File(filename);
        Utils.createFilePath(filename);
        byte[] buffer = new byte[1024];
        String outputFilePath = d1.getAbsolutePath() + '/'
                + FilenameUtils.getBaseName(fromArchive);

        try (GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(fromArchive));
        FileOutputStream out = new FileOutputStream(new File(outputFilePath))) {
            int len;
            while ((len = gzis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        }
    }

    /**
     * Method for uncompressing .tar.gz/ .zip/ .gz files
     * @param fromArchive path to the archive
     * @param toFile path for the uncompressed contents
     */
    public static void uncompress(String fromArchive, String toFile) {
        log.info("Decompressing file " + fromArchive + " now");
        try {
            if (fromArchive.endsWith(".tar.gz") || fromArchive.endsWith(".zip")) {
                tarGZorZip(fromArchive, toFile);
            } else if (!fromArchive.endsWith(".tar.gz") && fromArchive.endsWith(".gz")) {
                GZip(fromArchive, toFile);
            } else {
                log.info("Unknown archive type!");
            }
            log.info("Finished!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
