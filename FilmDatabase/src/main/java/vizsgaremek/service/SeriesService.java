package vizsgaremek.service;

import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import vizsgaremek.domain.Episodes;
import vizsgaremek.domain.Series;
import vizsgaremek.dto.EpisodesInfoFull;
import vizsgaremek.dto.SeriesInfo;
import vizsgaremek.dto.SeriesInfoFull;
import vizsgaremek.dto.commands.SeriesCommand;
import vizsgaremek.exceptionhandling.EpisodeNotFoundException;
import vizsgaremek.repository.EpisodesRepository;
import vizsgaremek.repository.SeriesRepository;

import javax.transaction.Transactional;
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

    public SeriesInfo saveSeries(SeriesCommand episodeCommand) {
        Series OneSeriesToSave = modelMapper.map(episodeCommand, Series.class);
        Series savedSeries = seriesRepository.save(OneSeriesToSave);
        return modelMapper.map(savedSeries, SeriesInfo.class);
    }


    public List<SeriesInfo> listAllSeries() {
//        List<SeriesInfo> seriesInfoList = new ArrayList<>();
//        int size = 0;
//
//        for (Series series : seriesRepository.listAll()) {
//            size = series.getEpisodesList().size();
//            SeriesInfo seriesInfo = modelMapper.map(series, SeriesInfo.class);
//            seriesInfo.setNumberOfEpisodes(size);
//            seriesInfoList.add(seriesInfo);
//        }
//       return seriesInfoList;
//    }

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
            throw new EpisodeNotFoundException(id);
        }
    }

    public SeriesInfo updateOrInsertSeries(Integer id, SeriesCommand seriesCommand) {
        Series seriesToUpdate = modelMapper.map(seriesCommand, Series.class);
        seriesToUpdate.setId(id);
        Series updatedSeries = seriesRepository.updateOrInsert(seriesToUpdate);
        return modelMapper.map(updatedSeries, SeriesInfo.class);
    }
}
