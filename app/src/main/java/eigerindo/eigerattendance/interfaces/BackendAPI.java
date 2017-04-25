package eigerindo.eigerattendance.interfaces;

import eigerindo.eigerattendance.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


/**
 * Created by ahmad on 12/13/2016.
 */


public interface BackendAPI {

    @POST("user/login")
    Call<User> login(@Body User user);




}
