package getajob.pymes.freelancepymes.profile.service;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {
    private final S3Client s3Client;

    @Value("${do.spaces.bucket}")
    private String bucketName;

    @Value("${do.spaces.endpoint}")
    private String endpoint;

    public S3Service(S3Client s3Client){
        this.s3Client = s3Client;
    }

    public List<String> uploadImages(List<MultipartFile> files){
        List<String> urls = new ArrayList<>();

        for(MultipartFile file: files){
            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
            try{
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .acl("public-read") //url accesible
                            .contentType(file.getContentType())
                            .build();
                s3Client.putObject(putObjectRequest, 
                            RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

                urls.add(endpoint + "/" + bucketName + "/" + fileName);
            }catch(IOException e){
                throw new RuntimeException("Error al subir archivo a DigitalOcean",e);
            }
        }
        return urls;
    }
}
