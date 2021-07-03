package net.ionoff.forex.ea.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Table;
import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
//@javax.persistence.Entity
//@Table(name = "supports")
public class Support {
    private long id;
    private long candle;
    private Instant time;
    private double value;
    private boolean active;
}
