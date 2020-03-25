package ru.buttpirate;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class FileService {
    public static void saveFile(String directLink, String filename) throws IOException {
        File file = new File(filename);
        FileUtils.copyURLToFile(new URL(directLink), file);
    }

}
