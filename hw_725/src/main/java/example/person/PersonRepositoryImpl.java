package example.person;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonRepositoryImpl implements PersonRepository {
    private final List<Person> persons = new ArrayList<>();

    {
        persons.add(new Person("Иван", "Иванов"));
        persons.add(new Person("Петр", "Петров"));
        persons.add(new Person("Роман", "Романов"));
    }

    @Override
    public Optional<Person> findByLastName(String lastName) {
        return persons
                .stream()
                .filter(person -> person.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
    }
}
