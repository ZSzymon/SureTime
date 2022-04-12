package com.assigment.suretime.competition;

import com.assigment.suretime.event.Event;
import com.assigment.suretime.generics.MongoModel;
import com.assigment.suretime.person.models.Person;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import org.apache.tomcat.jni.Address;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Document
public class Competition implements MongoModel {
    @Id
    private String id;
    private Address address;
    private List<Event> events;

    @DocumentReference(lazy = true)
    private Map<String,Person> competitors;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime startTime, endTime;


    public CompetitionDto toDto(Competition competition){
        CompetitionDto competitionDto = new CompetitionDto();
        return competitionDto;

    }

}
