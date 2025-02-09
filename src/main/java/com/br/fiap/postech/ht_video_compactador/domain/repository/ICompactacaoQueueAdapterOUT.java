package com.br.fiap.postech.ht_video_compactador.domain.repository;

public interface ICompactacaoQueueAdapterOUT {
	void publishVideoProcessado(String videoJson);
	void publishVideoComErro(String videoJson);
}
