package com.chat.server.Services;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chat.client.Models.AudioMessage;
import com.chat.client.Models.ImageMessage;
import com.chat.client.Models.User;
import com.chat.server.Services.DatabaseProcedures.AudioMessageDatabaseProcedures;
import com.chat.server.Services.DatabaseProcedures.ImageMessageDatabaseProcedures;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

public class FileUpload extends HttpServlet{
    public void doPost(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        FileItemFactory factory = new DiskFileItemFactory();
        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        try{
            // Parse the request
            List items = upload.parseRequest(request);

            // Process the uploaded items
            Iterator iter = items.iterator();

            //FileItemIterator iter = upload.getItemIterator(request);

            while (iter.hasNext()) {
                FileItem item = (FileItem)  iter.next();

                //handling a normal form-field
                if(item.isFormField()) {
                    System.out.println("Got a form field");
                    String name = item.getFieldName();
                    String value = item.getString();
                    System.out.print("Name:"+name+",Value:"+value);

                } else {

                    //handling file loads
                    System.out.println("Not form field");
                    String fileName = item.getName();
                    if (fileName != null) {
                        fileName = FilenameUtils.getName(fileName);
                    }

                    String contentType = item.getContentType();
                    String filePath = getServletContext().getInitParameter("file-path");
                    File file;

                    if( fileName.lastIndexOf("/") >= 0 ){
                        fileName = fileName.substring( fileName.lastIndexOf("/"));
                    }else{
                        fileName = fileName.substring(fileName.lastIndexOf("/")+1);
                    }


                    String userid = request.getParameter("userid");
                    int conversationid = Integer.parseInt(request.getParameter("conversationid"));

                    User user = new User(userid);
                    filePath += "/AudiosAndImages";
                    file = new File(getNewFileName(filePath + "/" + fileName));

                    item.write( file ) ;

                    String message = "AudiosAndImages" +  file.getPath().substring(file.getPath().lastIndexOf("/"));
                    String contentTypeFile = contentType.split("/")[0];
                    if(contentTypeFile.equals("image")){
                        ImageMessage imageMessage = new ImageMessage(user, message);
                        ImageMessageDatabaseProcedures imageMessageDatabaseProcedures = new ImageMessageDatabaseProcedures();
                        imageMessageDatabaseProcedures.insert(imageMessage, conversationid);
                    }
                    else if(contentTypeFile.equals("audio")){
                        AudioMessage audioMessage = new AudioMessage(user, message);
                        AudioMessageDatabaseProcedures audioMessageDatabaseProcedures = new AudioMessageDatabaseProcedures();
                        audioMessageDatabaseProcedures.insert(audioMessage, conversationid);

                    }


                    System.out.print("File Uploaded Successfully!");
                }
            }
        } catch(Exception e){
            System.out.print("File Uploading Failed!" + e.getMessage());
        }

    }

    private static String getNewFileName(String filename) throws IOException {
        File aFile = new File(filename);
        int fileNo = 0;
        String newFileName = "";
        String filenameType = filename.substring(filename.lastIndexOf("."));
        String filenameNoType = filename.substring(0, filename.lastIndexOf("."));

        if (aFile.exists() && !aFile.isDirectory()) {


            //newFileName = filename.replaceAll(getFileExtension(filename), "(" + fileNo + ")" + getFileExtension(filename));

            while(aFile.exists()){
                fileNo++;

                aFile = new File(filenameNoType+ "(" + fileNo + ")" + filenameType);
                newFileName = filenameNoType+ "(" + fileNo + ")" + filenameType;
            }


        } else if (!aFile.exists()) {
            aFile.createNewFile();
            newFileName = filename;
        }
        return newFileName;
    }
}