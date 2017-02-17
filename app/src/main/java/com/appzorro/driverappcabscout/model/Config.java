package com.appzorro.driverappcabscout.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by pankaj on 18/1/17.
 */
public class Config {
    static final  String base_url = "http://35.162.151.221/driver_api.php?action=";
    static  final String login_url =base_url+"driver_login&device_type=A";
    static  final String signUp_url = base_url+"driver_register";
    public static LatLng startpoint;
    public static LatLng endpoint;
    public static final String companyaliasurl ="http://35.162.151.221/driver_api.php?action=get_cab&cab_alias=";
    public static final  String simplesignupurl ="http://35.162.151.221/driver_api.php?action=driver_register";
    static final String verify_email ="";
    public static final String facebook_login_verify_url = base_url+"driverDetailFacebookId&facebook_id=";
    public static final String cab_companies_url = base_url + "company_list";
    public   static final String fb_login_url = base_url+"driver_register";
    public static final String userdetail_url = "http://35.162.151.221/driver_api.php?action=user_detail&driver_id=";
    public static final String baserurl_image="http://35.162.151.221/profile_pics/";
    public static final String customerrequesturl="http://35.162.151.221/driver_api.php?action=get_customer_list&driver_id=";
    public static final String acceptrequestbydriver="http://35.162.151.221/driver_api.php?action=requestAcceptedByDriver&driver_id=";
    public static final String laterbookingurl="http://35.162.151.221/driver_api.php?action=myBooking&driver_id=";
    public static final String changepasswordurl ="http://35.162.151.221/driver_api.php?action=reset_password&driver_id=";
    public static final String updateprofileurl="http://35.162.151.221/driver_api.php?action=update_profile";
    public static final String reviewcheckurl="http://35.162.151.221/driver_api.php?action=checkReview&driver_id=";
    public static final String driveravilablityurl=" http://35.162.151.221/driver_api.php?action=driverStatus&driver_id=";
    public static final String ratingtocustomerurl ="35.162.151.221/driver_api.php?action=driverFeedback&driver_id=1&customer_id=2&rating=5&feedback=great ride";
    public static final String changecompanyurl ="http://35.162.151.221/driver_api.php?action=updateCabCompany&driver_id=4&cab_alias=";
    public static final String arrivedurl="http://35.162.151.221/driver_api.php?action=driverArrived&driver_id=";
    public static final String starttripsurl="http://35.162.151.221/driver_api.php?action=tripStarted&driver_id=";
    public static final String canceltripsurl="http://35.162.151.221/driver_api.php?action=tripCancel&driver_id=4&ride_request_id=8";
}
