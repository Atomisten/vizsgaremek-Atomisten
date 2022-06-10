package vizsgaremek.service;

import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import vizsgaremek.domain.Episodes;
import vizsgaremek.dto.EpisodesInfo;
import vizsgaremek.dto.archive.DeletedEpisodes;
import vizsgaremek.dto.commands.EpisodeCommand;
import vizsgaremek.exceptionhandling.EpisodeNotFoundException;
import vizsgaremek.repository.EpisodesRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EpisodesService {

    private EpisodesRepository episodesRepository;
    private ModelMapper modelMapper;

    public EpisodesService(EpisodesRepository episodesRepository, ModelMapper modelMapper) {
        this.episodesRepository = episodesRepository;
        this.modelMapper = modelMapper;
    }

    public EpisodesInfo saveEpisode(EpisodeCommand episodeCommand) {
        Episodes AnEpisodeToSave = modelMapper.map(episodeCommand, Episodes.class);
        Episodes savedEpisode = episodesRepository.save(AnEpisodeToSave);
        return modelMapper.map(savedEpisode, EpisodesInfo.class);
    }

    public List<EpisodesInfo> listAllEpisodes() {
        return episodesRepository.listAll().stream()
                .map(episodes -> modelMapper.map(episodes, EpisodesInfo.class))
                .collect(Collectors.toList());

    }


    public EpisodesInfo findById(Integer id) {
        Episodes episodeById = episodesRepository.findByID(id);
        return modelMapper.map(episodeById, EpisodesInfo.class);
    }


    public EpisodesInfo updateOrInsert(Integer id, EpisodeCommand command) {
        Episodes episodeToUpdate = modelMapper.map(command, Episodes.class);
        episodeToUpdate.setId(id);
        Episodes updatedEpisode = episodesRepository.updateOrInsert(episodeToUpdate);
        return modelMapper.map(updatedEpisode, EpisodesInfo.class);
    }

    public List<DeletedEpisodes> archiveList() {
        return episodesRepository.archiveList();
    }

    public void deleteById(Integer id) {
        Episodes episodeFound = episodesRepositoryExceptionHandler(id);
        archive(id);
        episodesRepository.delete(episodeFound);
    }

    private DeletedEpisodes archive(Integer id) {
        Episodes episodeFound = episodesRepository.findByID(id);
        DeletedEpisodes episodeToArchive = modelMapper.map(episodeFound, DeletedEpisodes.class);
        return episodesRepository.archive(episodeToArchive);
    }


    public Episodes episodesRepositoryExceptionHandler(Integer id) {
        try {
            return episodesRepository.findByID(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EpisodeNotFoundException(id);
        }
    }
}