package com.swayaan.nysf.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);

	public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {

		FileSystem fileSystem = FileSystems.getDefault();
		Path uploadPath = Paths.get(uploadDir);

		if (!Files.exists(uploadPath)) {
			// Files.createDirectories(uploadPath);
			Files.createDirectories(fileSystem.getPath(uploadDir));
		}
		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			inputStream.close();
		} catch (IOException ex) {
			throw new IOException("Could not save file: " + fileName, ex);
		}
	}

	public static void cleanDir(String dir) {
		Path dirPath = Paths.get(dir);

		try {
			Files.list(dirPath).forEach(file -> {
				if (!Files.isDirectory(file)) {
					try {
						Files.delete(file);
					} catch (IOException ex) {
						LOGGER.error("Could not delete file: " + file);
					}
				}
			});
		} catch (IOException ex) {
			LOGGER.error("Could not list directory: " + dirPath);
		}
	}

	public static void removeDir(String dir) {
		cleanDir(dir);
		try {
			Files.delete(Paths.get(dir));
		} catch (IOException e) {
			LOGGER.error("Could not remove directory: " + dir);
		}

	}

	public static boolean deleteFile(String filePath) {
		Path path = Paths.get(filePath);
		// Delete file if it exists
		boolean isDeleted = false;
		try {
			isDeleted = Files.deleteIfExists(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error("File could not be found or deleted");
		}
		return isDeleted;
	}

	public static String getFileExtension(String fileName) {
		if (fileName == null) {
			throw new IllegalArgumentException("fileName must not be null!");
		}
		String extension = "";
		int index = fileName.lastIndexOf('.');
		if (index > 0) {
			extension = fileName.substring(index + 1);
		}
		return extension.toLowerCase();
	}

	public static String getFileNameWithoutExtension(String fileName) {
		if (fileName == null) {
			throw new IllegalArgumentException("fileName must not be null!");
		} else {
			if (fileName.indexOf(".") > 0) {
				return fileName.substring(0, fileName.lastIndexOf("."));
			} else {
				return fileName;
			}
		}
	}

	public static void deleteExistingFile(String uploadDirectory, String fileName) {

		File directory = new File(uploadDirectory);
		if (directory.exists()) {
			File[] files = directory.listFiles();
			for (File f : files) {
				if (f.getName().contains(fileName)) {

					f.delete();
				}
			}
		}
	}

}
