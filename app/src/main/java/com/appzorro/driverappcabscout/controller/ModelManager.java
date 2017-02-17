package com.appzorro.driverappcabscout.controller;

public class ModelManager {
    private CabCompaniesManager cabCompaniesManager;
    private RegistrationManager registrationManager;
    private LoginManager loginManager;
    private FacebookLoginManager facebookLoginManager;
    private UserDetailManager userDetailManager;
    private static ModelManager modelManager;
    private CustomerReQuestManager customerReQuestManager;
    private AcceptCustomerRequest acceptCustomerRequest;
    private LaterBookingManager laterBookingManager;
    private ChangepasswordManager changepasswordManager;
    private UpdateProfileManager updateProfileManager;
    private  ChangeComapnyManager changeComapnyManager;
    private ReviewManager reviewManager;
    private OnlineOfflineManager onlineOfflineManager;
    private ArrivedManager arrivedManager;
    private StartTripsManager startTripsManager;



    private ModelManager() {
        cabCompaniesManager = new CabCompaniesManager();
        registrationManager = new RegistrationManager();
        loginManager = new LoginManager();
        facebookLoginManager = new FacebookLoginManager();
        userDetailManager = new UserDetailManager();
        customerReQuestManager = new CustomerReQuestManager();
        acceptCustomerRequest = new AcceptCustomerRequest();
        laterBookingManager = new LaterBookingManager();
        changepasswordManager = new ChangepasswordManager();
        updateProfileManager = new UpdateProfileManager();
        changeComapnyManager = new ChangeComapnyManager();
        reviewManager = new ReviewManager();
        onlineOfflineManager = new OnlineOfflineManager();
        arrivedManager = new ArrivedManager();
        startTripsManager = new StartTripsManager();



    }

    public static ModelManager getInstance() {
        if (modelManager == null)
            return modelManager = new ModelManager();
        else
            return modelManager;
    }

    public OnlineOfflineManager getOnlineOfflineManager() {
        return onlineOfflineManager;
    }

    public ArrivedManager getArrivedManager() {

        return arrivedManager;
    }

    public StartTripsManager getStartTripsManager() {
        return startTripsManager;
    }

    public ReviewManager getReviewManager() {
        return reviewManager;
    }

    public LaterBookingManager getLaterBookingManager() {
        return laterBookingManager;
    }

    public ChangepasswordManager getChangepasswordManager() {
        return changepasswordManager;
    }

    public UpdateProfileManager getUpdateProfileManager() {
        return updateProfileManager;
    }

    public ChangeComapnyManager getChangeComapnyManager() {
        return changeComapnyManager;
    }

    public AcceptCustomerRequest getAcceptCustomerRequest() {
        return acceptCustomerRequest;
    }

    public UserDetailManager getUserDetailManager() {
        return userDetailManager;
    }

    public CustomerReQuestManager getCustomerReQuestManager() {
        return customerReQuestManager;
    }

    public FacebookLoginManager getFacebookLoginManager() {
        return facebookLoginManager;
    }

    public CabCompaniesManager getCabCompaniesManager() {
        return cabCompaniesManager;
    }

    public RegistrationManager getRegistrationManager() {
        return registrationManager;
    }

   public LoginManager getLoginManager() {
        return loginManager;
    }

    /*public SearchAddressManager getSearchAddressManager() {
        return searchAddressManager;
    }

    public LocationDirectionManager getLocationDirectionManager() {
        return locationDirectionManager;
    }*/
}