package vizsgaremek.service;

import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import vizsgaremek.domain.Episodes;
import vizsgaremek.domain.Series;
import vizsgaremek.dto.EpisodesInfoFull;
import vizsgaremek.dto.SeriesInfo;
import vizsgaremek.dto.SeriesInfoFull;
import vizsgaremek.dto.archive.DeletedEpisodes;
import vizsgaremek.dto.archive.DeletedSeries;
import vizsgaremek.dto.archive.dto.DeletedSeriesInfo;
import vizsgaremek.dto.commands.SeriesCommand;
import vizsgaremek.exceptionhandling.DeletedSeriesNotFoundException;
import vizsgaremek.exceptionhandling.SeriesNotFoundException;
import vizsgaremek.repository.EpisodesRepository;
import vizsgaremek.repository.SeriesRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SeriesService {
    private SeriesRepository seriesRepository;
    private EpisodesRepository episodesRepository;
    private ModelMapper modelMapper;

    public SeriesService(SeriesRepository seriesRepository, EpisodesRepository episodesRepository, ModelMapper modelMapper) {
        this.seriesRepository = seriesRepository;
        this.episodesRepository = episodesRepository;
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

    public Series findByID(Integer id) {
        return seriesRepository.findByID(id);
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
            return seriesRepository.findByID(id);
        } catch (EmptyResultDataAccessException e) {
            throw new SeriesNotFoundException(id);
        }
    }

    public SeriesInfo updateOrInsertSeries(Integer id, SeriesCommand seriesCommand) {
        Series seriesToUpdate = modelMapper.map(seriesCommand, Series.class);
        seriesToUpdate.setId(id);
        try {
            Series foundByID = seriesRepository.findByID(seriesToUpdate.getId());
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
        Series seriesById = seriesRepository.findByID(seriesId);
        DeletedSeries deletedSeries = modelMapper.map(seriesById, DeletedSeries.class);
        deletedSeries.setSeriesId(seriesId);
        deletedSeries.setLocalDateTime(LocalDateTime.now());
        DeletedSeries archivedWithoutEpisodes = seriesRepository.archive(deletedSeries);
        List<Episodes> episodesList1 = seriesById.getEpisodesList();
        List<DeletedEpisodes> deletedEpisodesList1 = episodesList1.stream()
                .map(episodes -> {
                    DeletedEpisodes deletedEpisodes = modelMapper.map(episodes, DeletedEpisodes.class);
                    deletedEpisodes.setEpisodeId(episodes.getId());
                    deletedEpisodes.setLocalDateTime(LocalDateTime.now());
                    deletedEpisodes.setDeletedSeries(archivedWithoutEpisodes);
                    return episodesRepository.archive(deletedEpisodes);
                })
                .collect(Collectors.toList());
        archivedWithoutEpisodes.setDeletedEpisodesList(deletedEpisodesList1);               //TODO ez elvileg nem kell
        return archivedWithoutEpisodes;


    }

    public DeletedSeries archiveSeriesById(Integer seriesId) {
        try {
            DeletedSeries deletedSeriesFound = seriesRepository.ArchivefindBySeriesId(seriesId);
            return seriesRepository.archive(deletedSeriesFound);
        } catch (EmptyResultDataAccessException e) {
            Series seriesFound = seriesRepository.findByID(seriesId);
            DeletedSeries seriesToArchive = modelMapper.map(seriesFound, DeletedSeries.class);
            seriesToArchive.setSeriesId(seriesId);
            seriesToArchive.setLocalDateTime(LocalDateTime.now());
            return seriesRepository.archive(seriesToArchive);
        }
    }


    public DeletedSeries archiveSeriesByEpisode(Integer id) {
        try {
            DeletedSeries deletedSeriesFound = seriesRepository.ArchivefindBySeriesId(id);
            return seriesRepository.archive(deletedSeriesFound);
        } catch (EmptyResultDataAccessException e) {
            Series seriesFound = seriesRepository.findByID(id);
            DeletedSeries seriesToArchive = modelMapper.map(seriesFound, DeletedSeries.class);
            seriesToArchive.setSeriesId(id);
            seriesToArchive.setLocalDateTime(LocalDateTime.now());
            return seriesRepository.archive(seriesToArchive);
        }
    }

    public List<DeletedSeriesInfo> archiveList() {
        return seriesRepository.archiveList().stream()
                .map(deletedSeries -> {
                    DeletedSeriesInfo deletedSeriesInfo = modelMapper.map(deletedSeries, DeletedSeriesInfo.class);
                    deletedSeriesInfo.setNumberOfEpisodes(deletedSeries.getDeletedEpisodesList().size());
                    return deletedSeriesInfo;
                })
                .collect(Collectors.toList());
    }

    public void deleteArchivedSeries(Integer deletedSeriesId) {
        DeletedSeries deletedSeries = deletedSeriesRepositoryExceptionHandler(deletedSeriesId);
        seriesRepository.deleteAllEpisodesFromArchivedSeries(deletedSeriesId);
        seriesRepository.deleteArchivedSeries(deletedSeries);
    }

    public DeletedSeries deletedSeriesRepositoryExceptionHandler(Integer id) {
        try {
            return seriesRepository.ArchivefindBySeriesId(id);
        } catch (EmptyResultDataAccessException e) {
            throw new DeletedSeriesNotFoundException(id);
        }
    }

    public void purgeAll() {
        episodesRepository.purge();
        seriesRepository.purge();
    }
}
