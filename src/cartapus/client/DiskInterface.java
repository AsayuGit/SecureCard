package client;

import java.io.File;

import java.io.DataInputStream;
import java.io.FileInputStream;

import java.io.DataOutputStream;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.FileNotFoundException;

// The DiskInterface class abstract the disk I/O
public abstract class DiskInterface {
    // Open an input filestream
    public static DataInputStream openFileInput(String filename) throws IOException {
        File file = new File(filename);
        return new DataInputStream(new FileInputStream(file));
    }

    // Open an output filestream
    public static DataOutputStream openFileOutput(String filename) throws IOException {
        File file = new File(filename);
        return new DataOutputStream(new FileOutputStream(file));
    }

    // Load a file into a byte buffer
    public static byte[] loadFile(String filename) throws IOException {
        DataInputStream fileStream = null;
        try {
            File file = new File(filename);
            fileStream = new DataInputStream(new FileInputStream(file));

            byte[] buffer = new byte[(int)file.length()];
            fileStream.readFully(buffer);

            return buffer;
        } finally {
            if (fileStream != null) fileStream.close();
        }
    }

    // Save a byte buffer to a file
    public static void saveFile(String filename, byte[] buffer) throws IOException {
        DataOutputStream fileStream = null;
        try {
            File file = new File(filename);
            fileStream = new DataOutputStream(new FileOutputStream(file));

            fileStream.write(buffer);
        } finally {
            fileStream.close();
        }
    }

    // Remove the directory path form the path leaving only the filename
    public static String trimDir(String path) {
        File file = new File(path);
        return file.getName();
    }
}