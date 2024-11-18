package co.allconnected.fussiontech.businessesservice.services;

import com.google.cloud.storage.Bucket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class FirebaseService {

    public String uploadImg(String imageName, String extension, MultipartFile imageFile) throws IOException {
        InputStream inputStream = imageFile.getInputStream();
        Bucket bucket = StorageClient.getInstance().bucket();
        bucket.create("business_photos/"+imageName, inputStream, "image/"+extension);
        return bucket.get("business_photos/"+imageName)
                .signUrl(360, java.util.concurrent.TimeUnit.DAYS).toString();
    }

    public void deleteImg(String imageName) {
        Bucket bucket = StorageClient.getInstance().bucket();
        bucket.get("business_photos/"+imageName).delete();
    }
}
