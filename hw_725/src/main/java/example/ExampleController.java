package example;

import example.person.PersonRepository;

public class ExampleController {

    private final PersonRepository personRepository;

    public ExampleController(final PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
    public String hello() {
        return "Здравствуй Мир!";
    }

    public String hello(final String lastName) {
        var foundPerson = personRepository.findByLastName(lastName);

        return foundPerson
                .map(person -> String.format("Здравствуй, %s %s!", person.getFirstName(), person.getLastName()))
                .orElse(String.format("Не знаю никого по фамилии '%s'", lastName));
    }
}
