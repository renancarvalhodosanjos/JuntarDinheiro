package com.example.juntardinheiro;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker {

    private static final String UPDATE_URL = "http://alagoasti.ddns.net/app/update.json";
    private BroadcastReceiver onComplete;

    public interface UpdateListener {
        void onUpdateAvailable(String apkUrl, String changelog);
        void onNoUpdate();
    }

    public static void checkForUpdate(final Context context, final UpdateListener listener) {
        new Thread(() -> {
            try {
                URL url = new URL(UPDATE_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                JSONObject updateInfo = new JSONObject(result.toString());
                int latestVersionCode = updateInfo.getInt("versionCode");
                String apkUrl = updateInfo.getString("apkUrl");
                String changelog = updateInfo.getString("changelog");

                int currentVersionCode = context.getPackageManager()
                        .getPackageInfo(context.getPackageName(), 0).versionCode;

                if (latestVersionCode > currentVersionCode) {
                    listener.onUpdateAvailable(apkUrl, changelog);
                } else {
                    listener.onNoUpdate();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void downloadAndInstallApk(String apkUrl, Context context) {
        // Verificar permissão para instalar apps de fontes desconhecidas no Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !context.getPackageManager().canRequestPackageInstalls()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
                    .setData(Uri.parse(String.format("package:%s", context.getPackageName())));
            context.startActivity(intent);
            return;
        }

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
        request.setTitle("Atualizando aplicativo...");
        request.setDescription("Baixando nova versão.");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "myapp-latest.apk");

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadId = downloadManager.enqueue(request);

        // Registrar um BroadcastReceiver para instalar o APK quando o download terminar
        onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                Uri uri = downloadManager.getUriForDownloadedFile(downloadId);
                if (uri != null) {
                    installApk(uri, context);
                } else {
                    Toast.makeText(context, "Falha no download", Toast.LENGTH_SHORT).show();
                }
                // Desregistrar o receiver após o uso
                context.unregisterReceiver(onComplete);
            }
        };
        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void installApk(Uri apkUri, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }
}
