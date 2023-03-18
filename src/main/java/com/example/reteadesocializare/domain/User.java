package com.example.reteadesocializare.domain;

public class User extends Entity<Long> {
    private final String name;
    private final String password;
    private final String email;

    public User(Long id, String name, String password, String email) {
        super(id);
        this.name = name;
        this.password = password;
        this.email = email;
    }

    /**
     * getter pentru numele user-ului
     * @return numele user-ului
     */
    public String getName() {
        return name;
    }

    /**
     * getter pentru parola user-ului
     * @return parola user-ului
     */
    public String getPassword() {
        return password;
    }

    /**
     * getter pentru email-ul user-ului
     * @return email-ul user-ului
     */
    public String getEmail() {
        return email;
    }

    /**
     * transforma un user intr-un string
     * @return string-ul care reprezinta user-ul
     */
    @Override
    public String toString() {
        return "User [id=" + getId() + ", name=" + name + ", password=" + password + ", email=" + email + "]";
    }
}
