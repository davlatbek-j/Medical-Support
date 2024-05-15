package med.support.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import med.support.enums.UserState;

import java.util.Set;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity(name = "doctor")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long chatId;

    @Column(length = 50)
    String firstname;

    @Column(length = 50)
    String lastname;

    @Column(length = 50)
    String surname;

    @Column(length = 20)
    String phone;

    @Column(nullable = false)
    String login;

    @Column(nullable = false)
    String password;

    @Column(length = 300)
    String outline;

    @Column(length = 500)
    String motto;

    @Enumerated(EnumType.STRING)
    UserState state;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    Photo photo;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
    Set<Speciality> speciality;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    Set<Experience> experience;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
    Set<Language> language;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    Set<Education> education;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    Set<Achievement> achievement;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    Set<ReceptionAddress> receptionAddress;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    Set<Service> service;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    Set<Contact> contact;
}
