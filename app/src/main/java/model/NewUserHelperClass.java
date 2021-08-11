package model;

public class NewUserHelperClass {
    private String name, email, mobileno, favcategory, password;

    public NewUserHelperClass() {
    }

    public NewUserHelperClass(String name, String email, String mobileno, String favcategory, String password) {
        this.name = name;
        this.email = email;
        this.mobileno = mobileno;
        this.favcategory = favcategory;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getFavcategory() {
        return favcategory;
    }

    public void setFavcategory(String favcategory) {
        this.favcategory = favcategory;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
