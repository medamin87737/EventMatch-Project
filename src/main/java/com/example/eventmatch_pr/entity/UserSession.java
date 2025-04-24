package com.example.eventmatch_pr.entity;

public class UserSession {

        private static UserSession instance;
        private int userId;

        public UserSession() {}

        public static UserSession getInstance() {
            if (instance == null) {
                instance = new UserSession();
            }
            return instance;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }


