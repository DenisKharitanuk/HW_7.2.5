package exercise.article;

import exercise.worker.WorkerImpl;
import org.junit.jupiter.api.BeforeEach;
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
    Library library;


    private static String getTitlesFromFile() throws IOException {

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
    void getCatalogTitle_WithAlphabetOrder() throws IOException {

        when(library.getAllTitles()).thenReturn(titlesList);

        assertEquals("Список доступных статей:\n" +
                getTitlesFromFile(), worker.getCatalog());

    }


    // prepareArticles tests

    @Test
    void prepareArticles_allFieldsAreFilledIn_preparationIsGoingWell() {
        List<Article> artList = new ArrayList<>();

        artList.add(new Article(
                "Как правильно изучать языки программирования",
                "Как эффективно изучать программирование. Советы, которые помогут лучше и эффективнее учить любой язык программирования.",
                "Сергей Сергеев",
                LocalDate.of(2022, 10, 22)));

        assertEquals(artList, worker.prepareArticles(artList));


    }

    @Test
    void prepareArticles_withoutDataTheRemainingFieldsAreFilledIn_preparationIsSuccessfulDataIsGenerated() {
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
    void prepareArticles_allFieldsAreClean_prepareIsntSuccessful() {
        List<Article> artList = new ArrayList<>();
        Article artWithCleanFields = new Article(null,
                null,
                null,
                null);
        artList.add(artWithCleanFields);

        assertTrue(worker.prepareArticles(artList).isEmpty());
    }

    @Test
    void prepareArticles_withoutTitleTheRemainingFieldsAreFilledIn_prepareIsntSuccessful() {
        List<Article> artList = new ArrayList<>();
        Article artWithCleanFields = new Article(null,
                "This content is so big ",
                "Denis Ha",
                LocalDate.now());
        artList.add(artWithCleanFields);

        assertTrue(worker.prepareArticles(artList).isEmpty());
    }

    @Test
    void prepareArticles_withoutContentTheRemainingFieldsAreFilledIn_prepareIsntSuccessful() {
        List<Article> artList = new ArrayList<>();
        Article artWithCleanFields = new Article("Bla bla",
                null,
                "Denis Ha",
                LocalDate.now());
        artList.add(artWithCleanFields);

        assertTrue(worker.prepareArticles(artList).isEmpty());
    }

    @Test
    void prepareArticles_withoutAuthorTheRemainingFieldsAreFilledIn_prepareIsntSuccessful() {
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
    void addNewArticles_saveArticlesInLibrary() {

        newArticleList.add(new Article("MadeNewArticleTitle", "ContentContent",
                "AuthorAuthor", LocalDate.of(2023, 1, 1)));
        worker.addNewArticles(newArticleList);
        verify(library).store(2023, newArticleList);


    }
}