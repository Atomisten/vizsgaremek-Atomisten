package vizsgaremek.service;

import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import vizsgaremek.domain.Movies;
import vizsgaremek.dto.MoviesInfo;
import vizsgaremek.dto.archive.DeletedMovies;
import vizsgaremek.dto.commands.MovieCommand;
import vizsgaremek.exceptionhandling.MovieNotFoundException;
import vizsgaremek.repository.MoviesRepository;

import javax.persistence.NoResultException;
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
        Movies AMovieToSave = modelMapper.map(movieCommand, Movies.class);
        Movies savedMovie = moviesRepository.save(AMovieToSave);
        return modelMapper.map(savedMovie, MoviesInfo.class);
    }

    public List<MoviesInfo> listAllMovies() {
        return moviesRepository.listAll().stream()
                .map(movies -> modelMapper.map(movies, MoviesInfo.class))
                .collect(Collectors.toList());

    }

    //    public MoviesInfo findById(Integer id) {
////        Movies movieById = foundHandler(id);
////        return modelMapper.map(movieById, MoviesInfo.class);
//        Movies movieById = null;
//        try {
//            movieById = moviesRepository.findByID(id);
//                                                                                  //InvocationTargetException ?!!
//        } catch (Exception e) {
//            System.out.println("Underlying exception: " + e.getCause());
//            if (EmptyResultDataAccessException.class.equals(e.getCause().getClass()) |
//                    NoResultException.class.equals(e.getCause().getClass())) {
//                throw new MovieNotFoundException(id);
//            }
//            else e.printStackTrace();
//        }
//
//        return modelMapper.map(movieById, MoviesInfo.class);
//    }

    public MoviesInfo findById(Integer id) {
//        Movies movieById = foundHandler(id);
//        return modelMapper.map(movieById, MoviesInfo.class);

        try {
            Movies movieById = moviesRepository.findByID(id);
            return modelMapper.map(movieById, MoviesInfo.class);
        } catch (EmptyResultDataAccessException | NoResultException e) {                                 //InvocationTargetException ?!!
            throw new MovieNotFoundException(id);
        }
    }



    public MoviesInfo updateOrInsert(Integer id, MovieCommand command) {
        Movies movieToUpdate = modelMapper.map(command, Movies.class);
        movieToUpdate.setId(id);
        Movies updatedMovie = moviesRepository.updateOrInsert(movieToUpdate);
        return modelMapper.map(updatedMovie, MoviesInfo.class);
    }

    public List<DeletedMovies> archiveList() {
        return moviesRepository.archiveList();
    }

    public void deleteById(Integer id) {
        Movies movieFound = moviesRepository.findByID(id);
        archive(id);
        moviesRepository.delete(movieFound);
    }

    private DeletedMovies archive(Integer id) {
        Movies movieFound = moviesRepository.findByID(id);
        DeletedMovies movieToArchive = modelMapper.map(movieFound, DeletedMovies.class);
        return moviesRepository.archive(movieToArchive);
    }


    public Movies foundHandler(Integer id) {
        try {
            return moviesRepository.findByID(id);
        } catch (NoResultException e) {
            throw new MovieNotFoundException(id);
        }
    }

//    public MoviesInfo findById(Integer id) {
////        Movies movieById = foundHandler(id);
//        Optional<Movies> movieById = moviesRepository.findByID(id);
//        if (movieById.isPresent()) {
//            return modelMapper.map(movieById.get(), MoviesInfo.class);
//        } else {
//            throw new MovieNotFoundException(id);
//        }
//    }

//    private DeletedMovies archive(Integer id) {
//        Optional<Movies> movieFound = moviesRepository.findByID(id);
//        DeletedMovies movieToArchive = modelMapper.map(movieFound, DeletedMovies.class);
//        return moviesRepository.archive(movieToArchive);
//    }
//
//    public void deleteById(Integer id) {
//        Optional<Movies> movieFound = moviesRepository.findByID(id);
//        archive(id);
//        moviesRepository.delete(movieFound);
//    }
//
//    public Optional<Movies> foundHandler(Integer id) {
//        try {
//            return moviesRepository.findByID(id);
//        } catch (NoResultException e) {
//            throw new MovieNotFoundException(id);
//        }
//    }
}
