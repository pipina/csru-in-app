package model;

import lombok.Data;

@Data
public class SetDlzniciDto {
    private Long id;
    private String nazov;
    private String ico;
    private Integer idPo;
    private Boolean sync;
    private Long setDlzniciRefresh;
}
