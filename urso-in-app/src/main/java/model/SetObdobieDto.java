package model;

import lombok.Data;

import java.util.Date;

@Data
public class SetObdobieDto {
    private Long id;
    private Long setDlznici;
    private Date obdobieOd;
    private Date obdobieDo;
    private Short zdroj;
    private String typDlznika;
}
