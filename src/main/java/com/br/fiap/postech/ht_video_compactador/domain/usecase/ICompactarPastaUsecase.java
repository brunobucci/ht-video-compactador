package com.br.fiap.postech.ht_video_compactador.domain.usecase;

import com.br.fiap.postech.ht_video_compactador.application.dto.VideoDto;

public interface ICompactarPastaUsecase {
	void executar(VideoDto videoDto);
}
