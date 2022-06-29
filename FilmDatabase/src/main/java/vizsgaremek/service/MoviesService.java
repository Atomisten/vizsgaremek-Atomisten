package vizsgaremek.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import vizsgaremek.domain.Movies;
import vizsgaremek.domain.archive.DeletedMovies;
import vizsgaremek.dto.DeletedMoviesInfo;
import vizsgaremek.dto.MoviesInfo;
import vizsgaremek.dto.commands.MovieCommand;
import vizsgaremek.exceptionhandling.MovieNotFoundException;
import vizsgaremek.repository.MoviesRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MoviesService {

    private MoviesRepository moviesRepository;
    private ModelMapper modelMapper;
    private final int costToRent;

    public MoviesService(MoviesRepository moviesRepository, ModelMapper modelMapper,
                         @Value("${cost_to_rent}") int costToRent) {
        this.moviesRepository = moviesRepository;
        this.modelMapper = modelMapper;
        this.costToRent = costToRent;
    }

    public MoviesInfo saveMovie(MovieCommand movieCommand) {
        Movies movieToSave = modelMapper.map(movieCommand, Movies.class);
        movieToSave.setCostToRent(costToRent);
        Movies savedMovie = moviesRepository.save(movieToSave);
        return modelMapper.map(savedMovie, MoviesInfo.class);
    }

    public List<MoviesInfo> listAllMovies() {
        return moviesRepository.listAll().stream()
                .map(movies -> modelMapper.map(movies, MoviesInfo.class))
                .collect(Collectors.toList());

    }


    public MoviesInfo findById(Integer id) {
        Movies movieById = moviesRepositoryExceptionHandler(id);
        return modelMapper.map(movieById, MoviesInfo.class);
    }


    public MoviesInfo updateOrInsert(Integer id, MovieCommand command) {
        Movies movieToUpdate = modelMapper.map(command, Movies.class);
        movieToUpdate.setId(id);
        try {
            Movies movieFound = moviesRepository.findById(id);
            movieToUpdate.setCostToRent(movieFound.getCostToRent());           //ha megtalálja akkor ne bántsa az árat.
        } catch (EmptyResultDataAccessException e) {
            movieToUpdate.setCostToRent(costToRent);                           //ha nem találja akkor beállítja
        }

        Movies movieSaved = moviesRepository.updateOrInsert(movieToUpdate);
        return modelMapper.map(movieSaved, MoviesInfo.class);
    }

    public List<DeletedMoviesInfo> archiveList() {
        List<DeletedMovies> deletedMoviesList = moviesRepository.archiveList();
        return deletedMoviesList.stream()
                .map(deletedMovies -> modelMapper.map(deletedMovies, DeletedMoviesInfo.class))
                .collect(Collectors.toList());
    }

    public void deleteById(Integer id) {
        Movies movieFound = moviesRepositoryExceptionHandler(id);
        archive(id);
        moviesRepository.delete(movieFound);
    }

    private DeletedMovies archive(Integer id) {
        Movies movieFound = moviesRepository.findById(id);
        DeletedMovies movieToArchive = modelMapper.map(movieFound, DeletedMovies.class);
        movieToArchive.setTimeOfDeletion(LocalDateTime.now());
        movieToArchive.setMovieId(id);
        movieToArchive.setId(null);
        return moviesRepository.archive(movieToArchive);
    }


    public Movies moviesRepositoryExceptionHandler(Integer id) {
        try {
            return moviesRepository.findById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new MovieNotFoundException(id);
        }
    }

    public Movies moviesRepositoryUpdateOrInsertCost(Integer id) {
        try {
            return moviesRepository.findById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new MovieNotFoundException(id);
        }
    }

}
