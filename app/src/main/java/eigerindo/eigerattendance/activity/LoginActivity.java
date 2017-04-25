package eigerindo.eigerattendance.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import eigerindo.eigerattendance.R;
import eigerindo.eigerattendance.interfaces.BackendAPI;
import eigerindo.eigerattendance.model.User;
import eigerindo.eigerattendance.utils.Constants;
import eigerindo.eigerattendance.utils.Helper;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static eigerindo.eigerattendance.utils.Constants.USER_NIK;
import static eigerindo.eigerattendance.utils.Constants.USER_SITEID;


public class LoginActivity extends AppCompatActivity {

    /*private EditText Username;
    private EditText Userpassword;
    private Button btnLogin;*/

   @Bind(R.id.Username) protected EditText Username;
   @Bind(R.id.Userpassword) protected EditText Userpassword;
   @Bind(R.id.btnLogin) protected Button btnLogin;
   @Bind(R.id.marker_progress)
   ProgressBar marker_progress;

   private Activity activity;
   private String txtUsername;
   private String txtPassword;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_login_screen);
      ButterKnife.bind(this);
      activity = this;
      getLocation();

      marker_progress.setVisibility(View.GONE);

        /*Username = (EditText) findViewById(R.id.Username);
        Userpassword = (EditText) findViewById(R.id.Userpassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);*/

      btnLogin.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            txtUsername = Username.getText().toString();
            txtPassword = Userpassword.getText().toString();

            String data = txtUsername + " " + txtPassword;

            Log.e("#####", data);

            doLogin();
         }
      });

   }

   private void getLocation() {

   }

   private void doLogin() {
      marker_progress.setVisibility(View.VISIBLE);

      User user = new User();
      user.setNik(txtUsername);
      user.setPassword(txtPassword);

      Log.e("#######################", "login");

      Builder b = new Builder();
      b.readTimeout(60, TimeUnit.SECONDS);
      b.writeTimeout(60, TimeUnit.SECONDS);
      b.addInterceptor(new Interceptor() {
         @Override
         public okhttp3.Response intercept(Chain chain) throws IOException {

            okhttp3.Response response = chain.proceed(chain.request());

            Log.e("################", String.valueOf(response.code()));
            return response;
         }
      });

      OkHttpClient client = b.build();

      Gson gson = new GsonBuilder()
              .setDateFormat("dd-MM-yyyy")
              .create();

      Retrofit retrofit = new Retrofit.Builder()
              .baseUrl(Constants.SERVER_URL)
              .addConverterFactory(GsonConverterFactory.create(gson))
              .client(client)
              .build();

      BackendAPI api = retrofit.create(BackendAPI.class);
      Call<User> call = api.login(user);
      call.enqueue(new Callback<User>() {
         @Override
         public void onResponse(Call<User> call, Response<User> response) {

            if (response.code() == HttpURLConnection.HTTP_NO_CONTENT ||
                    response.code() == HttpURLConnection.HTTP_INTERNAL_ERROR) {
               Helper.showToast(activity, "Username atau Password Salah", Toast.LENGTH_LONG);
               marker_progress.setVisibility(View.GONE);
            } else if (response.code() == HttpURLConnection.HTTP_OK) {

               User user = response.body();

               Helper.setCookie(activity, USER_NIK, user.getNik());
               Helper.setCookie(activity, USER_SITEID, user.getSite_id());

               Intent intent = new Intent(activity, MainActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(intent);
               finish();
            }
         }

         @Override
         public void onFailure(Call<User> call, Throwable t) {

            Log.e("#########", t.getMessage());
            Helper.showToast(activity, "Login Gagal", Toast.LENGTH_LONG);
            marker_progress.setVisibility(View.GONE);
         }
      });
   }
}
