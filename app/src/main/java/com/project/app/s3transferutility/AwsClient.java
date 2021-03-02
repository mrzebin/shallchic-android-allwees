package com.project.app.s3transferutility;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.hb.basemodel.utils.LoggerUtil;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class AwsClient {
    static AmazonS3 s3;
    private static String REGION;
    private static String BUCKET_NAME;

    public AwsClient(String accessKey,String secretId,String sessionId,String bucketName,String region){
        BUCKET_NAME = bucketName;
        REGION = region;
        init(accessKey,secretId,sessionId);
    }

    public void init(String accessKey, String secretId, String sessionId) {
        ClientConfiguration config = new ClientConfiguration();
        config.setConnectionTimeout(10000);
        config.setSocketTimeout(300000);

        s3 = new AmazonS3Client(new BasicSessionCredentials(accessKey, secretId,sessionId),config);
        Region usWest2 = Region.getRegion(REGION);
        s3.setRegion(usWest2);
    }

    /**
     * @Title: uploadToS3
     * @Description: S3url
     * @param @param tempFile
     * @param @param remoteFileName
     * @param @return
     * @param @throws IOException
     * @return String
     * @throws
     */
    public String uploadToS3(long length,InputStream stream, String remoteFileName){
        try {
            //
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/jpg");
            metadata.setCacheControl("public,max-age=31536000");
            metadata.setContentLength(length);

            s3.putObject(new PutObjectRequest(BUCKET_NAME, remoteFileName, stream,metadata).withCannedAcl(CannedAccessControlList.PublicRead));
            //request
            GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(
                    BUCKET_NAME, remoteFileName);
            //url
            URL publicUrl = s3.generatePresignedUrl(urlRequest);
            String url = publicUrl.toString().substring(0, publicUrl.toString().indexOf("?"));
            LoggerUtil.i("url--------------------------------{}",url);
            return url;
        } catch (AmazonServiceException ase) {
            ase.printStackTrace();
        } catch (Exception e){
            return "";
        }
        return "";
    }


    /**
     * @Title: getContentFromS3
     * @Description: 2
     * @param @param remoteFileName
     * @param @throws IOException
     * @return S3ObjectInputStream
     * @throws
     */
    public static S3ObjectInputStream getContentFromS3(String remoteFileName) {
        try {
            GetObjectRequest request  = new GetObjectRequest(BUCKET_NAME,remoteFileName);
            S3Object object = s3.getObject(request);
            return object.getObjectContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @Title: downFromS3
     * @Description:
     * @param @param remoteFileName
     * @param @param path
     * @param @throws IOException
     * @return void
     * @throws
     */
    public static void downFromS3(String remoteFileName,String path) {
        try {
            GetObjectRequest request  = new GetObjectRequest(BUCKET_NAME,remoteFileName);
            s3.getObject(request,new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Title: getUrlFromS3
     * @Description: url
     * @param @param remoteFileName
     * @param @return
     * @param @throws IOException
     * @return String
     * @throws
     */
    public static String getUrlFromS3(String remoteFileName) {
        try {
            GeneratePresignedUrlRequest httpRequest=new GeneratePresignedUrlRequest(BUCKET_NAME, remoteFileName);
            return s3.generatePresignedUrl(httpRequest).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
//    /**
//     * s3bucketNameBucket
//     * @param s3
//     * @param bucketName
//     * @return
//     */
//    public static boolean checkBucketExists(AmazonS3 s3, String bucketName) {
//        List<Bucket> buckets = s3.listBuckets();
//        for (Bucket bucket : buckets) {
//            if (Objects.equals(bucket.getName(), bucketName)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public static void delFromS3(String remoteFileName) throws IOException {
//        try {
//            s3.deleteObject(BUCKET_NAME, remoteFileName);
//        } catch (AmazonServiceException ase) {
//            ase.printStackTrace();
//        } catch (AmazonClientException ace) {
//            ace.printStackTrace();
//        }
//    }


}
