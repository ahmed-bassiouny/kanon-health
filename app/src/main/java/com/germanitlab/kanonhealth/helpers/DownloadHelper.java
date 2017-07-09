package com.germanitlab.kanonhealth.helpers;

import android.content.Context;
import android.util.Log;

import com.germanitlab.kanonhealth.models.Download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Milad Metias on 7/9/17.
 */

public class DownloadHelper {

    private static final int BUFFER_SIZE = 4096;

    private static ArrayList<Download> downloadQueue = new ArrayList<>();
    private static boolean isDownloading = false;
    private static Context ctx = null;

    public static void processDownloads(Context context) {
        if (ctx == null) {
            ctx = context;
        }

        if (!isDownloading && downloadQueue != null && downloadQueue.size() > 0) {
            isDownloading = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    downloadFile();
                }
            });
        }
    }

    public static void addToDownloadQueue(Download download) {
        downloadQueue.add(download);
    }


    private static void downloadFile() {
        Download currentDownload = null;
        try {
            while (downloadQueue.size() > 0) {
                currentDownload = downloadQueue.remove(0);
                URL url = new URL(currentDownload.getUrl());

                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                int responseCode = httpConn.getResponseCode();

                // always check HTTP response code first
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String fileName = "";
                    String disposition = httpConn.getHeaderField("Content-Disposition");
                    String contentType = httpConn.getContentType();
                    int contentLength = httpConn.getContentLength();

                    if (disposition != null) {
                        // extracts file name from header field
                        int index = disposition.indexOf("filename=");
                        if (index > 0) {
                            fileName = disposition.substring(index + 10,
                                    disposition.length() - 1);
                        }
                    } else {
                        // extracts file name from URL
                        fileName = currentDownload.getUrl().substring(currentDownload.getUrl().lastIndexOf("/") + 1, currentDownload.getUrl().length());
                    }

                    System.out.println("Content-Type = " + contentType);
                    System.out.println("Content-Disposition = " + disposition);
                    System.out.println("Content-Length = " + contentLength);
                    System.out.println("fileName = " + fileName);

                    // opens input stream from the HTTP connection
                    InputStream inputStream = httpConn.getInputStream();
                    File f = new File(ctx.getCacheDir(), fileName);

                    // opens an output stream to save into file
                    FileOutputStream outputStream = new FileOutputStream(f);

                    int bytesRead = -1;
                    byte[] buffer = new byte[BUFFER_SIZE];
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    outputStream.close();
                    inputStream.close();

                    System.out.println("File downloaded");
                    if (currentDownload.getCallback() != null) {
                        currentDownload.getCallback().run();
                    }

                } else {
                    System.out.println("No file to download. Server replied HTTP code: " + responseCode);
                }
                httpConn.disconnect();
            }
        } catch (Exception e) {
            Log.e("DownloadManager", e.getMessage());
            downloadQueue.add(currentDownload);
        } finally {
            isDownloading = false;
        }
    }

    public static String getFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1, url.length());
    }

    public static boolean isFileAvailable(String url, Context ctx) {
        File f = new File(ctx.getCacheDir(), getFileNameFromUrl(url));
        return f.exists();
    }

    public static File getFileFromUrl(String url, Context ctx) {
        File f = new File(ctx.getCacheDir(), getFileNameFromUrl(url));
        if (f.exists()) {
            return f;
        } else {
            return null;
        }
    }
}

