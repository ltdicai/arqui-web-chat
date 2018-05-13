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

import com.chat.client.Models.ImageMessage;
import com.chat.client.Models.User;
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
                    String fieldName = item.getFieldName();
                    String fileName = item.getName();
                    if (fileName != null) {
                        fileName = FilenameUtils.getName(fileName);
                    }

                    String contentType = item.getContentType();
                    boolean isInMemory = item.isInMemory();
                    long sizeInBytes = item.getSize();
                    System.out.print("Field Name:"+fieldName +",File Name:"+fileName);
                    System.out.print("Content Type:"+contentType
                            +",Is In Memory:"+isInMemory+",Size:"+sizeInBytes);

                    byte[] data = item.get();
                    String pathName = getServletContext()
                            .getRealPath( "/uploadedFiles/" + fileName);
                    System.out.print("File name:" +pathName );

                    BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));

                    String uri = request.getScheme() + "://" +   // "http" + "://
                            request.getServerName() +       // "myhost"
                            ":" +                           // ":"
                            request.getServerPort();

                    String message = uri + "/uploadedFiles/" + fileName;

                    File filePath = new File(message);
                    ImageIO.write(img, contentType, filePath);
                    img.flush();

                    System.out.print("File Uploaded Successfully!");


                    String userid = request.getParameter("userid");
                    int conversationid = Integer.parseInt(request.getParameter("conversationid"));

                    User user = new User(userid);
                    ImageMessage imageMessage = new ImageMessage(user, message);
                    ImageMessageDatabaseProcedures imageMessageDatabaseProcedures = new ImageMessageDatabaseProcedures();
                    imageMessageDatabaseProcedures.insert(imageMessage, conversationid);

                }
            }
        } catch(Exception e){
            System.out.print("File Uploading Failed!" + e.getMessage());
        }

    }
}