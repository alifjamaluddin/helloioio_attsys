package ioio.examples.hello;



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import ioio.examples.hello.AndroidMultiPartEntity.ProgressListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class WorkerLoginActivity extends Activity {
	EditText pincode;
	private final String USER_AGENT = "Mozilla/5.0";
	String pincodeTxt;
	String userID;
	public final static String ID = "net.kodegeek.frontendioio.ID";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pincode);
		pincode = (EditText) findViewById(R.id.editText5);
		userID = "";
	}

	public void loginWorker(View view) {
		pincodeTxt = pincode.getText().toString();
		try {
			new PostData().execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void redirect(String id) {
		Intent intent = new Intent(this, WorkerMainActivity.class);
		intent.putExtra(ID, id);
		startActivity(intent);
	}

	private class PostData extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {

		}

		@Override
		protected String doInBackground(String... params) {

			try {
				return sendRequest();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		private String sendRequest() throws IOException {
			String responseString = null;

			String url = Config.HOST + "/process/checkemplogin.php";
			URL obj = new URL(url);
			HttpURLConnection con = null;
			try {
				con = (HttpURLConnection) obj.openConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// add request header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			String urlParameters = "password=" + pincodeTxt;

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			String response = "";

			while ((inputLine = in.readLine()) != null) {
				response += inputLine;

			}
			in.close();

			JSONObject resultObj;
			try {
				resultObj = new JSONObject(response);
				String status = resultObj.getString("status");
				userID = resultObj.getString("user_id");
				if (responseCode == 200) {

					if (status.equals("success"))
						responseString = "success";
					if (status.equals("failed"))
						responseString = "failed";

				} else {
					responseString = "error";
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {

			if (result.equals("success")) {
				String id = userID;
				redirect(id);
				// showAlert(result);

			} else if (result.equals("failed")) {
				showAlert("Login fail");
			} else {
				showAlert("Connection problem");
			}

			super.onPostExecute(result);
		}

	}

	/**
	 * Method to show alert dialog
	 * */
	private void showAlert(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message).setTitle("Response from Servers")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// do nothing
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
}
