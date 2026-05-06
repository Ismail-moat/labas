package com.labas.util;

import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

public class FileUploadUtil {

    public static final long MAX_FILE_SIZE = 5 * 1024 * 1024; 

    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
        "image/jpeg", "image/png", "image/webp", "image/gif"
    );

    private static final byte[][] JPEG_MAGIC  = {{(byte)0xFF, (byte)0xD8, (byte)0xFF}};
    private static final byte[]   PNG_MAGIC   = {(byte)0x89, 0x50, 0x4E, 0x47};
    private static final byte[]   GIF_MAGIC_1 = {0x47, 0x49, 0x46, 0x38, 0x37, 0x61};
    private static final byte[]   GIF_MAGIC_2 = {0x47, 0x49, 0x46, 0x38, 0x39, 0x61};

    private static final byte[]   WEBP_RIFF   = {0x52, 0x49, 0x46, 0x46};

    public static String saveImage(Part part, String uploadDir, String subDir) throws IOException {
        if (part == null || part.getSize() == 0) return null;

        if (part.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Fichier trop volumineux (max 5 Mo).");
        }

        String submittedMime = part.getContentType();
        if (submittedMime == null || !ALLOWED_MIME_TYPES.contains(submittedMime.toLowerCase())) {
            throw new IllegalArgumentException("Type de fichier non autorisé: " + submittedMime);
        }

        byte[] header = new byte[12];
        try (InputStream is = part.getInputStream()) {
            is.read(header);
        }
        String detectedExt = detectExtension(header, submittedMime);
        if (detectedExt == null) {
            throw new IllegalArgumentException("Contenu du fichier non valide (signature incorrecte).");
        }

        String destDir = uploadDir + File.separator + subDir;
        Files.createDirectories(Paths.get(destDir));
        String newFilename = UUID.randomUUID().toString() + detectedExt;
        Path destPath = Paths.get(destDir, newFilename);

        part.write(destPath.toString());

        return "/uploads/" + subDir + "/" + newFilename;
    }

    private static String detectExtension(byte[] header, String mime) {

        if (header.length >= 3 && header[0] == (byte)0xFF && header[1] == (byte)0xD8 && header[2] == (byte)0xFF) {
            return ".jpg";
        }

        if (header.length >= 4 && header[0] == (byte)0x89 && header[1] == 0x50 && header[2] == 0x4E && header[3] == 0x47) {
            return ".png";
        }

        if (header.length >= 6 &&
            header[0] == 0x47 && header[1] == 0x49 && header[2] == 0x46 && header[3] == 0x38 &&
            (header[4] == 0x37 || header[4] == 0x39) && header[5] == 0x61) {
            return ".gif";
        }

        if (header.length >= 12 &&
            header[0] == 0x52 && header[1] == 0x49 && header[2] == 0x46 && header[3] == 0x46 &&
            header[8] == 0x57 && header[9] == 0x45 && header[10] == 0x42 && header[11] == 0x50) {
            return ".webp";
        }
        return null;
    }

    public static boolean hasFile(Part part) {
        return part != null && part.getSize() > 0 && part.getSubmittedFileName() != null
               && !part.getSubmittedFileName().isBlank();
    }
}
