package com.appzorro.driverappcabscout.model.Beans;

/**
 * Created by vijay on 14/2/17.
 */

public class ReviewBeans {

    String name , profilepic,feedbacktime,rating,feedback;

    public ReviewBeans(String name, String profilepic, String feedbacktime, String rating, String feedback) {

        this.name=name;
        this.profilepic =profilepic;
        this.feedbacktime = feedbacktime;
        this.rating=rating;
        this.feedback =feedback;

    }

    public String getName() {
        return name;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public String getFeedbacktime() {
        return feedbacktime;
    }

    public String getRating() {
        return rating;
    }

    public String getFeedback() {
        return feedback;
    }
}
