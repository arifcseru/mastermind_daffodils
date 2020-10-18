/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.quiz.businesstier;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.io.FileUtils;

/**
 * @author Crunchify.com
 */
public class QuizUtils {

    private static Component rootPane;

    public static File browseMultimediaFile() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Multimedia Files", "jpeg", "jpg", "mp3", "mpeg3", "gif", "wav");

        JFileChooser chooser = new JFileChooser("\\");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        String fileName = "";
        File fileAddress = null;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            fileAddress = chooser.getSelectedFile();
            String song = fileAddress + "";
            fileName = chooser.getSelectedFile().getName();
            //JOptionPane.showMessageDialog(rootPane, "Address: "+ fileAddress + "FileName: "+fileName);
        }
        return fileAddress;
    }

    public static Integer copyFile(String fromAddress, String toAddress) {
        InputStream inStream = null;
        OutputStream outStream = null;
        Integer result = 0;
        try {
            File fromFileAddress = new File(fromAddress);  //File file1 =new File("mak_cameraman.jpeg");
            File toFileAddress = new File(toAddress);

            inStream = new FileInputStream(fromFileAddress);
            outStream = new FileOutputStream(toFileAddress); // for override file content
            //outStream = new FileOutputStream(file2,<strong>true</strong>); // for append file content
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inStream.read(buffer)) > 0) {
                //System.out.println(buffer+"->"+inStream);
                outStream.write(buffer, 0, length);
                //System.out.println(buffer);
            }
            if (inStream != null) {
                inStream.close();
            }
            if (outStream != null) {
                outStream.close();
            }
            result = 1;
            System.out.println("File Copied..");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error to copy File! " + e.getMessage());
        }
        return result;
    }

    public static String copyFileAction(File fromAddress, String fileName) {
        String extension = "dfa";
        if (fromAddress != null) {
            Long size = fromAddress.length();
            Double size_double = size.doubleValue();
            size_double = size_double / 1024;
            size_double = size_double / 1024; //size = size/1000;
            System.out.println(size_double);

            String fileFullName = fromAddress.getName();
            extension = getExtension(fileFullName);
            //extension = fileFullName.substring(fileFullName.indexOf("."), fileFullName.length());
            System.out.println(extension);

            File toAddress = new File("files/" + fileName + "." + extension);
            if (!(size_double >= 10.0)) {
                Integer result = copyFile(fromAddress.toString(), toAddress.toString());
                if (result != 0) {
                    String message = "From: " + fromAddress.getName() + "\nTo: " + toAddress.getName();
                    System.out.println(message);
                    JOptionPane.showMessageDialog(null, "File copied!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "File is too big");
            }
        }
        return extension;
    }

    private static String getExtension(String stringValue) {
        String reverseValue = reverse(stringValue);
        String extension = extension(reverseValue);
        return reverse(extension);
    }

    private static String reverse(String stringValue) {
        String reverseValue = "";
        for (int i = stringValue.length() - 1; i >= 0; i--) {
            char c = stringValue.charAt(i);
            reverseValue = reverseValue + c;
        }
        return reverseValue;
    }

    public static String[] getFolderNames(File curDir) {
        String[] folderNames = new String[50];
        int i = 0;
        File[] filesList = curDir.listFiles();
        for (File file : filesList) {
            if (file.isDirectory()) {
                folderNames[i++] = file.getName();
            }
        }
        String[] newFolderNames = new String[i];
        i = 0;
        for (String folderName : folderNames) {
            if (folderName != null) {
                newFolderNames[i++] = folderName;
            }
        }
        return newFolderNames;
    }

    public static List<String> getFileNames(File directoryAddress) {
        List<String> fileNames = new ArrayList();
        int i = 0;
        File[] filesList = directoryAddress.listFiles();
        for (File file : filesList) {
            if (file.isFile() && file.getName().contains("csv")) {
                fileNames.add(i, file.getName());
            }
        }

        return fileNames;
    }

    public static List<String> getMediaFileNames(File directoryAddress) {
        List<String> fileNames = new ArrayList();
        int i = 0;
        File[] filesList = directoryAddress.listFiles();
        for (File file : filesList) {
            String fileName = file.getName().toLowerCase();
            //String extension = getExtension(fileName);
            if (file.isFile()
                    && (fileName.contains("mp3") || fileName.contains("wav") || fileName.contains("jpg")
                    || fileName.contains("jpeg")||fileName.contains("mp4")
                    || fileName.contains("gif")|| fileName.contains("png") || fileName.contains("bmp"))) {
                fileNames.add(i, file.getName());
            }
        }

        return fileNames;
    }

    public static String extension(String stringValue) {
        String extension = "";
        for (int i = 0; i <= stringValue.length(); i++) {
            char c = stringValue.charAt(i);
            if (c == '.') {
                break;
            }
            extension = extension + c;
        }
        return extension;
    }

    public static void main(String args[]) {

        // File fromAddress = browseMultimediaFile();
        /*if (fromAddress!=null) {
           String ext = copyFileAction(fromAddress,"Arif");
           System.out.println("ext: "+ext);
       }*/
        getFileNames(new File("Questions"));
        /*System.out.println("strings:");
        String str = getExtension("hell.jpg.test.mp3.mp4");
        System.out.println("str:"+str);*/
    }
}
