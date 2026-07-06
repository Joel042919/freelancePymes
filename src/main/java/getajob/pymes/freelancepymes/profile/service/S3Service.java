package getajob.pymes.freelancepymes.profile.service;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Service
public class S3Service {
    private final S3Presigner s3Presigner;

    @Value("${cloudflare.r2.bucket}")
    private String bucketName;

    @Value("${cloudflare.r2.endpoint}")
    private String endpoint;

    public S3Service(S3Presigner s3Presigner){
        this.s3Presigner = s3Presigner;
    }

    public Map<String, String> generatePresignedUrl(String originalFileName, String contentType) {
        String fileName = UUID.randomUUID() + "-" + originalFileName.replaceAll("\\s+", "_");

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .putObjectRequest(objectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

        String presignedUrl = presignedRequest.url().toString();
        // Construimos la URL pública final donde estará el objeto (para guardarla en la DB)
        String publicUrl = endpoint + "/" + bucketName + "/" + fileName;

        Map<String, String> response = new HashMap<>();
        response.put("presignedUrl", presignedUrl);
        response.put("publicUrl", publicUrl);

        return response;
    }

    public String generatePresignedGetUrl(String publicUrl) {
        if (publicUrl == null || publicUrl.isEmpty()) return publicUrl;
        
        // Extraer el fileName (object key) de la publicUrl
        // Ejemplo publicUrl: https://.../pymes/file-name.jpg
        String prefix = endpoint + "/" + bucketName + "/";
        String fileName = publicUrl;
        if (publicUrl.startsWith(prefix)) {
            fileName = publicUrl.substring(prefix.length());
        } else {
            // Fallback por si acaso es solo el nombre del archivo
            int lastSlash = publicUrl.lastIndexOf('/');
            if (lastSlash >= 0) {
                fileName = publicUrl.substring(lastSlash + 1);
            }
        }

        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofHours(1)) // Válido por 1 hora
                .getObjectRequest(objectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        return presignedRequest.url().toString();
    }
}
