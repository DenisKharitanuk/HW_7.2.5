package exercise.article;

import exercise.worker.WorkerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class WorkerImplTest {

    private static List<String> titlesList = new ArrayList<>();
    private static List<Article> newArticleList;
    private WorkerImpl worker;

    @Mock
    private Library library;


    public static String getTitlesFromFile() throws IOException {

        StringBuilder expectedTitles = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader("src/main/java/resources/titless.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            titlesList.add(line);
        }
        reader.close();
        titlesList.stream()
                .sorted(String::compareTo)
                .forEachOrdered(x -> expectedTitles.append("    ").append(x).append("\n"));
        return expectedTitles.toString();
    }

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        worker = new WorkerImpl(library);
        newArticleList = new ArrayList<>();
    }


    // getCatalog's tests

    @Test
    @DisplayName("Получить каталог с  заголовками статей в алфавитном порядке ")
    public void getTitlesFromCatalog() throws IOException {

        when(library.getAllTitles()).thenReturn(titlesList);

        assertEquals("Список доступных статей:\n" +
                getTitlesFromFile(), worker.getCatalog());

    }


    // prepareArticles tests

    @Test
    @DisplayName(" подготовка статьи ,со всеми заполнеными полями, к сохранению   ")
    public void allFieldsAreFilledIn() {
        List<Article> artList = new ArrayList<>();

        artList.add(new Article(
                "Как правильно изучать языки программирования",
                "Как эффективно изучать программирование. Советы, которые помогут лучше и эффективнее учить любой язык программирования.",
                "Сергей Сергеев",
                LocalDate.of(2022, 10, 22)));

        assertEquals(artList, worker.prepareArticles(artList));


    }

    @Test
    @DisplayName("подготовка статьи , заголовки статей повторяются , к сохранению ")
    public void sameTitlesDefence() {

        newArticleList.add(new Article("Как правильно изучать языки программирования",
                "Как эффективно изучать программирование. Советы, которые помогут лучше и эффективнее учить любой язык программирования.",
                "Сергей Сергеев",
                LocalDate.of(2022, 10, 22)));

        newArticleList.add(new Article("Как правильно изучать языки программирования",
                "Как эффективно изучать программирование. Советы, которые помогут лучше и эффективнее учить любой язык программирования.",
                "Сергей Сергеев",
                LocalDate.of(2022, 10, 22)));
        List<Article> newSameArticles = worker.prepareArticles(newArticleList);
        assertEquals(1, newSameArticles.size());
    }

    @Test
    @DisplayName("подготовка статей к сохранению с незаполненным полем дата , которое генерируется автоматически")
    public void notData() {
        List<Article> expectedArtList = new ArrayList<>();

        newArticleList.add(new Article("Как правильно изучать языки программирования",
                "Как эффективно изучать программирование. Советы, которые помогут лучше и эффективнее учить любой язык программирования.",
                "Сергей Сергеев",
                null));


        Article expectedArt = new Article("Как правильно изучать языки программирования",
                "Как эффективно изучать программирование. Советы, которые помогут лучше и эффективнее учить любой язык программирования.",
                "Сергей Сергеев",
                LocalDate.now());
        expectedArtList.add(expectedArt);

        assertEquals(expectedArtList, worker.prepareArticles(newArticleList));
    }

    @Test
    @DisplayName("подготовка статьи , все поля не заполнены, к сохранению ")
    public void allFieldsIsEmpty() {
        List<Article> artList = new ArrayList<>();
        Article artWithCleanFields = new Article(null,
                null,
                null,
                null);
        artList.add(artWithCleanFields);

        assertTrue(worker.prepareArticles(artList).isEmpty());
    }

    @Test
    @DisplayName("подготовка статьи , поле  title не заполнено , к сохранению")
    public void notTitle() {
        List<Article> artList = new ArrayList<>();
        Article artWithCleanFields = new Article(null,
                "This content is so big ",
                "Denis Ha",
                LocalDate.now());
        artList.add(artWithCleanFields);

        assertTrue(worker.prepareArticles(artList).isEmpty());
    }

    @Test
    @DisplayName("подготовка статьи , поле content  не заполнено , к сохранению")
    public void notContent() {
        List<Article> artList = new ArrayList<>();
        Article artWithCleanFields = new Article("Bla bla",
                null,
                "Denis Ha",
                LocalDate.now());
        artList.add(artWithCleanFields);

        assertTrue(worker.prepareArticles(artList).isEmpty());
    }

    @Test
    @DisplayName("подготовка статьи , поле author  не заполнено , к сохранению")
    public void notAuthor() {
        List<Article> artList = new ArrayList<>();
        Article artWithCleanFields = new Article("Bla bla",
                "Гыыыы улыобочку",
                null,
                LocalDate.now());
        artList.add(artWithCleanFields);

        assertTrue(worker.prepareArticles(artList).isEmpty());
    }


    // addNewArticles tests


    @Test
    @DisplayName("сохранение статьи в библиотеку ")
    public void safeArticleInLibrary() {

        newArticleList.add(new Article("MadeNewArticleTitle", "ContentContent",
                "AuthorAuthor", LocalDate.of(2023, 1, 1)));
        worker.addNewArticles(newArticleList);
        verify(library).store(2023, newArticleList);


    }


}