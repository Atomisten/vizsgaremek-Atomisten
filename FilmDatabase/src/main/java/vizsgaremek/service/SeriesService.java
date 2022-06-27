package vizsgaremek.service;

import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import vizsgaremek.domain.Episodes;
import vizsgaremek.domain.Series;
import vizsgaremek.domain.archive.DeletedEpisodes;
import vizsgaremek.domain.archive.DeletedSeries;
import vizsgaremek.dto.DeletedSeriesInfo;
import vizsgaremek.dto.EpisodesInfoFull;
import vizsgaremek.dto.SeriesInfo;
import vizsgaremek.dto.SeriesInfoFull;
import vizsgaremek.dto.commands.SeriesCommand;
import vizsgaremek.exceptionhandling.DeletedSeriesNotFoundException;
import vizsgaremek.exceptionhandling.SeriesNotFoundException;
import vizsgaremek.repository.DeletedSeriesAndMoviesRepository;
import vizsgaremek.repository.SeriesRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SeriesService {
    private SeriesRepository seriesRepository;
    private DeletedSeriesAndMoviesRepository deletedSeriesAndMoviesRepository;
    private ModelMapper modelMapper;

    public SeriesService(SeriesRepository seriesRepository, DeletedSeriesAndMoviesRepository deletedSeriesAndMoviesRepository, ModelMapper modelMapper) {
        this.seriesRepository = seriesRepository;
        this.deletedSeriesAndMoviesRepository = deletedSeriesAndMoviesRepository;
        this.modelMapper = modelMapper;
    }

    public SeriesInfo saveSeries(SeriesCommand seriesCommand) {
        Series OneSeriesToSave = modelMapper.map(seriesCommand, Series.class);
        Series savedSeries = seriesRepository.save(OneSeriesToSave);
        return modelMapper.map(savedSeries, SeriesInfo.class);
    }


    public List<SeriesInfo> listAllSeries() {
        return seriesRepository.listAll().stream()
                .map(series -> {
                    SeriesInfo seriesInfo = modelMapper.map(series, SeriesInfo.class);
                    seriesInfo.setNumberOfEpisodes(series.getEpisodesList().size());
                    return seriesInfo;
                })
                .collect(Collectors.toList());
    }

    public Series findById(Integer id) {
        return seriesRepository.findById(id);
    }

    public SeriesInfoFull findByIdFull(Integer seriesId) {
        Series seriesFound = seriesRepositoryExceptionHandler(seriesId);
        SeriesInfoFull seriesInfoFull = modelMapper.map(seriesFound, SeriesInfoFull.class);
        List<EpisodesInfoFull> episodesInfoFullList = seriesFound.getEpisodesList().stream()
                .map(episodes -> modelMapper.map(episodes, EpisodesInfoFull.class))
                .collect(Collectors.toList());
        seriesInfoFull.setEpisodesInfoFull(episodesInfoFullList);
        return seriesInfoFull;
    }


    public Series seriesRepositoryExceptionHandler(Integer id) {
        try {
            return seriesRepository.findById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new SeriesNotFoundException(id);
        }
    }

    public SeriesInfo updateOrInsertSeries(Integer id, SeriesCommand seriesCommand) {
        Series seriesToUpdate = modelMapper.map(seriesCommand, Series.class);
        seriesToUpdate.setId(id);
        try {
            Series foundByID = seriesRepository.findById(seriesToUpdate.getId());
            seriesToUpdate.setEpisodesList(foundByID.getEpisodesList());
            Series updatedSeries = seriesRepository.updateOrInsert(seriesToUpdate);
            SeriesInfo seriesInfo = modelMapper.map(updatedSeries, SeriesInfo.class);
            seriesInfo.setNumberOfEpisodes(updatedSeries.getEpisodesList().size());
            return seriesInfo;
        } catch (EmptyResultDataAccessException e) {
            Series updatedSeries = seriesRepository.updateOrInsert(seriesToUpdate);
            return modelMapper.map(updatedSeries, SeriesInfo.class);
        }
    }


    public void deleteById(Integer seriesId) {
        Series seriesFound = seriesRepositoryExceptionHandler(seriesId);
        archive(seriesId);
        seriesRepository.deleteAllFromSeries(seriesId);
        seriesRepository.delete(seriesFound);
    }

    private DeletedSeries archive(Integer seriesId) {
        Series seriesById = seriesRepository.findById(seriesId);
        DeletedSeries deletedSeries = modelMapper.map(seriesById, DeletedSeries.class);
        deletedSeries.setSeriesId(seriesId);
        deletedSeries.setTimeOfDeletion(LocalDateTime.now());
        DeletedSeries archivedWithoutEpisodes = deletedSeriesAndMoviesRepository.archiveSeries(deletedSeries);
        List<Episodes> episodesList1 = seriesById.getEpisodesList();
        List<DeletedEpisodes> deletedEpisodesList1 = episodesList1.stream()
                .map(episodes -> {
                    DeletedEpisodes deletedEpisodes = modelMapper.map(episodes, DeletedEpisodes.class);
                    deletedEpisodes.setEpisodeId(episodes.getId());
                    deletedEpisodes.setTimeOfDeletion(LocalDateTime.now());
                    deletedEpisodes.setDeletedSeries(archivedWithoutEpisodes);
                    return deletedSeriesAndMoviesRepository.archiveEpisode(deletedEpisodes);
                })
                .collect(Collectors.toList());
        archivedWithoutEpisodes.setDeletedEpisodesList(deletedEpisodesList1);
        return archivedWithoutEpisodes;


    }


    public DeletedSeries archiveSeriesByEpisode(Integer id) {
        try {
            DeletedSeries deletedSeriesFound = deletedSeriesAndMoviesRepository.archivedSeriesFindById(id);
            return deletedSeriesAndMoviesRepository.archiveSeries(deletedSeriesFound);
        } catch (EmptyResultDataAccessException e) {
            Series seriesFound = seriesRepository.findById(id);
            DeletedSeries seriesToArchive = modelMapper.map(seriesFound, DeletedSeries.class);
            seriesToArchive.setSeriesId(id);
            seriesToArchive.setTimeOfDeletion(LocalDateTime.now());
            return deletedSeriesAndMoviesRepository.archiveSeries(seriesToArchive);
        }
    }

    public List<DeletedSeriesInfo> archiveList() {
        return deletedSeriesAndMoviesRepository.deletedSeriesList().stream()
                .map(deletedSeries -> {
                    DeletedSeriesInfo deletedSeriesInfo = modelMapper.map(deletedSeries, DeletedSeriesInfo.class);
                    deletedSeriesInfo.setNumberOfEpisodes(deletedSeries.getDeletedEpisodesList().size());
                    return deletedSeriesInfo;
                })
                .collect(Collectors.toList());
    }

    public void deleteArchivedSeries(Integer deletedSeriesId) {
        DeletedSeries deletedSeries = deletedSeriesRepositoryExceptionHandler(deletedSeriesId);
        deletedSeriesAndMoviesRepository.deleteAllEpisodesFromArchivedSeries(deletedSeriesId);
        deletedSeriesAndMoviesRepository.deleteArchivedSeries(deletedSeries);
    }

    public DeletedSeries deletedSeriesRepositoryExceptionHandler(Integer id) {
        try {
            return deletedSeriesAndMoviesRepository.archivedSeriesFindById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new DeletedSeriesNotFoundException(id);
        }
    }

    public void purgeAll() {
        deletedSeriesAndMoviesRepository.purgeEpisodes();
        deletedSeriesAndMoviesRepository.purgeSeries();
    }
}
