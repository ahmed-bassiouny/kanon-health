package com.germanitlab.kanonhealth.helpers;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.callback.DownloadListener;
import com.germanitlab.kanonhealth.callback.UploadListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import static com.germanitlab.kanonhealth.helpers.Constants.folder;


public class InternetFilesOperations {

    private static InternetFilesOperations internetFilesOperations;
    private Context context;

    public static InternetFilesOperations getInstance(Context context) {
        if (internetFilesOperations != null)
            return internetFilesOperations;
        else {
            return new InternetFilesOperations(context);
        }
    }

    private InternetFilesOperations(Context context) {
        this.context = context;

    }

    public void uploadFile(final String filePath, final UploadListener uploadListener) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                if (filePath != null) {
                    File file = new File(filePath);
                    Log.d("EDIT USER PROFILE", "UPLOAD: file length = " + file.length());
                    Log.d("EDIT USER PROFILE", "UPLOAD: file exist = " + file.exists());
                    mpEntity.addPart("file", new FileBody(file, "application/octet"));
                }

                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(Constants.CHAT_SERVER_URL + "/upload");
                    httppost.setEntity(mpEntity);

                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
//                    httpclient.getConnectionManager().shutdown();
                    // print responce
                    final String responseStr = EntityUtils.toString(entity);
                    uploadListener.onUploadFinish(responseStr);
                    Log.e("log_tag ******", "good connection");
                } catch (Exception e) {
                    Crashlytics.logException(e);
                }


            }
        });
        t.start();
    }

    public void uploadFileWithProgress(ProgressBar progressBar, String url, String filePath, UploadListener uploadListener) {

        new UploadFileToServer(progressBar, url, filePath, uploadListener).execute();
    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {

        private final String url;
        private final String filePath;
        private ProgressBar progressBar;
        long totalSize = 0;
        UploadListener uploadListener;

        public UploadFileToServer(ProgressBar progressBar, String url, String filePath, UploadListener uploadListener) {
            this.progressBar = progressBar;
            this.url = url;
            this.filePath = filePath;
            this.uploadListener = uploadListener;
        }

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
//            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(filePath);

                entity.addPart("file", new FileBody(sourceFile, "application/octet"));

                try {
                    entity.addPart("token", new StringBody(PrefHelper.get(context,PrefHelper.KEY_USER_PASSWORD,"")));

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }
                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (Exception e) {
                Crashlytics.logException(e);
            }


            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("video", "Response from server: " + result);

            // showing the server response in an alert dialog
            progressBar.setVisibility(View.GONE);
            uploadListener.onUploadFinish(result);

            super.onPostExecute(result);
        }

    }

//    public void downloadFile(final String path, final int type, final DownloadListener downloadListener) {
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String filepath = null;
//
//                try {
//
//                    URL url = new URL(context.getString(R.string.download_attachment) + "/" + GlobalVariabls.token + "/" + path);
//
//
//                    Log.d("URL", " " + url);
//                    //create the new connection
//
//                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//
//                    //set up some things on the connection
//                    urlConnection.setRequestMethod("POST");
//
//                    urlConnection.setDoOutput(true);
//                    urlConnection.setDoInput(true);
//                    //and connect!
//                    urlConnection.connect();
//                    urlConnection.getOutputStream().write(("name=").getBytes());
//
//
//                    if (!folder.exists()) folder.mkdirs();
//                    String filename = "";
//
//                    filename = path + "." + type;
//
//
//                    File file = new File(folder, filename);
//
//                    if (file.createNewFile())
//
//                    {
//
//                        file.createNewFile();
//
//                    }
//
//                    //this will be used to write the downloaded data into the file we created
//                    FileOutputStream fileOutput = new FileOutputStream(file);
//
//                    //this will be used in reading the data from the internet
//                    InputStream inputStream = urlConnection.getInputStream();
//
//                    //this is the total size of the file
//                    int totalSize = urlConnection.getContentLength();
//                    //variable to store total downloaded bytes
//                    int downloadedSize = 0;
//
//                    //create a buffer...
//                    byte[] buffer = new byte[1024];
//                    int bufferLength = 0; //used to store a temporary size of the buffer
//
//                    //now, read through the input buffer and write the contents to the file
//                    while ((bufferLength = inputStream.read(buffer)) > 0) {
//                        //add the data in the buffer to the file in the file output stream (the file on the sd card
//                        fileOutput.write(buffer, 0, bufferLength);
//                        //add up the size so we know how much is downloaded
//                        downloadedSize += bufferLength;
//                        //this is where you would do something to report the prgress, like this maybe
//                        Log.i("Progress:", "downloadedSize:" + downloadedSize + "totalSize:" + totalSize);
//                    }
//                    //close the output stream when done
//                    fileOutput.close();
//                    if (downloadedSize == totalSize)
//                        filepath = file.getPath();
//
//                    //catch some possible errors...
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    filepath = null;
//                    e.printStackTrace();
//                }
//                Log.i("filepath:", " " + filepath);
//                downloadListener.onDownloadFinish(filepath);
//            }
//        });
//        thread.start();
//    }


    public void downloadUrlWithProgress(ProgressBar progressBar, String mediaMessageType, String downloadUrl, DownloadListener onDownloadFinish) {

        new DownloadFileFromURL(progressBar, mediaMessageType, downloadUrl, onDownloadFinish).execute(downloadUrl);
    }


    class DownloadFileFromURL extends AsyncTask<String, String, String> {


        ProgressBar progressBar;
        String mediaMessageType;
        String filename = "";
        File file;
        DownloadListener onDownloadFinish;
        String downloadUrl;

        public DownloadFileFromURL(ProgressBar progressBar, String mediaMessageType, String downloadUrl, DownloadListener onDownloadFinish) {

            this.progressBar = progressBar;
            this.mediaMessageType = mediaMessageType;
            this.downloadUrl = downloadUrl;
            this.onDownloadFinish = onDownloadFinish;

            if (!folder.exists()) folder.mkdirs();
            String filename = "";

            filename = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);


            file = new File(folder, filename);

            try {
                if (file.createNewFile())

                {

                    file.createNewFile();

                }
            } catch (Exception e) {
                Crashlytics.logException(e);
            }

        }

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            URL url;
            try {
                url = new URL(Constants.CHAT_SERVER_URL_IMAGE + "/" + downloadUrl);
                if (Helper.isNetworkAvailable(context)) {
                    if (file != null && file.exists()) {
                        URLConnection conection = url.openConnection();
                        conection.connect();
                        // this will be useful so that you can show a tipical 0-100% progress bar
                        int lenghtOfFile = conection.getContentLength();

                        // download the file
                        InputStream input = new BufferedInputStream(url.openStream(), 8192);

                        //this will be used to write the downloaded data into the file we created
                        FileOutputStream output = new FileOutputStream(file);


                        byte data[] = new byte[1024];

                        long total = 0;

                        while ((count = input.read(data)) != -1) {
                            total += count;
                            // publishing the progress....
                            // After this onProgressUpdate will be called
                            publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                            // writing data to file
                            output.write(data, 0, count);
                        }

                        // flushing output
                        output.flush();

                        // closing streams
                        output.close();
                        input.close();
                    }
                }

            } catch (Exception e) {
                Crashlytics.setString("request file url", Constants.CHAT_SERVER_URL_IMAGE + "/" + downloadUrl);
                Crashlytics.logException(e);
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressBar.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            progressBar.setVisibility(View.GONE);

            ContentResolver cR = context.getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String type = mime.getExtensionFromMimeType(cR.getType(Uri.fromFile(file)));
            String extension = mime.getExtensionFromMimeType(cR.getType(Uri.fromFile(file)));

            String typeMim = cR.getType(Uri.fromFile(file));

            onDownloadFinish.onDownloadFinish(file.getPath());
        }

    }


}
