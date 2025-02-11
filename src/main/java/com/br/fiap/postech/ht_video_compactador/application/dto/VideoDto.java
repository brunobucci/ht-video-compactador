package com.br.fiap.postech.ht_video_compactador.application.dto;


import com.br.fiap.postech.ht_video_compactador.domain.entity.StatusEdicao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDto {
	private String id;
    private String codigoEdicao;
	private String nome;
	private String tentativasDeEdicao;
    private StatusEdicao statusEdicao;
}
