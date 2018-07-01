package frameworks.appsession;

import java.io.Serializable;

/**
 * Created by g1.sandeep on 8/22/2017.
 */

public class UserInfo implements Serializable {
    String user_type;
    String user_id;
    String rating;
    String profile_picture;
    Picture[] pictures;
    String name;
    String location;
    String last_name;
    String gender;
    String first_name;
    String date_of_birth;
    String customer_id;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    String email;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    String token;




    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public Picture[] getPictures() {
        return pictures;
    }

    public void setPictures(Picture[] pictures) {
        this.pictures = pictures;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }


    public static class RelationShip implements Serializable{
        Boolean favorited = false;
        Boolean bookmarked = false;
        Boolean isFriend = false;


        public Boolean getFriend() {
            return isFriend;
        }

        public void setFriend(Boolean friend) {
            isFriend = friend;
        }

        public Boolean getFavorited() {
            return favorited;
        }

        public void setFavorited(Boolean favorited) {
            this.favorited = favorited;
        }

        public Boolean getBookmarked() {
            return bookmarked;
        }

        public void setBookmarked(Boolean bookmarked) {
            this.bookmarked = bookmarked;
        }
    }

    public static class Picture implements Serializable{
        int user_id;
        String url;
        boolean is_profile_picture;
        int index;
        int id;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean is_profile_picture() {
            return is_profile_picture;
        }

        public void setIs_profile_picture(boolean is_profile_picture) {
            this.is_profile_picture = is_profile_picture;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

}
