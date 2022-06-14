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
import vizsgaremek.exceptionhandling.EpisodeNotFoundException;
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


    public void deleteById(Integer id) {
        Series seriesFound = seriesRepositoryExceptionHandler(id);
        archive(id);
        seriesRepository.deleteAllFromSeries(id);
        seriesRepository.delete(seriesFound);
    }

    private DeletedSeries archive(Integer id) {
        DeletedSeries deletedSeries = archiveSeriesById(id);
        List<Episodes> episodesList = episodesRepository.listAllEpisodesForService(id);
        List<DeletedEpisodes> deletedEpisodesList = episodesList.stream()
                .map(episodes -> {
                    DeletedEpisodes deletedEpisode = modelMapper.map(episodes, DeletedEpisodes.class);
                    deletedEpisode.setEpisodeId(episodes.getId());
                    deletedEpisode.setLocalDateTime(LocalDateTime.now());
                    deletedEpisode.setDeletedSeries(deletedSeries);
                    episodesRepository.archive(deletedEpisode);

                    return deletedEpisode;
                })
                .collect(Collectors.toList());
        deletedSeries.setDeletedEpisodesList(deletedEpisodesList);
        return seriesRepository.archive(deletedSeries);


    }

    public DeletedSeries archiveSeriesById(Integer id) {
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
}
