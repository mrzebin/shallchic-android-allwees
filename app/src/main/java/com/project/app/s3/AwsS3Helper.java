package com.project.app.s3;

public class AwsS3Helper {
    private static String hostName = "sa-api.allwees.com";

    public static String getHostName() {
        return hostName;
    }

    public static void setHostName(String hostName) {
        AwsS3Helper.hostName = hostName;
    }
}
