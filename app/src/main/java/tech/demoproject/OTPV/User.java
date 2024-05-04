package tech.demoproject.OTPV;

class User {
    public String name;
    public String username;
    public String password;
    private int income;


    public User() {

    }

    public User(String name, String email, String password) {
        this.name = name;
        this.username = email;
        this.password = password;
    }
}

