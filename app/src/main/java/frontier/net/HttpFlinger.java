package frontier.net;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import frontier.listeners.DataListener;
import frontier.net.parser.RespParser;

/**
 * 网络请求 封装类
 */

public class HttpFlinger {

    private HttpFlinger() {
    }

    /**
     * 发送get请求
     *
     * @param reqUrl 网页地址
     * @param listener 回调,执行在UI线程
     */
    public static <T> void get(final String reqUrl, final RespParser<T> parser,
                               final DataListener<T> listener) {

        new AsyncTask<Void, Void, T>() {
            @Override
            protected T doInBackground(Void... params) {

                HttpURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpURLConnection) new URL(reqUrl)
                            .openConnection();
                    urlConnection.connect();
                    String result = streamToString(urlConnection.getInputStream());

                        return parser.parseResponse(result); //解析器进行解析

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(T result) {



                if (listener != null) {
                    listener.onComplete(result); //接口回调结果


                }

            }
        }.execute();
    }


    private static String streamToString(InputStream inputStream) throws IOException {
        StringBuilder sBuilder = new StringBuilder();
        String line = null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                inputStream));
        while ((line = bufferedReader.readLine()) != null) {
            sBuilder.append(line).append("\n");
        }
        return sBuilder.toString();
    }

}
