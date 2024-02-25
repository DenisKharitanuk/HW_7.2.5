package example.person;

import java.util.Optional;

public interface PersonRepository {

    Optional<Person> findByLastName(String lastName);

}
