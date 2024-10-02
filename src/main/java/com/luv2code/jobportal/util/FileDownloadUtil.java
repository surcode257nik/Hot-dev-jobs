package com.luv2code.jobportal.util;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDownloadUtil {

    private Path foundfile;
    
    public Resource getFileAsResource(String downloadDir, String fileName) throws IOException {

        System.out.println("Into File Download Util");
        try{
            Path path = Paths.get(downloadDir);
            Files.list(path).forEach(file -> {
                if(file.getFileName().startsWith(fileName)){
                    System.out.println("Resume: "+fileName);
                    foundfile = file;
                }
            });

            if(foundfile!=null){
                return new UrlResource(foundfile.toUri());
            }
        }catch (Exception e){
            System.out.println("Exception:"+e.getMessage()+" \n"+e);
        }

        return null;
    }
}
