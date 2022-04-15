package com.assigment.suretime.competition;

import com.assigment.suretime.event.Event;
import com.assigment.suretime.event.EventRepository;
import com.assigment.suretime.exceptions.AlreadyExistsException;
import com.assigment.suretime.exceptions.NotFoundException;
import com.assigment.suretime.generics.GenericService;
import com.assigment.suretime.generics.GenericModelAssembler;
import com.assigment.suretime.person.PersonRepository;
import com.assigment.suretime.person.models.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
public class CompetitionService extends GenericService<Competition, CompetitionDto, CompetitionRepository> {

    final GenericModelAssembler<Competition> modelAssembler = new GenericModelAssembler<>(Competition.class, CompetitionController.class);
    final CompetitionRepository competitionRepository;
    final PersonRepository personRepository;
    final EventRepository eventRepository;

    public CompetitionService(CompetitionRepository competitionRepository,
                              PersonRepository personRepository,
                              EventRepository eventRepository) {
        this.competitionRepository = competitionRepository;
        this.personRepository = personRepository;
        this.eventRepository = eventRepository;
    }

    public ResponseEntity<?> addOne(CompetitionDto competitionDto){
        Competition competition = fromDto(competitionDto);
        competitionRepository.findByName(competitionDto.getName())
                .ifPresent(c -> {
                    throw new AlreadyExistsException(competition.getName(),
                            "First delete "+competition.getName()+"Object");});
        log.info("inserted: "+ competition.getName());
        return ResponseEntity.ok(modelAssembler.toModel(competitionRepository.insert(competition)));
    }

    public ResponseEntity<?> deleteOne(String competitionName){
        competitionRepository.deleteByName(competitionName);
        return ResponseEntity.ok("");
    }

    public ResponseEntity<?> updateCompetitionEvents(String competitionId, List<String> eventsId){
        eventsId.forEach(this::eventExistElseThrowNotFoundException);

        Competition competition = getCompetitionElseThrowNotFoundException(competitionId);

        competition.setEventsId(new HashSet<>(eventsId));

        return new ResponseEntity<>(modelAssembler.toModel(
                competitionRepository.save(competition)),
                HttpStatus.CREATED);

    }

    public ResponseEntity<?> addCompetitionEvent(String competitionId, String eventId){
        eventExistElseThrowNotFoundException(eventId);

        Competition competition = getCompetitionElseThrowNotFoundException(competitionId);

        competition.getEventsId().add(eventId);

        return new ResponseEntity<>(modelAssembler.toModel(
                competitionRepository.save(competition)),
                HttpStatus.OK);

    }

    public ResponseEntity<?> deleteCompetitionEvent(String competitionId, String eventId){
        eventExistElseThrowNotFoundException(eventId);

        Competition competition = getCompetitionElseThrowNotFoundException(competitionId);
        competition.getEventsId().remove(eventId);

        return new ResponseEntity<>(modelAssembler.toModel(
                competitionRepository.save(competition)),
                HttpStatus.OK);

    }



    public ResponseEntity<?> updateCompetitionCompetitors(String competitionId, List<String> emails){
        personsExistsElseThrowNotFoundException(emails);

        Competition competition = getCompetitionElseThrowNotFoundException(competitionId);

        competition.setCompetitors(new HashSet<>(emails));

        return new ResponseEntity<>(
                modelAssembler.toModel(competitionRepository.save(competition)),
                HttpStatus.OK);

    }


    public ResponseEntity<?> addCompetitionCompetitor(String competitionId, String email){
        personsExistsElseThrowNotFoundException(List.of(email));

        Competition competition = getCompetitionElseThrowNotFoundException(competitionId);

        competition.getCompetitors().add(email);

        return new ResponseEntity<>(
                modelAssembler.toModel(competitionRepository.save(competition)),
                HttpStatus.OK);

    }

    public ResponseEntity<?> removeCompetitionCompetitor(String competitionId, String email){
        personsExistsElseThrowNotFoundException(List.of(email));

        Competition competition = getCompetitionElseThrowNotFoundException(competitionId);

        competition.getCompetitors().remove(email);

        return new ResponseEntity<>(
                modelAssembler.toModel(competitionRepository.save(competition)),
                HttpStatus.OK);

    }

    private void personsExistsElseThrowNotFoundException(List<String> emails) {
        emails.forEach(personEmail -> personRepository.findById(personEmail)
                        .orElseThrow(() -> new NotFoundException(Person.class.getSimpleName(), personEmail)));
    }

    @Override
    public Competition fromDto(CompetitionDto competitionDto) {
        competitionDto.getCompetitors().forEach(this::personsExistElseThrowNotFoundException);
        competitionDto.getEventsId().forEach(this::eventExistElseThrowNotFoundException);

        return Competition.builder()
                .competitors(competitionDto.getCompetitors())
                .address(competitionDto.getAddress())
                .startTime(competitionDto.getStartTime())
                .endTime(competitionDto.getEndTime())
                .events(competitionDto.getEventsId())
                .name(competitionDto.getName())
                .build();
    }

    private void personsExistElseThrowNotFoundException(String email) {
        personRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(Person.class.getSimpleName(), email));
    }

    private Competition getCompetitionElseThrowNotFoundException(String competitionId) {
        return competitionRepository.findById(competitionId)
                .orElseThrow(()->new NotFoundException(Competition.class.getSimpleName(), competitionId));
    }

    private void eventExistElseThrowNotFoundException(String eventId) {
        eventRepository.findById(eventId)
                .orElseThrow(()->new NotFoundException(Event.class.getSimpleName(), eventId));
    }

}
