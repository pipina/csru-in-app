package model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "set_dlznici_obdobie", schema = "csru_set")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@NoArgsConstructor
@FieldNameConstants
@SuperBuilder
public class SetDlzniciObdobie {

    @Id
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_dlznici")
    private SetDlznici setDlznici;

    @Column(name = "obdobie_od")
    private Date obdobieOd;

    @Column(name = "obdobie_do")
    private Date obdobieDo;

    @Column(name = "zdroj")
    private Short zdroj;

    @Column(name = "typDlznika")
    private String typDlznika;
}
