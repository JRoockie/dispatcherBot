package org.voetsky.dispatcherBot.services.repo.songService;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.repository.song.SongRepository;
import org.voetsky.dispatcherBot.repository.tgUser.TgUserRepository;

@Service
public class SongRepositoryService implements SongRepo {

    private final SongRepository songRepository;

    public SongRepositoryService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public Song findSongById(Long id) {
        return songRepository.findSongById(id);
    }

    public Song save(Song song) {
        return songRepository.save(song);
    }

    public Song defaultSong(OrderClient orderClient) {
        return Song.builder()
                .orderClient(orderClient)
                .isFilled(false)
                .build();
    }

}
