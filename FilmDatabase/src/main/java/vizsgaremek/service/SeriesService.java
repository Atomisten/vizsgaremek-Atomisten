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
import vizsgaremek.repository.DeletedSeriesAndEpisodesRepository;
import vizsgaremek.repository.SeriesRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SeriesService {
    private SeriesRepository seriesRepository;
    private DeletedSeriesAndEpisodesRepository deletedSeriesAndEpisodesRepository;
    private ModelMapper modelMapper;

    public SeriesService(SeriesRepository seriesRepository, DeletedSeriesAndEpisodesRepository deletedSeriesAndEpisodesRepository, ModelMapper modelMapper) {
        this.seriesRepository = seriesRepository;
        this.deletedSeriesAndEpisodesRepository = deletedSeriesAndEpisodesRepository;
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
        DeletedSeries archivedWithoutEpisodes = getOrCreateSeries(seriesId);
        Series seriesFound = findById(seriesId);
        List<Episodes> episodesList1 = seriesFound.getEpisodesList();
        List<DeletedEpisodes> deletedEpisodesList1 = episodesList1.stream()
                .map(episodes -> {
                    DeletedEpisodes deletedEpisodes = modelMapper.map(episodes, DeletedEpisodes.class);
                    deletedEpisodes.setEpisodeId(episodes.getId());
                    deletedEpisodes.setId(null);
                    deletedEpisodes.setTimeOfDeletion(LocalDateTime.now());
                    deletedEpisodes.setDeletedSeries(archivedWithoutEpisodes);
                    return deletedSeriesAndEpisodesRepository.archiveEpisode(deletedEpisodes);
                })
                .collect(Collectors.toList());
        return archivedWithoutEpisodes;
    }

    private DeletedSeries getOrCreateSeries(Integer seriesId) {
        try {                                                                                                       //ha van DELETED series akkor visszaadja,
            DeletedSeries deletedSeriesFound = deletedSeriesAndEpisodesRepository.archivedSeriesFindById(seriesId);
            return deletedSeriesAndEpisodesRepository.archiveSeries(deletedSeriesFound);
        } catch (EmptyResultDataAccessException e) {                                                            //ha nincs DELETED SERIES akkor létrehozza az epizódokkal együtt
            Series seriesFound = seriesRepository.findById(seriesId);
            DeletedSeries deletedSeries = modelMapper.map(seriesFound, DeletedSeries.class);
            deletedSeries.setSeriesId(seriesId);
            deletedSeries.setId(null);
            deletedSeries.setTimeOfDeletion(LocalDateTime.now());
            return deletedSeriesAndEpisodesRepository.archiveSeries(deletedSeries);
        }
    }


    public DeletedSeries archiveSeriesByEpisode(Integer id) {
        try {
            DeletedSeries deletedSeriesFound = deletedSeriesAndEpisodesRepository.archivedSeriesFindById(id);
            return deletedSeriesAndEpisodesRepository.archiveSeries(deletedSeriesFound);
        } catch (EmptyResultDataAccessException e) {
            Series seriesFound = seriesRepository.findById(id);
            DeletedSeries seriesToArchive = modelMapper.map(seriesFound, DeletedSeries.class);
            seriesToArchive.setSeriesId(id);
            seriesToArchive.setTimeOfDeletion(LocalDateTime.now());
            return deletedSeriesAndEpisodesRepository.archiveSeries(seriesToArchive);
        }
    }

    public List<DeletedSeriesInfo> archiveList() {
        return deletedSeriesAndEpisodesRepository.deletedSeriesList().stream()
                .map(deletedSeries -> {
                    DeletedSeriesInfo deletedSeriesInfo = modelMapper.map(deletedSeries, DeletedSeriesInfo.class);
                    deletedSeriesInfo.setNumberOfEpisodes(deletedSeries.getDeletedEpisodesList().size());
                    return deletedSeriesInfo;
                })
                .collect(Collectors.toList());
    }

    public void deleteArchivedSeries(Integer deletedSeriesId) {
        DeletedSeries deletedSeries = deletedSeriesRepositoryExceptionHandler(deletedSeriesId);
        deletedSeriesAndEpisodesRepository.deleteAllEpisodesFromArchivedSeries(deletedSeriesId);
        deletedSeriesAndEpisodesRepository.deleteArchivedSeries(deletedSeries);
    }

    public DeletedSeries deletedSeriesRepositoryExceptionHandler(Integer id) {
        try {
            return deletedSeriesAndEpisodesRepository.archivedSeriesFindById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new DeletedSeriesNotFoundException(id);
        }
    }

    public void purgeAll() {
        deletedSeriesAndEpisodesRepository.purgeEpisodes();
        deletedSeriesAndEpisodesRepository.purgeSeries();
    }
}
