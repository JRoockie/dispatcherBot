package org.voetsky.dispatcherBot.repository.song;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TypeDef;
import org.voetsky.dispatcherBot.WhoWillSing;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.tgAudio.TgAudio;
import org.voetsky.dispatcherBot.repository.tgVoice.TgVoice;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Builder
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "orderClient"})
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "songs")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Entity
public class Song implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "order_client_id", referencedColumnName = "id")
    private OrderClient orderClient;

    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TgAudio> tgAudios;

    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TgVoice> tgVoices;

    private String songName;
    private String link;
    private Integer singerCount;

    @Enumerated(EnumType.STRING)
    private WhoWillSing whoWillSing;

    private boolean isFilled;

}