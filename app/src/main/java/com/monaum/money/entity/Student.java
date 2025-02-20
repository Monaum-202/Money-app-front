package com.monaum.money.entity;

    public class Student {

        private String id;
        private String username;
        private String name;
        private String password;

        public Student( String name, String username, String password) {
            this.username = username;
            this.name = name;
            this.password = password;
        }

        public Student(String id, String username, String name, String password) {
            this.id = id;
            this.username = username;
            this.name = name;
            this.password = password;
        }



        public Student() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String toString() {
            return "Student{" +
                    "id=" + id +
                    ", username='" + username + '\'' +
                    ", name='" + name + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }

