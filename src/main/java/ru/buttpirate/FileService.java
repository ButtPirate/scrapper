package ru.buttpirate;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

public class FileService {
    public static void saveFile(String directLink, String filename) throws IOException {
        File file = new File(filename);
        FileUtils.copyURLToFile(new URL(directLink), file);
    }

    public static String readFileContent(String path) throws IOException {
        return FileUtils.readFileToString(new File(path), Charset.defaultCharset());

    }

}
