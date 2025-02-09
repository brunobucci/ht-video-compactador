package com.br.fiap.postech.ht_video_compactador.domain.repository;

public interface ICompactacaoQueueAdapterOUT {
	void publishVideoCompactado(String videoJson);
	void publishVideoComErro(String videoJson);
}
