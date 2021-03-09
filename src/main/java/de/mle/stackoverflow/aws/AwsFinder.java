package de.mle.stackoverflow.aws;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AwsFinder {
    private AmazonS3 amazonS3;

    @Value("${amazon.s3.access-key}")
    private String accessKey;
    @Value("${amazon.s3.secret-key}")
    private String secretKey;

    @PostConstruct
    private void init() {
        this.amazonS3 = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.EU_CENTRAL_1)
                .withCredentials(new StaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }

    public List<String> findFolders(String bucket, String prefix) {
        ListObjectsV2Request request = new ListObjectsV2Request().withBucketName(bucket).withPrefix(prefix).withDelimiter("/");
        return amazonS3.listObjectsV2(request).getCommonPrefixes();
    }
}
