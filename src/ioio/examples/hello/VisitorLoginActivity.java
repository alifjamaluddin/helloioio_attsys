package ioio.examples.hello;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//import net.kodegeek.frontendioio.WorkerLoginActivity.PostData;


import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class VisitorLoginActivity extends Activity {
	private final String USER_AGENT = "Mozilla/5.0";
	String name, company, meet, reason;
	EditText nameField;
	EditText companyField;
	EditText meetField;
	EditText reasonField;
	String userID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visitor);
		nameField = (EditText) findViewById(R.id.editText1);
		companyField = (EditText) findViewById(R.id.editText2);
		meetField = (EditText) findViewById(R.id.editText3);
		reasonField = (EditText) findViewById(R.id.editText4);
	}
	
	public void loginVisitor(View view) {
		name = nameField.getText().toString();
		company = companyField.getText().toString();
		meet = meetField.getText().toString();
		reason = reasonField.getText().toString();
		try {
			new PostData().execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void redirect(String id) {
		Intent intent = new Intent(this, VisitorMainActivity.class);
		intent.putExtra("id", id);
		
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

			String url = Config.HOST + "/process/visitorinsert.php";
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

			String urlParameters = "name=" + name+"&company="+company+"&with="+meet+"&reason="+reason;

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
				
				
				 showAlert(result);

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
		redirect(userID);
	}
	
	
}
