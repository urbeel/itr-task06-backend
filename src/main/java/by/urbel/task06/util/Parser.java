package by.urbel.task06.util;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.apache.tomcat.util.http.fileupload.FileUtils;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;

public class Parser {
    public static List<String[]> parseCsv(String resourceLocation) {
        try {
            Path path = getPathFromString(resourceLocation);
            try (InputStream inputStream = getResourceAsIOStream(path.toString());
                 CSVReader reader = new CSVReaderBuilder(new InputStreamReader(inputStream)).withSkipLines(1).build()) {
                return reader.readAll();
            } catch (CsvException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String parseFile(String resourceLocation) {
        try {
            Path path = getPathFromString(resourceLocation);
            try (InputStream inputStream = getResourceAsIOStream(path.toString())) {
                byte[] data = inputStream.readAllBytes();
                return new String(data);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Path getPathFromString(String path) throws IOException {
        FileSystem fileSystem = null;
        URI uri;
        try {
            uri = FileUtils.class.getResource(path).toURI();

            if ("jar".equals(uri.getScheme())) {
                fileSystem = getFileSystem(uri);
                return fileSystem.getPath("/BOOT-INF/classes" + path);
            } else {
                return Paths.get(uri);
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } finally {
            if (fileSystem != null) {
                fileSystem.close();
            }
        }
    }

    private static FileSystem getFileSystem(URI uri) throws IOException {
        try {
            return FileSystems.getFileSystem(uri);
        } catch (FileSystemNotFoundException e) {
            return FileSystems.newFileSystem(uri, Collections.<String, String>emptyMap());
        }
    }

    public static InputStream getResourceAsIOStream(final String path) throws FileNotFoundException {
        InputStream ioStream = Parser.class.getResourceAsStream(path);
        if (ioStream == null) {
            File file = new File(path);
            ioStream = new FileInputStream(file);
        }
        return ioStream;
    }
}
