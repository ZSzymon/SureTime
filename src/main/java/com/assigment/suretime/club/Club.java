package com.assigment.suretime.club;

import com.assigment.suretime.address.Address;
import com.assigment.suretime.person.models.Person;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Document
@NoArgsConstructor
public class Club {
    @Id
    private String id;
    private Address address;

    @Indexed(unique = true)
    private String name;

    @DocumentReference
    private Set<Person> members, clubModerators;

    @Indexed(direction = IndexDirection.DESCENDING)
    private LocalDateTime created = LocalDateTime.now();


    @PersistenceConstructor
    public Club(String id, Address address, String name, Set<Person> members, LocalDateTime created) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.members = members;
        this.created = LocalDateTime.now();
    }
    @PersistenceConstructor
    public Club(String id, Address address, String name, List<Person> members, LocalDateTime created) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.members = new HashSet<>(members);
        this.created = LocalDateTime.now();
    }

    @PersistenceConstructor
    public Club(Address address, String name) {
        this.address = address;
        this.name = name;
    }

    public Club update(Club other) {
        this.address = other.getAddress();
        this.name = other.getName();
        this.members = other.getMembers();
        return this;
    }
}
