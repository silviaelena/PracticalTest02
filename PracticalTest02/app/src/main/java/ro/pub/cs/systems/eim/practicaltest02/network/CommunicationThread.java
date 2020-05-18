package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;
import ro.pub.cs.systems.eim.practicaltest02.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;
import ro.pub.cs.systems.eim.practicaltest02.model.CurrencyInformation;

public class CommunicationThread extends Thread {
    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }

        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client(currency!");
            String currency = bufferedReader.readLine();
            if (currency == null || currency.isEmpty()) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (currency!");
                return;
            }

            HashMap<String, CurrencyInformation> data = serverThread.getData();
            CurrencyInformation currencyInformation = null;

            if (data.containsKey(currency)) {
                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the cache...");
                currencyInformation = data.get(currency);
            } else {
                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
                HttpClient httpClient = new DefaultHttpClient();
                String pageSourceCode = "";

                HttpGet httpGet = new HttpGet(Constants.WEB_SERVICE_ADDRESS + currency + Constants.WEB_SERVICE_MODE);
                HttpResponse httpGetResponse = httpClient.execute(httpGet);
                HttpEntity httpGetEntity = httpGetResponse.getEntity();
                if (httpGetEntity != null) {
                    pageSourceCode = EntityUtils.toString(httpGetEntity);

                }

                if (pageSourceCode == null) {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error getting the information from the webservice!");
                    return;
                } else
                    Log.i(Constants.TAG, pageSourceCode);

                JSONObject content = new JSONObject(pageSourceCode);
                JSONObject time = content.getJSONObject("time");
                JSONObject bpi = content.getJSONObject("bpi");
                JSONObject currencyJson = bpi.getJSONObject(currency);
                String condition = "";

                String updateTime = time.getString("updated");
                String rate = currencyJson.getString("rate");

                currencyInformation = new CurrencyInformation(rate, updateTime);
                serverThread.setData(currency, currencyInformation);


//                JSONObject content = new JSONObject(pageSourceCode);
//
//                JSONObject currencyDict = content.getJSONObject(Constants.BPI);
//                JSONObject curr;
//                curr = currencyDict.getJSONObject(Constants.USD);
//                String USD = curr.getString(Constants.RATE);
//
//                curr = currencyDict.getJSONObject(Constants.EUR);
//                String EUR = curr.getString(Constants.RATE);
//
//
//                JSONObject main = content.getJSONObject(Constants.TIME);
//                String updated = main.getString(Constants.UPDATED);
//
//                currencyInformation = new CurrencyInformation(USD, EUR, updated);

            }

            if (data == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Currency Information is null!");
                return;
            }

            String result = null;
            switch (currency) {
                case Constants.EUR:
                    result = currencyInformation.getRateEUR();
                    break;
                case Constants.USD:
                    result = currencyInformation.getRateUSD();
                    break;
                default:
                    result = "[COMMUNICATION THREAD] Wrong information type (EUR / USD)!";
            }

//            String result = currencyInformation.toString();

            printWriter.println(result);
            printWriter.flush();

        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } catch (JSONException jsonException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + jsonException.getMessage());
            if (Constants.DEBUG) {
                jsonException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}