package net.ionoff.forex.ea.model.slope;

import net.ionoff.forex.ea.model.Slope;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

@Entity
@Table(name = "eur_usd_v300_slopes")
public class V300Slope extends Slope {

    private static final long serialVersionUID = 1L;

}
