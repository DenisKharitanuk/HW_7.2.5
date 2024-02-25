package example;

import example.person.Person;
import example.person.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

class ExampleControllerTest {

    private ExampleController subject;
    @Mock
    private PersonRepository personRepository;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        subject = new ExampleController(personRepository);
    }

    @Test
    public void shouldReturnHelloWorld() {
        assertEquals("Здравствуй Мир!", subject.hello());
    }

    @Test
    public void shouldReturnFullNameOfAPerson() throws Exception {
        Person peter = new Person("Иван", "Иванов");
        given(personRepository.findByLastName("Иванов")).willReturn(Optional.of(peter));

        var greeting = subject.hello("Иванов");

        assertEquals("Здравствуй, Иван Иванов!", greeting);
    }

    @Test
    public void shouldTellIfPersonIsUnknown() throws Exception {
        given(personRepository.findByLastName(anyString())).willReturn(Optional.empty());

        var greeting = subject.hello("Иванов");

        assertEquals("Не знаю никого по фамилии 'Иванов'", greeting);
    }
}