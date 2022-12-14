package vizsgaremek.service;

import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import vizsgaremek.domain.Episodes;
import vizsgaremek.domain.Series;
import vizsgaremek.domain.archive.DeletedEpisodes;
import vizsgaremek.domain.archive.DeletedSeries;
import vizsgaremek.dto.DeletedEpisodesInfo;
import vizsgaremek.dto.EpisodesInfo;
import vizsgaremek.dto.commands.EpisodeCommand;
import vizsgaremek.exceptionhandling.DeletedEpisodeNotFoundException;
import vizsgaremek.exceptionhandling.EpisodeNotFoundException;
import vizsgaremek.repository.DeletedSeriesAndEpisodesRepository;
import vizsgaremek.repository.EpisodesRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EpisodesService {

    private EpisodesRepository episodesRepository;
    private ModelMapper modelMapper;
    private SeriesService seriesService;
    private DeletedSeriesAndEpisodesRepository deletedSeriesAndEpisodesRepository;

    public EpisodesService(EpisodesRepository episodesRepository, ModelMapper modelMapper, SeriesService seriesService, DeletedSeriesAndEpisodesRepository deletedSeriesAndEpisodesRepository) {
        this.episodesRepository = episodesRepository;
        this.modelMapper = modelMapper;
        this.seriesService = seriesService;
        this.deletedSeriesAndEpisodesRepository = deletedSeriesAndEpisodesRepository;
    }

    public EpisodesInfo saveEpisode(Integer id, EpisodeCommand episodeCommand) {
        Episodes toSave = modelMapper.map(episodeCommand, Episodes.class);
        Series seriesForEpisode = seriesService.seriesRepositoryExceptionHandler(id);
        toSave.setSeries(seriesForEpisode);
        Episodes saved = episodesRepository.save(toSave);
        return mapToEpisodesInfo(saved);

    }

    public List<EpisodesInfo> listAllEpisodes() {
        return episodesRepository.listAll().stream()
                .map(episodes -> modelMapper.map(episodes, EpisodesInfo.class))
                .collect(Collectors.toList());

    }


    public EpisodesInfo findById(Integer id) {
        Episodes episodeById = episodesRepositoryExceptionHandler(id);
        return modelMapper.map(episodeById, EpisodesInfo.class);
    }


    public EpisodesInfo updateOrInsert(Integer id, EpisodeCommand command) {
        Episodes foundById = episodesRepositoryExceptionHandler(id);
        Episodes episodeToUpdate = modelMapper.map(command, Episodes.class);
        episodeToUpdate.setId(id);
        episodeToUpdate.setSeries(foundById.getSeries());
        Episodes updatedEpisode = episodesRepository.updateOrInsert(episodeToUpdate);
        return modelMapper.map(updatedEpisode, EpisodesInfo.class);
    }

    public List<DeletedEpisodesInfo> archiveList() {
        return deletedSeriesAndEpisodesRepository.deletedEpisodeList().stream()
                .map(deletedEpisodes -> {
                    DeletedEpisodesInfo deletedEpisodesInfo = modelMapper.map(deletedEpisodes, DeletedEpisodesInfo.class);
                    deletedEpisodesInfo.setEpisodeId(deletedEpisodes.getEpisodeId());
                    deletedEpisodesInfo.setSeriesTitle(deletedEpisodes.getDeletedSeries().getTitle());
                    deletedEpisodesInfo.setSeriesId(deletedEpisodes.getDeletedSeries().getSeriesId());
                    return deletedEpisodesInfo;
                })
                .collect(Collectors.toList());
    }

    public void deleteById(Integer id) {
        Episodes episodeFound = episodesRepositoryExceptionHandler(id);
        archive(id);
        episodesRepository.delete(episodeFound);
    }

    private DeletedEpisodes archive(Integer id) {
        Episodes episodeFound = episodesRepository.findById(id);
        Series seriesOfEpisode = episodeFound.getSeries();
        DeletedSeries deletedSeries = seriesService.archiveSeriesByEpisode(seriesOfEpisode.getId());
        DeletedEpisodes episodeToArchive = modelMapper.map(episodeFound, DeletedEpisodes.class);
        episodeToArchive.setEpisodeId(id);
        episodeToArchive.setTimeOfDeletion(LocalDateTime.now());
        episodeToArchive.setDeletedSeries(deletedSeries);
        return deletedSeriesAndEpisodesRepository.archiveEpisode(episodeToArchive);
    }


    public Episodes episodesRepositoryExceptionHandler(Integer id) {
        try {
            return episodesRepository.findById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EpisodeNotFoundException(id);
        }
    }

    public List<EpisodesInfo> listAllEpisodesForSeries(Integer id) {
        seriesService.seriesRepositoryExceptionHandler(id);
        List<Episodes> episodesList = episodesRepository.listAllEpisodesForService(id);
        return episodesList.stream()
                .map(this::mapToEpisodesInfo)
                .collect(Collectors.toList());

    }


    //b??r nem k??ne, de ha v??letlen m??gis a felhaszn??l??nak valami??rt kellene, akkor itt legyen

//    public EpisodesInfo setSeries(Integer episodeId, Integer seriesId) {
//        Episodes episode = episodesRepository.findById(episodeId);
//        Series series = seriesFindById(seriesId);
//        episode.setSeries(series);
//        return mapToEpisodesInfo(episode);
//    }

    private EpisodesInfo mapToEpisodesInfo(Episodes episode) {
        EpisodesInfo episodesInfo = modelMapper.map(episode, EpisodesInfo.class);
        episodesInfo.setSeriesTitle(episode.getSeries().getTitle());
        return episodesInfo;
    }

    public void deleteArchivedEpisodeById(Integer id) {
        DeletedEpisodes deletedEpisode = deletedEpisodesRepositoryExceptionHandler(id);
        deletedSeriesAndEpisodesRepository.deleteArchiveEpisode(deletedEpisode);
    }


    public DeletedEpisodes deletedEpisodesRepositoryExceptionHandler(Integer id) {
        try {
            return deletedSeriesAndEpisodesRepository.archivedEpisodeFindById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new DeletedEpisodeNotFoundException(id);
        }
    }

    public void purge() {
        deletedSeriesAndEpisodesRepository.purgeEpisodes();
    }
}