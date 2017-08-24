package br.com.rutiyama;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Principal {

    public Principal() throws IOException {
        //Aqui seria a String codificada em base64 (t51):
        String value = FileExample.get();

        //Primeiro faz a conversão para array de bytes lendo a String em UTF-8
        byte[] decodedImg = Base64.getDecoder().decode(value.getBytes(StandardCharsets.UTF_8));
        String path = "/Users/rodrigo/Documents/";
        String filename = "arquivo.bin";

        /* Depois de transformado de string para array de byte, eu jogo o array de byte
        em um arquivo criado. Lembrando que deve estar utilizando o JDK 8 e ter o Java 8 instalado no servidor */
        Path destinationFile = Paths.get(path, filename);
        Files.write(destinationFile, decodedImg);

        extrair(path+filename, path);
    }

    public void extrair(String zipFile, String outputFolder){

        byte[] buffer = new byte[1024];

        try{

            // Caso os diretórios não tem criado fisicamente, aqui é criado automaticamente:
            File folder = new File(outputFolder);
            if(!folder.exists()){
                folder.mkdir();
            }

            // Recupera o arquivo .bin
            ZipInputStream zis =
                    new ZipInputStream(new FileInputStream(zipFile));
            // E aqui recupera a lista de arquivos dentro da pasta
            ZipEntry ze = zis.getNextEntry();


            while(ze!=null){

                String fileName = ze.getName();
                File newFile = new File(outputFolder + File.separator + fileName);

                System.out.println("Arquivo extraído: "+ newFile.getAbsoluteFile());

                // Cria todas as pastas caso não exista, para evitar o erro FileNotFoundException
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public static void main (String args[]) throws IOException{
        new Principal();
    }
}
