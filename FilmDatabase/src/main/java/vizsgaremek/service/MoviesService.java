package vizsgaremek.service;

import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import vizsgaremek.domain.Movies;
import vizsgaremek.dto.MoviesInfo;
import vizsgaremek.domain.archive.DeletedMovies;
import vizsgaremek.dto.commands.MovieCommand;
import vizsgaremek.exceptionhandling.MovieNotFoundException;
import vizsgaremek.repository.MoviesRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MoviesService {

    private MoviesRepository moviesRepository;
    private ModelMapper modelMapper;

    public MoviesService(MoviesRepository moviesRepository, ModelMapper modelMapper) {
        this.moviesRepository = moviesRepository;
        this.modelMapper = modelMapper;
    }

    public MoviesInfo saveMovie(MovieCommand movieCommand) {
        Movies OneMovieToSave = modelMapper.map(movieCommand, Movies.class);
        Movies savedMovie = moviesRepository.save(OneMovieToSave);
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
        Movies movieSaved = moviesRepository.updateOrInsert(movieToUpdate);
        return modelMapper.map(movieSaved, MoviesInfo.class);
    }

    public List<DeletedMovies> archiveList() {
        return moviesRepository.archiveList();
    }

    public void deleteById(Integer id) {
        Movies movieFound = moviesRepositoryExceptionHandler(id);
        archive(id);
        moviesRepository.delete(movieFound);
    }

    private DeletedMovies archive(Integer id) {
        Movies movieFound = moviesRepository.findByID(id);
        DeletedMovies movieToArchive = modelMapper.map(movieFound, DeletedMovies.class);
        return moviesRepository.archive(movieToArchive);
    }


    public Movies moviesRepositoryExceptionHandler(Integer id) {
        try {
            return moviesRepository.findByID(id);
        } catch (EmptyResultDataAccessException e) {
            throw new MovieNotFoundException(id);
        }
    }
}
