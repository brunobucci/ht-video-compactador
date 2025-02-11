package com.br.fiap.postech.ht_video_compactador.application.dto;


import com.br.fiap.postech.ht_video_compactador.domain.entity.StatusEdicao;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VideoDto {
	private String id;
    private String codigoEdicao;
	private String nome;
    private StatusEdicao statusEdicao;
}
