package co.com.reactive;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MovieTest {

    List<Movie> movies1 = new ArrayList<>();
    List<Movie> movies2 = new ArrayList<>();
    Flux<Movie> movies;

    @Before
    public void setup(){
        Movie movie1 = Movie.builder()
                .name("Rapidos y furiosos")
                .durationInMin(107)
                .score(9.7)
                .director("Rob Cohen")
                .build();

        Movie movie2 = Movie.builder()
                .name("Mas rapido mas furioso")
                .durationInMin(108)
                .score(8.5)
                .director("John Singleton")
                .build();

        Movie movie3 = Movie.builder()
                .name("Rapido y furioso: reto Tokio")
                .durationInMin(105)
                .score(7.7)
                .director("Justin Lin")
                .build();

        Movie movie4 = Movie.builder()
                .name("Son como niños")
                .durationInMin(102)
                .score(9.2)
                .director("Dennis Dugan")
                .build();

        Movie movie5 = Movie.builder()
                .name("El aro")
                .durationInMin(145)
                .score(7.1)
                .director("Gore Verbinski")
                .build();

        movies1.add(movie1);
        movies1.add(movie2);
        movies1.add(movie3);
        movies2.add(movie4);
        movies2.add(movie5);

        movies = Flux.fromIterable(movies1)
                .mergeWith(Flux.fromIterable(movies2));
    }

    @Test
    public void filterByScoreGreaterThan8(){
        movies
                .filter(movie -> movie.getScore()>8)
                .subscribe(
                        result ->
                                log.info("Peliculas cuyo score es mayor a 8  {}", result));
    }

    @Test
    public void filterByTimeGreaterThan120Min(){
        movies
                .filter(movie -> movie.getDurationInMin()>120)
                .subscribe(
                        result ->
                                log.info("Peliculas cuya duración es mayor a 120 min {}", result));
    }

    @Test
    public void filterByDirector(){
        String directorName = "Justin Lin";
        movies
                .filter(movie -> movie.getDirector().equalsIgnoreCase(directorName))
                .subscribe(
                        result ->
                                log.info("Peliculas cuyo director es "+ directorName + ": " + result));
    }

    @Test
    public void filterByAllCriteria(){
        String directorName = "Gore Verbinski";
        movies
                .filter(movie -> movie.getScore()>7)
                .filter(movie -> movie.getDurationInMin()>100)
                .filter(movie -> movie.getDirector().equalsIgnoreCase(directorName))
                .subscribe(
                        result ->
                                log.info("Peliculas que cumplen todos los criterios {}", result));
    }

}
