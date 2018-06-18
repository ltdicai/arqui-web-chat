package com.chat.server.Services;

import com.chat.client.Models.AudioMessage;
import com.chat.client.Models.ImageMessage;
import com.chat.client.Models.User;
import com.chat.server.Services.DatabaseProcedures.AudioMessageDatabaseProcedures;
import com.chat.server.Services.DatabaseProcedures.ImageMessageDatabaseProcedures;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

public class FileUpload extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FileItemFactory factory = new DiskFileItemFactory();
        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            // Parse the request
            List items = upload.parseRequest(request);

            // Process the uploaded items
            Iterator iter = items.iterator();

            //FileItemIterator iter = upload.getItemIterator(request);

            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();

                //handling a normal form-field
                if (item.isFormField()) {
                    System.out.println("Got a form field");
                    String name = item.getFieldName();
                    String value = item.getString();
                    System.out.print("Name:" + name + ",Value:" + value);

                } else {

                    //handling file loads
                    System.out.println("Not form field");

                    String contentType = item.getContentType();
                    String userid = request.getParameter("userid");
                    String conversationid = request.getParameter("conversationid");
                    User user = new User(userid);
                    String message = Base64.getEncoder().encodeToString(item.get());
                    String contentTypeFile = contentType.split("/")[0];
                    if (contentTypeFile.equals("image")) {
                        message = "data:" + contentType + ";base64," + message;
                        ImageMessage imageMessage = new ImageMessage(user, message);
                        ImageMessageDatabaseProcedures imageMessageDatabaseProcedures = new ImageMessageDatabaseProcedures();
                        imageMessageDatabaseProcedures.insert(imageMessage, conversationid);
                    } else if (contentTypeFile.equals("audio")) {
                        message = "data:" + contentType + ";base64," + message;
                        AudioMessage audioMessage = new AudioMessage(user, message);
                        AudioMessageDatabaseProcedures audioMessageDatabaseProcedures = new AudioMessageDatabaseProcedures();
                        audioMessageDatabaseProcedures.insert(audioMessage, conversationid);
                    }


                    System.out.print("File Uploaded Successfully!");
                }
            }
        } catch (Exception e) {
            System.out.print("File Uploading Failed!" + e.getMessage());
        }

    }
}